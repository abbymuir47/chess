package dataaccess;

import model.AuthData;

import java.util.HashMap;

public class MemoryAuthDataAccess implements AuthDAO {

    private HashMap<String, AuthData> auths = new HashMap<>();

    public AuthData getAuth(String authToken) throws DataAccessException {return auths.get(authToken);}

    @Override
    public AuthData createAuth(AuthData auth) {
        //AuthData myAuth = new AuthData(auth.authToken(), auth.username());
        auths.put(auth.authToken(), auth);
        return auth;
    }

    @Override
    public void clearAuthDAO() throws DataAccessException{
        auths.clear();
    }

    public void deleteAuth(String authToken) throws DataAccessException{
        auths.remove(authToken);
    }
}
