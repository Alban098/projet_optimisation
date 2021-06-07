import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;

public class OpenTxt {
    private final ArrayList<Integer> weights;
    private String file;
    private String[] feature;

    public OpenTxt(){
        weights = new ArrayList<>();
    }

    public ArrayList<Integer> fileDialogWindow() throws Exception {
        FileDialog dialog = new FileDialog((Frame)null, "Sélectionner le fichier à ouvrir");
        dialog.setMode(FileDialog.LOAD);
        dialog.setFile("*.txt");
        dialog.setVisible(true);
        this.file = dialog.getFile();
        String directory = dialog.getDirectory();
        if (file == null){
            JInternalFrame frame = new JInternalFrame();
            JOptionPane.showMessageDialog(frame,
                    "Fichier introuvable ou non spécifié",
                    "Erreur fichier",
                    JOptionPane.ERROR_MESSAGE);
            return weights;
        }
        return openFile(directory + file);
    }

    public ArrayList<Integer> openFile(String file_path) throws Exception {
        File file = new File(file_path);
        //System.out.println(file.exists());
        BufferedReader br = new BufferedReader(new FileReader(file));
        String line = br.readLine();
        feature = line.split(" ");

        weights.clear();
        while ((line = br.readLine()) != null) {
            if (Integer.parseInt(line) <= 0)
                throw new Exception("Weight needs to greater than 0");
            weights.add(Integer.parseInt(line));
        }
        br.close();
        return weights;
    }

    public String getFile() {
        return file;
    }

    public String[] getFeature() {
        return feature;
    }
}
