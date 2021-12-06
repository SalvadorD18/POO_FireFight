package pt.iul.poo.firefight.starterpack;

import pt.iul.ista.poo.utils.Point2D;

//Esta classe de exemplo esta' definida de forma muito basica, sem relacoes de heranca
//Tem atributos e metodos repetidos em relacao ao que está definido noutras classes 
//Isso sera' de evitar na versao a serio do projeto

public class Fire extends GameElement {

	public Fire(Point2D position) {
		super(position);
	}

	@Override
	public String getName() {
		return "fire";
	}

	@Override
	public int getLayer() {
		return 1;
	}

	@Override
	public void updateElement() {
		// TODO Auto-generated method stub
		
	}
}
