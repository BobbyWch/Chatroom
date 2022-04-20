package cr.data;

import cr.util.user.UserInfo;

import java.io.File;
import java.io.Serial;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public final class FileInfo implements java.io.Serializable{
    @Serial
    private static final long serialVersionUID = 943444364L;
    private static final SimpleDateFormat format=new SimpleDateFormat("ddhhmmss@");

    public final String name;
    public final long len;
    public final UserInfo sender;
    public final int id;
    public final String path;
    public final String filename;

    public FileInfo(File file,UserInfo sender){
        this.name=file.getName();
        this.len=file.length();
        this.sender=sender;
        this.path=file.getAbsolutePath();

        this.filename=format.format(new Date())+name;
        this.id= name.hashCode()*sender.hashCode()*filename.hashCode();
    }
    public FileInfo(File file,FileInfo old){
        this.name=old.name;
        this.len=old.len;
        this.sender=old.sender;
        this.path=file.getAbsolutePath();
        this.id=old.id;
        this.filename=old.filename;
    }
    public String getLength(){
        if (len>=1024*1024*1024)
            return String.format("%.2f",len/1024/1024/1024.0)+"GB";
        else if (len>=1024*1024)
            return String.format("%.2f",len/1024/1024.0)+"MB";
        else if (len>=1024)
            return String.format("%.2f",len/1024.0)+"KB";
        else
            return len+"B";
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
