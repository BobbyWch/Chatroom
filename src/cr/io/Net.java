package cr.io;

import cr.LocalEnum;
import cr.Main;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author Bobbywang
 * @date 2021-07-08 20:51
 */
public final class Net {
    /**
     * No instances.
     */
    private Net() {
    }

    public static int toIntPort(String port) throws IllegalArgumentException {
        int p;
        p = Integer.parseInt(port);
        checkPort(p);
        return p;
    }

    public static void checkPort(int port) throws IllegalArgumentException {
        if (port < 0 || port > 65535)
            throw new IllegalArgumentException("out of range");
    }
}