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
        //UserData myUser = new UserData(user.username(), user.password(), user.email());
        users.put(user.username(), user);
        return user;
    }

    @Override
    public void clearUserDAO() throws DataAccessException{
        users.clear();
    }
    /*
    public void deletePet(Integer id) {
        pets.remove(id);
    }
    public void deleteAllPets() {
        pets.clear();
    }
     */
}
