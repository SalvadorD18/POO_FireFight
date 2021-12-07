package pt.iul.poo.firefight.starterpack;

import pt.iul.ista.poo.utils.Point2D;

//Esta classe de exemplo esta' definida de forma muito basica, sem relacoes de heranca
//Tem atributos e metodos repetidos em relacao ao que está definido noutras classes 
//Isso sera' de evitar na versao a serio do projeto

public class Pine extends Terrain {

	public Pine(Point2D position) {
		super(position);
	}

	@Override
	public int getLayer() {
		return 0;
	}

	@Override
	public String getName() {
		if (!super.isBurnt()) {
			return "pine";
		}
		return "burntpine";
	}

	@Override
	public void updateElement() {
		super.updateElement();
		burned(10);
	}
}