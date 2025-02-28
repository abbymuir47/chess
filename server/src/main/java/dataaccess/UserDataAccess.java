package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class UserDataAccess implements UserDAO {

    private HashMap<String, UserData> users = new HashMap<>();

    @Override
    public UserData getUser(String username) {
        return users.get(username);
    }

    @Override
    public UserData createUser(UserData user) {
        users.put(user.username(), user);
        return user;
    }

    @Override
    public void clearUserDAO() throws DataAccessException{
        users.clear();
    }
}
