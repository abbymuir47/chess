package service;

import dataaccess.*;
import handlerModel.*;
import model.*;
import org.eclipse.jetty.server.Authentication;

import java.util.Collection;



public class GameService {

    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;

    public GameService(UserDAO userDataAccess, AuthDAO authDataAccess, GameDAO gameDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public ClearResult clear() throws DataAccessException {
        userDataAccess.clearUserDAO();
        authDataAccess.clearAuthDAO();
        gameDataAccess.clearGameDAO();
        ClearResult result = new ClearResult();
        return result;
    }

//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}
}
