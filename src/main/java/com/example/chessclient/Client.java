package com.example.chessclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client extends Thread
{
    public volatile String latestCommand = null;
    public volatile String latestResponse = "--idle--";

    public void setLatestCommand(String newCommand)
    {
        latestCommand = newCommand;
    }

    public String getLatestResponse() {
        return latestResponse;
    }

    public void run()
    {
        String serverAddress = "127.0.0.1"; // The server's IP address
        int PORT = 8100; // The server's port
        try (
                Socket socket = new Socket(serverAddress, PORT);
                PrintWriter out =
                        new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()))) {
            do {
                // Send a request to the server
                StringBuilder builder = new StringBuilder(AuthenticationToken.getAuthenticationToken() + " ");

                String command;

                if (latestCommand != null)
                {
                    command = latestCommand;
                    latestCommand = null;
                }
                else
                {
                    command = "idle";
                }

                if (command.equals("exit"))
                    break;

                builder.append(command);
//                System.out.println("Sent command: " + builder);
                out.println(builder);
                latestResponse = in.readLine();
//                System.out.println("Latest response: " + latestResponse);
                String[] lookForToken = latestResponse.split("%");
                if (lookForToken[0].equals("#@Tkn") && lookForToken.length == 2)
                    AuthenticationToken.setAuthenticationToken(lookForToken[1]);

                if ( !latestResponse.equals("--idle--") )
                    System.out.println(latestResponse);
            } while (true);
        } catch (Exception e) {
            System.err.println("No server listening... " + e);
            System.out.println(e.getMessage());
        }
    }

}

