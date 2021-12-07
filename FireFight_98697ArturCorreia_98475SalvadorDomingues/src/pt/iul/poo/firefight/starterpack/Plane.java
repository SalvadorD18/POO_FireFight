package pt.iul.poo.firefight.starterpack;

import pt.iul.ista.poo.utils.Point2D;

public class Plane extends GameElement {

	GameEngine gameEngine = GameEngine.getInstance();

	public Plane(Point2D position) {
		super(position);
	}

	@Override
	public String getName() {
		return "plane";
	}

	@Override
	public int getLayer() {
		return 2;
	}

	public void removeFire() {
		if (gameEngine.isBurning(getPosition())) {
			gameEngine.removeGameElement(gameEngine.fireOfThisPosition(getPosition()));
		}
		if (gameEngine.isBurning(new Point2D(getPosition().getX(), getPosition().getY() + 1))) {
			gameEngine.removeGameElement(gameEngine.fireOfThisPosition(new Point2D(getPosition().getX(), getPosition().getY() + 1)));
		}
		if (gameEngine.isBurning(new Point2D(getPosition().getX(), gameEngine.GRID_HEIGHT - 1))) {
			gameEngine.removeGameElement(gameEngine.fireOfThisPosition(new Point2D(getPosition().getX(), gameEngine.GRID_HEIGHT - 1)));
		}
	}

	@Override
	public void updateElement() {
		setPosition(new Point2D(getPosition().getX(), getPosition().getY() - 2));	
		removeFire();
	}
}
