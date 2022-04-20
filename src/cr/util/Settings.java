package cr.util;

import cr.Main;
import cr.io.IO;
import cr.data.MicroRect;
import cr.util.user.User;

import java.io.*;

/**
 * @author Bobbywang
 * @date 2021-07-28 11:36
 */
public final class Settings implements Serializable {
    @Serial
    private static final long serialVersionUID = 24132364L;
    public transient static Settings obj = null;

    public static void init(){
        if (obj == null) {
            long t=System.currentTimeMillis();
            Settings setting=getSettings();
            if (setting==null) {
                obj = new Settings();
                Logger.getLogger().info("ChatRoom.dat not exists. Use default Setting.");
            }
            else {
                obj = setting;
                Logger.getLogger().info("Find ChatRoom.dat. Load Successfully.");
            }
            System.out.println("Setting init:"+(System.currentTimeMillis()-t)+"ms");
        }
    }
    private Settings(){
        defaultValue();
    }
    /************ These are the values ************/
    public String name;
    public int serverPort;
    public int fontSize;
    public boolean isMute;
    /************ These are records ***************/
    public int lastPort;
    public String lastIP;
    public MicroRect mainFrame;
    /****************Function**********************/
    public void defaultValue() {
        name = null;
        serverPort = 50000;
        lastPort = -1;
        lastIP = null;
        fontSize=13;
        mainFrame = null;
        isMute=true;
    }

    public void save() {
        name = User.getLocalUser().getName();
        mainFrame = new MicroRect(Main.mainFrame.getBounds());
        isMute= IO.isMute;
        try {
            FileOutputStream stream=new FileOutputStream("ChatRoom.dat");
            ObjectOutputStream os=new ObjectOutputStream(stream);
            os.writeObject(this);
            os.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
            Logger.getLogger().err(e);
        }
    }
    private static Settings getSettings() {
        File file = new File("ChatRoom.dat");
        if (file.exists()) {
            try {
                FileInputStream fs = new FileInputStream(file);
                ObjectInputStream os = new ObjectInputStream(fs);
                Settings readObject = (Settings) os.readObject();
                os.close();
                fs.close();
                return readObject;
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                Logger.getLogger().err(e);
                return null;
            }
        } else return null;
    }
}