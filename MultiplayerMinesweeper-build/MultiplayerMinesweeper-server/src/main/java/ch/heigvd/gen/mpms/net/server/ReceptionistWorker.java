package ch.heigvd.gen.mpms.net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import ch.heigvd.gen.mpms.net.Protocol.MinesweeperProtocol;

/**
 * This class imlements the receptionnist of the server. For each client
 * that wants to connect to the server, the server will create a threaded
 * servant for him. This Servant will manage the clients command.
 *
 * @source cf https://wasadigi.gitbooks.io/res-heigvd-network-programming-application-protoc/content/tcp_programming.html
 * @author Olivier Liechti
 *
 * @author Corentin Basler, Antonio Cusanelli, Marc Labie, Simon Jobin
 */
public class ReceptionistWorker implements Runnable {

    final static Logger LOG = Logger.getLogger(ReceptionistWorker.class.getName());

    private int port;

    /**
     * Default constructor. Instanciate with the default port defined in
     * the protocol.
     */
    public ReceptionistWorker(){
        this(MinesweeperProtocol.DEFAULT_PORT);
    }


    /**
     * Instanciate the receptionnist with a given port.
     * @param port
     */
    public ReceptionistWorker(int port){
        this.port = port;
    }


    /**
     * Run method of the thread. Starts the receptionnist.
     */
    @Override
    public void run() {
        ServerSocket serverSocket;

        try{
            serverSocket = new ServerSocket(port);
        }catch (IOException e){
            LOG.log(Level.SEVERE, e.getMessage(), e);
            return;
        }

        while (true) {
            LOG.info("Waiting for a new client...");
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ServantWorker(clientSocket)).start();
            } catch (IOException e) {
                LOG.log(Level.SEVERE, e.getMessage(), e);
            }
        }

    }
}
