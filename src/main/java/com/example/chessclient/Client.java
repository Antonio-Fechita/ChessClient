package com.example.chessclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Client extends Thread {
    public volatile String latestCommand = null;
    public volatile String latestResponse = "--idle--";
    List<String> commandsQueue = new ArrayList<>();

    public void setLastCommand(String newCommand) {
        commandsQueue.add(newCommand);
    }

    public void setFirstCommand(String newCommand) {
        if (commandsQueue.size() == 0)
            commandsQueue.add(newCommand);
        else
            commandsQueue.add(0, newCommand);

    }

    public String getLatestResponse(boolean forBoardListener) {
        if (forBoardListener)
            while (!latestResponse.startsWith("%")) ;
        else {
            while (latestResponse.equals("--idle--") || latestResponse.startsWith("%")) ;
        }
        return latestResponse;
    }

    public String getResponseForCommand(String command, boolean hasPriority, boolean includingBoardListener) {
        if (hasPriority) {
            setFirstCommand(command);
//            System.out.println("Command sent with priority: " + command);
//            System.out.println("Confirmation: " + commandsQueue.get(0));
        } else
            setLastCommand(command);
        return getLatestResponse(includingBoardListener);
    }


    public void run() {
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

//                if (latestCommand != null)
//                {
//                    commandsQueue.add(0,latestCommand);
//                    latestCommand = null;
//                }
//                else
//                {
//                    commandsQueue.add(0,"idle");
//                }


//                if(commandsQueue.size()==0){
//                    commandsQueue.add("idle");
//                }


                if (commandsQueue.size() > 0) {
                    builder.append(commandsQueue.get(0));
                    commandsQueue.remove(0);
                    if (!builder.toString().endsWith("idle") && !builder.toString().endsWith("boardStatus"))
                        System.out.println("COMMAND SENT: " + builder);
                    out.println(builder);
                }
                if(in.ready()){
                    latestResponse = in.readLine();
                }

                if (!latestResponse.startsWith("%") && !latestResponse.startsWith("--idle--"))
                    System.out.println("RESPONSE RECEIVED [" + latestResponse + "]");
                String[] lookForToken = latestResponse.split("%");
                if (lookForToken[0].equals("#@Tkn") && lookForToken.length == 2)
                    AuthenticationToken.setAuthenticationToken(lookForToken[1]);


            } while (true);
        } catch (Exception e) {
            // System.err.println("No server listening... " + e);
        }
    }

}

