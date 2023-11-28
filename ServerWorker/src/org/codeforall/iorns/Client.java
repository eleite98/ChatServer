package org.codeforall.iorns;

import java.io.*;
import java.net.Socket;

public class Client implements Runnable{

    public static void main(String[] args) {
        Client client = new Client();
        client.addClient();
    }

    private Socket clientSocket;
    private BufferedReader bufferedReader;
    public PrintWriter printWriter;

    public Client() {
        try {
            this.clientSocket = new Socket("localhost", Server.PORT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addClient() {

        try {

            while (true) {

                // Create a printWriter to read
                printWriter = new PrintWriter(clientSocket.getOutputStream(), true);

                // Create input and output streams for communication with the WorkerServer
                InputStream inputStream = clientSocket.getInputStream();
                OutputStream outputStream = clientSocket.getOutputStream();

                //Read the server's first message
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String messageFromServerWorker = bufferedReader.readLine();


                printWriter.println(messageFromServerWorker);
                printWriter.flush();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void run() {

        String consoleMessage = null;

    }
}
