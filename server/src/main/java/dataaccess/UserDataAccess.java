package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class UserDataAccess implements UserDAO {

    final private HashMap<String, UserData> users = new HashMap<>();

    public UserData getUser(String username) {
        return users.get(username);
    }

    public UserData createUser(UserData user) {
        UserData myUser = new UserData(user.username(), user.password(), user.email());

        users.put(user.username(), myUser);
        return user;
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
