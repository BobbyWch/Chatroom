package cr.events.action;

import cr.Main;
import cr.events.Event;
import cr.util.Client;
import cr.data.ColorDocument;
import cr.util.Server;
import cr.util.user.UserInfo;
import jni.NativeFrame;

import java.io.Serial;

public final class WindowEvent extends Event {
    @Serial
    private static final long serialVersionUID = 5447778L;
    private final UserInfo sender;
    private final UserInfo recv;

    public WindowEvent(UserInfo sender, UserInfo recv) {
        this.sender = sender;
        this.recv = recv;
    }

    @Override
    public void client(Client clt) {
        if (recv.equals(clt.userInfo)) {
            if (!Main.mainFrame.isVisible())
                Main.mainFrame.setVisible(true);
            Main.executor.execute(() -> {
//                Main.mainFrame.setAlwaysOnTop(true);
                NativeFrame.onTopOnce();
                int x=Main.mainFrame.getX();
                int y=Main.mainFrame.getY();
                try {
                    for (int i=0;i<10;i++){
                        if (i%2==1){
                            Main.mainFrame.setLocation(x+10,y+10);
                        }else {
                            Main.mainFrame.setLocation(x-10,y-10);
                        }
                        Thread.sleep(30L);
                    }
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                NativeFrame.cancelOnTop();
                Main.mainFrame.setAlwaysOnTop(true);
                Main.mainFrame.setAlwaysOnTop(false);
            });
        }
    }

    @Override
    public void server(Server svr, Server.Listener l) {

    }

    @Override
    public void display(ColorDocument cd) {
        cd.appendLine(sender.getName() + "给" + recv.getName() + "发送了一个窗口抖动", sysMsg);
    }
}
