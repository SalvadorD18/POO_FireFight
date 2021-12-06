package pt.iul.poo.firefight.starterpack;

import java.util.List;

import pt.iul.ista.poo.gui.ImageTile;
import pt.iul.ista.poo.utils.Point2D;

public abstract class Terrain extends GameElement implements Burnable, Updatable {

	GameEngine gameEngine = GameEngine.getInstance();

	private GameElement gameElement;

	private boolean isBurnt;
	
	int count = 0;

	public Terrain(Point2D position) {

		super(position);
	}

	public boolean isBurnt() {
		return isBurnt;
	}
	
	public void setBurnt(boolean isBurnt) {
		this.isBurnt = isBurnt;
	}

	@Override
	public void burn() {
		if(!isBurnt && gameEngine.isBurning(this.getPosition())) {		
			List<Point2D> neighbours = this.getPosition().getNeighbourhoodPoints();
			for (Point2D point2d : neighbours) {
				double p = Math.random();
				GameElement gameElement = gameEngine.getGameElement(point2d);
				if(gameElement != null) {
					if (gameElement.getName().equals("eucaliptus") && p < 0.10) {
						gameEngine.addGameElement(new Fire(point2d));
					} else if (gameElement.getName().equals("pine") && p < 0.15) {
						gameEngine.addGameElement(new Fire(point2d));
					} else if (gameElement.getName().equals("grass") && p < 0.05) {
						gameEngine.addGameElement(new Fire(point2d));
					}
				}
			}
		}
	}
		
	public void updateElement() {
		burn();	
	}
}