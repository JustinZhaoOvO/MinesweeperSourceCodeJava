package GlobalParameters;
//CreateTime: 2023-01-20 10:29 a.m.

import java.awt.*;

public class Constant {
    //coordinates difference of surrounding coordinates
    public static final int[][] SURROUNDINGCOORDINATES = {{0, -1}, {1, 0}, {0, 1}, {-1, 0}, {1, -1}, {1, 1}, {-1, 1}, {-1, -1}};

//    //screen horizontal resolution
//    public static final int SCREENHORIZONTALRESOLUTION = 1920;
//
//    //screen vertical resolution
//    public static final int SCREENVERTICALRESOLUTION = 1080;

    //screen width diffenrence
    public final static int ScreenVisibleX = 14;   //7 | 14
    //screen height diffenrence
    public final static int ScreenVisibleY = 37;  //31 | 37

    //header width
    public static final int HEADERHEOGHT = 40;

    //header background color
    public static final Color HEADERCOLOR = new Color(215, 215, 215);

    //matrix component color
    public static final Color MATRIXCOLOR = new Color(99, 222, 219);
    //unexposed cell component color
    public static final Color UNEXPOSEDCELLCOLORODD = new Color(169, 169, 169);
    public static final Color UNEXPOSEDCELLCOLOREVEN = new Color(100, 100, 100);

    //exposed cell component color
    public static final Color EXPOSEDCELLCOLORODD = new Color(142, 255, 104);
    public static final Color EXPOSEDCELLCOLOREVEN = new Color(224, 253, 118);

    //Background color of the mine clicked by the user
    public static final Color EXPLOSIONCOLOR = new Color(255, 134, 134);

    //Symbols in char array
    public static final char FLAG = 'F';
    public static final char UNEXPOSED = 0;
    public static final char EXPLOSION = 'E';
    public static final char MINE =  'M';

    public static final char EMPTY = '0';
}
