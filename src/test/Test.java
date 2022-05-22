package test;

import cr.ui.frame.ImgFrame;
import cr.util.user.User;
import cr.tool.Logger;
import cr.tool.Settings;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class Test {
    public static void main(String... args) throws IOException {
        System.out.println(Arrays.toString(ImageIO.getReaderFileSuffixes()));
        loadLocalEnum();
        Image img=Toolkit.getDefaultToolkit().getImage(Test.class.getClassLoader().getResource("res/img/UserBg.jpg"));
        ImgFrame.showImage(img,e -> {});
    }

    private static void userFrame() {

    }
    private static void loadLocalEnum(){
        Logger.init();
        Settings.init();
    }
}