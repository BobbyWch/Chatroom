package cr.tool;

import cr.LocalEnum;
import cr.ui.frame.MainFrame;

import javax.swing.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Bobbywang
 * @date 2021-09-17 21:35
 */
public final class Logger {
    private static Logger instance = null;

    public static Logger getLogger() {
        return instance;
    }
    public static void init(){
        long t=System.currentTimeMillis();
        instance=new Logger();
        System.out.println("Logger init:"+(System.currentTimeMillis()-t)+"ms");
    }

    public enum Type {
        INFO(" [Info] "), WARNING(" [Warning] "), EXCEPTION(" [Exception] "), SERVER(" [Server] ");
        private final String name;

        Type(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    private File logFile;
    private FileOutputStream stream;
    private final StringWriter stringWriter=new StringWriter();
    private final PrintWriter printWriter=new PrintWriter(stringWriter,true);
    private BufferedWriter writer;
    public JTextArea area;
    private boolean enableFile=false;

    private Logger() {}

    private File checkFolder(String path) {
        File file = new File(path);
        if (!file.exists() || !file.isDirectory())
            file.mkdir();
        return file;
    }
    private void enableLog(){
        File file = checkFolder("Logs");
        File file1 = checkFolder("Logs/olds");
        File file2 = new File(file1.getPath(), "Log5.txt");
        if (file2.exists())
            file2.delete();
        for (int i = 0; i < 4; i++) {
            int num = 4 - i;
            File temp = new File(file1.getPath(), "Log" + num + ".txt");
            if (temp.exists()) {
                temp.renameTo(new File(file1.getPath(), "Log" + (num + 1) + ".txt"));
            }
        }
        File file3 = new File(file.getPath(), "Log.txt");
        try {
            if (file3.exists()) {
                try {
                    Files.move(file3.toPath(), new File(file1.getPath(), "Log1.txt").toPath(), StandardCopyOption.REPLACE_EXISTING);
                }catch (Exception e){
                    MainFrame.err("日志文件被占用！导出日志失败");
                    e.printStackTrace();
                    Runtime.getRuntime().exit(0);
                }
            }
            logFile = new File(file.getPath(), "Log.txt");
            stream = new FileOutputStream(logFile);
            save(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void save(FileOutputStream s){
        enableFile=true;
        writer=new BufferedWriter(new OutputStreamWriter(s,StandardCharsets.UTF_8));
        try {
            writer.write('#' + String.format("%tc", new Date()));
            writer.newLine();
            writer.write('#' + LocalEnum.TITTLE);
            writer.newLine();
            writer.write(stringWriter.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void print(String s){
        printWriter.print(s);
        if (area!=null){
            area.append(s);
        }
        if (enableFile){
            try {
                writer.write(s);
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void println(String s){
        if (!s.endsWith(System.lineSeparator()))
            s=s+ System.lineSeparator();
        print(s);
    }

    public void err(Exception e) {
        print(format.format(new Date()) + Type.EXCEPTION);
        e.printStackTrace(printWriter);
    }

    private final SimpleDateFormat format = new SimpleDateFormat("[yyyy-MM-dd hh:mm:ss]");

    public void info(String text, Type type) {
        println(format.format(new Date()) + type.toString() + text);
    }

    public void info(String text) {
        info(text, Type.INFO);
    }

    public StringWriter getStringWriter() {
        return stringWriter;
    }
}