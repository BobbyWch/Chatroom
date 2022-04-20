package cr.ui.popmenu;

import cr.Main;

import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @author Bobbywang
 * @date 2021-09-01 07:23
 */
public final class IconPopMenu extends PopupMenu {
    public IconPopMenu() {
        super();
        add(create("Show/Hide Main Frame", new Font("Arial", Font.BOLD, 14), e -> {
            if (!Main.mainFrame.isActive())
                Main.mainFrame.disHide();
            else
                Main.mainFrame.doHide();
        }));
        addSeparator();
        add(create("Exit", new Font("Arial", Font.PLAIN, 14), e -> {
            Main.exit();
            Runtime.getRuntime().exit(0);
        }));
    }

    private MenuItem create(String title, Font font, ActionListener listener) {
        MenuItem item = new MenuItem(title);
        item.setFont(font);
        item.addActionListener(listener);
        return item;
    }
}
