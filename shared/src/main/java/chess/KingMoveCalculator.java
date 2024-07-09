package chess;

import java.util.ArrayList;

public class KingMoveCalculator extends MoveCalculator {
    @Override
    public void moveCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        checkNearby(avMvs, board, myPosition, 1, 1);
        checkNearby(avMvs, board, myPosition, -1, 1);
        checkNearby(avMvs, board, myPosition, 1, -1);
        checkNearby(avMvs, board, myPosition, -1, -1);
        checkNearby(avMvs, board, myPosition, 0, 1);
        checkNearby(avMvs, board, myPosition, 0, -1);
        checkNearby(avMvs, board, myPosition, 1, 0);
        checkNearby(avMvs, board, myPosition, -1, 0);
    }
}
