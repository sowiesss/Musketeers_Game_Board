package game;

import java.util.List;

public class RandomAgent extends Agent {

    public RandomAgent(Board board) {
        super(board);
    }

    /**
     * Gets a valid random move the RandomAgent can do.
     * @return a valid Move that the RandomAgent can perform on the Board
     */
    @Override
    public Move getMove() { // TODO
    	Move move;
    	do {
    		// pick a fromCell randomly
        	List<Cell> fromList = board.getPossibleCells();
        	int fromIndex = (int) Math.random() * fromList.size(); // 0 - size-1
        	Cell fromCell = fromList.get(fromIndex);
        	// pick a toCell randomly
        	List<Cell> toList = board.getPossibleDestinations(fromCell);
        	int toIndex = (int) Math.random() * toList.size(); // 0 - size-1
        	Cell toCell = toList.get(toIndex);
        	
        	move = new Move(fromCell, toCell);
		} while (!board.isValidMove(move));
    	
        return move;
    }
}
