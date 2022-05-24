package cr.ui.comp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class ScrollPane extends JPanel {
    private final JPanel pane=new JPanel(null);
//    private int y=0;
    public ScrollPane(){
        super(null);
        add(pane).setBounds(0,0,getWidth(),getHeight());
        pane.setBackground(Color.blue);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                pane.setBounds(0,pane.getY(),getWidth(),getHeight());
            }
        });
        pane.addMouseWheelListener(e -> pane.setLocation(0,pane.getY()+e.getWheelRotation()*e.getScrollAmount()*2));
    }

    public void addComponent(Component c){
        int size=pane.getComponentCount();
        System.out.println(getBounds());
        if (size==0){
            pane.add(c).setBounds(0,0, pane.getWidth(), c.getHeight());
        }else {
            Component last = pane.getComponent(pane.getComponentCount() - 1);
            pane.add(c).setBounds(0,last.getY()+last.getHeight(), pane.getWidth(), c.getHeight());
        }
    }
}
