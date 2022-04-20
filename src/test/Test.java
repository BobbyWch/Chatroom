package test;

import cr.util.user.User;
import cr.util.Logger;
import cr.util.Settings;

public class Test {
    public static void main(String... args){

    }

    private static void userFrame() {
        Logger.init();
        Settings.init();

        User.init();
        User.getLocalUser().showFrame();
    }
}