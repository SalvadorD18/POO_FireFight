package pt.iul.poo.firefight.starterpack;

import pt.iul.ista.poo.utils.Point2D;

public class Land extends GameElement{

	public Land(Point2D position) {
		super(position);
	}

	@Override
	public String getName() {
		return "land";
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public void updateElement() {
		// TODO Auto-generated method stub
		
	}
	
	

	
}
