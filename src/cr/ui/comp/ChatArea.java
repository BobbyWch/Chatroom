package cr.ui.comp;

import cr.LocalEnum;
import cr.inter.Background;
import cr.ui.popmenu.ChatPopMenu;
import cr.util.Client;

import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Bobbywang
 * @date 2021-08-28 22:32
 */
public final class ChatArea extends JEditorPane implements Background {
    private static ChatArea instance = null;

    public static ChatArea getInstance() {
        return instance;
    }
    public static void init(){
        long t=System.currentTimeMillis();
        instance=new ChatArea();
        System.out.println("ChatArea init:"+(System.currentTimeMillis()-t)+"ms");
    }

    private Image bg;

    private ChatArea() {
        super();
        setEditorKit(new StyledEditorKit());
        setDocument(Client.getClient().getDocument());
        setEditable(false);
        setBorder(null);
        setOpaque(false);
        setFont(LocalEnum.FONT_MENU);
        flushImage("chat");
        Client.getClient().setScreen(this);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton()==MouseEvent.BUTTON3){
                    ChatPopMenu.getInstance().show(ChatArea.this,e.getX(),e.getY(),ChatArea.this.getSelectedText()!=null);
                }
            }
        });
//        setBgImage(Toolkit.getDefaultToolkit().getImage("D:\\Desktop\\微笑照\\IMG_005.jpg"));
    }
    @Override
    public void setBgImage(Image img){
        this.bg=img;
        if (isVisible()) repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (bg!=null) g.drawImage(bg,0,0,getWidth(),getHeight(),this);
        super.paintComponent(g);
    }

    public void roll(){
        setCaretPosition(getDocument().getLength());
    }
}