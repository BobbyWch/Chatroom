package cr.events;

import cr.data.ColorDocument;
import cr.util.Client;
import cr.util.Server;
import cr.util.user.UserInfo;

import java.io.IOException;

public class PrivateMsg extends Event{
    private final UserInfo sender;
    private final UserInfo target;

    public PrivateMsg(UserInfo sender, UserInfo target) {
        this.sender = sender;
        this.target = target;
    }

    @Override
    public final void client(Client clt) {

    }

    @Override
    public void server(Server svr, Server.Listener l) throws IOException {

    }

    @Override
    public void display(ColorDocument cd) {

    }
//    public vo
}
