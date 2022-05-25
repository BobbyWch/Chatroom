package cr.events;

import cr.data.ColorDocument;
import cr.data.FileInfo;
import cr.util.Client;
import cr.util.Server;
import cr.util.user.UserManager;

import java.io.Serial;
import java.util.ArrayList;

public final class Ack extends Event {
    @Serial
    private static final long serialVersionUID = 99444444L;
    private final boolean passed;
    private final UserManager users;
    private final ColorDocument text;
    public final ArrayList<FileInfo> files;
    public final String dsp;

    public Ack(boolean passed, UserManager users, ColorDocument text, ArrayList<FileInfo> files, String dsp) {
        this.passed = passed;
        this.users = users;
        this.text = text;
        this.files = files;
        this.dsp = dsp;
    }

    public UserManager getUsers() {
        return users;
    }

    public ColorDocument getText() {
        return text;
    }

    @Override
    public void client(Client clt) {
        if (passed) {
            clt.updateProperties(this);
            clt.sendMessage(Events.getJoin());
            clt.logger.info("Join Successfully.");
        } else {
            clt.leave(false);
        }
    }

    @Override
    public void server(Server svr, Server.Listener l) {

    }

    @Override
    public void display(ColorDocument cd) {
        if (!passed) {
            cd.appendLine("连接失败。  原因：" + dsp, userMsg);
            System.out.println("dis");
        }
    }
}
