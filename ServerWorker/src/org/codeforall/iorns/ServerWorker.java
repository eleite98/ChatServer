package org.codeforall.iorns;

import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.net.Socket;

public class ServerWorker implements Runnable {

    private Server server;

    private Socket clientSocket;
    private BufferedReader bufferedReader;
    public PrintWriter printWriter;
    public String name;


    public ServerWorker(Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.server = server;
        setUpStreams();
    }


    public String receive() {
        try {
            return bufferedReader.readLine();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void send(String message) {
        printWriter.println(message);
    }


    public String getAddress(Socket clientSocket) {
        return clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getLocalPort();
    }

    public void setUpStreams() {
        try {
            printWriter = new PrintWriter(clientSocket.getOutputStream(), true);

            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            printWriter.write("What's your user name?");
            printWriter.flush();
            name = bufferedReader.readLine();


           // InputStreamReader inputStreamReader = new InputStreamReader(System.in);
           // BufferedReader bufferedReader = new BufferedReader(inputStreamReader);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String getName(){
        return name;
    }

    @Override
    public void run() {

        while (true) {
            String message = receive();
            server.sendAll(name, message);
            System.out.println(name +": "+ message);
        }
    }

}

