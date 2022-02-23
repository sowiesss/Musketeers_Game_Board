package game;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ThreeMusketeers {

    private final Board board;
    private Agent musketeerAgent, guardAgent;
    private final Scanner scanner = new Scanner(System.in);
    private final List<Move> moves = new ArrayList<>();

    // All possible game modes
    public enum GameMode {
        Human("Human vs Human"),
        HumanRandom("Human vs Computer (Random)");

        private final String gameMode;
        GameMode(final String gameMode) {
            this.gameMode = gameMode;
        }
    }

    /**
     * Default constructor to load Starter board
     */
    public ThreeMusketeers() {
        this.board = new Board();
    }

    /**
     * Constructor to load custom board
     * @param boardFilePath filepath of custom board
     */
    public ThreeMusketeers(String boardFilePath) {
        this.board = new Board(boardFilePath);
    }

    /**
     * Play game with human input mode selector
     */
    public void play(){
        System.out.println("Welcome! \n");
        final GameMode mode = getModeInput();
        System.out.println("Playing " + mode.gameMode);
        play(mode);
    }

    /**
     * Play game without human input mode selector
     * @param mode the GameMode to run
     */
    public void play(GameMode mode){
        selectMode(mode);
        runGame();
    }

    /**
     * Mode selector sets the correct agents based on the given GameMode
     * @param mode the selected GameMode
     */
    private void selectMode(GameMode mode) {
        switch (mode) {
            case Human -> {
                musketeerAgent = new HumanAgent(board);
                guardAgent = new HumanAgent(board);
            }
            case HumanRandom -> {
                String side = getSideInput();
                
                // The following statement may look weird, but it's what is known as a ternary statement.
                // Essentially, it sets musketeerAgent equal to a new HumanAgent if the value M is entered,
                // Otherwise, it sets musketeerAgent equal to a new RandomAgent
                musketeerAgent = side.equals("M") ? new HumanAgent(board) : new RandomAgent(board);
                
                guardAgent = side.equals("G") ? new HumanAgent(board) : new RandomAgent(board);
            }

        }
    }

    /**
     * Runs the game, handling human input for move actions
     * Handles moves for different agents based on current turn 
     */
    private void runGame() {
        try {
			while(!board.isGameOver()) {
			    System.out.println("\n" + board);

			    final Agent currentAgent;
			    if (board.getTurn() == Piece.Type.MUSKETEER)
			        currentAgent = musketeerAgent;
			    else
			        currentAgent = guardAgent;

			    if (currentAgent instanceof HumanAgent) // Human move
			        switch (getInputOption()) {
			            case "M":
			                move(currentAgent);
			                break;
			            case "U":
			                if (moves.size() == 0) {
			                    System.out.println("No moves to undo.");
			                    continue;//skip println
			                }
			                else if (moves.size() == 1 || isHumansPlaying()) {
			                    undoMove();
			                }
			                else {//human random
			                    undoMove();
			                    undoMove();
			                }
			                System.out.println("Undid the previous move.");
			                break;
			            case "S":
			                board.saveBoard();
			                break;
			        }
			    else { // Computer move
			        System.out.printf("[%s] Calculating move...\n", currentAgent.getClass().getSimpleName());
			        move(currentAgent);
			    }
			}
		} catch (IllegalStateException e) {
			System.out.println(e.getMessage() +"\n Please restart.");
		}

        System.out.println(board);
        System.out.printf("\n%s won!%n", board.getWinner().getType());
    }

    /**
     * Gets a move from the given agent, adds a copy of the move using the copy constructor to the moves stack, and
     * does the move on the board.
     * @param agent Agent to get the move from.
     */
    protected void move(final Agent agent) { // TODO
    	Move m = agent.getMove();
    	moves.add(new Move(m));
    	if(agent instanceof RandomAgent) {
    		System.out.printf("[%s (Random Agent)] Moving piece %s.\n"
    							,board.getTurn(), m);
    	}
    	board.move(m);
    }
    
//    private String translateMove(Cell cell) {
//		return "" + Utils.convertIntToLetter(cell.getCoordinate().col+1) + (cell.getCoordinate().row +1);
//	}

    /**
     * Removes a move from the top of the moves stack and undoes the move on the board.
     */
    private void undoMove() { // TODO
    	Move m = moves.remove(moves.size()-1);
    	board.undoMove(m);
    }

    /**
     * Get human input for move action
     * @return the selected move action, 'M': move, 'U': undo, and 'S': save
     */
    private String getInputOption() {
        System.out.printf("[%s] Enter 'M' to move, 'U' to undo, and 'S' to save: ", board.getTurn().getType());
        while (!scanner.hasNext("[MUSmus]")) {
            System.out.print("Invalid option. Enter 'M', 'U', or 'S': ");
            scanner.next();//skip the invalid 
        }
        return scanner.next().toUpperCase();
    }

    /**
     * Returns whether both sides are human players
     * @return True if both sides are Human, False if one of the sides is a computer
     */
    private boolean isHumansPlaying() {
        return musketeerAgent instanceof HumanAgent && guardAgent instanceof HumanAgent;
    }

    /**
     * Get human input for side selection
     * @return the selected Human side for Human vs Computer games,  'M': Musketeer, G': Guard
     */
    private String getSideInput() {
        System.out.print("Enter 'M' to be a Musketeer or 'G' to be a Guard: ");
        while (!scanner.hasNext("[MGmg]")) {
            System.out.println("Invalid option. Enter 'M' or 'G': ");
            scanner.next();
        }
        return scanner.next().toUpperCase();
    }

    /**
     * Get human input for selecting the game mode
     * @return the chosen GameMode
     */
    private GameMode getModeInput() {
        System.out.println("""
                    0: Human vs Human
                    1: Human vs Computer (Random)""");//
        System.out.print("Choose a game mode to play i.e. enter a number: ");
        while (!scanner.hasNextInt()) {
            System.out.print("Invalid option. Enter 0 or 1: ");
            scanner.next();
        }
        final int mode = scanner.nextInt();
        if (mode < 0 || mode > 1) {
            System.out.println("Invalid option.");
            return getModeInput();
        }
        return GameMode.values()[mode];
    }

    public static void main(String[] args) {
        String boardFileName = "boards/Starter.txt";
        ThreeMusketeers game = new ThreeMusketeers(boardFileName);
        game.play();
    }
   
}
