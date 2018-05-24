package ch.heigvd.gen.mpms.game;

import java.util.ArrayList;
import java.util.Map;

public class BoardGame {
	private ArrayList<ArrayList<Map<Integer, Boolean>>> board;

	public BoardGame(int size, int difficulty, boolean bonusMalus){
		board = new ArrayList<>(size);
	}
}
