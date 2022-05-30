package cr.ui.comp;

import cr.LocalEnum;
import cr.data.FileInfo;
import cr.events.file.DeleteEvent;
import cr.inter.Background;
import cr.io.IO;
import cr.ui.frame.MainFrame;
import cr.util.Client;
import cr.util.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public final class FileList extends JPanel implements ComponentListener, Background {
    public static FileList obj = null;

    public static void init() {
        try {
            obj = new FileList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    private final HashMap<FileInfo,FileButton> map=new HashMap<>();

    public final ScrollPane pane = new ScrollPane();
    private final JLabel title = new JLabel("文件");

    private FileList() throws IOException {
        super(null);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(LocalEnum.FONT_MENU.deriveFont(17f));
        title.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(IO.urlOfRes("res/icon/upload.png"))));
        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if (Client.getClient().isJoined()) {
                    File[] files = IO.openFiles();
                    if (files != null) {
                        for (File f : files) {
                            Client.getClient().upload(f);
                        }
                    }
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                title.setForeground(Color.lightGray);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                title.setForeground(Color.blue);
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                title.setForeground(Color.blue);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                title.setForeground(Color.black);
            }
        });
        add(title);
        add(pane);
        pane.setBgImage(getBgImage("file"));
        addComponentListener(this);
//        pane.setBgImage(Toolkit.getDefaultToolkit().getImage("D:\\Desktop\\微笑照\\IMG_013.jpg"));
    }

    public void addFile(FileInfo info) {
        FileButton b = new FileButton(info);
        b.setSize(50, 20);
        pane.addComponent(b);
    }
    public void removeFile(FileInfo info){
        for (Component c:pane.getComponents()){
            if (c instanceof FileButton b){
                if (b.f.equals(info)) {
                    pane.removeComponent(b);
                    pane.repaint();
                }
            }
        }

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
        public FileButton(FileInfo file) {
            super(file.name + "   大小：" + file.getLength());
            f = file;
            setFont(LocalEnum.FONT_MENU);
            setOpaque(false);
            setToolTipText("文件名称：" + file.name + "；文件大小：" + file.getLength() + "；上传者：" + file.sender.getName() + "；上传时间：" + String.format("%tT", new Date()));
            addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == MouseEvent.BUTTON1) {
                Client.getClient().download(f.id);
            } else if (e.getButton() == MouseEvent.BUTTON3) {
                if (User.getLocalUser().getPermission()==LocalEnum.Permission_OWNER) {
                    if (MainFrame.confirm("确定要删除该文件吗？")) {
                        Client.getClient().sendMessage(new DeleteEvent(f));
                    }
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            this.setForeground(Color.lightGray);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            this.setForeground(Color.blue);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            this.setForeground(Color.blue);
        }

        @Override
        public void mouseExited(MouseEvent e) {
            this.setForeground(Color.black);
        }
    }

    public void clear() {
        pane.clear();
    }
}