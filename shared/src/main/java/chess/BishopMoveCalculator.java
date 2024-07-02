package chess;

import java.util.ArrayList;

public class BishopMoveCalculator extends MoveCalculator {
    @Override
    protected void moveCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        diagonalCheck(avMvs, board, myPosition);
    }
}
