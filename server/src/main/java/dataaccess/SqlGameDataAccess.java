package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class SqlGameDataAccess implements GameDAO{
    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        GameData myGame;
        String query = "SELECT * FROM game WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, String.valueOf(gameID));
                var rs = preparedStatement.executeQuery();
                if (rs.next()) {
                    ChessGame chessGame = new Gson().fromJson(rs.getString("game_data"), ChessGame.class);
                    myGame = new GameData(rs.getInt("gameID"), rs.getString("whiteUsername"),
                            rs.getString("blackUsername"), rs.getString("game_name"), chessGame);
                    return myGame;
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public GameData createGame(GameData game) throws DataAccessException {
        var json = new Gson().toJson(game.game());
        int myID = 0;

        String query = "INSERT INTO game (gameID, whiteUsername, blackUsername, game_name, game_data) VALUES (?, ?, ?, ?, ?)";

        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(query, RETURN_GENERATED_KEYS)) {

                preparedStatement.setInt(1, game.gameID());
                preparedStatement.setString(2, game.whiteUsername());
                preparedStatement.setString(3, game.blackUsername());
                preparedStatement.setString(4, game.gameName());
                preparedStatement.setString(5, json);
                preparedStatement.executeUpdate();

                var rs = preparedStatement.getGeneratedKeys();
                if (rs.next()) {
                    myID = rs.getInt(1);
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        return new GameData(myID, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
    }

    @Override
    public GameData updateGame(GameData game) throws DataAccessException {
        var json = new Gson().toJson(game.game());
        String query = "UPDATE game SET whiteUsername = ?, blackUsername = ?, game_name = ?, game_data = ? WHERE gameID = ?";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(query)) {

                preparedStatement.setString(1, game.whiteUsername());
                preparedStatement.setString(2, game.blackUsername());
                preparedStatement.setString(3, game.gameName());
                preparedStatement.setString(4, json);
                preparedStatement.setInt(5, game.gameID());

                preparedStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        return game;
    }

    @Override
    public void clearGameDAO() throws DataAccessException {
        String query = "TRUNCATE TABLE game";
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        ArrayList<GameData> list = new ArrayList<GameData>();
        ChessGame chessGame;
        GameData currGame;

        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM game";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    while (rs.next()) {
                        Integer id = rs.getInt("gameID");
                        String white = rs.getString("whiteUsername");
                        String black = rs.getString("blackUsername");
                        String name = rs.getString("game_name");
                        var json = rs.getString("game_data");
                        chessGame = new Gson().fromJson(json, ChessGame.class);

                        currGame = new GameData(id, white, black, name, chessGame);
                        list.add(currGame);
                    }
                }
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }
}
