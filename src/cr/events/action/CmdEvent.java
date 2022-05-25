package cr.events.action;

import cr.data.ColorDocument;
import cr.events.Event;
import cr.io.MShell;
import cr.util.Client;
import cr.util.Server;
import cr.util.user.UserInfo;

import java.io.Serial;

public final class CmdEvent extends Event {
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
