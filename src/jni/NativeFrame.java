package jni;

import cr.LocalEnum;
import cr.ui.frame.MainFrame;

public final class NativeFrame {
    private static boolean onTop=false;
    public static void init(){
        setFrame(LocalEnum.TITTLE);
    }
    public static void alwaysOnTop(){
        if (!onTop) {
            onTop=true;
            new Thread(() -> start_top(5)).start();
        }
    }
    public static void stopAlwaysTop(){
        if (onTop) {
            onTop=false;
            stop_top();
            MainFrame.obj.setAlwaysOnTop(true);
            MainFrame.obj.setAlwaysOnTop(false);
        }
    }
    public static boolean isOnTop(){
        return onTop;
    }
    public static void onTopOnce(){
        top();
    }
    public static void cancelOnTop(){
        cancel();
    }

    private static native void setFrame(String title);
    private static native void top();
    private static native void start_top(int delay);
    private static native void stop_top();
    private static native void cancel();
    static {
        System.loadLibrary("lib/NativeFrame");
    }
}