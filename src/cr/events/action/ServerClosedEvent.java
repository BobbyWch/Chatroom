package cr.events.action;

import cr.data.ColorDocument;
import cr.events.Event;
import cr.io.IO;
import cr.util.Client;
import cr.util.Server;

import java.io.Serial;

public final class ServerClosedEvent extends Event {
    @Serial
    private static final long serialVersionUID = 99422364L;

    public ServerClosedEvent() {
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
