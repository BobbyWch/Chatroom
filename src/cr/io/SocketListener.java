package cr.io;

import cr.LocalEnum;
import cr.inter.InputEvent;
import cr.tool.Logger;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;

public final class SocketListener {
    private static final LinkedList<InputEvent> listeners=new LinkedList<>();
    private static MulticastSocket socket=null;
    private static Thread thread=null;
    private static final Runnable run=()->{
        try {
            socket=new MulticastSocket(55599);
            socket.joinGroup(new InetSocketAddress(LocalEnum.liveAd, 0), null);
            byte[] data = new byte[1024];
            DatagramPacket p = new DatagramPacket(data, data.length, LocalEnum.liveAd, 55599);
            while (true) {
                socket.receive(p);
                for (InputEvent l:listeners){
                    l.onEvent(new String(p.getData(), StandardCharsets.UTF_8));
                }
                Thread.sleep(100);
            }
        }catch (InterruptedException ignore){
        }catch (IOException io){
            if (!io.getMessage().equals("Socket closed")) {
                io.printStackTrace();
                Logger.getLogger().err(io);
            }
        }
    };
    public static void addListener(InputEvent l){
        listeners.add(l);
        if (listeners.size()==1)
            start();
    }
    public static void removeListener(InputEvent l){
        listeners.remove(l);
        if (listeners.isEmpty())
            stop();
    }
    private static void start(){
        thread=new Thread(run);
        thread.start();
    }
    private static void stop(){
        thread.interrupt();
        thread=null;
        socket.close();
        socket=null;
    }
}
