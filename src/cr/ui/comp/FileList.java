package cr.ui.comp;

import cr.LocalEnum;
import cr.data.FileInfo;
import cr.util.Client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Date;

public final class FileList extends JPanel{
    public static FileList obj = null;

    public static void init() {
        try {
            obj = new FileList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final ScrollPane pane = new ScrollPane();

    private FileList() throws IOException {
        super();
        JLabel title = new JLabel("文件");
        title.setFont(LocalEnum.FONT_MENU);
        title.setIcon(new ImageIcon(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("res/icon/upload.png")).getScaledInstance(20,20,Image.SCALE_SMOOTH)));
        title.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (Client.getClient().isJoined())
                    Client.getClient().upload();
            }
        });
        add(title, BorderLayout.NORTH);
        add(pane, BorderLayout.CENTER);
        pane.setBackground(Color.white);
    }

    public void addFile(FileInfo info) {
        FileButton b = new FileButton(info);
        b.setSize(50, 20);
        pane.addComponent(b);
    }
    private static final class FileButton extends JLabel {
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
//        pane.setBounds(1, title.getHeight() + 10, getWidth() - 2, getHeight() - title.getHeight() - 2);
//        for (Component c : pane.getComponents()) {
//            c.setSize(getWidth() - 3, 20);
//        }
        super.paint(g);
    }
    public void clear(){
        pane.removeAll();
    }
}