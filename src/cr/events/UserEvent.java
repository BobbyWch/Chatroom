package cr.events;

import cr.data.ColorDocument;
import cr.io.IO;
import cr.tool.Logger;
import cr.util.*;
import cr.util.user.User;

import java.io.Serial;

public final class UserEvent extends Event {
    private final User user;
    private final boolean isJoin;
    @Serial
    private static final long serialVersionUID = 994364L;

    public UserEvent(User user, boolean isJoin) {
        this.user = user;
        this.isJoin = isJoin;
    }

    @Override
    public void client(Client clt) {
        if (isJoin) {
            clt.users.addUser(user);
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
