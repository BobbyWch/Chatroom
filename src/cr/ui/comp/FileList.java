package cr.ui.comp;

import cr.LocalEnum;
import cr.data.FileInfo;
<<<<<<< Updated upstream
=======
import cr.events.file.DeleteEvent;
import cr.inter.Background;
import cr.io.IO;
import cr.ui.frame.MainFrame;
>>>>>>> Stashed changes
import cr.util.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Date;

<<<<<<< Updated upstream
public final class FileList extends JPanel {
=======
public final class FileList extends JPanel implements ComponentListener, Background {
>>>>>>> Stashed changes
    public static FileList obj = null;

    public static void init() {
        try {
            obj = new FileList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final JLabel title = new JLabel("文件");
    private final JPanel pane = new JPanel(null);
    private int count = 0;

    private FileList() throws IOException {
<<<<<<< Updated upstream
        super();
        title.setFont(LocalEnum.FONT_MENU);
        title.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("res/icon/upload.png")).getScaledInstance(20,20,Image.SCALE_SMOOTH)));
=======
        super(null);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(LocalEnum.FONT_MENU.deriveFont(17f));
        title.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(IO.urlOfRes("res/icon/upload.png"))));
>>>>>>> Stashed changes
        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Client.getClient().isJoined())
                    Client.getClient().upload();
            }
        });
<<<<<<< Updated upstream
        add(title, BorderLayout.NORTH);
        add(pane, BorderLayout.CENTER);
        pane.setBackground(Color.white);
=======
        add(title);
        add(pane);
        pane.setBgImage(getBgImage("file"));
        addComponentListener(this);
//        pane.setBgImage(Toolkit.getDefaultToolkit().getImage("D:\\Desktop\\微笑照\\IMG_013.jpg"));
>>>>>>> Stashed changes
    }

    public void addFile(FileInfo info) {
        System.out.println("add");
        FileButton b = new FileButton(info);
        b.setBounds(1, count * 22, 50, 20);
        pane.add(b);
        repaint();
        count++;
    }

<<<<<<< Updated upstream
    private static final class FileButton extends JLabel {
=======
    }

    @Override
    public void componentResized(ComponentEvent e) {
        title.setBounds(0, 0, getWidth(), 35);
        pane.setBounds(0, 30, getWidth(), getHeight() - 35);
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {
        componentResized(null);
    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }

    @Override
    public void setBgImage(Image img) {
        pane.setBgImage(img);
    }

    private static final class FileButton extends JLabel implements MouseListener {
        public final FileInfo f;
>>>>>>> Stashed changes
        public FileButton(FileInfo file) {
            super(file.name + "   大小：" + file.getLength());
            setFont(LocalEnum.FONT_MENU);
            setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
            setToolTipText("文件名称：" + file.name + "；文件大小：" + file.getLength() + "；上传者：" + file.sender.getName() + "；上传时间：" + String.format("%tT", new Date()));
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    Client.getClient().download(file.id);
                }
            });
        }
    }

    @Override
    public void paint(Graphics g) {
        pane.setBounds(1, title.getHeight() + 10, getWidth() - 2, getHeight() - title.getHeight() - 2);
        for (Component c : pane.getComponents()) {
            c.setSize(getWidth() - 3, 20);
        }
        super.paint(g);
    }
    public void clear(){
        pane.removeAll();
        count=0;
    }
}