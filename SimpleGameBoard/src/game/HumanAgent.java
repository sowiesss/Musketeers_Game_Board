package game;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import game.Exceptions.InvalidMoveException;

import static java.lang.System.out;

public class HumanAgent extends Agent {
	private final Scanner scanner = new Scanner(System.in);
	
    public HumanAgent(Board board) {
        super(board);
    }

    /**
     * Asks the human for a move with from and to coordinates and makes sure its valid.
     * Create a Move object with the chosen fromCell and toCell
     * @return the valid human inputted Move
     */
    @Override
    public Move getMove() { // TODO
    	Cell fromCell, toCell;
    	Piece.Type turn = board.getTurn();
    	Move move;
    	
    	do {
    		//get fromCell
        	List<Cell> fromList = board.getPossibleCells();
        	List<Coordinate> fromCoors = parseList(fromList);
        	fromCell = getCell(fromCoors, turn, "pieces", "the piece");
        	
        	//get toCell
        	List<Cell> toList = board.getPossibleDestinations(fromCell);
        	List<Coordinate> toCoors = parseList(toList);
        	toCell = getCell(toCoors, turn, "destinations", "where");
        	
        	move = new Move(fromCell, toCell);
		} while (!board.isValidMove(move));//double check
    	
        return move;
    }
    
    private Cell getCell(List<Coordinate> coors, Piece.Type turn, String msg1, String msg2) {
    	Coordinate coor;
    	Cell cell;
    	do {
    		out.printf("[%s] Possible %s are %s. Enter %s you want to move: ", turn, msg1, coors, msg2);
        	String input = scanner.next().toUpperCase();// pumping for user input and validate
        	coor = parseInput(input);
        	cell = board.getCell(coor);
		} while (!coors.contains(cell.getCoordinate()));
    	
    	return cell;
	}

    private List<Coordinate> parseList(List<Cell> cells) {
    	List<Coordinate> re = new ArrayList<>();	
    	for(Cell c : cells) {
    		re.add(c.getCoordinate());
    	}
		return re;	
    }
    
    private Coordinate parseInput(String input) {
    	boolean validInput;
    	Coordinate coors = null;
    	do {
    		validInput = true;
    		try {
    			coors = Utils.parseUserMove(input);
    		} catch (InvalidMoveException e) {
    			validInput = false;
    			out.print("Please enter a coordinate like 'A1': ");
    			input = scanner.next().toUpperCase();
    		}
		} while (!validInput);
    	
    	return coors;
	}

}
