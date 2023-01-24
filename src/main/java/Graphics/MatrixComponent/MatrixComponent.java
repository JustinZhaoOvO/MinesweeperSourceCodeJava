package Graphics.MatrixComponent;
//CreateTime: 2023-01-20 10:45 p.m.

import GlobalParameters.Constant;
import GlobalParameters.Difficulty;
import GlobalParameters.Images;
import GlobalParameters.StaticMethod;
import Graphics.Window.Window;
import Graphics.Window.windowContentPanel;
import MinesGenerator.MinesGenerator;
import Util.Coordinate;

import javax.swing.*;
import java.awt.*;
import java.awt.print.PrinterAbortException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;

public class MatrixComponent extends JPanel {

//    private ArrayDeque<>

    private Image flag = StaticMethod.readImage("flag.png").getImage();


    //matrix : '0' to '8' : mine around this cell, 'M' : this's a mine
    char[][] matrix;

    //status of each cell in matrix : ' '(initial value of char array) : unexposed, 'F' : flagged, '0' to '8' : mine around this cell
    char[][] matrixStatus;

    //current difficulty
    Difficulty difficulty;

    //Whether it has been clicked (the first click starts the timer)
    private boolean firstClick;

    //width and height and its getter and setter
    private int width;
    private  int height;

    boolean disabledListener = false;

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    //row, column, each cell length
    private int row;
    private int column;
    private int length;

    //window content panel
    private final windowContentPanel root;
    
    //unexposed cell number, when all cell is exposed, user won
    private int unexposedCellsNum;
    //to store the coordinate of cells need to be updated(repaint)
    private ArrayDeque<int[]> uopdateDeque;

    /**
     * constructor
     */
    public MatrixComponent(windowContentPanel root){
        this.root = root;
        //initial diffculty is easy
        changeDifficulty(Difficulty.EASY);
        this.addMouseListener(new MatrixListener(this));
    }
    public void generateMatrix(int firstClickY, int firstClickX){
        //initialize 2 matrix
        matrix = new char[row][column];
        //intialize a minesGenerator object
        MinesGenerator minesGenerator = new MinesGenerator(matrix, firstClickY, firstClickX);
        //generate mines
        minesGenerator.generateMines(difficulty.getMINESNUM());
    }
    /**
     * update matrix component with the change of difficulty
     * @param difficulty : current difficulty
     */
    public void changeDifficulty(Difficulty difficulty){
        //initialize attributes
        this.disabledListener = false;
        this.firstClick = true;
        this.difficulty = difficulty;
        this.row = difficulty.getROW();
        this.column = difficulty.getCOLUMN();
        this.unexposedCellsNum = row * column;
        this.length = difficulty.getCELLLEN();
        matrixStatus = new char[row][column];
        this.repaint();
    }

    /**
     * cell can be opened or not
     * @param y : y coordinate of the cell was clicked
     * @param x : x coordinate of the cell was clicked
     * @return : -1 : opened a mine, 0 : do nothing, 1 : opened a normal cell
     */
    public  int determineLeftClick(int y, int x){

        if (matrix[y][x] == Constant.MINE)return -1;
        if (matrixStatus[y][x] != Constant.UNEXPOSED) return 0;
        return 1;
    }

    /**
     * open the cell and return a list of surrounding cells that will exposed together with this cell
     * @param y : y coordinate of the cell was clicked
     * @param x : x coordinate of the cell was clicked
     * @return : how many cell is exposed this time
     */
    public int CellsWillBeOpenedTogether(int y, int x){
        int ans = 1;
        //expose this cell
        matrixStatus[y][x] = matrix[y][x];
        if (matrix[y][x] != Constant.EMPTY) return ans;

        //initialize a queue to dfs to find the cells will be opened together with this cell
        Coordinate root = new Coordinate(y, x);
        ArrayDeque<Coordinate> deque = new ArrayDeque<>();
        deque.add(root);
        //dfs
        while (!deque.isEmpty()){
            int len = deque.size();
            for (int i = 0; i < len; i++){
                Coordinate currentCoordinate = deque.pollFirst();
                for (int[] possibleCoordinate : Constant.SURROUNDINGCOORDINATES){
                    int newY = possibleCoordinate[0] + currentCoordinate.y();
                    int newX = possibleCoordinate[1] + currentCoordinate.x();
                    if (newY < 0 || newY >= row || newX < 0 || newX >= column || matrixStatus[newY][newX] != Constant.UNEXPOSED || matrix[newY][newX] == Constant.MINE) continue;
                    Coordinate newCoordinate = new Coordinate(newY, newX);
                    ans ++;
                    //expose cells
                    matrixStatus[newY][newX] = matrix[newY][newX];
                    

                    if (matrix[newY][newX] == Constant.EMPTY) deque.add(newCoordinate);
                }
            }
        }return ans;
    }

