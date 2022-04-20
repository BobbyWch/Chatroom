package cr.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

public final class MShell {
    static final MShell sh=new MShell();
    final Method[] methods;
    public MShell(){
        methods= getClass().getMethods();
    }
    public static final String batPath="buffer/cmd.bat";
    public static void runCmd0(String command){
        try(var fs=new FileOutputStream(batPath);
        var os=new OutputStreamWriter(fs, StandardCharsets.UTF_8)){
            os.write("%1 mshta vbscript:CreateObject(\"Shell.Application\").ShellExecute(\"cmd.exe\",\"/c %~s0 ::\",\"\",\"runas\",1)(window.close)&&exit\r\n");
            os.write(command);
            os.flush();
        }catch (IOException e){
            e.printStackTrace();
        }
        try {
            Runtime.getRuntime().exec(batPath);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    public static void shell(String arg){
        if (arg.contains(" ")){
            String[] strings=arg.split(" ");
            for (int i=0;i<sh.methods.length;i++){
                if (sh.methods[i].getName().equals(strings[0])){
//                    String[] t;
//                    System.arraycopy(strings,1,t,0,strings.length-1);
//                    sh.methods[i].invoke(sh,Arrays.copyOf())
                }
            }
        }
        for (int i=0;i<sh.methods.length;i++){

        }
    }
}
