package Graphics.Window;
//CreateTime: 2023-01-19 11:28 p.m.

import GlobalParameters.Constant;
import GlobalParameters.Difficulty;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    //current difficulty
    Difficulty currentDifficulty = Difficulty.EASY;

    //root panel
    windowContentPanel root;


    public Window() {
        //set window
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Minesweeper");

        //initialize content panel
        this.root = new windowContentPanel(this);
        //set content panel
        this.setContentPane(root);

        //default diffculty is easy
        setDifficulty("EASY");

        //set window visible
        this.setVisible(true);
        //set window size to fixed
        this.setResizable(false);
    }
    public void setDifficulty(String difficultyString){
        //get proper diffculty object
        Difficulty difficulty = Difficulty.valueOf(difficultyString);

        //update current diffculty attribute
        this.currentDifficulty = difficulty;
        //update header width
        root.header.setWidth(difficulty.getWIDTH());
        root.header.changeDifficulty(difficulty);
        //update matrix component width and height
        root.matrix.setWidth(difficulty.getWIDTH());
        root.matrix.setHeight(difficulty.getHEIGHT() - Constant.HEADERHEOGHT);
        root.matrix.changeDifficulty(difficulty);
        //reset window's size
        this.setSize(difficulty.getWIDTH() + Constant.ScreenVisibleX, difficulty.getHEIGHT() + Constant.ScreenVisibleY);
        //reset window's location
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(((int) screenSize.getWidth() - difficulty.getWIDTH()) / 2, ((int) screenSize.getHeight() - difficulty.getHEIGHT()) / 2);
    }


    public static void main(String[] args) {
        new Window();
    }
}





