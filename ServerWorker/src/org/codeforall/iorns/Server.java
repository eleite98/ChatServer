package org.codeforall.iorns;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ThreadPoolExecutor;

public class Server {

    public static final int PORT = 9090;
    private ServerSocket serverSocket;
    private LinkedList<ServerWorker> list;


    public static void main(String[] args) {

        while (true) {
            Server server = new Server();
            server.listen(PORT);
        }
    }

    public Server() {
        this.list = new LinkedList<>();
    }


    private void listen(int port) {
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server is running in PORT: " + PORT);
            serve(serverSocket);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void serve(ServerSocket serverSocket) {
        while (true) {

            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("new connection from " + clientSocket.getInetAddress().getHostName());
                ServerWorker serverWorker = new ServerWorker(clientSocket, this);
                list.add(serverWorker);
                Thread thread = new Thread(serverWorker);
                thread.start();


            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public void sendAll(String name, String message) {
        if (message.equals("/exit")) {
            try {
                serverSocket.close();
                System.exit(1);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (message.equals("/list")) {
            for (int i = 0; i < list.size(); i++) {

                if (name.equals(list.get(i).getName())) {

                    ServerWorker selected = list.get(i);

                    for (int j = 0; j < list.size(); j++) {
                        selected.printWriter.write("Persons online: " + list.get(j).getName() + "\n");
                    }
                }
            }
        }

        for (int i = 0; i < list.size(); i++) {
            list.get(i).send(name + ": " + message);
        }
    }
}
