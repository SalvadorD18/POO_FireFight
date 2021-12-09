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

	public Fireman getFireman() {
		return fireman;
	}


	// O metodo update() e' invocado sempre que o utilizador carrega numa tecla
	// no argumento do metodo e' passada um referencia para o objeto observado (neste caso seria a GUI)
	@Override
	public void update(Observed source) {
		aux_add.clear();
		aux_remove.clear();
		int key = gui.keyPressed();              // obtem o codigo da tecla pressionada

		if(Direction.isDirection(key)) { // controlar o movimento do elemento que está ativo, recebendo uma 'key' (direção dada pela tecla carregada no teclado)
			activeElement().move(key);
		}
		
		if (key == KeyEvent.VK_ENTER) { // ao ser pressionada a tecla ENTER no teclado, o bombeiro sai do bulldozer, sendo definida a mesma posição do bulldozer, e utilização do ActiveElement para reativar o fireman
			fireman.exitVehicle();
			fireman.setPosition(activeElement().getPosition());
			activeElement().setActiveElement(false);
		}
		
		if (key == KeyEvent.VK_P) // chamada do avião ao ser pressionada a tecla 'P'
			callPlane();

		if (isTheMapStillOnFire() == false) { // caso já não exista nenhum fogo no mapa, as pontuações são registadas num ficheiro, pela função scoreRegist(), e coloca a pontuação a 0 para que seja inicializado o novo nível, sendo carregado o novo mapa
			scoreRegist();
			score = 0;
			changeMap();
		}

		for (GameElement gameElement : tileList) {
			gameElement.updateElement();
		}
		gui.setStatusMessage("Pontuação de " + nickname + " : "+getGameEngineScore()); // definição da barra de estado, que é atualizada ao longo do jogo com a pontuação do jogador

		tileList.addAll(aux_add);
		tileList.removeAll(aux_remove);

		gui.update();                            // redesenha as imagens na GUI, tendo em conta as novas posicoes
	}

	public MovableObject activeElement() { // percorre a tileList à procura de um MovableObject que esteja ativo, e retorna-o
		for (GameElement gameElement : tileList) {
			if (gameElement instanceof MovableObject && ((MovableObject) gameElement).isActive())
				return (MovableObject) gameElement; 
		}
		return null;
	}

	// Criacao dos objetos e envio das imagens para GUI
	public void start(File file) throws FileNotFoundException {

		createMatrix(file);      // criar mapa do terreno
		createMoreStuff(file);    // criar mais objetos (bombeiro, fogo,...)
		sendImagesToGUI();    // enviar as imagens para a GUI
		ImageIcon icon = new ImageIcon("images/fireman.png");
		nickname = String.valueOf(JOptionPane.showInputDialog(null, "Introduz o teu nickname: ", "FireFight", JOptionPane.PLAIN_MESSAGE, icon, null, "")); // criação da janela para pedir um nickname ao utilizador para que se inicie o jogo
	}

	public char[][] createMatrix(File file) throws FileNotFoundException { // transformação de um ficheiro ".txt" numa matriz, para criar o terreno de jogo com a mesma
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
	private void createTerrain(char[][] mapa) { // recebe a matriz acima criada, e constrói o terreno de jogo, com os diferentes tipos de terreno

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
				} else if (c == 'a'){
					tileList.add(new Abies(new Point2D(x,y)));
				} else if (c == 'b'){
					tileList.add(new FuelBarrel(new Point2D(x,y)));
				}
			}
		}
	}


	// Criacao de mais objetos
	private void createMoreStuff(File file) throws FileNotFoundException { // leitura do resto do ficheiro, para que sejam adicionados os elementos de jogo
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

	public GameElement getGameElement(Point2D position) { // percorre a tileList, e retorna um GameElement de uma determinada posição
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

	public boolean isBurning(Point2D position) { // verifica se uma determinada posição está a arder
		for (GameElement gameElement : tileList) {
			if (gameElement instanceof Fire && gameElement.getPosition().equals(position)) {
				return true;
			}
		}
		return false;
	}

	public boolean isThereABulldozer(Point2D position) { // verifica se uma determinada posição contém um bulldozer 
		for (GameElement gameElement : tileList) {
			if (gameElement instanceof Bulldozer && gameElement.getPosition().equals(position)) {
				return true;
			}
		}
		return false;
	}

	public boolean isThereAFireman() { // verifica se uma determinada posição contém um bombeiro 
		for (GameElement gameElement : tileList) {
			if (gameElement instanceof Fireman) {
				return true;
			}
		}
		return false;
	}

	public void callPlane() { // chama o avião, e coloca-o na posição, tendo em conta o número de fogos de uma coluna
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

	public int fireCount(int x) { // conta os fogos de cada coluna, para atribuir a posição do avião
		int count = 0;
		for (GameElement gameElement : tileList) {
			if (gameElement instanceof Fire && gameElement.getPosition().getX() == x) {
				count ++;
			}
		}
		return count;
	}

	public GameElement fireOfThisPosition(Point2D position) { // devolve o fogo de uma determinada posição
		for (GameElement gameElement : tileList) {
			if (gameElement instanceof Fire && gameElement.getPosition().equals(position)) {
				return gameElement;
			}
		}
		return null;
	}
	
	public GameElement explosionOfThisPosition(Point2D position) { // devolve a explosão de uma determinada posição
		for (GameElement gameElement : tileList) {
			if (gameElement instanceof Explosion && gameElement.getPosition().equals(position)) {
				return gameElement;
			}
		}
		return null;
	}
	
	public MovableObject bulldozerOfThisPosition(Point2D position) { // devolve o bulldozer de uma determinada posição
		for (GameElement gameElement : tileList) {
			if (gameElement instanceof Bulldozer && gameElement.getPosition().equals(position)) {
				return (MovableObject) gameElement;
			}
		}
		return null;
	}
	
	public Terrain terrainOfThisPosition(Point2D position) { // devolve o terreno de uma determinada posição
		for (GameElement gameElement : tileList) {
			if (gameElement instanceof Terrain && gameElement.getPosition().equals(position)) {
				return (Terrain) gameElement;
			}
		}
		return null;
	}

	public boolean isTheMapStillOnFire() { // verifica se o mapa ainda está a arder
		for (GameElement gameElement : tileList) {
			if (gameElement instanceof Fire)
				return true;
		}
		return false;
	}

	public void changeMap() { // muda de mapa, quando o atual já não está a arder
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

	public void readScoreFile() { // lê as pontuações de um ficheiro
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

	public void scoreRegist() { // regista as pontuações num ficheiro
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