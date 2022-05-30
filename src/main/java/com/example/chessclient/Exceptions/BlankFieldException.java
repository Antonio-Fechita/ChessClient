package com.example.chessclient.Exceptions;

public class BlankFieldException extends Exception{

    public BlankFieldException() {
        super("You cannot leave blank fields!");
    }
}
