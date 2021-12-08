package pt.iul.poo.firefight.starterpack;

import pt.iul.ista.poo.utils.Point2D;

public class Grass extends Terrain {

	public Grass(Point2D position) {
		super(position, 3);
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public String getName() {
		if (!super.isBurnt()) {
			return "grass";
		}
		return "burntgrass";
	}

	@Override
	public void updateElement() {
		super.updateElement();
		burned(3);
	}
}