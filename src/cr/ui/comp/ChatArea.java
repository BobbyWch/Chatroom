package cr.ui.comp;

import cr.LocalEnum;
import cr.ui.popmenu.ChatPopMenu;
import cr.util.Client;

import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Bobbywang
 * @date 2021-08-28 22:32
 */
public final class ChatArea extends JEditorPane {
    private static ChatArea instance = null;

    public static ChatArea getInstance() {
        return instance;
    }
    public static void init(){
        long t=System.currentTimeMillis();
        instance=new ChatArea();
        System.out.println("ChatArea init:"+(System.currentTimeMillis()-t)+"ms");
    }

    private ChatArea() {
        super();
        setEditorKit(new StyledEditorKit());
        setDocument(Client.getClient().getDocument());
        setEditable(false);
        setBorder(null);
        setFont(LocalEnum.FONT_MENU);
        Client.getClient().setScreen(this);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton()==MouseEvent.BUTTON3){
                    ChatPopMenu.getInstance().show(ChatArea.this,e.getX(),e.getY(),ChatArea.this.getSelectedText()!=null);
                }
            }
        });
    }
    public void roll(){
        setCaretPosition(getDocument().getLength());
    }
}
