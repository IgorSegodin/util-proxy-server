package org.isegodin.proxyserver;

import java.io.IOException;
import java.net.Socket;

/**
 * @author i.segodin
 */
public class ClientConnection extends Thread implements Executor {

    private Socket clientSocket;
    private Socket forwardSocket;

    private static volatile int count = 0;

    private volatile int executed = 2;

    public ClientConnection(Socket clientSocket, Socket forwardSocket) {
        this.clientSocket = clientSocket;
        this.forwardSocket = forwardSocket;
    }

    @Override
    public synchronized void finished() {
        executed -= 1;
        notify();
    }

    private synchronized void begin(){
        System.out.println(count);
        count++;}
    private synchronized void end(){count--;
        System.out.println(count);}

    @Override
    public void run() {
        begin();
        try {
            new StreamForwardThread(this, clientSocket.getInputStream(), forwardSocket.getOutputStream()).start();
            new StreamForwardThread(this, forwardSocket.getInputStream(), clientSocket.getOutputStream()).start();

            while (executed > 0) {

                synchronized (this) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        //
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                forwardSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        end();
    }
}
