package pt.iul.poo.firefight.starterpack;

import java.awt.event.KeyEvent;

import pt.iul.ista.poo.utils.Direction;
import pt.iul.ista.poo.utils.Point2D;

// Esta classe de exemplo esta' definida de forma muito basica, sem relacoes de heranca
// Tem atributos e metodos repetidos em relacao ao que está definido noutras classes 
// Isso sera' de evitar na versao a serio do projeto

public class Fireman extends MovableObject {
	
	String name = "fireman";

	private boolean isOnBulldozer;

	GameEngine gameEngine = GameEngine.getInstance();

	public Fireman(Point2D position) {
		super(position);
	}

	public boolean isOnBulldozer() {
		return isOnBulldozer;
	}

	public void setOnBulldozer(boolean isOnBulldozer) {
		this.isOnBulldozer = isOnBulldozer;
	}
	
	public void exitBulldozer() {
		setOnBulldozer(false);
		gameEngine.addGameElement(gameEngine.getFireman());
	}

	// Metodos de ImageTile
	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getLayer() {
		return 3;
	}

	@Override
	public void move(int key) {

		Direction direction = Direction.directionFor(key);
		Point2D newPosition = super.getPosition().plus(direction.asVector());

		super.move(key);
		
		setFiremanDirection(key);

		if (gameEngine.isBurning(newPosition)) {
			Water water = new Water(newPosition);
			water.setWaterDirection(key);
			gameEngine.addGameElement(water);
			gameEngine.removeGameElement(gameEngine.getGameElement(newPosition));
		}

		if (key == KeyEvent.VK_ENTER) {
			exitBulldozer();
		}
	}
	
	public void setFiremanDirection(int key) {
		if (key == KeyEvent.VK_LEFT)
			name = "fireman_left";
		if (key == KeyEvent.VK_RIGHT)
			name = "fireman_right";
	}

	@Override
	public void updateElement() {
		if (gameEngine.isThereABulldozer(getPosition())) {
			setOnBulldozer(true);
			gameEngine.removeGameElement(gameEngine.getFireman());
		}
	}

	@Override
	public void activateElement() {
		// TODO Auto-generated method stub
		
	}
}