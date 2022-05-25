package cr.events;

import cr.LocalEnum;
import cr.data.ColorDocument;
import cr.io.IO;
import cr.util.Client;
import cr.util.Server;
import cr.util.user.User;
import cr.util.user.UserInfo;

import java.io.Serial;

public final class MessageEvent extends Event {
    @Serial
    private static final long serialVersionUID = 99422554L;
    private final UserInfo sender;
    private final UserInfo receiver;
    private final String msg;

    public MessageEvent(UserInfo sender, UserInfo receiver, String msg) {
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
