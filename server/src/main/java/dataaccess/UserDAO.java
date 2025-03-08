package dataaccess;
import model.UserData;

public interface UserDAO {
    //see PetShop for example implementation of this!
    //createUser, getUser
    UserData getUser(String username) throws DataAccessException;
    UserData createUser(UserData user) throws DataAccessException;
    void clearUserDAO() throws DataAccessException;
//    void deleteUser(Integer id) throws DataAccessException;
//    void deleteAllUsers() throws DataAccessException;
}