    /**
     *
     * @param y : y coordinate of the cell was clicked
     * @param x : x coordinate of the cell was clicked
     * @return : 1 : flag the cell , 0 : do nothing, -1 : remove the flag in the cell
     */
    public int flagACell(int y, int x){
        if (matrixStatus[y][x] == Constant.UNEXPOSED){ // if the cell is unexposed
            matrixStatus[y][x] = Constant.FLAG;  //flag it
            
            root.getHeader().flagNumberMinusOne();
            this.unexposedCellsNum -= 1;
            isWon();
            return 1;
        }else if (matrixStatus[y][x] == Constant.FLAG){ //if the cell is flagged
            matrixStatus[y][x] = Constant.UNEXPOSED; //remove the flag
            
            root.getHeader().flagNumberPlusOne();
            this.unexposedCellsNum += 1;
            return -1;
        }else{                        //if the cell is exposed
            return 0;                //do nothing
        }
    }

    /**
     * did it won
     */
    public void isWon(){
        if (unexposedCellsNum == 0){
            disabledListener = true;
            root.getHeader().won();
        }
    }

    /**
     * when left clicked a location in matrix
     * @param clickedY : y coordinate
     * @param clickedX : x coordinate
     */
    public void leftClicked(int clickedY, int clickedX){

        int yVal = clickedY / difficulty.getCELLLEN();
        int xVal = clickedX/ difficulty.getCELLLEN();
        if (firstClick && matrixStatus[yVal][xVal] != Constant.UNEXPOSED) return;
        if (firstClick){
            root.startTimer();
            firstClick = false;
            generateMatrix(yVal, xVal);
        }
        int result = determineLeftClick(yVal, xVal);
        if (result == 1){
            openACell(yVal,xVal);
        }else if (result == -1){
            ClickedAMine(yVal, xVal);
        }

        this.repaint();
    }

    /**
     * open a cell and update matrix
     * @param y
     * @param x
     */
    public void openACell(int y, int x){
        this.unexposedCellsNum -= CellsWillBeOpenedTogether(y, x);
        isWon();
    }

    /**
     * clicked a mine, update the matrix and game over
     * @param y: y coordinate
     * @param x : x coordinate
     */
    public void ClickedAMine(int y, int x){

        for (int i = 0; i < row; i ++){
            for (int j = 0; j < column; j++){
                if (matrix[i][j] == Constant.MINE) matrixStatus[i][j] = Constant.MINE;
            }
        }
        matrixStatus[y][x] = Constant.EXPLOSION;
        root.stopTimer();
        root.getHeader().lost();
        disabledListener = true;
    }

    /**
     * when right clicked a location in matrix
     * @param clickedY : y coordinate
     * @param clickedX : x coordinate
     */

    public void rightClicked(int clickedY, int clickedX){
        int yVal = clickedY / difficulty.getCELLLEN();
        int xVal = clickedX/ difficulty.getCELLLEN();
        flagACell(yVal, xVal);
        this.repaint();
    }

    /**
     * paint components
     * @param g the <code>Graphics</code> object to protect
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        //paint matrix
        for (int i = 0; i <row; i++){
            for (int j =0; j <column; j++){
                //if matrix is unexposed or flagged
                if (matrixStatus[i][j] == Constant.UNEXPOSED || matrixStatus[i][j] == Constant.FLAG){
                    if ((i + j) % 2 == 0) g.setColor(Constant.UNEXPOSEDCELLCOLOREVEN);
                    else g.setColor(Constant.UNEXPOSEDCELLCOLORODD);
                    g.fillRect(j * length, i * length, length, length);
                    if (matrixStatus[i][j] == Constant.FLAG) g.drawImage(flag, j * length, i * length, length, length, null);
                }else{
                    if (matrixStatus[i][j] == Constant.EXPLOSION) g.setColor(Constant.EXPLOSIONCOLOR);
                    else if ((i + j) % 2 == 0) g.setColor(Constant.EXPOSEDCELLCOLOREVEN);
                    else g.setColor(Constant.EXPOSEDCELLCOLORODD);
                    g.fillRect(j * length, i * length, length, length);
                    if (matrixStatus[i][j] != Constant.EMPTY){
                        g.drawImage(Images.valueOf("I" + matrixStatus[i][j]).getImg(), j * length, i * length, length, length, null);
                    }

                }
            }
        }
    }
}
