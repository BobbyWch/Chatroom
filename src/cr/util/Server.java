package cr.util;

import cr.LocalEnum;
import cr.data.FileInfo;
import cr.io.Connection;
import cr.util.user.User;
import cr.util.user.UserInfo;
import cr.util.user.UserManager;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

/**
 * @author Bobbywang
 * @date 2021-10-14 21:24
 */
public final class Server implements DocumentCreator {
    private static Server instance = null;

    private Server() {
        tempDir = new File("buffer/server");
        if (!tempDir.exists())
            tempDir.mkdirs();
    }

    public static void init() {
        long t=System.currentTimeMillis();
        instance = new Server();
        System.out.println("Server init:"+(System.currentTimeMillis()-t)+"ms");
    }

    public static Server getServer() {
        return instance;
    }

    public final HashSet<String> bans = new HashSet<>();
    public final UserManager manager = new UserManager();
    public final ArrayList<Listener> listeners = new ArrayList<>();
    public boolean on = false;
    public ColorDocument text = null;
    private Thread listenThd = null;
    private Thread liveThd = null;
    private final Logger logger = Logger.getLogger();
    public final ArrayList<FileInfo> files = new ArrayList<>();

    /* Server Info */
    private int port;
    final String serverName = "A Server";
    String ownerName;
    final String ip = LocalEnum.IP;
    final String version = LocalEnum.VERSION;
    /* End */

    private ServerSocket serverSocket = null;

    private final Runnable liveRunnable = () -> {
        MulticastSocket ms = null;
        try {
            ms = new MulticastSocket(55599);
            ms.setTimeToLive(5);
            ms.joinGroup(LocalEnum.liveAd);

            String splitter = "\n\t";
            String s = serverName + splitter +
                    ownerName + splitter + ip +
                    splitter + port + splitter + version;
            byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
            DatagramPacket p = new DatagramPacket(bytes, bytes.length, LocalEnum.liveAd, 55599);
            while (on) {
                ms.send(p);
                Thread.sleep(2500L);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Objects.requireNonNull(ms).close();
    };
    private final Runnable lisRunnable = () -> {
        Socket socket = null;
        while (on) {
            try {
                if (serverSocket == null)
                    serverSocket = new ServerSocket(port,5, InetAddress.getLocalHost());
                socket = serverSocket.accept();
            } catch (IOException e) {
                if (e.getMessage().equals("socket closed"))
                    return;
                logger.err(e);
                e.printStackTrace();
            }
            if (socket != null) {
                new Listener(new Connection(socket, LocalEnum.SERVER));
            }
        }
    };

    @Override
    public void clear(Event lastEvent) {
        text = new ColorDocument(this);
        if (lastEvent instanceof ClearEvent clear) {
            text.setManager(manager);
            text.appendLine(clear.getUser().getName() + "清除了聊天记录", Event.sysMsg);
        }
    }

    public void start() {
        on = true;
        clearDownloads();
        text = new ColorDocument(this);
        port = Settings.obj.serverPort;
        if (listenThd == null) {
            listenThd = new Thread(lisRunnable);
            listenThd.start();
        }
        logger.info("Server started at [Port:" + port + ']');
        text.setManager(manager);
        User.getLocalUser().setPermission(LocalEnum.Permission_OWNER);
        Client.getClient().join(LocalEnum.IP, port);
        setOwner(User.getLocalUser().getInfo());
        if (liveThd == null) {
            liveThd = new Thread(liveRunnable);
            liveThd.start();
        }
    }

    public void clearDownloads() {
        File[] files = tempDir.listFiles();
        for (File f : files) {
            f.delete();
        }
    }

    public void close() {
        on = false;
        if (listenThd != null) {
            listenThd = null;
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            serverSocket = null;
        }
        if (liveThd != null) {
            liveThd.interrupt();
            liveThd = null;
        }
        sendMessage(Events.getClosed());
        if (!listeners.isEmpty()) {
            for (Listener var : listeners) {
                var.clear();
            }
        }
        listeners.clear();
        manager.clear();
        bans.clear();
        files.clear();
    }

    public void sendMessage(Event e) {
        text.append(e);
        for (Listener var : listeners) {
            var.con.writeMessage(e);
        }
    }

    public void onJoin(User user) {
        manager.addUser(user);
        if (manager.isOwner(user)) {
            sendMessage(Events.getPermission(user.getInfo(), LocalEnum.Permission_OWNER));
        }
    }

    public void addBan(User user) {
        bans.add(user.getIp());
        sendMessage(Events.getKick(LocalEnum.SERVER, user.getInfo(), null));
    }

    public void removeBan(String ip) {
        bans.remove(ip);
    }

    public void isBanned(User user) {
        bans.contains(user.getIp());
    }

    public void onLeave(User user) {
        manager.removeUser(user);
        System.out.println("leave:"+user.getName());
    }

    public void setOwner(UserInfo owner) {
        manager.setOwner(owner);
        ownerName = owner.getName();
    }

    public File getFile(FileInfo info) {
        for (int i = 0; i < files.size(); i++) {
            if (info.equals(files.get(i)))
                return new File(tempDir,files.get(i).filename);
        }
        return null;
    }

    public final File tempDir;

    public final class Listener extends Thread {
        final Connection con;
        public User user;

        @Override
        public void run() {
            Event e;
            try {
                while (on) {
                    e = con.readMessage();
                    e.server(Server.this,this);
                    if (e.isPublic)
                        sendMessage(e);
                }
            } catch (EOFException e1) {
                if (!on)
                    return;
                listeners.remove(this);
                if (user != null) {
                    sendMessage(Events.getLeave(user));
                    onLeave(user);
                    logger.info(user.getName() + " Left[IP："
                            + user.getIp() + "]. Server size:" + manager.size());
                }
            }catch (IOException|ClassNotFoundException e2){
                e2.printStackTrace();
                logger.err(e2);
            }
        }

        public Listener(Connection con) {
            this.con = con;
            start();
        }

        public void clear() {
            user = null;
            con.close();
        }
    }
}