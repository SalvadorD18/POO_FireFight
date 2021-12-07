package pt.iul.poo.firefight.starterpack;

public class Player {
	String nickname;
	int score = 0;

	public Player(String nickname) {
		this.nickname = nickname;
	}

	public String getNickname() {
		return nickname;
	}

	public int getScore() {
		return score;
	}
	
	public void setScore(int newScore) {
		score = newScore;
	}
}
