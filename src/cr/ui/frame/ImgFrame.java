package cr.ui.frame;

import cr.LocalEnum;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ImgFrame extends JFrame {
    private Image img;
    private final ImgPane pane;
    private ImgFrame(Image img, ActionListener onDownload){
        super("查看图片");
        this.img=img;
        resize();
        pane=new ImgPane(img);
        add(pane,BorderLayout.CENTER);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        MainFrame.putMiddle(this);

        JPanel tools=new JPanel(new GridLayout(1,3,10,10));
        JButton down=new JButton("保存");
        down.setSize(80,30);
        down.setFont(LocalEnum.FONT_MENU);
        down.addActionListener(onDownload);
        JButton rotate=new JButton("旋转");
        rotate.setFont(LocalEnum.FONT_MENU);
        rotate.addActionListener(e -> pane.rotate90());
        JButton scale=new JButton("自动调整");
        scale.setFont(LocalEnum.FONT_MENU);
        scale.addActionListener(e -> pane.autoScale());

        tools.add(down);
        tools.add(rotate);
        tools.add(scale);
        tools.setMaximumSize(new Dimension(999999,40));
        add(tools,BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                obj=null;
            }
        });
    }
    private static final class ImgPane extends JPanel{
        private Image img;
        private double scale=1f;
        private int degree=0;

        private int x=0;
        private int y=0;

        private int oldWidth=0;
        private int oldHeight=0;
        private final Point p = new Point();
        public ImgPane(Image img){
            this.img=img;
            addMouseWheelListener(e -> {
                if (e.getWheelRotation()==1){
                    scale/=1.03;
                    x*=1.03;
                    y*=1.03;
                }else {
                    scale*=1.03;
                    x/=1.03;
                    y/=1.03;
                }
                repaint();
            });
            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (!(e.getX() == p.x && e.getY() == p.y)) {
                        switch (degree) {
                            case 0 -> {
                                x += e.getX() - p.x;
                                y += e.getY() - p.y;
                            }
                            case 90 -> {
                                x += e.getY() - p.y;
                                y -= e.getX() - p.x;
                            }
                            case 180 -> {
                                x -= e.getX() - p.x;
                                y -= e.getY() - p.y;
                            }
                            case 270 -> {
                                x -= e.getY() - p.y;
                                y += e.getX() - p.x;
                            }
                        }
                        p.x=e.getX();
                        p.y=e.getY();
                        repaint();
                    }
                }
            });
            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    p.x = e.getX();
                    p.y = e.getY();
                }
            });
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    if (oldHeight!=0){
                        x+=(getWidth()-oldWidth)/2/scale;
                        y+=(getHeight()-oldHeight)/2/scale;
                    }
                    oldHeight=getHeight();
                    oldWidth=getWidth();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2= ((Graphics2D) g);
            if (degree!=0){
                g2.rotate(Math.toRadians(degree),getWidth()/2,getHeight()/2);
            }
            g2.scale(scale,scale);
            g.drawImage(img,x,y,this);
        }
        public void setImg(Image img){
            this.img=img;
        }
        public void rotate90(){
            degree+=90;
            degree%=360;
            repaint();
        }
        public void autoScale(){
            ImageIcon i=new ImageIcon(img);
            double x= ((double) getWidth())/i.getIconWidth();
            double y= ((double) getHeight())/i.getIconHeight();
            scale= Math.min(x, y);
            if (x>y) {
                this.x = (int) ((getWidth() - i.getIconWidth() * scale) / 2/scale);
                this.y=0;
            } else {
                this.y = (int) ((getHeight() - i.getIconHeight() * scale) / 2/scale);
                this.x=0;
            }
            repaint();
        }
    }
    private void setImg(Image img){
        this.img=img;
        resize();
        pane.setImg(img);
    }
    private void resize(){
        ImageIcon t=new ImageIcon(img);
        if (t.getIconHeight()>t.getIconWidth()){
            setSize(550,680);
        }else {
            setSize(680,550);
        }
    }

    private static ImgFrame obj=null;
    public static void showImage(Image img,ActionListener onDownload){
        if (obj==null) {
            ImgFrame f = new ImgFrame(img,onDownload);
            f.setVisible(true);
            obj=f;
        }else obj.setImg(img);
        obj.pane.autoScale();
    }
}
