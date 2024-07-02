package chess;

import java.util.ArrayList;

public class KingMoveCalculator extends MoveCalculator{
    @Override
    protected void moveCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        checkNearby(avMvs, board, myPosition, 1, 0); // Check Up
        checkNearby(avMvs, board, myPosition, -1, 0); // Check Down
        checkNearby(avMvs, board, myPosition, 0, -1); // Check Left
        checkNearby(avMvs, board, myPosition, 0, 1); // Check Right
        checkNearby(avMvs, board, myPosition, -1, -1); // Check diagonally down-left
        checkNearby(avMvs, board, myPosition, -1, 1); // Check diagonally down-right
        checkNearby(avMvs, board, myPosition, 1, -1);  // Check diagonally up-left
        checkNearby(avMvs, board, myPosition, 1, 1); // Check diagonally up-right
    }
}
