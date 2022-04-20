package cr.ui.comp;

import cr.LocalEnum;
import cr.util.Client;

import javax.swing.*;
import javax.swing.text.StyledEditorKit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class InputPane extends JEditorPane {
    public static InputPane obj = null;

    public static void init() {
        obj = new InputPane();
    }

    private InputPane() {
        super();
        StyledEditorKit kit = new StyledEditorKit();
        setEditorKit(kit);
        setDocument(kit.createDefaultDocument());
        setFont(LocalEnum.FONT_MENU);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    if (!Client.getClient().isJoined()) {
                        setText(null);
                        return;
                    }
                    if (getText().trim().equals("")) {
                        setText(null);
                        return;
                    }
                    Client.getClient().say(getText(), LocalEnum.USER_ALL);
                    setText(null);
                }
            }
        });
    }
}