package cr.events.action;

import cr.events.Event;
import cr.io.IO;
import cr.util.Client;
import cr.data.ColorDocument;
import cr.util.Server;
import cr.util.user.UserInfo;

import java.io.Serial;

public final class ClearEvent extends Event {
    @Serial
    private static final long serialVersionUID = 99422333L;
    private final UserInfo user;

    public ClearEvent(UserInfo user) {
        this.user = user;
    }

    public UserInfo getUser() {
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
