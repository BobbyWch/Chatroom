package cr.ui.frame;

import cr.LocalEnum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ScareFrame extends JFrame {
    private Image img;

    private ScareFrame(Image img) {
        super();
        this.img = img;
        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, d.width, d.height);
        setAlwaysOnTop(true);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
    }

    public static void scare(Image img, int t) {
        ScareFrame obj = new ScareFrame(img);
        obj.setVisible(true);
        new Thread(() -> {
            try {
                Thread.sleep(t);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            obj.setVisible(false);
        }).start();
    }
}