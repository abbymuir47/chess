package service;

import chess.ChessGame;
import dataaccess.*;
import handlermodel.*;
import model.*;


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

//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}
}
