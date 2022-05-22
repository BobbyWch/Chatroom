package cr.events.action;

import cr.LocalEnum;
import cr.events.Event;
import cr.util.Client;
import cr.data.ColorDocument;
import cr.util.Server;
import cr.util.user.UserInfo;

import java.io.Serial;

public final class KickEvent extends Event {
    @Serial
    private static final long serialVersionUID = 994324L;
    private final UserInfo sender;
    private final UserInfo receiver;
    private final String reason;

    public KickEvent(UserInfo sender, UserInfo receiver, String reason) {
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
