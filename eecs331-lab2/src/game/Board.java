package game;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import game.Exceptions.InvalidMoveException;
import game.Piece.Type;

public class Board {
    public int size = 5;

    // 2D Array of Cells for representation of the game board
    public final Cell[][] board = new Cell[size][size];

    private Piece.Type turn;
    private Piece.Type winner;

    /**
     * Create a Board with the current player turn set.
     */
    public Board() {
        this.loadBoard("boards/Starter.txt");
    }

    /**
     * Create a Board with the current player turn set and a specified board.
     * @param boardFilePath The path to the board file to import (e.g. "Boards/Starter.txt")
     */
    public Board(String boardFilePath) {
        this.loadBoard(boardFilePath);
    }

    /**
     * Creates a Board copy of the given board.
     * @param board Board to copy
     */
    public Board(Board board) {
        this.size = board.size;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                this.board[row][col] = new Cell(board.board[row][col]);
            }
        }
        this.turn = board.turn;
        this.winner = board.winner;
    }

    /**
     * @return the Piece.Type (Muskeeteer or Guard) of the current turn
     */
    public Piece.Type getTurn() {
        return turn;
    }

    /**
     * Get the cell located on the board at the given coordinate.
     * @param coordinate Coordinate to find the cell
     * @return Cell that is located at the given coordinate
     */
    public Cell getCell(Coordinate coordinate) { // TODO
        return board[coordinate.row][coordinate.col];
    }

    /**
     * @return the game winner Piece.Type (Muskeeteer or Guard) if there is one otherwise null
     */
    public Piece.Type getWinner() {
        return winner;
    }

    /**
     * Gets all the Musketeer cells on the board.
     * @return List of cells
     */
    public List<Cell> getMusketeerCells() { // TODO
    	List<Cell> musks = find(Piece.Type.MUSKETEER);
        return List.copyOf(musks);
    }
    
    /**
     * 
     * @param type the Piece Type of the cells to be returned
     * @return cells with the designated Piece
     */
    private List<Cell> find(Piece.Type type) {
    	List<Cell> re = new ArrayList<>();
    	for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
            	Piece curr = this.board[row][col].getPiece();
                if(curr!= null && curr.getType() == type)
                	re.add(this.board[row][col]);
            }
        }
    	return re;
	}

    /**
     * Gets all the Guard cells on the board.
     * @return List of cells
     */
    public List<Cell> getGuardCells() { // TODO
    	List<Cell> guards = find(Piece.Type.GUARD);
        return List.copyOf(guards);
    }

    /**
     * Executes the given move on the board and changes turns at the end of the method.
     * @param move a valid move
     */
    public void move(Move move){ // TODO
    	Cell from = move.fromCell;
    	Cell to = move.toCell;
    	to.setPiece(from.getPiece());
    	from.setPiece(null);
    	nextTurn();
    }
    
    private void nextTurn() {
		if(turn == Piece.Type.GUARD)
			turn = Piece.Type.MUSKETEER;
		else 
			turn = Piece.Type.GUARD;
	}

    /**
     * Undo the move given.
     * @param move Copy of a move that was done and needs to be undone. The move copy has the correct piece info in the
     *             from and to cell fields. Changes turns at the end of the method.
     */
    public void undoMove(Move move) { // TODO
    	Coordinate fromPosition = move.fromCell.getCoordinate();
    	Coordinate toPosition = move.toCell.getCoordinate();
    	
    	Cell currFrom = board[fromPosition.row][fromPosition.col];
    	Cell currTo = board[toPosition.row][toPosition.col];
    	
    	//recover the pieces for the cells
    	currFrom.setPiece(move.fromCell.getPiece());
    	currTo.setPiece(move.toCell.getPiece());
    	nextTurn();
    }

    /**
     * Checks if the given move is valid. Things to check:
     * (1) the toCell is next to the fromCell
     * (2) the fromCell piece can move onto the toCell piece.
     * @param move a move
     * @return     True, if the move is valid, false otherwise
     */
    public Boolean isValidMove(Move move) { // TODO
    	if (!move.fromCell.hasPiece())
    		return false;
    	
    	Coordinate from = move.fromCell.getCoordinate();
    	Coordinate to = move.toCell.getCoordinate();
    	boolean valid = false;
    	
    	if(from.col == to.col && Math.abs(from.row-to.row) == 1 
    		|| from.row == to.row && Math.abs(from.col-to.col) == 1) // next to each other
    	{
    		valid = move.fromCell.getPiece().canMoveOnto(move.toCell);
    	}
    		
        return valid;
    }

    /**
     * Get all the possible cells that have pieces that can be moved this turn.
     * @return      Cells that can be moved from the given cells
     */
    public List<Cell> getPossibleCells() { // TODO
    	List<Cell> candidates;
    	ArrayList<Cell> result = new ArrayList<>();
    	
    	if(turn == Piece.Type.MUSKETEER)
    		candidates = getMusketeerCells();	
    	else 
    		candidates = getGuardCells();
    	
    	for(Cell c: candidates) {
			if (!getPossibleDestinations(c).isEmpty())
				result.add(c);
		}
    		
        return List.copyOf(result);
    }

    /**
     * Get all the possible cell destinations that is possible to move to from the fromCell.
     * @param fromCell The cell that has the piece that is going to be moved
     * @return List of cells that are possible to get to
     */
    public List<Cell> getPossibleDestinations(Cell fromCell) { // TODO
    	List<Cell> result = new ArrayList<>();
    	
    	if(!fromCell.hasPiece()) // empty cell
    		return result;
    	
    	int row = fromCell.getCoordinate().row;
    	int col = fromCell.getCoordinate().col;
    	Piece fromPiece = fromCell.getPiece();
    	
    	Coordinate[] candidates = {
    			new Coordinate(row + 1, col),
    			new Coordinate(row - 1, col),
    			new Coordinate(row, col + 1),
    			new Coordinate(row, col - 1)
    	};
    	
    	for(int i = 0; i < candidates.length; i ++) {
    		Coordinate temp = candidates[i];
    		if(temp.row < size && temp.row >= 0 && temp.col < size && temp.col >= 0) {
    			Cell to = this.getCell(temp);
    			if(fromPiece.canMoveOnto(to))
    				result.add(to);	
    		}
    	}
    	
    	return List.copyOf(result); 
    }

    /**
     * Get all the possible moves that can be made this turn.
     * @return List of moves that can be made this turn
     */
    public List<Move> getPossibleMoves() { // TODO
    	List<Cell> cells = getPossibleCells();
    	ArrayList<Move> re = new ArrayList<>();
    	
    	for(Cell c: cells) {
    		List<Cell> destinations = getPossibleDestinations(c);
    		for(Cell des: destinations) {
    			re.add(new Move(c, des));
    		}
    	}
    		
        return List.copyOf(re);
    }

    /**
     * Checks if the game is over and sets the winner if there is one.
     * @return True, if the game is over, false otherwise.
     */
    public boolean isGameOver() throws IllegalStateException { // TODO
    	// first check if guard win
    	List<Cell> musks =  getMusketeerCells();
    	if(musks.size() != 3) 
    		throw new IllegalStateException("Some Musketeer not found");
    	
    	Coordinate[] coors = new Coordinate[3];
    	for(int i = 0; i < 3; i++) {
    		coors[i] = musks.get(i).getCoordinate();
    	}
    	
    	if(coors[1].col == coors[2].col && coors[2].col == coors[0].col 
    			|| coors[1].row == coors[2].row && coors[2].row == coors[0].row ) {
    		winner = Piece.Type.GUARD;
    		return true;// guard win!
    	}
    		
    	// check if musketeers win
    	boolean reset = false;
    	boolean muskWin = false;
    	if(turn != Piece.Type.MUSKETEER) {
    		reset = true;
    		nextTurn();
    	}
    	// Get musketeers possible cells
    	List<Cell> candiMusks = getPossibleCells();
    	if (candiMusks.isEmpty()) {
    		winner = Piece.Type.MUSKETEER;
    		muskWin = true;
    	}
    	
    	if(reset)
    		nextTurn();
    	
        return muskWin;
    }

    /**
     * Saves the current board state to the boards directory
     */
    public void saveBoard() {
        String filePath = String.format("boards/%s.txt",
                new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date()));
        File file = new File(filePath);

        try {
            file.createNewFile();
            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)));
            writer.write(turn.getType() + "\n");
            for (Cell[] row: board) {
                StringBuilder line = new StringBuilder();
                for (Cell cell: row) {
                    if (cell.getPiece() != null) {
                        line.append(cell.getPiece().getSymbol());
                    } else {
                        line.append("_");
                    }
                    line.append(" ");
                }
                writer.write(line.toString().strip() + "\n");
            }
            writer.close();
            System.out.printf("Saved board to %s.\n", filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.printf("Failed to save board to %s.\n", filePath);
        }
    }

    @Override
    public String toString() {
        StringBuilder boardStr = new StringBuilder("  | A B C D E\n");
        boardStr.append("--+----------\n");
        for (int i = 0; i < size; i++) {
            boardStr.append(i + 1).append(" | ");
            for (int j = 0; j < size; j++) {
                Cell cell = board[i][j];
                boardStr.append(cell).append(" ");
            }
            boardStr.append("\n");
        }
        return boardStr.toString();
    }

    /**
     * Loads a board file from a file path.
     * @param filePath The path to the board file to load (e.g. "Boards/Starter.txt")
     */
    private void loadBoard(String filePath) {
        File file = new File(filePath);
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException e) {
            System.err.printf("File at %s not found.", filePath);
            System.exit(1);
        }

        turn = Piece.Type.valueOf(scanner.nextLine().toUpperCase());

        int row = 0, col = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] pieces = line.trim().split(" ");
            for (String piece: pieces) {
                Cell cell = new Cell(new Coordinate(row, col));
                switch (piece) {
                    case "O" -> cell.setPiece(new Guard());
                    case "X" -> cell.setPiece(new Musketeer());
                    default -> cell.setPiece(null);
                }
                this.board[row][col] = cell;
                col += 1;
            }
            col = 0;
            row += 1;
        }
        scanner.close();
        System.out.printf("Loaded board from %s.\n", filePath);
    }
}
