package service;

import chess.ChessGame;
import dataaccess.*;
import handlermodel.*;
import model.*;

import java.util.ArrayList;

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
        ListResult result = new ListResult((ArrayList<GameData>) gameDataAccess.listGames());
        return result;
    }

    public CreateResult creategame(CreateRequest createRequest) throws DataAccessException {
        ChessGame chessGame = new ChessGame();
        GameData game = new GameData(0, null, null, createRequest.gameName(), chessGame);
        GameData insertedGame = gameDataAccess.createGame(game);
        CreateResult result = new CreateResult(insertedGame.gameID());
        return result;
    }

    public void joingame(JoinRequest joinRequest, String username) throws DataAccessException {
        GameData game = gameDataAccess.getGame(joinRequest.gameID());
        GameData updatedGame;

        if(game != null){

            if(joinRequest.playerColor()==null){
                throw new DataAccessException(400, "Error: bad request");
            }

            if(joinRequest.playerColor().equals("WHITE")){
                if(game.whiteUsername() == null){
                    updatedGame = new GameData(game.gameID(), username, game.blackUsername(), game.gameName(), game.game());
                }
                else{
                    throw new DataAccessException(403, "Error: white color already taken");
                }
            }
            else if(joinRequest.playerColor().equals("BLACK")){
                if(game.blackUsername() == null){
                    updatedGame = new GameData(game.gameID(), game.whiteUsername(), username, game.gameName(), game.game());
                }
                else{
                    throw new DataAccessException(403, "Error: black color already taken");
                }
            }
            else{
                throw new DataAccessException(400, "Error: bad request");
            }
            gameDataAccess.updateGame(updatedGame);
        }
        else{
            throw new DataAccessException(400, "Error: bad request");
        }
    }



//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}
}
