package cr.io;

import cr.LocalEnum;
import cr.Main;
import cr.tool.Settings;
import cr.ui.frame.MainFrame;
import cr.util.Client;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

/**
 * @author Bobbywang
 * @date 2021-08-15 17:37
 */
public final class IO {
    private IO() {
    }

    public static void backupChat() {
        try {
            File file = saveFile(new FileNameExtensionFilter("文本文档(*.txt)", "txt"), "聊天记录.txt");
            if (file!=null) {
                FileWriter writer = new FileWriter(file);
                Date date = new Date();
                writer.write(String.format("%tc", date));
                String newline=System.lineSeparator();
                writer.write(newline);
                writer.write(LocalEnum.TITTLE);
                writer.write(newline);
                writer.write(newline);
                writer.write(Client.getClient().getScreen().getText());
                writer.close();
                MainFrame.msg("已导出聊天记录至：\n" + file.getAbsolutePath());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private static final URL soundURL = “res/sound/sound.wav”;
    public static boolean isMute = Settings.obj.isMute;
//    private static final Runnable forSound = () -> Applet.newAudioClip(soundURL).play();

    public static void playSound() {
//        if (!isMute)
//            Main.executor.execute(forSound);
    }
    public static void outPutInput(InputStream source, OutputStream target) throws IOException {
        byte[] buff=new byte[262144];
        int len;
        while ((len=source.read(buff))!=-1){
            target.write(buff,0,len);
        }
        target.flush();
    }
    public static File openFile(FileNameExtensionFilter filter){
        JFileChooser chooser=new JFileChooser();
        chooser.setDialogTitle("打开");
        if (filter!=null)
            chooser.setFileFilter(filter);
        if (chooser.showOpenDialog(Main.mainFrame) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }else {
            return null;
        }
    }
    public static File[] openFiles(FileNameExtensionFilter filter){
        JFileChooser chooser=new JFileChooser();
        chooser.setDialogTitle("打开");
        if (filter!=null)
            chooser.setFileFilter(filter);
        chooser.setMultiSelectionEnabled(true);
        if (chooser.showOpenDialog(Main.mainFrame) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFiles();
        }else {
            return null;
        }
    }
    public static File saveFile(FileNameExtensionFilter filter,String filename){
        JFileChooser chooser=new JFileChooser();
        chooser.setDialogTitle("保存");
        if (filename!=null)
            chooser.setSelectedFile(new File("./"+filename));
        if (filter!=null)
            chooser.setFileFilter(filter);
        if (chooser.showSaveDialog(Main.mainFrame) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }else {
            return null;
        }
    }
    public static File openFile(){
        return openFile(null);
    }
    public static File[] openFiles(){
        return openFiles(null);
    }
    public static File saveFile(){
        return saveFile(null,null);
    }
    public static void openHttp(String url){
        try {
            Desktop.getDesktop().browse(new URI(url));
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
