package cr.ui.comp;

import cr.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Bobbywang
 * @date 2021-07-23 21:47
 */
public final class MainPanel extends JPanel {
    private final JScrollPane chat=new JScrollPane(ChatArea.getInstance());
    private final JScrollPane fileBar=new JScrollPane(FileList.obj);
    private final JScrollPane inputPane=new JScrollPane(InputPane.obj);
    private final JScrollPane userList=new JScrollPane(UserList.getInstance().getPanel());

    public MainPanel() {
        super();
        setBackground(Color.white);
        setLayout(null);
        add(chat);
        add(inputPane);
        add(fileBar);
        add(userList);
        chat.setBorder(BorderFactory.createLineBorder(Color.darkGray,1));
        inputPane.setBorder(BorderFactory.createLineBorder(Color.darkGray,1));
        fileBar.setBorder(BorderFactory.createLineBorder(Color.darkGray,1));
        userList.setBorder(BorderFactory.createLineBorder(Color.darkGray,1));
        userList.setBackground(Color.white);
        userList.setForeground(Color.white);
        fileBar.setBackground(Color.white);
        fileBar.setForeground(Color.white);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                reLayout();
            }
        });
    }
    /**
     * (0,0)             (0.7width,0)         (width,0)
     *
     *
     *
     *                   (0.7width,0.6height)
     * (0,0.8height)
     *
     *
     * (0,height)                             (width,height)
 */
    public void reLayout(){
        int width=getWidth();
        int height=getHeight();
        int wid7= ((int) (width * 0.7));
        int hei6=(int) (height*0.6);
        int hei8=(int) (height*0.8);
        chat.setBounds(0,0, wid7, hei8);
        inputPane.setBounds(0,hei8,wid7,height-hei8);
        fileBar.setBounds(wid7,0,width-wid7,hei6);
        userList.setBounds(wid7,hei6,width-wid7,height-hei6);
        paintComponents(getGraphics());
    }
}
