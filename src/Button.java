import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Button extends JButton implements MouseListener{
    ButtonEnum bEnum;
    Color base = new Color(0,0,255);
    Color hover = new Color(0,250,0);
    Color pressed = new Color(250,0,0);

    public Button(ButtonEnum bEnum){
        this.bEnum = bEnum;
        this.addMouseListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (getModel().isPressed()){
            g.setColor(pressed);
        }
        else if (getModel().isRollover()){
            g.setColor(hover);
        }
        else{
            g.setColor(base);
        }
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void mouseReleased(MouseEvent event) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}