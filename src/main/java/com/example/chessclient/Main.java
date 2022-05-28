package com.example.chessclient;

import java.util.Scanner;

public class Main
{
    public static void main(String[] args)
    {

        Client client = new Client();

        client.start();

        while (true)
        {
            System.out.print(">");

            Scanner scanner = new Scanner( System.in );

            String line = scanner.nextLine();

            client.setLastCommand( line );
        }
    }
}
