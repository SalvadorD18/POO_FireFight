package pt.iul.poo.firefight.starterpack;

import pt.iul.ista.poo.utils.Point2D;

public class Abies extends Terrain {

	public Abies(Point2D position) {
		super(position, 20);
	}

	@Override
	public String getName() {
		if (!super.isBurnt()) {
			return "abies";
		}
		return "burntabies";
	}

	@Override
	public int getLayer() {
		return 0;
	}
	
	@Override
	public void updateElement() {
		super.updateElement();
		burned(20);
	}

}
