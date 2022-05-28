package com.example.chessclient;

public class AuthenticationToken
{
    static String authenticationToken;

    public static String getAuthenticationToken()
    {
        if (authenticationToken == null)
            return "NULL";

        return authenticationToken;
    }

    public static void setAuthenticationToken(String newAuthenticationToken) {
        authenticationToken = newAuthenticationToken;
    }
}
