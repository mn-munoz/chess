package chess;

import java.util.ArrayList;

public class QueenMoveCalculator extends MoveCalculator{
    @Override
    protected void moveCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        diagonalCheck(avMvs,board,myPosition);
        calculateHorizontalMoves(avMvs, board, myPosition);
        calculateVerticalMoves(avMvs, board, myPosition);
    }
}
