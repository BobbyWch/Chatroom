package cr.util.user;

import cr.LocalEnum;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

/**
 * @author Bobbywang
 * @date 2021-10-14 21:35
 */
public final class UserInfo implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 99846764L;

    private String name;
    public long id;

    public UserInfo(String name) {
        this.name=(name==null)? "User" + LocalEnum.IP.substring(LocalEnum.IP.indexOf('.') + 1) :name;
        /*
        File file = new File("jre/lib");
        if (!file.exists())
            file.mkdirs();
        file = new File("jre/lib/id");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writeId();
            return;
        }
        try {
            FileInputStream stream = new FileInputStream(file);
            InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);
            BufferedReader br = new BufferedReader(reader);
            String str = br.readLine();
            id = Long.parseLong(str);

            br.close();
            reader.close();
            stream.close();
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
            writeId();
        }

         */
        id=getId();
    }

    public UserInfo(String name, long id) {
        this.name = name;
        this.id = id;
    }

    private static long getId() {
        return new Random().nextLong() ^ System.currentTimeMillis();
    }

    private void writeId() {
        try {
            FileOutputStream stream = new FileOutputStream("jre/lib/id");
            OutputStreamWriter writer = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
            id = getId();
            writer.write(String.valueOf(id));

            writer.flush();
            writer.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object o) {
        UserInfo userInfo = (UserInfo) o;
        if (id != userInfo.id) return false;
        return Objects.equals(name, userInfo.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}