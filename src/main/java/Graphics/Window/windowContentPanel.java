package Graphics.Window;
//CreateTime: 2023-01-20 11:25 p.m.

import GlobalParameters.Constant;
import Graphics.Adapter.LayoutAdapter;
import Graphics.Header.Header;
import Graphics.MatrixComponent.MatrixComponent;

import javax.swing.*;
import java.awt.*;

public class windowContentPanel extends JPanel {
    final Header header;
    final MatrixComponent matrix;

    public Window window;

    public Header getHeader(){
        return header;
    }
    public windowContentPanel(Window window){
        //store window object
        this.window = window;
        //add header component
        this.header = new Header(this);
        header.updateTime("0");
        header.updateFlagNumber(0);
        this.add(header);
        //add matrix component
        this.matrix = new MatrixComponent(this);
        this.add(matrix);
        //set layout
        this.setLayout(new WindowContentPanelLayout());
    }

    /**
     * to start the timer in the header
     */
    public void startTimer(){
        header.startTimer();
    }

    /**
     * to stop the timer in the header
     */
    public void stopTimer(){
        header.stopTimer();
    }

    private class WindowContentPanelLayout extends LayoutAdapter {

        @Override
        public void layoutContainer(Container parent) {
            //set header component layout
            header.setBounds(0, 0, header.getWidth(), Constant.HEADERHEOGHT);
            //set header matrix layout
            matrix.setBounds(0, Constant.HEADERHEOGHT, matrix.getWidth(), matrix.getHeight());
        }
    }

}
