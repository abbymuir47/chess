package dataaccess;

import model.UserData;

import java.util.Collection;
import java.util.HashMap;

public class UserDataAccess implements UserDAO {

    final private HashMap<UserData> users = new HashMap<>();

    public UserData createUser(UserData user) {
        user = new UserData(user.username(), user.password(), user.email());
        //in petshop they have an integer for id, should i implement that that way?

        users.put(user);
        return user;
    }

    public UserData getPet(String username) {
        return users.get(username);
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
