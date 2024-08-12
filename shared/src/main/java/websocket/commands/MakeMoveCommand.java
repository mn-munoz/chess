package websocket.commands;
import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    private final ChessMove move;


    public MakeMoveCommand(String authToken, Integer gameID,ChessMove chessMove) {
        super(CommandType.MAKE_MOVE, authToken, gameID);
        this.move = chessMove;
    }

    public ChessMove getChessMove() {
        return move;
    }
}
