package com.example.chessclient.Exceptions;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super("User is not registered in database!");
    }
}
