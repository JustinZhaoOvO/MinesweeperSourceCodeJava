package Graphics.MatrixComponent;
//CreateTime: 2023-01-21 10:59 p.m.

import Graphics.Adapter.ListenerAdapter;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MatrixListener extends ListenerAdapter {
    private MatrixComponent parent;

    public MatrixListener(MatrixComponent parent){
        this.parent = parent;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (parent.disabledListener) return;
        int clickedX = e.getX();
        int clickedY = e.getY();
        if (e.getButton() == 1){  //left click
            parent.leftClicked(clickedY, clickedX);
        }else if (e.getButton() == 3){ //right click
            parent.rightClicked(clickedY, clickedX);
        }
    }
}
