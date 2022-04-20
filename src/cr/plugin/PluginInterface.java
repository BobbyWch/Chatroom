package cr.plugin;

import javax.swing.*;

public interface PluginInterface {
    void loadPlugin(ClassLoader loader);
    void unloadPlugin();
    void onExit();
    JMenu getMenu();
}
