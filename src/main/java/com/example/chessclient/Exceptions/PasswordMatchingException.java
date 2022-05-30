package com.example.chessclient.Exceptions;

public class PasswordMatchingException extends Exception{

    public PasswordMatchingException() {
        super("Password does not match with confirmation password");
    }
}
