import javax.swing.*;

public class King extends Man{

    private final int speed = 8;
    public static String whiteMan = "\u26C3";
    public static String blackMan = "\u26C1";

    String[] colors = new String[]{"white", "black"};
    public CheckersSquare currentSquare;


    public King(int _x, int _y, boolean isWhite){
        super(_x, _y, isWhite);
        System.out.println("king constr " + _x + " " + _y);
//        x = _x;
//        y = _y;
//        white = isWhite;
//        setIcon();
//        setButton();
        setButton();
        String color = "black";
        if (isWhite) color = "white";
        System.out.println("creating " + color + " king on " + x + ", " + y);
    }
    public void setIcon(){
        String color = "white";
        if (!isWhite) color = "black";
        String name = getFilename() + "_" + color;
        icon = new ImageIcon(name);
    }
    public void setButton(){
        CheckersSquare button = JavaCheckers.jButtons[x][y];
        button.setPiece(this);
        button.setText(blackMan);
        if (isWhite)
            button.setText(whiteMan);
        currentSquare = button;
    }

    public String getFilename(){
        return filename;
    }

    public boolean isValidMove(CheckersSquare square1, CheckersSquare square2) {
        if (isTeamMate(square2))
            return false;
        if (!isPossibleMove(square2))
            return false;
        return true;
    }

    public boolean isPossibleMove(CheckersSquare square2){
        if (!square2.isPlayableSquare()) return false;
        CheckersSquare oldSquare = currentSquare;
        if (square2.getPiece() != null) return false;
        int horizontalDiff = Math.abs(x - square2.getRow());
        int verticalDiff = Math.abs(y - square2.getCol());
        return horizontalDiff == verticalDiff;
    }

    private boolean isTeamMate(CheckersSquare square2) {
        return square2.getPiece() != null && square2.getPiece().isWhite == isWhite;
    }

    public int getDirection(){
        int multiplier = isWhite ? 1 : 0;
        multiplier *= 2;
        multiplier -= 1;
        return -multiplier;
    }
//    public boolean move(CheckersSquare square1, CheckersSquare square2){
//        if (!isValidMove(square1, square2)) {
//            JavaCheckers.removeCurrentSquare();
//            return false;
//        }
//
//        x = square2.getRow();
//        y = square2.getCol();
//        moveCount += 1;
//        square2.setIcon(icon);
//        JavaCheckers.currentSquare.removeIcon();
//        JavaCheckers.currentSquare.removePiece();
//        JavaCheckers.removeCurrentSquare();
//        previousMove = JavaCheckers.moveNum;
//        System.out.println("moved" + this.getClass() + "move = "+ previousMove);
//        currentSquare = square2;
//        return true;
//
//    }

    public String location(){
        return "(" + x + "," + y + ")";
    }

    public int getSpeed() {
        return speed;
    }
}

