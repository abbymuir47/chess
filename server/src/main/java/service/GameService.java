package service;

import dataaccess.*;


public class GameService {

    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;
    private final GameDAO gameDataAccess;

    public GameService(UserDAO userDataAccess, AuthDAO authDataAccess, GameDAO gameDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
        this.gameDataAccess = gameDataAccess;
    }

    public void clear() throws DataAccessException {
        gameDataAccess.clearGameDAO();
    }

//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}
}
