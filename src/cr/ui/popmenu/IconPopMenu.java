package cr.ui.popmenu;

import cr.LocalEnum;
import cr.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * @author Bobbywang
 * @date 2021-09-01 07:23
 */
public final class IconPopMenu extends JPopupMenu {
    public IconPopMenu() {
        super();
        add(create("显示/隐藏主窗口", LocalEnum.FONT_MENU, e -> {
            if (!Main.mainFrame.isActive())
                Main.mainFrame.disHide();
            else
                Main.mainFrame.doHide();
        }));
        addSeparator();
        add(create("退出程序", LocalEnum.FONT_MENU, e -> {
            Main.exit();
            Runtime.getRuntime().exit(0);
        }));
        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                setVisible(false);
            }
        });
    }

    private JMenuItem create(String title, Font font, ActionListener listener) {
        JMenuItem item = new JMenuItem(title);
        item.setFont(font);
        item.addActionListener(listener);
        return item;
    }
}