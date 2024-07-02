package chess;

import java.util.ArrayList;

public class KnightMoveCalculator extends MoveCalculator {
    @Override
    protected void moveCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        // Check diagonally down-left
        checkNearby(avMvs, board, myPosition, -2, -1);
        checkNearby(avMvs, board, myPosition, -1, -2);
        // Check diagonally down-right
        checkNearby(avMvs, board, myPosition, -2, 1);
        checkNearby(avMvs, board, myPosition, -1, 2);
        // Check diagonally up-left
        checkNearby(avMvs, board, myPosition, 2, -1);
        checkNearby(avMvs, board, myPosition, 1, -2);
        // Check diagonally up-right
        checkNearby(avMvs, board, myPosition, 1, 2);
        checkNearby(avMvs, board, myPosition, 2, 1);
    }
}
