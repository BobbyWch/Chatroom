package cr.util;

import cr.*;
import cr.data.ColorDocument;
import cr.data.FileInfo;
import cr.events.*;
import cr.events.action.ClearEvent;
import cr.inter.DocumentCreator;
import cr.io.Connection;
import cr.io.IO;
import cr.tool.Logger;
import cr.tool.Settings;
import cr.ui.comp.ChatArea;
import cr.ui.comp.FileList;
import cr.ui.frame.ImgFrame;
import cr.ui.frame.MainFrame;
import cr.ui.comp.UserList;
import cr.ui.XMenuBar;
import cr.util.user.User;
import cr.util.user.UserInfo;
import cr.util.user.UserManager;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * @author Bobbywang
 * @date 2021-10-14 21:23
 */
public final class Client implements DocumentCreator {
    private Client() {
        user = User.getLocalUser();
        userInfo = user.getInfo();
    }

    private static Client instance = null;

    public static void init() {
        long t = System.currentTimeMillis();
        instance = new Client();
        System.out.println("Client init:" + (System.currentTimeMillis() - t) + "ms");
    }

    public static Client getClient() {
        return instance;
    }

    private String ip = null;
    private int port = -1;
    public final User user;
    public final UserInfo userInfo;
    private Connection con = null;
    private boolean joined = false;
    private ChatArea screen;
    private ColorDocument text = new ColorDocument(this);
    public final Logger logger = Logger.getLogger();
    public UserManager users;
    public final UserList userList = UserList.getInstance();
    public final ArrayList<FileInfo> files = new ArrayList<>();

    private Thread lisThd = null;

    @Override
    public void clear(Event lastEvent) {
        text = new ColorDocument(this);
        if (lastEvent instanceof ClearEvent clear) {
            text.reInit(users, this);
            text.appendLine(clear.getUser().getName() + "清除了聊天记录", Event.sysMsg);
            screen.setDocument(text);
        }
    }

    public void join(String ip, int port) {
        if (joined)
            leave(true);
        joined = true;
        UserList.getInstance().clear();
        this.ip = ip;
        this.port = port;

        try {
            Socket socket = new Socket(ip, port);
            connect(socket);
            Settings.obj.lastIP = ip;
            Settings.obj.lastPort = port;
            XMenuBar.flush();
        } catch (IOException e) {
            logger.info("Connect failed.");
            JOptionPane.showMessageDialog(Main.mainFrame, "连接失败。请检查端口和IP地址，或服务器是否开启。");
            leave(false);
        }
    }

    public void leave(boolean bool) {
        joined = false;
        if (lisThd != null) {
            lisThd = null;
        }
        if (con != null)
            con.close();
        if (bool) {
            logger.info("Leave chat server.");
            text.appendLine("您已离开聊天室", Event.userMsg);
        }
        UserList.getInstance().clear();
        files.clear();
        FileList.obj.clear();
        con = null;
    }

    private void onMessage(Event e) {
        e.client(this);
        text.append(e);
        if (e instanceof Ack a) {
            System.out.println(a.dsp);
        }
        ChatArea.getInstance().roll();
        checkTray();
    }

    public void updateProperties(Ack a) {
        text = a.getText();
        users = a.getUsers();
        users.setUserList(UserList.getInstance());
        users.removeUser(user);
        users.addUser(user);
        text.reInit(users, this);
        screen.setDocument(text);

        files.clear();
        files.addAll(a.files);
        for (FileInfo f:a.files){
            FileList.obj.addFile(f);
        }
    }

    public void update(String ip, int port) {

        try {
            Socket socket = new Socket(ip, port);
            Connection con = new Connection(socket, LocalEnum.USER_ALL);
            con.writeMessage(Events.getUpdateReq());
            InputStream is = socket.getInputStream();
            var fs = new FileOutputStream("prog/ChatRoom.jar");
            IO.outPutInput(is, fs);
            socket.close();
            fs.close();
        } catch (IOException e) {
            logger.err(e);
            e.printStackTrace();
        }
        JOptionPane.showMessageDialog(Main.mainFrame, "更新成功！请重启程序");
    }

