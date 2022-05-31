package filemanager.Forms;

import java.awt.HeadlessException;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class Utils {

    private static String selected_disk = ".";
    private static File disk;
    private static String user_name;
    private static String search_hints = "";

    public static void select_disk() {

        JFileChooser dirChooser = new JFileChooser();
        dirChooser.setCurrentDirectory(new java.io.File("."));
        dirChooser.setDialogTitle("Select a Directory");
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.setAcceptAllFileFilterUsed(false);

        if (dirChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

            selected_disk = dirChooser.getSelectedFile().getAbsolutePath();
            disk = dirChooser.getSelectedFile();

        } else {
            System.out.println("No Selection ");
        }

        System.out.println("Selected Dist " + selected_disk);

    }

    public static void change_disk() {

        JFileChooser dirChooser = new JFileChooser();
        dirChooser.setCurrentDirectory(new java.io.File("."));
        dirChooser.setDialogTitle("Select a Directory");
        dirChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        dirChooser.setAcceptAllFileFilterUsed(false);

        try {
            if (dirChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

                selected_disk = dirChooser.getSelectedFile().getAbsolutePath();
                disk = dirChooser.getSelectedFile();

            } else {
                System.out.println("No Selection ");
            }

            System.out.println("Selected Dist " + selected_disk);

        } catch (HeadlessException exc) {

            System.out.println("Error");

        }

    }

    public static void create_folder() {

        String fname = JOptionPane.showInputDialog("Enter Folder Nam:?");

        String fileName = selected_disk + "/" + fname;

        Path path = Paths.get(fileName);

        if (!Files.exists(path)) {

            try {
                Files.createDirectory(path);
            } catch (IOException ex) {
                System.out.println("Failed to create directory");
            }
            System.out.println("Directory created");
        } else {

            System.out.println("Directory already exists");
        }

    }

    public static void create_file() {

        String file_path = select_directory().getAbsolutePath();

        String fname = JOptionPane.showInputDialog("Enter File Name:?");

        String fileName = file_path + "/" + fname;

        Path path = Paths.get(fileName);

        try {

            if (!Files.exists(path)) {

                File F = new File(fileName);
                boolean result = F.createNewFile();
                System.out.println("File created");

            } else {

                System.out.println("File already Exist");
            }

        } catch (IOException exc) {

        }

    }

    public static void Delete_folder() {

        String fname = JOptionPane.showInputDialog("Enter Folder Nam:?");

        String fileName = selected_disk + "/" + fname;

        Path path = Paths.get(fileName);
        File file = new File(fileName);

        deleteDirectory(file);

    }

    public static void Delete_Disk() {

        String fileName = selected_disk;

        File file = new File(fileName);

        deleteDirectory(file);

    }

    public static void Delete_File() {

        File fname = select_file();

        if (fname.delete()) {

            JOptionPane.showMessageDialog(null, "Deleted the file: " + fname.getName());
        } else {
            JOptionPane.showMessageDialog(null, "Failed to delete the file.");
        }

    }

    static void deleteDirectory(File file) {
        File[] contents = file.listFiles();
        if (contents != null) {
            for (File f : contents) {
                if (!Files.isSymbolicLink(f.toPath())) {
                    deleteDirectory(f);
                }
            }
        }
        file.delete();
    }

    public static void rename_file() {

        File to_rename = select_file();

        String extension = "";

        int i = to_rename.getName().lastIndexOf('.');
        if (i > 0) {
            extension = to_rename.getName().substring(i + 1);
        }

        String fname = JOptionPane.showInputDialog("Enter New File Name:?");

        Path olFile = Paths.get(to_rename.getAbsolutePath());

        String n_file = fname + "." + extension;

        try {
            Files.move(olFile, olFile.resolveSibling(n_file));

        } catch (IOException e) {
            System.out.println("operation failed" + e);
        }

    }

    public static File select_file() {

        String pathString = "";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File(selected_disk));
        fileChooser.setDialogTitle("Select File");
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            pathString = file.getAbsolutePath();
            return file;

        } else {
            System.out.println("No Selection ");
        }

        return null;
    }

    public static File select_directory() {

        String pathString = "";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new java.io.File(selected_disk));
        fileChooser.setDialogTitle("Select File");
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();

            return file;

        } else {
            System.out.println("No Selection ");
        }

        return null;
    }

    public static String getDiskTree() {
        if (!disk.isDirectory()) {
            throw new IllegalArgumentException("folder is not a Directory");
        }
        int indent = 0;
        StringBuilder disk_build = new StringBuilder();
        getDiskTree(disk, indent, disk_build);
        return disk_build.toString();
    }

    public static String getDiskTree_searching() {
        if (!disk.isDirectory()) {
            throw new IllegalArgumentException("folder is not a Directory");
        }
        int indent = 0;
        StringBuilder disk_build = new StringBuilder();
        getDiskTree_searching(disk, indent, disk_build);
        return disk_build.toString();
    }

    private static void getDiskTree(File folder, int indent, StringBuilder disk_build) {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("folder is not a Directory");
        }
        disk_build.append(getIndentString(indent));
        disk_build.append("+--");
        disk_build.append(folder.getName());
        disk_build.append("/");
        disk_build.append("\n");
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                getDiskTree(file, indent + 1, disk_build);
            } else {
                printFileTreaa(file, indent + 1, disk_build);
            }
        }

    }

    private static void getDiskTree_searching(File folder, int indent, StringBuilder disk_build) {
        if (!folder.isDirectory()) {
            throw new IllegalArgumentException("folder is not a Directory");
        }
        disk_build.append(getIndentString(indent));
        disk_build.append("+--");
        if (folder.getName().contains(search_hints)) {
            disk_build.append(folder.getName());
        } else {
            disk_build.append("##");
        }
        disk_build.append("/");
        disk_build.append("\n");
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                getDiskTree_searching(file, indent + 1, disk_build);
            } else {
                printFileTreaa_searching(file, indent + 1, disk_build);
            }
        }

    }

    private static void printFileTreaa(File file, int indent, StringBuilder disk_build) {

        disk_build.append(getIndentString(indent));
        disk_build.append("+--");
        disk_build.append(file.getName());
        disk_build.append("\n");

    }

    private static void printFileTreaa_searching(File file, int indent, StringBuilder disk_build) {
        if (file.getName().contains(search_hints)) {

            disk_build.append(getIndentString(indent));
            disk_build.append("+--");
            disk_build.append(file.getName());
            disk_build.append("\n");
        } else {

            disk_build.append(getIndentString(indent));
            disk_build.append("+--");
            disk_build.append("### mismatch file hidden");
            disk_build.append("\n");

        }
    }

    private static String getIndentString(int indent) {
        StringBuilder disk_build = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            disk_build.append("|  ");
        }
        return disk_build.toString();
    }

    public static void set_RW() {

        File file = select_file();
        file.setWritable(true);
        file.setReadable(true);

    }

    public static void unset_RW() {

        File file = select_file();
        file.setWritable(true);
        file.setReadable(true);

    }

    public static String getSelected_disk() {
        return selected_disk;
    }

    public static File getDisk() {
        return disk;
    }
/*
    public static String getUser_name() {

        user_name = System.getProperty("user.name");
        return user_name;
    }
*/
    public static void search_file() {

        String fname = JOptionPane.showInputDialog("Enter File Name");

        search_hints = fname;

    }
    
    public static void copyDir(){
    
        
        String from = JOptionPane.showInputDialog("Enter Directory to copy");
        String to = JOptionPane.showInputDialog("Enter directory to paste contents");
        
        from = selected_disk+"/"+from;
        to = selected_disk+"/"+to;
        
        JOptionPane.showMessageDialog(null, "Started!!, Click Ok to continue");
        
        copyDirectory(from, to);
        
        JOptionPane.showMessageDialog(null, "Finished, Check The Files in "+to+" directory");
        
        
        
    
    }

    public static void copyDirectory(String from, String todir) {
        
        
        try {
            Files.walk(Paths.get(from))
                    .forEach(source -> {
                        Path destination = Paths.get(todir, source.toString()
                                .substring(from.length()));
                        try {
                            Files.copy(source, destination);
                        } catch (IOException e) {
                            System.out.println(e);
                        }
                    });
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

}
