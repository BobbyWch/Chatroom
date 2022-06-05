package cr.ui.frame;

import cr.LocalEnum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ScareFrame extends JFrame {
    private Image img;
    private ScareFrame(Image img){
        super();
        this.img=img;
        setUndecorated(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,0,d.width,d.height);
        setAlwaysOnTop(true);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                obj=null;
            }
        });
    }
    
    private void setImg(Image img){
        this.img=img;
        repaint();
    }

    @Override
    public void paint(Graphics g){
        super.paint(g);
        g.drawImage(img,0,0,getWidth(),getHeight(),null);
    }

    private static ScareFrame obj=null;
    public static void scare(Image img,int t){
        if (obj==null) {
            obj = new ScareFrame(img);
            obj.setVisible(true);
        }else obj.setImg(img);
    }
}