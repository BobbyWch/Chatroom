package cr.util;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.io.IOException;

import static cr.util.ColorDocument.createStyle;

public abstract class Event implements java.io.Serializable {
    public final static SimpleAttributeSet sysMsg = createStyle(false, new Color(248, 102, 5), Settings.obj.fontSize);
    public final static SimpleAttributeSet userMsg=createStyle(false, Color.black, Settings.obj.fontSize + 2);

    static {
        StyleConstants.setItalic(sysMsg,true);
    }

    public boolean isPublic=true;

    public abstract void client(Client clt);
    public abstract void server(Server svr,Server.Listener l) throws IOException;
    public abstract void display(ColorDocument cd);
}