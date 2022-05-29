package cr.ui;

import cr.*;
import cr.events.Events;
import cr.io.IO;
import cr.io.Net;
import cr.plugin.PluginManager;
import cr.tool.Logger;
import cr.tool.Settings;
import cr.ui.comp.ChatArea;
import cr.ui.comp.FileList;
import cr.ui.comp.InputPane;
import cr.ui.frame.ConnectFrame;
import cr.ui.frame.MainFrame;
import cr.util.user.User;
<<<<<<< Updated upstream
import cr.util.*;
=======
import jni.NativeFrame;
>>>>>>> Stashed changes

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

/**
 * @author Bobbywang
 * @date 2021-06-23 20:09
 */
public final class XMenuBar extends JMenuBar {
    public static XMenuBar obj=new XMenuBar();

    public final JMenu menu1;
    public final JMenu menu2;
    public final JMenu menu5;
    public final JMenu menu6;
    public final JMenu menuPlugin;

    public static JMenuItem disconnect;
    public static JMenuItem startItem;
    public static JMenuItem stopItem;
    public static JMenuItem nameItem;
    private static JMenuItem fastItem;
    private final Client client = Client.getClient();

    private XMenuBar() {
        menu1 = new Menu("客户端(C)") {
            @Override
            public void paint(Graphics g) {
                nameItem.setEnabled(!client.isJoined());
                disconnect.setEnabled(client.isJoined());
                super.paint(g);
            }
        };
        fastItem = create("",  e -> client.join(Settings.obj.lastIP, Settings.obj.lastPort));
        menu1.add(fastItem);
        flush();
        menu1.add(create("加入聊天室", 'j', e -> new ConnectFrame("加入", source -> Client.getClient().join((String) source[0], (Integer) source[1])).show0()));
        disconnect = create("离开聊天室", 'l', e -> client.leave(true));
        disconnect.setEnabled(false);
        menu1.add(disconnect);
        menu1.addSeparator();
        nameItem = create("设置昵称", 'n', e -> {
            String input;
            try {
                input = JOptionPane.showInputDialog(Main.mainFrame, "当前昵称：" + User.getLocalUser().getName() + "\n输入新的昵称：\t\t\t", "设置昵称", JOptionPane.PLAIN_MESSAGE).trim();
            } catch (NullPointerException e1) {
                return;
            }
            if (input.equals(""))
                JOptionPane.showMessageDialog(Main.mainFrame, "昵称不能为空！\t\t\t\t", "错误", JOptionPane.ERROR_MESSAGE);
            else {
                User.getLocalUser().setName(input);
                JOptionPane.showMessageDialog(Main.mainFrame, "您的昵称已改为：" + input);
                Logger.getLogger().info("Change name into " + input);
                Settings.obj.save();
            }
        });
        JMenuItem sentence=create("设置个性签名",'s',e -> {
            if (Client.getClient().isJoined()){
                JOptionPane.showMessageDialog(Main.mainFrame,"请先退出当前聊天室！");
                return;
            }
            String s=JOptionPane.showInputDialog("输入个性签名:");
            User.getLocalUser().setSentence(s);
            JOptionPane.showMessageDialog(Main.mainFrame,"设置成功！");
        });
        menu1.add(sentence);
        menu1.add(createCheckItem("静音", 'm', IO.isMute, e -> IO.isMute = !IO.isMute));
        menu1.add(createCheckItem("窗口置顶", 'u', false, e -> Main.mainFrame.setAlwaysOnTop(((JCheckBoxMenuItem)e.getSource()).isSelected())));
        menu1.add(createCheckItem("强制窗口置顶",'f',false,e -> {
            if (NativeFrame.isOnTop()){
                NativeFrame.stopAlwaysTop();
            }else {
                NativeFrame.alwaysOnTop();
            }
        }));
        menu1.add(nameItem);
        menu1.addSeparator();
        menu1.add(create("导出聊天记录", e -> IO.backupChat()));
        menu1.add((create("清除聊天记录", e -> client.getScreen().setText(null))));
        menu1.add(create("清除所有人聊天纪录", e -> {
            if (User.getLocalUser().getPermission() != LocalEnum.Permission_OWNER) {
                JOptionPane.showMessageDialog(Main.mainFrame, "你没有权限执行该命令！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            client.sendMessage(Events.getClear());
        }));
        menu1.addSeparator();
        menu1.add(create("字体大小", e -> {
            String var = JOptionPane.showInputDialog("当前大小：" + Settings.obj.fontSize + "\n输入字体大小：");
            if (var == null || var.equals("")) return;
            try {
                Settings.obj.fontSize = Integer.parseInt(var);
                JOptionPane.showMessageDialog(Main.mainFrame, "修改成功！请重启程序");
                Settings.obj.save();
            } catch (NumberFormatException e1) {
                JOptionPane.showMessageDialog(Main.mainFrame, "输入一个整数！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }));
        menu1.add(create("个人信息", e -> User.getLocalUser().showFrame()));
        menu2 = new Menu("服务器(S)") {
            @Override
            public void paint(Graphics g) {
                startItem.setEnabled(!Server.getServer().on);
                stopItem.setEnabled(Server.getServer().on);
                super.paint(g);
            }
        };
        startItem = create("创建服务器", 'm', e -> Server.getServer().start());
        menu2.add(startItem);
        stopItem = create("关闭服务器", 'l', false, e -> Server.getServer().close());
        menu2.add(stopItem);
        menu2.addSeparator();
        menu2.add(create("设置服务器端口", 'p', e -> {
            if (Server.getServer().on) {
                JOptionPane.showMessageDialog(Main.mainFrame, "你无法在服务器开启时改变端口！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String input = JOptionPane.showInputDialog(Main.mainFrame, "当前端口：" + Settings.obj.serverPort + "\n输入新的端口号：\t\t\t", "设置端口", JOptionPane.PLAIN_MESSAGE);
            if (input == null)
                return;
            int port;
            try {
                port = Net.toIntPort(input);
                Settings.obj.serverPort = port;
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(Main.mainFrame, "非法端口！\t\t\t\t", "错误", JOptionPane.ERROR_MESSAGE);
            }
        }));
        menu2.add(create("Remove Ban", e -> {
            String in = JOptionPane.showInputDialog("输入解封IP：");
            if (in == null) return;
            Server.getServer().removeBan(in);
            JOptionPane.showMessageDialog(Main.mainFrame, "已解封");
        }));

        menu5 = new Menu("工具(T)");
        menu5.add(create("Fuck极域", e -> {
            try {
                Runtime.getRuntime().exec("taskkill /f /im studentmain.exe");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }));
        menu5.add(create("检查更新",e -> new ConnectFrame("下载", source -> Client.getClient().update((String) source[0], (Integer) source[1])).setVisible(true)));
        menu5.add(create("滑动关机（Windows10）", e -> {
            try {
                Runtime.getRuntime().exec("SlideToShutDown");
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(Main.mainFrame, "当前系统不支持该功能！");
                ioException.printStackTrace();
                Logger.getLogger().err(ioException);
            }
        }));
        menu5.addSeparator();
        menu5.add(create("查看日志", e -> {
            JFrame frame = new JFrame("日志");
            frame.setSize(800, 500);
            MainFrame.putMiddle(frame);
            JTextArea area = new JTextArea();
            frame.add(new JScrollPane(area), BorderLayout.CENTER);
            area.setLineWrap(true);
            area.setAutoscrolls(true);
            area.setEditable(false);
            area.setFont(new Font("微软雅黑", Font.PLAIN, 15));
            area.setText(Logger.getLogger().getStringWriter().toString());
            Logger.getLogger().area = area;
            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    Logger.getLogger().area = null;
                }
            });
            frame.setVisible(true);
        }));
        menu5.add(create("清理内存", e -> System.gc()));
//        menu5.add(create("Dll Loader",e -> {
//            File file=IO.openFile(new FileNameExtensionFilter("DLL(*.dll)","dll"));
//            if (file==null)
//                return;
//            String name=JOptionPane.showInputDialog("输入函数名称：(半角逗号分隔)\n警告：数字开头的函数名可能会造成程序死锁");
//            if (name==null)
//                return;
//            String[] func=name.trim().split(",");
//            int r=DllBridge.runDll(file.getAbsolutePath(),func);
//            switch (r) {
//                case 0 -> JOptionPane.showMessageDialog(Main.mainFrame, "加载Dll失败");
//                case 1 -> JOptionPane.showMessageDialog(Main.mainFrame, "所有函数加载成功！");
//                case 2 -> JOptionPane.showMessageDialog(Main.mainFrame, "部分函数加载失败");
//            }
//        }));
        menuPlugin=new Menu("插件(P)");
        menuPlugin.add(create("插件管理器",'m',e -> PluginManager.showDialog()));
        menuPlugin.addSeparator();
<<<<<<< Updated upstream
        menu6=new JMenu("关于(A)");
        menu6.add(create("Github仓库",e -> IO.openHttp("https://github.com/BobbyWch/Chatroom/tree/java")));
        menu6.add(create("Github中国",e -> IO.openHttp("https://hub.fastgit.xyz/BobbyWch/Chatroom/tree/java")));
        menu6.add(create("进入官网",e -> IO.openHttp("http://bobbyschatroom.top/")));
        menu1.setMnemonic('c');
        menu2.setMnemonic('s');
        menu5.setMnemonic('T');
        menuPlugin.setMnemonic('p');
        menu6.setMnemonic('a');
=======
        menu5=new Menu("背景(B)",'b');
        menu5.add(create("设置聊天背景",e -> ChatArea.getInstance().setBgImage(IO.openImage(),"chat")));
        menu5.add(create("设置文本框背景",e -> InputPane.obj.setBgImage(IO.openImage(),"input")));
        menu5.add(create("设置文件列表背景",e -> FileList.obj.setBgImage(IO.openImage(),"file")));
        menu5.addSeparator();
        menu5.add(create("如何清除背景？",e->{
            try {
                Desktop.getDesktop().open(new File("res/background"));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            MainFrame.msg("在程序安装目录中的\"res/background\"文件夹中找到对应文件删除，然后重启程序");
        }));

        menu6 =new Menu("关于(A)",'a');
        menu6.add(create("Github仓库", e -> IO.openHttp("https://github.com/BobbyWch/Chatroom/tree/java")));
        menu6.add(create("Github中国", e -> IO.openHttp("https://hub.fastgit.xyz/BobbyWch/Chatroom/tree/java")));
        menu6.add(create("进入官网", e -> IO.openHttp("http://bobbyschatroom.top/")));
>>>>>>> Stashed changes
        add(menu1);
        add(menu2);
        add(menu5);
        add(menuPlugin);
        add(menu5);
        add(menu6);
    }

    public static void flush() {
        Settings set = Settings.obj;
        if (set.lastIP == null || set.lastPort == -1) {
            fastItem.setText("快速加入");
            fastItem.setEnabled(false);
            return;
        }
        fastItem.setText("快速加入 IP:" + set.lastIP + ":" + set.lastPort);
        fastItem.setEnabled(true);
    }

    public static JMenuItem create(String title, ActionListener listener) {
        JMenuItem item = new JMenuItem(title);
        item.setFont(LocalEnum.FONT_MENU);
        item.addActionListener(listener);
        return item;
    }

    public static JMenuItem create(String title, char mnemonic, ActionListener listener) {
        JMenuItem item = new JMenuItem(title + '(' + Character.toUpperCase(mnemonic) + ')');
        item.setMnemonic(mnemonic);
        item.addActionListener(listener);
        item.setFont(LocalEnum.FONT_MENU);
        return item;
    }

    public static JMenuItem create(String title, char mnemonic, boolean enabled, ActionListener listener) {
        JMenuItem item = create(title, mnemonic, listener);
        item.setEnabled(enabled);
        return item;
    }

    public static JCheckBoxMenuItem createCheckItem(String title, char mnemonic, boolean selected, ActionListener listener) {
        JCheckBoxMenuItem item = new JCheckBoxMenuItem(title + '(' + Character.toUpperCase(mnemonic) + ')');
        item.setSelected(selected);
        item.setFont(LocalEnum.FONT_MENU);
        item.setMnemonic(mnemonic);
        item.addActionListener(listener);
        return item;
    }

    public static class Menu extends JMenu {
        public Menu(String name) {
            super(name);
            setFont(LocalEnum.FONT_MENU);
        }
    }
}