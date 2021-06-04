import javax.swing.*;
import java.awt.*;

//public class Display extends JPanel {
//
//
//    public Display() {
//        this.setDoubleBuffered(true);
//        setBackground(new Color(50,50,50));
//        this.requestFocus();
//    }
//
//    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//
//        Graphics2D g2 = (Graphics2D) g;
//
//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//
//    }
//}

public class Display extends JTextPane {

    public Display(String str){
        super();
        this.setMargin(new Insets(5,5,5,5));
        this.setFont(new Font(Font.SANS_SERIF, 3, 20));;
        this.setForeground(Color.white);
        this.setText(str);
        this.setCaretPosition(0);
        this.setBackground(new Color(50,50,50));
    }
}
