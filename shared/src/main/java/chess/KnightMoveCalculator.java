package chess;

import java.util.ArrayList;

public class KnightMoveCalculator extends MoveCalculator{
    @Override
    public void moveCheck(ArrayList<ChessMove> avMvs, ChessBoard board, ChessPosition myPosition) {
        checkNearby(avMvs, board, myPosition, 2, 1);
        checkNearby(avMvs, board, myPosition, 1, 2);

        checkNearby(avMvs, board, myPosition, -2, 1);
        checkNearby(avMvs, board, myPosition, 2, -1);

        checkNearby(avMvs, board, myPosition, -1, 2);
        checkNearby(avMvs, board, myPosition, 1, -2);

        checkNearby(avMvs, board, myPosition, -1, -2);
        checkNearby(avMvs, board, myPosition, -2, -1);
    }
}
