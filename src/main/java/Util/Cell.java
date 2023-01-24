package Util;
//CreateTime: 2023-01-21 10:40 p.m.

import GlobalParameters.Constant;
import GlobalParameters.StaticMethod;

import javax.swing.*;
import java.awt.*;

/**
 * use to create a component with a particular image
 */
public class Cell extends JPanel {

    private Image img;

    public Cell(String str){
        this.img = StaticMethod.readImage(str).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Constant.HEADERCOLOR);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.drawImage(this.img, 0,0,this.getWidth(),this.getHeight(),null);
    }
}
