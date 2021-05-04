import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class OpenTxt {
    private ArrayList<Integer> arrayList;
    private String file;
    private String[] feature;

    public OpenTxt(){
        arrayList = new ArrayList<>();
    }

    public ArrayList<Integer> open() throws IOException {
        FileDialog dialog = new FileDialog((Frame)null, "Select File to Open");
        dialog.setMode(FileDialog.LOAD);
        dialog.setFile("*.txt");
        dialog.setVisible(true);
        this.file = dialog.getFile();
        String directory = dialog.getDirectory();
        if (file == null){
            if (arrayList.isEmpty()){
                JInternalFrame frame = new JInternalFrame();
                JOptionPane.showMessageDialog(frame,
                        "File not found or not specified\nExiting...",
                        "Error file not found",
                        JOptionPane.ERROR_MESSAGE);
                System.exit(-1);
            }
            else {
                JInternalFrame frame = new JInternalFrame();
                JOptionPane.showMessageDialog(frame,
                        "File not found or not specified.\nUsing old one.",
                        "Error file not found",
                        JOptionPane.WARNING_MESSAGE);
                return arrayList;
            }
        }

        File file = new File(directory + this.file);
        //System.out.println(file.exists());
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        feature = line.split(" ");

        while ((line = br.readLine()) != null) {
            arrayList.add(Integer.parseInt(line));
        }
        br.close();
        return arrayList;
    }

    public ArrayList<Integer> getArrayList() {
        return arrayList;
    }

    public String getFile() {
        return file;
    }

    public String[] getFeature() {
        return feature;
    }
}
