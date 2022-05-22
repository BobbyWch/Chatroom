package cr.events.action;

import cr.events.Events;
import cr.util.Client;
import cr.util.Server;
import cr.util.user.UserInfo;

import java.io.File;
import java.io.IOException;
import java.io.Serial;

public final class FileRequest extends FileEvent {
    @Serial
    private static final long serialVersionUID = 5245444444L;

    public FileRequest(File file, UserInfo sender) {
        super(file, sender);
        isPublic = false;
    }

    @Override
    public void client(Client clt) {

    }

    @Override
    public void server(Server svr, Server.Listener l) throws IOException {
        fileInfo.writeCache(l.con.getSocket().getInputStream());
//        var os = new FileOutputStream(file1);
        svr.files.add(fileInfo);
//        InputStream is = l.con.getSocket().getInputStream();
//        IO.outPutInput(is, os);
        svr.sendMessage(Events.getFileEvent(fileInfo));
        l.clear();
//        os.close();
    }
}