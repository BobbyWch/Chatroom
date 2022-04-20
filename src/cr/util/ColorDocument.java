package cr.util;

import cr.LocalEnum;
import cr.util.user.UserInfo;
import cr.util.user.UserManager;

import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.Serial;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Bobbywang
 * @date 2021-09-21 14:13
 */
public final class ColorDocument extends DefaultStyledDocument implements java.io.Serializable {
    @Serial
    private final static long serialVersionUID = 202192115L;
    private transient UserManager manager;
    private transient DocumentCreator creator;

    private final static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public final static SimpleAttributeSet dateStyle=createStyle(true, Color.darkGray, Settings.obj.fontSize);
    public final static SimpleAttributeSet userDefault=createStyle(true, Color.black, Settings.obj.fontSize + 2);
    public final static SimpleAttributeSet userAdmin=createStyle(true, new Color(0, 200, 0), Settings.obj.fontSize + 2);
    public final static SimpleAttributeSet userOwner=createStyle(true, Color.orange, Settings.obj.fontSize + 2);

    public ColorDocument(DocumentCreator creator){
        this.creator=creator;
    }

    public void appendLine(String text, SimpleAttributeSet style) {
        if (!text.endsWith("\n"))
            text = text + '\n';
        append(text, style);
    }

    public void append(String text, SimpleAttributeSet style) {
        try {
            insertString(getLength(), text, style);
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void appendDate(Date date) {
        appendMiddle(format.format(date), dateStyle);
    }

    public static SimpleAttributeSet createStyle(boolean isBold, Color color, int fontsize) {
        SimpleAttributeSet temp = new SimpleAttributeSet();
        StyleConstants.setBold(temp, isBold);
        StyleConstants.setForeground(temp, color);
        StyleConstants.setFontSize(temp, fontsize);
        StyleConstants.setFontFamily(temp, "微软雅黑");
        return temp;
    }

    public void appendMiddle(String str, SimpleAttributeSet set) {
        int p = getLength();
        str = str + '\n';
        append(str, set);
        StyleConstants.setAlignment(set, StyleConstants.ALIGN_CENTER);
        setParagraphAttributes(p, str.length(), set, true);
    }

    private long lastTime = 0;

    public void append(UserInfo info){
        switch (manager.getByInfo(info).getPermission()) {
            case LocalEnum.Permission_ADMIN -> append(info.getName() + ">", userAdmin);
            case LocalEnum.Permission_OWNER -> append(info.getName() + ">", userOwner);
            case LocalEnum.Permission_DEFAULT -> append(info.getName() + ">", userDefault);
        }
    }
    public void append(Event e){
        if (e instanceof ClearEvent) {
            creator.clear(e);
            return;
        }
        Date date=new Date();
        if ((date.getTime() - lastTime) >= 180000) {
            appendDate(date);
        }
        lastTime = date.getTime();
        e.display(this);
    }

    public void setManager(UserManager manager) {
        this.manager = manager;
    }
    public void reInit(UserManager manager,DocumentCreator creator){
        this.manager=manager;
        this.creator=creator;
    }
}