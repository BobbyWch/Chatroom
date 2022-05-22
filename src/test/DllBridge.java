package test;
/*
public class DllBridge {
    private final String path;
    private int index;
    private static int lastIndex=0;

    public DllBridge(String path) {
        this.path = path;
        if (lastIndex==10)
            throw new UnsupportedOperationException("reach max: 10 dll");
        index=lastIndex++;
    }

    private native int open0(String path,int index);
    private native int close0(int index);
    public int open(){
        return open0(path,index);
    }
    public int close(){
        return close0(index);
    }
    private native int run(String func,int index);
    private static native int init();
    public int execute(String func){
        return run(func,index);
    }
//    private native static String listFunc(String path);
//    public static String[] listFunctions(String path){
//        String s=listFunc(path);
//        return s.split(",,");
//    }
    static {
        System.loadLibrary("DllBridge");
        init();
    }
}
*/
