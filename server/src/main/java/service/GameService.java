package service;

import chess.ChessGame;
import dataaccess.*;
import handlermodel.*;
import model.*;

import static chess.ChessGame.TeamColor.WHITE;


public class GameService {

    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;

    public GameService(UserDAO userDataAccess, AuthDAO authDataAccess, GameDAO gameDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public void clear() throws DataAccessException {
        gameDataAccess.clearGameDAO();
    }

    public ListResult listgames(String authToken) throws DataAccessException {
        ListResult result = new ListResult(gameDataAccess.listGames());
        return result;
    }

    public CreateResult creategame(CreateRequest createRequest) throws DataAccessException {
        int gameID = 1;
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(gameID, null, null, createRequest.gameName(), chessGame);
        gameDataAccess.createGame(game);
        CreateResult result = new CreateResult(gameID);
        return result;
    }

    public void joingame(JoinRequest joinRequest, String username) throws DataAccessException {
        GameData game = gameDataAccess.getGame(joinRequest.gameID());
        if(game != null){
            ChessGame.TeamColor color = joinRequest.color();
            if(color == ChessGame.TeamColor.WHITE){
                if(game.whiteUsername() == null){
                    game = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                }
                else{
                    throw new DataAccessException(403, "Error: white color already taken");
                }
            }
            else {
                if(game.blackUsername() == null){
                    game = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                }
                else{
                    throw new DataAccessException(403, "Error: black color already taken");
                }
            }
            gameDataAccess.updateGame(game);
        }
        else{
            throw new DataAccessException(400, "Error: bad request");
        }
    }



//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}
}
