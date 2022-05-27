package cr.events.file;

import cr.data.ColorDocument;
import cr.data.FileInfo;
import cr.io.IO;
import cr.ui.comp.FileList;
import cr.util.Client;
import cr.util.Server;

import java.io.IOException;

public class DeleteEvent extends FileEvent{
    public DeleteEvent(FileInfo info) {
        super(info);
    }

    @Override
    public void client(Client clt) {
        clt.files.remove(fileInfo);
        FileList.obj.removeFile(fileInfo);
    }

    @Override
    public void server(Server svr, Server.Listener l) throws IOException {
        svr.files.remove(fileInfo);
    }

    @Override
    public void display(ColorDocument cd) {
        cd.appendLine("群主删除了文件"+fileInfo.name,sysMsg);
        IO.playSound();
    }
}
