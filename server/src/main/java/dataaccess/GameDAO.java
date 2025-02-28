package dataaccess;

import model.*;

import java.util.List;

public interface GameDAO {
    GameData getGame(int gameID) throws DataAccessException;
    GameData createGame(GameData game) throws DataAccessException;
    GameData updateGame(GameData game) throws DataAccessException;
    List<GameData> listGames() throws DataAccessException;
    void clearGameDAO() throws DataAccessException;
}
