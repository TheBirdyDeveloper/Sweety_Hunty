package multiplayerLogic;

import org.jgroups.Address;
import java.io.Serializable;

public class GameMessageContent implements Serializable{

	private final int opCode;
	private final String id;
	private final int x;
	private final int y;
	private final int score;
	
	public GameMessageContent(int opCode, String id, int x, int y, int score) {
		// TODO Auto-generated constructor stub
		this.opCode = opCode;
		this.id = id;
		this.x = x;
		this.y = y;
		this.score = score;
	}

	public int getOpCode() {
		return opCode;
	}

	public String getId() {
		return id;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getScore() {
		return score;
	}
	
	
	
}
