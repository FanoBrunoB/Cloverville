package com.example.cloverville.Login;

public class LoginModel {

    public boolean isEmpty(String input) {
        return input.isEmpty();
    }

    public boolean login(String username, String password)
    {
        String usernameCredentials = "green bob";
        if(!(usernameCredentials.equals(username)))
            return false;
        String passwordCredentials = "admin";
        if(passwordCredentials.equals(password))
            return true;
        else
            return false;
    }
}
