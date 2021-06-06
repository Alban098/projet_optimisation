import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.lang.reflect.Array;
import java.util.Arrays;

public class Button extends JButton implements MouseListener{
    ButtonEnum bEnum;
    Color base = new Color(201,218,235);
    Color hover = new Color(180,196,211);
    Color pressed = new Color(160,174,188);

    public Button(String name, ButtonEnum bEnum){
        super(name);
        this.bEnum = bEnum;
        this.addMouseListener(this);
        setBackground(base);
        Window.map.put(bEnum, this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (Window.gen_sol == bEnum || Window.method == bEnum)
            setBackground(pressed);
        super.paintComponent(g);
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
//            System.out.println("button " + bEnum + " pressed");
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
        setBackground(hover);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        setBackground(base);
    }

    public void changeColor() {
        if (Window.gen_sol != bEnum || Window.method != bEnum)
            setBackground(base);
    }
}