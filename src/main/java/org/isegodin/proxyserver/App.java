package org.isegodin.proxyserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author i.segodin
 */
public class App {

    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(8084);

        while (true) {
            Socket clientSocket = server.accept();
            System.out.println(clientSocket.getInetAddress());
            try {
                new ClientConnection(clientSocket, new Socket("localhost", 8083)).start();
            } catch (IOException e) {
                e.printStackTrace();
                clientSocket.close();
            }
        }
    }
}
