package pt.iul.poo.firefight.starterpack;

public class Player {

	GameEngine gameEngine = GameEngine.getInstance();
	
	String nickname;
	int score = 0;
	
	public Player(String nickname, int score) {
		this.nickname = nickname;
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
}
