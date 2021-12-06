package pt.iul.poo.firefight.starterpack;

import java.awt.event.KeyEvent;

import pt.iul.ista.poo.utils.Point2D;

public class Water extends GameElement { 

	private String name = "water";
	public Water(Point2D position) {
		super(position);
	}

	GameEngine gameEngine = GameEngine.getInstance();

	@Override
	public String getName() {
		return name;
	}
	
	public void setWaterDirection(int key) {
		if (key == KeyEvent.VK_UP)
			name = "water_up";
		if (key == KeyEvent.VK_LEFT)
			name = "water_left";
		if (key == KeyEvent.VK_RIGHT)
			name = "water_right";
		if (key == KeyEvent.VK_DOWN)
			name = "water_down";}

	@Override
	public int getLayer() {
		return 0;
	}

	//	public Point2D getPosition() {
	//		return position;
	//	}

	@Override
	public void updateElement() {
		gameEngine.removeGameElement(gameEngine.getGameElement(getPosition()));
	}
}
