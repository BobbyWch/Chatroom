package cr.events;

import cr.LocalEnum;
import cr.data.ColorDocument;
import cr.data.FileInfo;
import cr.events.action.*;
import cr.events.file.FileEvent;
import cr.events.file.FileRequest;
import cr.util.user.User;
import cr.util.user.UserInfo;
import cr.util.user.UserManager;

import java.io.File;
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