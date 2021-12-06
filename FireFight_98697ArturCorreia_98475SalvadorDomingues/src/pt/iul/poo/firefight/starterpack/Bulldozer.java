package pt.iul.poo.firefight.starterpack;

import java.awt.event.KeyEvent;

import pt.iul.ista.poo.utils.Direction;
import pt.iul.ista.poo.utils.Point2D;

public class Bulldozer extends MovableObject {

	String name = "bulldozer";

	public Bulldozer(Point2D position) {
		super(position);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getLayer() {
		return 2;
	}

	public void move(int key) {
		GameEngine gameEngine = GameEngine.getInstance();

		Direction direction = Direction.directionFor(key);
		Point2D newPosition = super.getPosition().plus(direction.asVector());

		setBulldozerDirection(key);
		if(gameEngine.isBurning(newPosition)) {

		}

		if (canMoveTo(newPosition) && !gameEngine.isBurning(newPosition)) {
			setPosition(newPosition);
			gameEngine.addGameElement(new Land(getPosition()));
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

	public void setBulldozerDirection(int key) {
		if (key == KeyEvent.VK_UP)
			name = "bulldozer_up";
		if (key == KeyEvent.VK_LEFT)
			name = "bulldozer_left";
		if (key == KeyEvent.VK_RIGHT)
			name = "bulldozer_right";
		if (key == KeyEvent.VK_DOWN)
			name = "bulldozer_down";
	}

	@Override
	public void updateElement() {
		// TODO Auto-generated method stub

	}

	@Override
	public void activateElement() {
		// TODO Auto-generated method stub
		
	}

}
