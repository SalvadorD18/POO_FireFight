package pt.iul.poo.firefight.starterpack;

import java.util.List;

import pt.iul.ista.poo.utils.Point2D;

public class FuelBarrel extends Terrain {

	public FuelBarrel(Point2D position) {
		super(position, 3);
	}

	@Override
	public String getName() {
		if (!super.isBurnt()) {
			return "fuelbarrel";
		} else
			return "burntfuelbarrel";
	}

	@Override
	public int getLayer() {
		return 0;
	}

	public void burned(int cycle) {
		if (gameEngine.isBurning(getPosition())) {
			count++;
			if (count >= cycle) {
				gameEngine.score -= cycle;
				List<Point2D> wideNeighbours = this.getPosition().getWideNeighbourhoodPoints();
				Explosion explosion = new Explosion(getPosition());
				gameEngine.addGameElement(explosion);
				setBurnt(true);
				gameEngine.removeGameElement(gameEngine.fireOfThisPosition(getPosition()));
				for (Point2D point2d : wideNeighbours) {
					if (!gameEngine.getGameElement(point2d).getName().equals("land") && !gameEngine.terrainOfThisPosition(point2d).isBurnt() && !gameEngine.isBurning(point2d))
						gameEngine.addGameElement(new Fire(point2d));
				}
			}
		}
	}

	@Override
	public void updateElement() {
		super.updateElement();
		burned(3);
	}
}
