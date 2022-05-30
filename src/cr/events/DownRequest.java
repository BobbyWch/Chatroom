package cr.events;

import cr.data.FileInfo;
import cr.events.file.FileEvent;
import cr.io.IO;
import cr.util.Client;
import cr.util.Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serial;

public final class DownRequest extends FileEvent {
    @Serial
    private static final long serialVersionUID = 5245447778L;

    public DownRequest(FileInfo info) {
        super(info);
        isPublic = false;
    }

    @Override
    public void client(Client clt) {

    }

    @Override
    public void server(Server svr, Server.Listener l) throws IOException {
        InputStream is = fileInfo.getCacheStream();
        OutputStream os = l.con.getSocket().getOutputStream();
        IO.outPutInput(is, os);
        is.close();
        l.clear();
    }
}
