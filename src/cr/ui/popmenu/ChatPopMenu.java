package cr.ui.popmenu;

import cr.LocalEnum;
import cr.events.Events;
import cr.io.IO;
import cr.ui.XMenuBar;
import cr.ui.comp.ChatArea;
import cr.util.Client;
import cr.util.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

/**
 * @author Bobbywang
 * @date 2021-08-13 15:40
 */
public final class ChatPopMenu extends JPopupMenu {
    private static ChatPopMenu currentInstance = null;

    public static ChatPopMenu getInstance() {
        return currentInstance;
    }
    public static void init(){
        long t=System.currentTimeMillis();
        currentInstance=new ChatPopMenu();
        System.out.println("ChatPopMenu init:"+(System.currentTimeMillis()-t)+"ms");
    }

    private final JMenuItem copyItem;
    private final JMenuItem clearItem;

    private ChatPopMenu() {
        super();
        copyItem=XMenuBar.create("复制",'c',e -> Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(ChatArea.getInstance().getSelectedText()),null));
        clearItem = XMenuBar.create("清除所有人聊天纪录", e -> Client.getClient().sendMessage(Events.getClear()));
        add(copyItem);
        addSeparator();
        add(XMenuBar.create("导出聊天记录", e -> IO.backupChat()));
        add(XMenuBar.create("清除聊天记录", e -> Client.getClient().getScreen().setText(null)));
        add(clearItem);
    }

    public void show(Component component, int x, int y,boolean enableCopy) {
        super.show(component, x, y);
        clearItem.setEnabled(User.getLocalUser().getPermission()== LocalEnum.Permission_OWNER);
        copyItem.setEnabled(enableCopy);
    }
}
