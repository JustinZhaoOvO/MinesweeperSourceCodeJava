package GlobalParameters;

public enum Difficulty {
    EASY(8, 8, 400, 440, 50, 10),
    MEDIUM(16, 16, 480, 520, 30, 40),

    HARD(16, 30, 900, 520, 30, 99);



    private final int ROW;
    private final int COLUMN;
    private final int WIDTH;
    private final int HEIGHT;

    private final int CELLLEN;
    private final int MINESNUM;

    Difficulty(int ROW, int COLUMN, int WIDTH, int HEIGHT, int CELLLEN, int MINESNUM){
        this.ROW = ROW;
        this.COLUMN = COLUMN;
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.CELLLEN = CELLLEN;
        this.MINESNUM =MINESNUM;
    }

    public int getROW() {
        return ROW;
    }

    public int getCOLUMN() {
        return COLUMN;
    }

    public int getWIDTH() {
        return WIDTH;
    }

    public int getHEIGHT() {
        return HEIGHT;
    }

    public int getCELLLEN() {
        return CELLLEN;
    }

    public int getMINESNUM() {
        return MINESNUM;
    }
}
