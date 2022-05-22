package cr.events.action;

import cr.LocalEnum;
import cr.events.Event;
import cr.io.IO;
import cr.util.Client;
import cr.data.ColorDocument;
import cr.util.Server;
import cr.util.user.UserInfo;
import cr.util.user.UserManager;

import java.io.Serial;

public final class PermissionEvent extends Event {
    @Serial
    private static final long serialVersionUID = 9942236444L;
    private final UserInfo user;
    private final int permission;

    public PermissionEvent(UserInfo user, int permission) {
        this.user = user;
        this.permission = permission;
    }

    @Override
    public void client(Client clt) {
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
