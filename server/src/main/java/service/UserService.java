package service;

import dataaccess.*;
import handlerModel.*;
import model.*;
import org.eclipse.jetty.server.Authentication;

import java.util.Collection;



public class UserService {

    private final UserDAO userDataAccess;
    private final AuthDAO authDataAccess;

    public UserService(UserDAO userDataAccess, AuthDAO authDataAccess) {
        this.userDataAccess = userDataAccess;
        this.authDataAccess = authDataAccess;
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {

        UserData user = userDataAccess.getUser(registerRequest.username());
        if(user != null){
            throw new DataAccessException(400, "Error: user already exists");
        }
        else{
            UserData myUser = new UserData(registerRequest.username(), registerRequest.password(), registerRequest.email());
            userDataAccess.createUser(myUser);
            String authToken = AuthService.generateToken();
            AuthData myAuth = new AuthData(authToken, registerRequest.username());
            AuthData auth = authDataAccess.createAuth(myAuth);
            RegisterResult result = new RegisterResult(authToken, user.username());
            return result;
        }
    }
//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}
}
