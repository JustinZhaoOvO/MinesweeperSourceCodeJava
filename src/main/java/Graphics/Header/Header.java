package Graphics.Header;
//CreateTime: 2023-01-20 11:35 a.m.

import Util.Cell;
import GlobalParameters.Constant;
import GlobalParameters.Difficulty;
import Graphics.Adapter.LayoutAdapter;
import Graphics.Header.RestartComponent.Restart;
import Graphics.Window.windowContentPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Header extends JPanel {

    //height and width of the component
    private int height = Constant.HEADERHEOGHT;
    private int width = 0;

    private Timer timer;

    //difficulty Select Box
    private final JComboBox<String> difficultySelectBox;
    private final Cell flagIcon;

    private final JLabel flagCounter;

    private final Cell timerIcon;

    private final Restart restart;

    private int flagNumber;

    private final JLabel time;

    private windowContentPanel root;

    private Difficulty currentDiffculty;
    public Header(windowContentPanel root){
        this.root = root;
        //initialize select box
        this.difficultySelectBox = new JComboBox<>();
        // add into panel
        this.add(difficultySelectBox);
        //add selections item into select box
        difficultySelectBox.addItem("EASY");
        difficultySelectBox.addItem("MEDIUM");
        difficultySelectBox.addItem("HARD");
        //add a action listener to select box, when select a difficulty, update the window
        difficultySelectBox.addActionListener(e -> root.window.setDifficulty(difficultySelectBox.getItemAt(difficultySelectBox.getSelectedIndex())));
        //add flag icon

        this.flagIcon = new Cell("flag.png");
        this.add(flagIcon);
        //add flag counter
        this.flagCounter = new JLabel();
        this.add(flagCounter);
        //add timer
        this.timerIcon = new Cell("timer.png");
        this.add(timerIcon);
        //add time component
        this.time = new JLabel();
        this.add(time);
        //add restart component
        this.restart = new Restart(this);
        this.add(restart);
        //set current difficulty(initial difficulty is easy)
        changeDifficulty(Difficulty.EASY);
        //add a layout
        this.setLayout(new HeaderLayout());

        this.updateUI();
    }

    /**
     * change difficulty
     * @param difficulty : current difficulty
     */
    public void changeDifficulty(Difficulty difficulty){
        this.currentDiffculty = difficulty;
        //update flag number
        updateFlagNumber(difficulty.getMINESNUM());
        //reset time
        stopTimer();
        time.setText("0");
        //reset restart button picture
        restart.setImg("INDIFFERENCE");
        this.updateUI();
    }

    /**
     * restart the game
     */
    public void restartTheGame(){
        root.window.setDifficulty(currentDiffculty.name());
    }

    /**
     * lost the game
     */
    public void lost(){
        this.restart.setImg("SAD");
    }

    /**
     * won the game
     */
    public void won(){
        this.restart.setImg("SMILE");
    }

    /**
     * to start the timer in the header
     */
    public void startTimer(){
        int delay = 1000;
        TimeCounter counter = new TimeCounter(this);
        this.timer = new Timer(delay, counter);
        this.timer.start();

    }

    /**
     * to stop the timer in the header
     */
    public void stopTimer(){
        if (timer != null) timer.stop();
    }


    /**
     * update timer's time
     * @param newTime : string to be used
     */
    public void updateTime(String newTime){
        time.setText(newTime);
        this.repaint();
    }

    /**
     * update flag number
     * @param flagNumber : new flag number
     */
    public void updateFlagNumber(int flagNumber){
        this.flagNumber = flagNumber;
        flagCounter.setText(flagNumber + "");
        this.repaint();
    }

    /**
     * flag number + 1
     */
    public void flagNumberPlusOne(){
        updateFlagNumber(flagNumber+1);
    }

    /**
     * flag number - 1
     */
    public void flagNumberMinusOne(){
        updateFlagNumber(flagNumber-1);
    }

    public void setWidth(int width){
        this.width = width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        g.setColor(Constant.HEADERCOLOR);
        g.fillRect(0, 0, width, height);
    }
    class HeaderLayout extends LayoutAdapter{

        @Override
        public void layoutContainer(Container parent) {
            int x = 0;
            difficultySelectBox.setBounds(x, height / 4, Math.min(80, width / 6), height / 2);
            x += Math.min(80, width / 6) + width / 8;
            flagIcon.setBounds(x, height / 4, height / 2, height / 2);
            x += height/2 + width / 50;
            flagCounter.setBounds(x, height/4, height, height/2);
            x += height + width / 10;
            restart.setBounds(x, height/4, height / 2, height/2);
            x += height / 2+ width / 6;
            timerIcon.setBounds(x, height / 4, height/2, height/2);
            x += height/2 + width/50;
            time.setBounds(x, height/4, height, height/2);
        }
    }
}
class TimeCounter implements ActionListener{

    public long startTimeStamp;
    public final Header root;
    public TimeCounter (Header root){
        this.root = root;
        this.startTimeStamp = System.currentTimeMillis();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        root.updateTime(((int)((System.currentTimeMillis() - startTimeStamp) / 1000)) + "");
    }
}
