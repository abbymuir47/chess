package service;

import dataaccess.*;
import handlermodel.*;
import model.*;

import java.util.Objects;


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

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        //UserData myUser = new UserData(loginRequest.username(), loginRequest.password());

        UserData currUser = userDataAccess.getUser(loginRequest.username());
        if(currUser != null){
            String expectedPassword = currUser.password();

            if (loginRequest.password().equals(expectedPassword)) {
                String authToken = AuthService.generateToken();
                LoginResult result = new LoginResult(loginRequest.username(), authToken);
                //need to make an AuthData object and call createAuth() ??
                return result;
            }
            else{
                throw new DataAccessException(401, "Error: unauthorized");
            }
        }
        else{
            throw new DataAccessException(401, "Error: unauthorized");
        }
    }

    public void logout(String authToken) throws DataAccessException {
        authDataAccess.deleteAuth(authToken);
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserService that = (UserService) o;
        return Objects.equals(userDataAccess, that.userDataAccess) && Objects.equals(authDataAccess, that.authDataAccess);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userDataAccess, authDataAccess);
    }
}
