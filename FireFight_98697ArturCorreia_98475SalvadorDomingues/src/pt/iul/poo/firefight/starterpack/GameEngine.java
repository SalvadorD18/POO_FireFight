package pt.iul.poo.firefight.starterpack;

import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import pt.iul.ista.poo.gui.ImageMatrixGUI;
import pt.iul.ista.poo.observer.Observed;
import pt.iul.ista.poo.observer.Observer;
import pt.iul.ista.poo.utils.Direction;
import pt.iul.ista.poo.utils.Point2D;

// Note que esta classe e' um exemplo - nao pretende ser o inicio do projeto, 
// embora tambem possa ser usada para isso.
//
// No seu projeto e' suposto haver metodos diferentes.
// 
// As coisas que comuns com o projeto, e que se pretendem ilustrar aqui, sao:
// - GameEngine implementa Observer - para  ter o metodo update(...)  
// - Configurar a janela do interface grafico (GUI):
//        + definir as dimensoes
//        + registar o objeto GameEngine ativo como observador da GUI
//        + lancar a GUI
// - O metodo update(...) e' invocado automaticamente sempre que se carrega numa tecla
//
// Tudo o mais podera' ser diferente!


public class GameEngine implements Observer {

	private static GameEngine INSTANCE = null;

	// Dimensões da grelha de jogo
	public static final int GRID_HEIGHT = 10;
	public static final int GRID_WIDTH = 10;

	private ImageMatrixGUI gui;  		// Referência para ImageMatrixGUI (janela de interface com o utilizador) 
	private List<GameElement> tileList;	// Lista de imagens
	private Fireman fireman;			// Referencia para o bombeiro
	private Bulldozer bulldozer;
	int level = 0;
	int score = 0;
	String nickname;
	ArrayList<Player> ListOfPlayers = new ArrayList<>();
	//Player ActivePlayer = new Player(nickname, score);

	List<GameElement> aux_add = new ArrayList<>(); // lista auxiliar para que se adicionem elementos para que posteriormente sejam todos adicionados à tileList
	List<GameElement> aux_remove = new ArrayList<>();


	// Neste exemplo o setup inicial da janela que faz a interface com o utilizador e' feito no construtor 
	// Tambem poderia ser feito no main - estes passos tem sempre que ser feitos!
	private GameEngine() {

		gui = ImageMatrixGUI.getInstance();    // 1. obter instancia ativa de ImageMatrixGUI	
		gui.setSize(GRID_HEIGHT, GRID_WIDTH);  // 2. configurar as dimensoes 
		gui.registerObserver(this);            // 3. registar o objeto ativo GameEngine como observador da GUI
		gui.go();                              // 4. lancar a GUI

		tileList = new ArrayList<>();   
	}

	public List<GameElement> getTileList() {
		return tileList;
	}

