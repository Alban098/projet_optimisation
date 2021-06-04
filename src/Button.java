import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Button extends JButton implements MouseListener{
    ButtonEnum bEnum;
    Color base = new Color(201,218,235);
    Color hover = new Color(150, 170, 190);
    Color pressed = new Color(255,0,0);

    public Button(ButtonEnum bEnum){
        super(bEnum.toString());
        this.bEnum = bEnum;
        this.addMouseListener(this);
        setBackground(base);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        setBackground(pressed);
    }

    @Override
    public void setContentAreaFilled(boolean b) {
    }

    @Override
    public void mouseClicked(MouseEvent event) {
    }

    @Override
    public void mousePressed(MouseEvent event) {
        try {
            Main.window.changeContent(bEnum);
            setBackground(pressed);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        setBackground(hover);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBackground(base);
    }
}