package service;

import handlerModel.*;

public class UserService {
    public RegisterResult register(RegisterRequest registerRequest) {
        //call getUser(username) - UserDAO function
        //if user exists, throw exception
        //if user doesn't exist, createUser() and createAuth() and add to database
        //return registerResult
        return null;
    }
//    public LoginResult login(LoginRequest loginRequest) {}
//    public void logout(LogoutRequest logoutRequest) {}
}