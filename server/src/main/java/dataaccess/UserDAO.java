package dataaccess;
import model.UserData;

import java.sql.SQLException;

public interface UserDAO {
    //see PetShop for example implementation of this!
    //createUser, getUser
    UserData getUser(String username) throws DataAccessException, SQLException;
    UserData createUser(UserData user) throws DataAccessException, SQLException;
    void clearUserDAO() throws DataAccessException;
//    void deleteUser(Integer id) throws DataAccessException;
//    void deleteAllUsers() throws DataAccessException;
}
