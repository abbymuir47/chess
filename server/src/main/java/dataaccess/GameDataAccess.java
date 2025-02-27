package dataaccess;
import java.util.HashMap;

import model.GameData;

public class GameDataAccess implements GameDAO {

    private HashMap<Integer, GameData> games = new HashMap<>();


    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        games.put(game.gameID(), game);
        return game;
    }

    @Override
    public GameData updateGame(GameData game) throws DataAccessException {
        return null;
    }

    @Override
    public GameData listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void clearGameDAO() throws DataAccessException{
        games.clear();
    }
}