	public static GameEngine getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new GameEngine();
		}

		return INSTANCE;
	}

	public GameElement getFireman() {
		return fireman;
	}


	// O metodo update() e' invocado sempre que o utilizador carrega numa tecla
	// no argumento do metodo e' passada um referencia para o objeto observado (neste caso seria a GUI)
	@Override
	public void update(Observed source) {
		aux_add.clear();
		aux_remove.clear();
		int key = gui.keyPressed();              // obtem o codigo da tecla pressionada

		if (!fireman.isOnBulldozer()) {
			if (key == KeyEvent.VK_DOWN)            // se a tecla for ENTER, manda o bombeiro mover
				fireman.move(key);	
			else if (key == KeyEvent.VK_UP)
				fireman.move(key);
			else if (key == KeyEvent.VK_LEFT)
				fireman.move(key);
			else if (key == KeyEvent.VK_RIGHT)
				fireman.move(key);
		}	else {
			if (key == KeyEvent.VK_DOWN)            
				bulldozer.move(key);	// Quando estão 2 bulldozer's no mapa, só move um deles, independentemente do qual o bombeiro entra, devido a este bulldozer.move
			else if (key == KeyEvent.VK_UP)
				bulldozer.move(key);
			else if (key == KeyEvent.VK_LEFT)
				bulldozer.move(key);
			else if (key == KeyEvent.VK_RIGHT)
				bulldozer.move(key);
		}
		if (key == KeyEvent.VK_ENTER) {
			fireman.exitBulldozer();
			fireman.setPosition(bulldozer.getPosition());
		}

		if (key == KeyEvent.VK_P)
			callPlane();

		if (isTheMapStillOnFire() == false) {
			scoreRegist();
			score = 0;
			changeMap();
		}

		//Controlar para não ter pontos negativos -> impor um limite
		//Pontuar por fogos apagados pelo avião
		//Pontuar, talvez, por .....

		for (GameElement gameElement : tileList) {
			gameElement.updateElement();
		}
		gui.setStatusMessage("Pontuação de " + nickname + " : "+getGameEngineScore());

		tileList.addAll(aux_add);
		tileList.removeAll(aux_remove);

		for (GameElement gameElement : tileList) {
			System.out.println("nome:" + gameElement.getName() + " posição:" + gameElement.getPosition() + " está a arder:" + isBurning(gameElement.getPosition()));
		}
		gui.update();                            // redesenha as imagens na GUI, tendo em conta as novas posicoes
	}

	// Criacao dos objetos e envio das imagens para GUI
	public void start(File file) throws FileNotFoundException {

		createMatrix(file);      // criar mapa do terreno
		createMoreStuff(file);    // criar mais objetos (bombeiro, fogo,...)
		sendImagesToGUI();    // enviar as imagens para a GUI
		ImageIcon icon = new ImageIcon("images/fireman.png");
		nickname = String.valueOf(JOptionPane.showInputDialog(null, "Introduz o teu nickname: ", "FireFight", JOptionPane.PLAIN_MESSAGE, icon, null, ""));
	}

	public char[][] createMatrix(File file) throws FileNotFoundException {
		char[][] m = new char[GRID_HEIGHT][];
		Scanner s = new Scanner(file);
		int i = 0;

		while(s.hasNextLine()) {
			if(i < 10) {
				m[i] = s.nextLine().toCharArray();
			} else {
				s.nextLine();
			}
			i++;

		}
		createTerrain(m);
		s.close();
		return m;
	}

	// Criacao do terreno
	private void createTerrain(char[][] mapa) {

		for (int y = 0; y < GRID_HEIGHT; y++) {
			for (int x = 0; x < GRID_WIDTH; x++) {
				char c = mapa[y][x];
				if (c == 'p') {
					tileList.add(new Pine(new Point2D(x,y)));
				} else if (c == 'e'){
					tileList.add(new Eucaliptus(new Point2D(x,y)));
				} else if (c == 'm'){
					tileList.add(new Grass(new Point2D(x,y)));
				} else if (c == '_'){
					tileList.add(new Land(new Point2D(x,y)));
				}
			}
		}
	}


	// Criacao de mais objetos
	private void createMoreStuff(File file) throws FileNotFoundException {
		Scanner s = new Scanner(file);
		while (s.hasNextLine()) {
			String str = s.next();
			if (str.equals("Fireman")) {
				fireman = new Fireman(new Point2D(s.nextInt(),s.nextInt()));
				tileList.add(fireman);
			} else if (str.equals("Bulldozer")) {
				bulldozer = new Bulldozer(new Point2D(s.nextInt(),s.nextInt()));
				tileList.add(bulldozer);
			} else if (str.equals("Fire")) {
				tileList.add(new Fire(new Point2D(s.nextInt(),s.nextInt())));
			} else {
				s.nextLine();
			}
		}
		s.close();
	}


	// Envio das mensagens para a GUI - note que isto só precisa de ser feito no inicio
	// Nao é suposto re-enviar os objetos se a unica coisa que muda sao as posicoes  
	private void sendImagesToGUI() {
		for (GameElement gameElement : tileList) {
			gui.addImage(gameElement);
		}
	}

	public GameElement getGameElement(Point2D position) {
		for (int i = tileList.size() - 1; i >= 0; i--) {
			GameElement gameElement = tileList.get(i);
			if (gameElement.getPosition().equals(position)) {
				return gameElement;
			}
		}
		return null;
	}

	public void addGameElement(GameElement ge) {
		aux_add.add(ge);
		gui.addImage(ge);
	}

	public void removeGameElement(GameElement ge) {
		aux_remove.add(ge);
		gui.removeImage(ge);
	}

	public boolean isBurning(Point2D position) {
		for (GameElement gameElement : tileList) {
			if (gameElement instanceof Fire && gameElement.getPosition().equals(position)) {
				return true;
			}
		}
		return false;
	}

	public boolean isThereABulldozer(Point2D position) {
		for (GameElement gameElement : tileList) {
			if (gameElement instanceof Bulldozer && gameElement.getPosition().equals(position)) {
				return true;
			}
		}
		return false;
	}

	public boolean isThereAFireman() {
		for (GameElement gameElement : tileList) {
			if (gameElement instanceof Fireman) {
				return true;
			}
		}
		return false;
	}

	public void callPlane() {
		int max_index = 0;
		int max_value = fireCount(0);
		for (int x = 1; x < GRID_WIDTH; x++) {
			int count = fireCount(x);
			if(count > max_value) {
				max_value = count;
				max_index = x;
			}
		}
		Plane plane = new Plane(new Point2D(max_index, GRID_HEIGHT - 1));
		addGameElement(plane);
	}

	public int fireCount(int x) {
		int count = 0;
		for (GameElement gameElement : tileList) {
			if (gameElement instanceof Fire && gameElement.getPosition().getX() == x) {
				count ++;
			}
		}
		return count;
	}

	public GameElement fireOfThisPosition(Point2D position) {
		for (GameElement gameElement : tileList) {
			if (gameElement instanceof Fire && gameElement.getPosition().equals(position)) {
				return gameElement;
			}
		}
		return null;
	}

	public boolean isTheMapStillOnFire() {
		for (GameElement gameElement : tileList) {
			if (gameElement instanceof Fire)
				return true;
		}
		return false;
	}

	public void changeMap() {
		try {
			tileList.clear();
			ListOfPlayers.clear();
			gui.clearImages();
			level++;
			String name = "levels\\level"+level+".txt";
			File file = new File(name);
			System.out.println(name+ " "+ file.exists());
			createTerrain(createMatrix(file));
			createMoreStuff(file);
			sendImagesToGUI();
		} catch (FileNotFoundException e) {
			System.out.println("Ficheiro não encontrado.");
		}
	}

	public void readScoreFile() {
		try {
			String name = "Leaderboards\\scores_level"+level+".txt";
			File file = new File(name);
			Scanner s = new Scanner(file);
			while (s.hasNextLine()) {
				String nick = s.next();
				int scoreOfMap = s.nextInt();
				if (s.hasNextLine())
					s.nextLine();
				ListOfPlayers.add(new Player(nick, scoreOfMap));
			}
			s.close();
		} catch (FileNotFoundException e) {
			System.out.println("O mapa não tem pontuações disponíveis.");
		}
	}

	public void scoreRegist() {
		try {
			readScoreFile();
			String name = "Leaderboards\\scores_level"+level+".txt";
			Player ActivePlayer = new Player(nickname, getGameEngineScore());
			ListOfPlayers.add(ActivePlayer);
			File file = new File(name);
			PrintWriter pw = new PrintWriter(file);
			ListOfPlayers.sort((a,b) -> b.getScore() - a.getScore());
			for (int i = 0; i < 5 && i < ListOfPlayers.size(); i++) {
				pw.println(ListOfPlayers.get(i).getNickname()+ " " +ListOfPlayers.get(i).getScore());
			}
			pw.close();

		} catch (FileNotFoundException e) {
			System.out.println("O mapa não tem pontuações disponíveis.");
		}
	}

	public int getGameEngineScore() {
		if (score < 0)
			score = 0;
		return score;
	}

	public ArrayList<Player> getListOfPlayers() {
		return ListOfPlayers;
	}
}