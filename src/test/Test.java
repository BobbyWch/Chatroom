package test;

import cr.io.IO;
import cr.ui.comp.ScrollPane;
import cr.ui.frame.ImgFrame;
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
        JFrame frame=new JFrame();
        frame.setBounds(250,250,500,400);
        ScrollPane pane=new ScrollPane();
        frame.add(pane,BorderLayout.CENTER);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JLabel label1=new JLabel("sdfdfsfsdf");
        JLabel label=new JLabel("sdddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddddsfsdf");
        label.setSize(10,30);
        label1.setSize(10,30);
        JLabel  label2=new JLabel(new ImageIcon(IO.urlOfRes("res/img/User.jpg")));
        label2.setSize(10,30);
        pane.addComponent(label);
        pane.addComponent(label1);
        pane.addComponent(label2);
        System.out.println(label.getBounds());
        System.out.println(label1.getBounds());
//        label.getPreferredSize()
    }

    private static void userFrame() {

    }
    private static void loadLocalEnum(){
        Logger.init();
        Settings.init();
    }
}