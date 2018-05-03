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
        String line;
        boolean shouldRun = true;

        try {
            LOG.info("Reading until client sends BYE or closes the connection...");
            while ((shouldRun) && (line = br.readLine()) != null) {

            }

            LOG.info("Cleaning up resources...");
            br.close();
            pw.close();
            clientSocket.close();

        } catch (IOException e) {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
            if (pw != null) {
                pw.close();
            }
            if (clientSocket != null) {
                try {
                    clientSocket.close();
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
            LOG.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
