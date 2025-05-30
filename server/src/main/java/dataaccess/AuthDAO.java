package dataaccess;
import model.*;

import javax.xml.crypto.Data;
import java.sql.SQLException;
import java.util.Collection;

public interface AuthDAO {

    AuthData getAuth(String authToken) throws DataAccessException, SQLException;
    AuthData createAuth(AuthData auth) throws DataAccessException;
    void clearAuthDAO() throws DataAccessException;
    void deleteAuth(String authToken) throws DataAccessException;

//    void deleteUser(Integer id) throws DataAccessException;
//    void deleteAllUsers() throws DataAccessException;
}
