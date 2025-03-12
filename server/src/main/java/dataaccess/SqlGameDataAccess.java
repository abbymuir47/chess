package dataaccess;

import model.GameData;

import java.util.List;

public class SqlGameDataAccess implements GameDAO{
    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        return null;
    }

    @Override
    public GameData updateGame(GameData game) throws DataAccessException {
        return null;
    }

    @Override
    public List<GameData> listGames() throws DataAccessException {
        return List.of();
    }

    @Override
    public void clearGameDAO() throws DataAccessException {

    }
}
