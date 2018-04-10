import java.util.*;

public class Landscape
{
	private Cell[][] cellList;
	protected static NUM_ROWS;
	protected static NUM_COLS;

	public Landscape(int row, int col)
	{
		cellList = new Cell[row][col];
		NUM_ROWS = cellList.length();
		NUM_COLS = cellList[0].length();
	}

	public Cell getCellAt(int row, int col)
	{
		return cellList[row][col];
	}
}