package GlobalParameters;

import javax.swing.*;
import java.awt.*;

public enum Images {

    I1("1"),
    I2("2"),
    I3("3"),
    I4("4"),
    I5("5"),
    I6("6"),
    I7("7"),
    I8("8"),
    IF("flag"),
    IM("mine"),
    IE("mine"),
    SMILE("smile"),
    INDIFFERENCE("indifference"),
    SAD("sad");

    private Image img;

    public Image getImg() {
        return img;
    }

    Images(String str){
        this.img = StaticMethod.readImage(str + ".png").getImage();
    }
}
