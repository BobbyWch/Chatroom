package cr.events.action;

import cr.data.FileInfo;
import cr.events.Event;
import cr.io.IO;
import cr.ui.comp.FileList;
import cr.util.Client;
import cr.data.ColorDocument;
import cr.util.Server;
import cr.util.user.UserInfo;

import java.io.File;
import java.io.IOException;
import java.io.Serial;

public class FileEvent extends Event {
    @Serial
    private static final long serialVersionUID = 994545454L;
    public final FileInfo fileInfo;

    public FileEvent(File file, UserInfo sender) {
        this(new FileInfo(file, sender));
    }

    public FileEvent(FileInfo info) {
        this.fileInfo = info;
    }

    @Override
    public void client(Client clt) {
        clt.files.add(fileInfo);
        FileList.obj.addFile(fileInfo);
    }

    @Override
    public void server(Server svr, Server.Listener l) throws IOException {

    }

    @Override
    public void display(ColorDocument cd) {
        cd.appendLine(fileInfo.sender.getName() + "上传了文件：" + fileInfo.name, sysMsg);
        IO.playSound();
    }
}