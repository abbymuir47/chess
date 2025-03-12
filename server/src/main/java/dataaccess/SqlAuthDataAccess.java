package dataaccess;

import model.AuthData;

public class SqlAuthDataAccess implements AuthDAO{
    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return null;
    }

    @Override
    public AuthData createAuth(AuthData auth) throws DataAccessException {
        return null;
    }

    @Override
    public void clearAuthDAO() throws DataAccessException {

    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {

    }
}
