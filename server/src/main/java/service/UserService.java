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
        }
        /*
        //create AuthData object, then create authtoken, then create RegisterResult object and return it

        //AuthData auth = new
        //RegisterResult result = new RegisterResult(user.username(), authData.authToken());
        //return registerResult
         */

        return null;
    }
//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}
}
