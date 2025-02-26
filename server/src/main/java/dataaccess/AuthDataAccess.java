package dataaccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashMap;

public class AuthDataAccess implements AuthDAO {

    final private HashMap<String, AuthData> auths = new HashMap<>();

    //public AuthData getAuth(String username) {return auths.get(username);}

    public AuthData createAuth(AuthData auth) {
        AuthData myAuth = new AuthData(auth.authToken(), auth.username());

        auths.put(myAuth.authToken(), myAuth);
        return auth;
    }

    // i know im not doing this right - should the hashmap
}
