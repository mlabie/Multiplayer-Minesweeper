package ch.heigvd.gen.mpms.net.client;

public interface IMinesweeperClientAdmin {
	void openLobby();

	void closeLobby();

	void expelLobby(String player);

	void setScoreMode(int mode);

	void setMineAmount(int ratio);

	void setSize(int size);

	void setPlayerAmount(int numberOfPlayer);

	void enableBonusMalus(boolean enable);

	void startGame();
}
