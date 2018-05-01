package ch.heigvd.gen.mpms.net.server;

import java.io.*;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * cf https://wasadigi.gitbooks.io/res-heigvd-network-programming-application-protoc/content/tcp_programming.html
 */
public class ServantWorker implements Runnable{

    final static Logger LOG = Logger.getLogger(ServantWorker.class.getName());

    private Socket clientSocket;

    private BufferedReader  br = null;
    private PrintWriter     pw = null;


    public ServantWorker(Socket clientSocket) {

        try {
            this.clientSocket = clientSocket;
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
        } catch (IOException e) {
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    public void run() {

    }
}