    public void download(int id) {
        FileInfo file = getFile(id);
        if (file == null) {
            JOptionPane.showMessageDialog(Main.mainFrame, "该文件不存在！:(");
            return;
        }
        if (file.isImg){
            byte[] bytes;
            bytes=file.getCache();
            if (bytes==null) {
                try {
                    Socket socket = new Socket(ip, port);
                    Connection con = new Connection(socket, LocalEnum.USER_ALL);
                    con.writeMessage(new DownRequest(file));
                    file.writeCache(socket.getInputStream());
                    socket.close();
                    bytes= file.getCache();
                } catch (IOException ioException) {
                    logger.err(ioException);
                    ioException.printStackTrace();
                }
            }
            ImageIcon icon = new ImageIcon(bytes);
            ImgFrame.showImage(icon.getImage(), e -> {
                File f = IO.saveFile(null, file.name);
                if (f == null) return;
                try (var fs = new FileOutputStream(f);
                var is=file.getCacheStream()) {
                    IO.outPutInput(is,fs);
                } catch (IOException ioException) {
                    logger.err(ioException);
                    ioException.printStackTrace();
                }
            });

        }else {
            File f = IO.saveFile(null, file.name);
            if (f == null) return;
            try (var fs = new FileOutputStream(f)) {
                Socket socket = new Socket(ip, port);
                Connection con = new Connection(socket, LocalEnum.USER_ALL);
                con.writeMessage(new DownRequest(file));
                InputStream is = socket.getInputStream();
                IO.outPutInput(is, fs);
                socket.close();
            } catch (IOException ioException) {
                logger.err(ioException);
                ioException.printStackTrace();
            }
        }
    }

    public void upload(File file) {
        if (file == null) return;
//        text.appendLine("正在上传，请稍后……", Event.userMsg);
        try (var socket = new Socket(ip, port);
             var fs = new FileInputStream(file)) {
            Connection con = new Connection(socket, LocalEnum.USER_ALL);
            con.writeMessage(Events.getFileRequest(file));
            OutputStream os = socket.getOutputStream();
            IO.outPutInput(fs, os);
            os.close();
        } catch (IOException e) {
            logger.err(e);
            e.printStackTrace();
        }
    }

    /**
     * @noinspection ForLoopReplaceableByForEach
     */
    private FileInfo getFile(int id) {
        for (int i = 0; i < files.size(); i++) {
            if (files.get(i).id == id)
                return files.get(i);
        }
        return null;
    }

    private void connect(Socket s) {
        con = new Connection(s, LocalEnum.USER_ALL);
        if (lisThd == null) {
            lisThd = new Thread(runnable);
            lisThd.start();
        }
        con.writeMessage(Events.getRequest());
    }

    private final Runnable runnable = () -> {
        Event m;
        if (con != null) {
            try {
                while (joined) {
                    if ((m = con.readMessage()) != null) {
                        onMessage(m);
                    }
                }
            } catch (Exception e) {
                if (joined) {
                    logger.info("Lost Connection. Try to reconnect.");
                    logger.err(e);
                    leave(false);
                    join(ip, port);
                    e.printStackTrace();
                } else {
                    if (e.getMessage().equals("Socket closed"))
                        return;
                    e.printStackTrace();
                    logger.err(e);
                }
            }
        }
    };

    private void checkTray() {
        if (!Main.mainFrame.isActive()) {
            Main.mainFrame.requestFocus();
            if (!MainFrame.hasMessage)
                Main.executor.execute(Main.mainFrame.flush);
        }
    }

    public void sendMessage(Event e) {
        con.writeMessage(e);
    }

    public void kick(User user, String reason) {
        sendMessage(Events.getKick(this.user.getInfo(), user.getInfo(), reason));
    }

    public void say(String msg, UserInfo recv) {
        sendMessage(Events.getMsg(recv, msg));
    }

    public boolean isJoined() {
        return joined;
    }

    public void setScreen(ChatArea screen) {
        this.screen = screen;
    }

    public ChatArea getScreen() {
        return screen;
    }

    public ColorDocument getDocument() {
        return text;
    }
}