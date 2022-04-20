package cr.data;

import java.awt.*;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;

/**
 * @author Bobbywang
 * @date 2021-12-15 21:38
 */
public final class MicroRect implements Externalizable {
    private static final int serialID=23423423;
    public int x, y, width, height;

    public MicroRect(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public MicroRect(Rectangle r) {
        this(r.x, r.y, r.width, r.height);
    }

    public MicroRect() {
    }

    public void setUp(Component c) {
        c.setBounds(x, y, width, height);
    }

    @Override
    public void writeExternal(ObjectOutput out) throws IOException {
        out.writeInt(serialID);
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(width);
        out.writeInt(height);
    }

    @Override
    public void readExternal(ObjectInput in) throws IOException {
        int i=in.readInt();
        if (i!=serialID)
            throw new IOException("failed to verify serial id.");
        x=in.readInt();
        y=in.readInt();
        width=in.readInt();
        height=in.readInt();
    }
}