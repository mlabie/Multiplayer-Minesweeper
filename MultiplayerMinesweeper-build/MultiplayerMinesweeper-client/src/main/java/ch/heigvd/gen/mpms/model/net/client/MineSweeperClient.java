package ch.heigvd.gen.mpms.model.net.client;

import ch.heigvd.gen.mpms.controller.*;

public class MineSweeperClient {

    private SenderWorker       senderWorker;
    private ReceptionistWorker receptionistWorker;

    private MainWindowController mainWindowController;

    public MineSweeperClient(SenderWorker senderWorker, ReceptionistWorker receptionistWorker){
        this.senderWorker       = senderWorker;
        this.receptionistWorker = receptionistWorker;
        mainWindowController    = null;
    }

    public void setMainWindowController(MainWindowController mainWindowController) {
        this.mainWindowController = mainWindowController;
    }

    public MainWindowController getMainWindowController() {
        return mainWindowController;
    }

    public SenderWorker getSenderWorker() {
        return senderWorker;
    }

    public void setSenderWorker(SenderWorker senderWorker) {
        this.senderWorker = senderWorker;
    }

    public ReceptionistWorker getReceptionistWorker() {
        return receptionistWorker;
    }

    public void setReceptionistWorker(ReceptionistWorker receptionistWorker) {
        this.receptionistWorker = receptionistWorker;
    }
}
