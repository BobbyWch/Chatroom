package cr.inter;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public interface Background {
    default Image getBgImage(final String n){
        File[] fs=new File("res/background").listFiles((dir, name) -> name.startsWith(n));
        if (fs==null||fs.length==0) return null;
        return Toolkit.getDefaultToolkit().getImage(fs[0].getAbsolutePath());
    }
    default void setBgImage(File img, String name){
        if (img==null) return;
        String n=img.getName();
        try {
            Files.copy(img.toPath(),new File("res/background/"+name+n.substring(n.lastIndexOf("."))).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setBgImage(Toolkit.getDefaultToolkit().getImage(img.getAbsolutePath()));
    }
    default void flushImage(String n){
        setBgImage(getBgImage(n));
    }
    void setBgImage(Image img);
}
