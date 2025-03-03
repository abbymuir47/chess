package dataaccess;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import chess.ChessGame;
import model.GameData;

public class GameDataAccess implements GameDAO {

    private int nextId = 1;
    private HashMap<Integer, GameData> games = new HashMap<>();


    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        game = new GameData(nextId++, null, null, game.gameName(), new ChessGame());

        games.put(game.gameID(), game);
        return game;
    }

    @Override
    public GameData updateGame(GameData game) throws DataAccessException {
        games.put(game.gameID(), game);
        return game;
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return new ArrayList<>(games.values());
    }

    @Override
    public void clearGameDAO() throws DataAccessException{
        games.clear();
    }
}