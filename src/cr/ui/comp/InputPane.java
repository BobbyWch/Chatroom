package cr.ui.comp;

import cr.LocalEnum;
import cr.util.Client;

import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public final class InputPane extends JEditorPane {
    public static InputPane obj = null;

    public static void init() {
        obj = new InputPane();
    }

    private Image bg=null;
    private InputPane() {
        super();
        StyledEditorKit kit = new StyledEditorKit();
        setEditorKit(kit);
        setOpaque(false);
        setDocument(kit.createDefaultDocument());
        setFont(LocalEnum.FONT_MENU);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    if (!Client.getClient().isJoined()) {
                        setText(null);
                        return;
                    }
                    if (getText().trim().equals("")) {
                        setText(null);
                        return;
                    }
                    Client.getClient().say(getText(), LocalEnum.USER_ALL);
                    setText(null);
                }
            }
        });
        setBgImage(Toolkit.getDefaultToolkit().getImage("D:\\Desktop\\微笑照\\872CD08C76929651F8E9971588D50A50.jpg"));
    }
    public void setBgImage(Image img){
        this.bg=img;
        if (isVisible()) repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        if (bg!=null) g.drawImage(bg,0,0,getWidth(),getHeight(),this);
        super.paintComponent(g);
    }
}