package cr;

import cr.util.user.UserInfo;
import cr.tool.Logger;
import cr.tool.Settings;

import java.awt.*;
import java.io.IOException;
import java.net.InetAddress;

/**
 * @author Bobbywang
 * @date 2021-09-07 18:45
 */
public final class LocalEnum {
    private LocalEnum() {
    }

    public static final int Permission_DEFAULT = 3001;
    public static final int Permission_ADMIN = 3002;
    public static final int Permission_OWNER = 3003;

    public static final UserInfo USER_ALL = new UserInfo("user all", 50);
    public static final UserInfo SERVER = new UserInfo("server", 51);
    public static Font FONT_MENU=new Font("微软雅黑", Font.PLAIN, Settings.obj.fontSize);

    public static String VERSION = "u1.001";
    public final static String TITTLE = "Chatroom " + VERSION;
    public static String IP;
    public static InetAddress liveAd;

    static {
        try {
            IP = InetAddress.getLocalHost().getHostAddress();
            liveAd = InetAddress.getByName("224.255.0.0");
        } catch (IOException e2) {
            e2.printStackTrace();
            Logger.getLogger().err(e2);
        }
    }
}