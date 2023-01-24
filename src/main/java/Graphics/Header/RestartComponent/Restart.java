package Graphics.Header.RestartComponent;
//CreateTime: 2023-01-22 2:39 p.m.

import GlobalParameters.Constant;
import GlobalParameters.Images;
import Graphics.Header.Header;

import javax.swing.*;
import java.awt.*;

public class Restart extends JPanel {
    private Header root;
    private Image img;
    public Restart(Header root){
        this.root = root;
        this.addMouseListener(new RestartListener(this));
    }

    public void setImg(String str){
        this.img = Images.valueOf(str).getImg();
        this.repaint();
    }

    public void restartTheGame(){
        root.restartTheGame();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Constant.HEADERCOLOR);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.drawImage(img, 0, 0, this.getWidth(), this.getHeight(), null);
    }
}
