package chess;

import java.util.ArrayList;

public class QueenMoveCalculator extends MoveCalculator{
    @Override
    public void moveCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        diagonalCheck(avMvs, board, myPosition);
        straightCheck(avMvs, board, myPosition);
    }
}
