package service;

import dataaccess.*;
import handlermodel.*;
import model.*;


public class UserService {

    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;

    public UserService(UserDAO userDataAccess, AuthDAO authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {

        if (registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null) {
            throw new DataAccessException(400, "Error: bad request - all fields must be filled");
        }

        UserData user = userDataAccess.getUser(registerRequest.username());
        if(user != null){
            throw new DataAccessException(403, "Error: user already exists");
        }
        else{
            UserData myUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            userDataAccess.createUser(myUser);
            String authToken = AuthService.generateToken();
            AuthData myAuth = new AuthData(authToken, registerRequest.username());
            AuthData auth = authDataAccess.createAuth(myAuth);
            RegisterResult result = new RegisterResult(registerRequest.username(), authToken);
            return result;
        }
    }

    public void clear() throws DataAccessException {
        userDataAccess.clearUserDAO();
    }
//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}
}
