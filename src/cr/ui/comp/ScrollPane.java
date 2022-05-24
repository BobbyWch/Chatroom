package cr.ui.comp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

public class ScrollPane extends JPanel implements ComponentListener {
    private final JPanel pane = new JPanel(null);
    public ScrollPane() {
        super(null);
        setBackground(Color.white);
        pane.setBackground(Color.white);
        add(pane).setBounds(0, 0, getWidth(), 40);
        addComponentListener(this);
        pane.addMouseWheelListener(e -> {
            if (getHeight()>pane.getHeight()) return;
            int y=pane.getY() - e.getWheelRotation() * e.getScrollAmount() * 3;
            if (y>0){
                y=0;
            }else if (y<getHeight()-pane.getHeight()){
                y=getHeight()-pane.getHeight();
            }
            if (y!=pane.getY()) {
                pane.setLocation(0, y);
            }
        });
    }
    public void autoLayout(Component c){
        int size = pane.getComponentCount();
        if (size == 1) {
            c.setBounds(0, 0, pane.getWidth(), c.getHeight());
        } else {
            Component last = pane.getComponent(pane.getComponentCount() - 1);
            c.setBounds(0, last.getY() + last.getHeight(), pane.getWidth(), c.getHeight());
        }
        if (c.getY() + c.getHeight() > pane.getHeight()) {
            pane.setSize(pane.getWidth(), c.getY() + c.getHeight() + 2);
        }
    }
    public void addComponent(Component c) {
        int size = pane.getComponentCount();
        if (size == 0) {
            pane.add(c).setBounds(0, 0, pane.getWidth(), c.getHeight());
        } else {
            Component last = pane.getComponent(pane.getComponentCount() - 1);
            pane.add(c).setBounds(0, last.getY() + last.getHeight(), pane.getWidth(), c.getHeight());
        }
        if (c.getY() + c.getHeight() > pane.getHeight()) {
            pane.setSize(pane.getWidth(), c.getY() + c.getHeight() + 2);
        }
    }
    public void removeComponent(Component c) {
        if (pane.getComponentCount()==1){
            pane.remove(c);
        }else {

        }
    }

    @Override
    public void componentResized(ComponentEvent e) {
        pane.setBounds(0, 0, getWidth(), pane.getHeight());
        for (Component c:pane.getComponents()){
            c.setSize(pane.getWidth(),c.getHeight());
        }
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}