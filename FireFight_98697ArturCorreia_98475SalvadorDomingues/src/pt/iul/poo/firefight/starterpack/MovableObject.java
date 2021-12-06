package pt.iul.poo.firefight.starterpack;

import pt.iul.ista.poo.utils.Direction;
import pt.iul.ista.poo.utils.Point2D;

public abstract class MovableObject extends GameElement implements Movable, Updatable, ActiveElement {

	GameEngine gameEngine = GameEngine.getInstance();

	private boolean isOnBulldozer;

	public MovableObject(Point2D position) {
		super(position);
	}

	public void move(int key) {

		Direction direction = Direction.directionFor(key);
		Point2D newPosition = super.getPosition().plus(direction.asVector());

		if (!gameEngine.isBurning(newPosition) && canMoveTo(newPosition)) {
			setPosition(newPosition);
		}
	}

	// Verifica se a posicao p esta' dentro da grelha de jogo
	public boolean canMoveTo(Point2D p) {

		if (p.getX() < 0) return false;
		if (p.getY() < 0) return false;
		if (p.getX() >= GameEngine.GRID_WIDTH) return false;
		if (p.getY() >= GameEngine.GRID_HEIGHT) return false;
		return true;
	}

	public boolean isOnBulldozer() {
		return isOnBulldozer;
	}

	public void setOnBulldozer(boolean isOnBulldozer) {
		this.isOnBulldozer = isOnBulldozer;
	}
}
