package cr.ui.comp;

import cr.LocalEnum;
import cr.data.FileInfo;
import cr.io.IO;
import cr.util.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Date;

public final class FileList extends JPanel implements ComponentListener {
    public static FileList obj = null;

    public static void init() {
        try {
            obj = new FileList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final ScrollPane pane = new ScrollPane();
    private final JLabel title = new JLabel("文件");

    private FileList() throws IOException {
        super(null);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        title.setFont(LocalEnum.FONT_MENU);
        title.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("res/icon/upload.png")).getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
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
        addComponentListener(this);
    }

    public void addFile(FileInfo info) {
        FileButton b = new FileButton(info);
        b.setSize(50, 20);
        pane.addComponent(b);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        title.setBounds(2, 0, getWidth() - 4, 30);
        pane.setBounds(2, 30, getWidth() - 4, getHeight() - 30);
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

    private static final class FileButton extends JLabel implements MouseListener {
        private final FileInfo f;

        public FileButton(FileInfo file) {
            super(file.name + "   大小：" + file.getLength());
            f = file;
            setFont(LocalEnum.FONT_MENU);
            setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            setToolTipText("文件名称：" + file.name + "；文件大小：" + file.getLength() + "；上传者：" + file.sender.getName() + "；上传时间：" + String.format("%tT", new Date()));
            addMouseListener(this);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            Client.getClient().download(f.id);
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
        pane.removeAll();
    }
}