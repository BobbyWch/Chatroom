package cr.events;

import cr.LocalEnum;
import cr.data.ColorDocument;
import cr.io.IO;
import cr.util.Client;
import cr.util.Server;
import cr.util.user.User;
import cr.util.user.UserInfo;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serial;

public final class Request extends Event {
    @Serial
    private static final long serialVersionUID = 3394224L;
    private final String version;
    private final UserInfo info;
    final String ip;
    final boolean isUpdate;

    public Request(String version, User user, boolean isUpdate) {
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
