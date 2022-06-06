package test;

import cr.io.IO;
import cr.ui.comp.ScrollPane;
import cr.ui.frame.ImgFrame;
import cr.ui.frame.ScareFrame;
import cr.util.user.User;
import cr.tool.Logger;
import cr.tool.Settings;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;

public class Test {
    public static void main(String... args) throws IOException {
        loadLocalEnum();
        ScareFrame.scare(Toolkit.getDefaultToolkit().getImage(IO.urlOfRes("res/img/User.jpg")),2000);
        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ScareFrame.scare(Toolkit.getDefaultToolkit().getImage(IO.urlOfRes("res/img/UserBg.jpg")),2000);
//        label.getPreferredSize()
    }

    private static void userFrame() {

    }
    private static void loadLocalEnum(){
        Logger.init();
        Settings.init();
    }
}