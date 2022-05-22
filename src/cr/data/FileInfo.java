package cr.data;

import cr.io.IO;
import cr.util.user.UserInfo;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public final class FileInfo implements java.io.Serializable {
    @Serial
    private static final long serialVersionUID = 943444364L;
    private static final SimpleDateFormat format = new SimpleDateFormat("ddhhmmss@");
    public static final File cacheDir = new File("buffer/FileCache");

    public static void init() {
        if (cacheDir.exists()) {
            for (File f : cacheDir.listFiles()) {
                f.delete();
            }
        } else {
            cacheDir.mkdir();
        }
    }

    public final String name;
    public final long len;
    public final UserInfo sender;
    public final int id;
    public final String path;
    public final String filename;
    public final boolean isImg;

    public FileInfo(File file, UserInfo sender) {
        this.name = file.getName();
        this.len = file.length();
        this.sender = sender;
        this.path = file.getAbsolutePath();

        this.filename = format.format(new Date()) + name;
        isImg = name.endsWith(".jpg") || name.endsWith(".jpeg") || name.endsWith(".png") || name.endsWith(".bmp") || name.endsWith(".wbmp");
        this.id = name.hashCode() * sender.hashCode() * filename.hashCode();
    }

    public String getLength() {
        if (len >= 1024 * 1024 * 1024)
            return String.format("%.2f", len / 1024 / 1024 / 1024.0) + "GB";
        else if (len >= 1024 * 1024)
            return String.format("%.2f", len / 1024 / 1024.0) + "MB";
        else if (len >= 1024)
            return String.format("%.2f", len / 1024.0) + "KB";
        else
            return len + "B";
    }

    public void writeCache(InputStream is) {
        try {
            var fs = new FileOutputStream(new File(cacheDir, filename));
            IO.outPutInput(is, fs);
            fs.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public byte[] getCache() {
        File f = new File(cacheDir, filename);
        if (f.exists()) {
            byte[] bs = new byte[((int) f.length())];
            try {
                InputStream fs = new FileInputStream(f);
                fs.read(bs);
                fs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bs;
        }
        return null;
    }

    public InputStream getCacheStream() {
        try {
            return new FileInputStream(new File(cacheDir, filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        FileInfo fileInfo = (FileInfo) o;
        if (len != fileInfo.len) return false;
        if (id != fileInfo.id) return false;
        if (!Objects.equals(name, fileInfo.name)) return false;
        return Objects.equals(sender, fileInfo.sender);
    }
}