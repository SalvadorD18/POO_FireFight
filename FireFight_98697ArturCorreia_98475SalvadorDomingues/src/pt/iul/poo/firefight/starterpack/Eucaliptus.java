package pt.iul.poo.firefight.starterpack;

import pt.iul.ista.poo.utils.Point2D;

public class Eucaliptus extends Terrain {


	public Eucaliptus(Point2D position) {
		super(position);
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public String getName() {
		if (!super.isBurnt()) {
			return "eucaliptus";
		}
		return "burnteucaliptus";
	}

	@Override
	public void updateElement() {
		super.updateElement();
		GameEngine gameEngine = GameEngine.getInstance();
		if (gameEngine.isBurning(getPosition())) {
			count++;
			if (count > 5) {
				setBurnt(true);
				gameEngine.removeGameElement(gameEngine.getGameElement(getPosition()));
			}

		}
	}


}
