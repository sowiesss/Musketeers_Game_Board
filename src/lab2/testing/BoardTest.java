package lab2.testing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.swing.undo.UndoableEdit;

import org.hamcrest.CoreMatchers;
import org.junit.*;

import lab2.Board;
import lab2.Cell;
import lab2.Coordinate;
import lab2.Move;
import lab2.Piece;

public class BoardTest {

    private Board board;

    @Before
    public void setup() {
        this.board = new Board();
    }

    @Test
    public void testGetCell() {
        Cell cell = board.getCell(new Coordinate(1, 4));
        Assert.assertNotNull(cell.getPiece());
    }

    @Test
    public void testGetMusketeerCells() {
    	List<Cell> musks = board.getMusketeerCells();
    	List<Cell> expected = new ArrayList<>();
    	expected.add(board.getCell(new Coordinate(0, 4)));
    	expected.add(board.getCell(new Coordinate(2, 2)));
    	expected.add(board.getCell(new Coordinate(4, 0)));
    	assertEquals(expected, musks);
    }
    
    @Test
    public void TestGetPossibleDestinations() {
    	List<Cell> des = board.getPossibleDestinations(board.getCell(new Coordinate(0, 4)));
    	List<Cell> exp = new ArrayList<>();
    	exp.add(board.getCell(new Coordinate(0, 3)));
    	exp.add(board.getCell(new Coordinate(1, 4)));
    	
    	assertTrue(des.size() == exp.size());
    	assertTrue(des.contains(exp.get(0)));
    	assertTrue(des.contains(exp.get(1)));
    }
    
    @Test
    public void TestIsGameOver() {
    	boolean over = board.isGameOver();
    	assertFalse(over); 	
    }
    
    @Test
    public void TestMove() {
    	assertEquals(Piece.Type.MUSKETEER, board.getTurn());
    	Cell from = board.getCell(new Coordinate(0,4));
    	Piece.Type frompiece = from.getPiece().getType();
    	Cell to = board.getCell(new Coordinate(0,3));
    	Move move = new Move(from, to);
    	board.move(move);
    	assertEquals(frompiece, to.getPiece().getType());
    	assertEquals(from.getPiece(), null);
    	assertEquals(Piece.Type.GUARD, board.getTurn());
    }
    
    @Test
    public void TestUndoMove() {
    	
    	Cell from = board.getCell(new Coordinate(0,4));
    	Piece.Type frompiece = from.getPiece().getType();
    	Cell to = board.getCell(new Coordinate(0,3));
    	Piece.Type topiece = to.getPiece().getType();
    	Move move = new Move(from, to);
    	Move backup = new Move(move);
    	board.move(move);
    	assertEquals(frompiece, to.getPiece().getType());
    	assertEquals(from.getPiece(), null);
    	
    	
    	board.undoMove(backup);
    	assertEquals(Piece.Type.MUSKETEER, board.getTurn());
    	assertEquals(frompiece, from.getPiece().getType());
    	assertEquals(topiece, to.getPiece().getType());
    }
    
    @Test
    public void TestPossibleCells() {
    	board = new Board("boards/2022.02.20.19.57.15.txt");
    	assertEquals(Piece.Type.MUSKETEER, board.getTurn());
    	List<Cell> fromList = board.getPossibleCells();
    	assertTrue(fromList.size()==3);
    	assertTrue(fromList.contains(board.getCell(new Coordinate(1,2))));
    	assertTrue(fromList.contains(board.getCell(new Coordinate(1,4))));
    	assertTrue(fromList.contains(board.getCell(new Coordinate(4,3))));
    }
    
    @Test
    public void TestPossibleMoves() {
    	board = new Board("boards/2022.02.20.19.57.15.txt");
    	List<Move> moves = board.getPossibleMoves();
    	Cell from1 = board.getCell(new Coordinate(1,2));
    	Cell from2 = board.getCell(new Coordinate(1,4));
    	Cell from3 = board.getCell(new Coordinate(4,3));
    	
    	Cell to1 = board.getCell(new Coordinate(1,3));
    	Cell to2 = board.getCell(new Coordinate(3,3));
    	Cell to3 = board.getCell(new Coordinate(4,4));
    	
//    	Move m1 = new Move(from1, to1);
//    	Move m2 = new Move(from2, to1);
//    	Move m3 = new Move(from3, to2);
//    	Move m4 = new Move(from3, to3);
//    	
//    	Move m1 = moves.get(0);
//    	Move m2 = moves.get(1);
//    	Move m3 = moves.get(3);
//    	Move m4 = moves.get(4);
    	
    	assertTrue(moves.size()==4);
   
    }
}
