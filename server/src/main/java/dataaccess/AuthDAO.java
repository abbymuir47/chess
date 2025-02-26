package dataaccess;
import model.*;

import javax.xml.crypto.Data;
import java.util.Collection;

public interface AuthDAO {

    //AuthData getAuth(String username) throws DataAccessException;
    AuthData createAuth(AuthData auth) throws DataAccessException;

//    void deleteUser(Integer id) throws DataAccessException;
//    void deleteAllUsers() throws DataAccessException;
}
