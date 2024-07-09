package chess;

import java.util.ArrayList;

public class RookMoveCalculator extends MoveCalculator{
    @Override
    public void moveCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        straightCheck(avMvs, board, myPosition);
    }
}
