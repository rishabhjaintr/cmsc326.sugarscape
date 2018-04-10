// find unoccuppied cell with highest currentRes

// row - FOV + NUM_ROWS % NUM_ROWS for donut view. Same for column.
public class Agent
{
    private String id;   // identifier for the agent
    private int row;
    private int col;
    private int vision; // how many cells in any one direction can be seen
    private double wealth;   
    private double metRate;
    private int maxAge;

    private double t_move;
    private double t_death;

    protected static Landscape landscape = NULL;

    
    public Agent(String id)
    {
        this.id = id;
    }

    // simple accessor methods below
    public int getRow()
    {
        return this.row; 
    }
    public int getCol()
    {
        return this.col; 
    }
    public String getID()  
    {
        return this.id;  
    }
    public int getVision() 
    {
        return this.vision; 
    }
    public double getMetRate()
    {
        return this.metRate;
    }

    public double getMoveTime()
    {
        return this.t_move;
    }

    public double getDeathTime()
    {
        return this.t_death;
    }

    // simple mutator methods below
    public void setRowCol(int row, int col)
    {
        this.row = row;
        this.col = col;
    }
    public void setVision(int n)
    {
        this.vision = n;
    }
    public void setMetRate(double n)
    {
        this.metRate = n;
    }

    public void setMoveTime(double m)
    {
        this.t_move = m;
    }

    public void setDeathTime(double m)
    {
        this.t_death = m;
    }

   
    
    public Cell movement(double t)
    {
        Cell nextCell = findNextCell(this.row, this.col, this.vision);
        // set current cell to unoccupied before movement
        landscape.getCellAt(this.row, this.col).setOccupied(false);

        // assign new coordinates to agent and set the new cell to occupied
        this.row = nextCell.row;
        this.col = nextCell.col;
        nextCell.setOccupied(true);

        double tnext = t + this.getInterMove();

        double nextWealth = computeWealth(t, tnext);

        if(nextWealth > 0)
        {
            // live to see another move
            this.setMoveTime(tnext);
        }
        else if(nextWealth <= 0)
        {
            // schedule a death at the moment the wealth dropped below 0 (linear)
        }

        return nextCell;
    }

    private double getInterMove()
    {
        // exponential(rate = 1)
    }

    private double computeWealth(double tnow, double tnext)
    {
        Cell currentCell = landscape.getCellAt(this.row, this.col);
        double gathered = (tnext - tnow) * currentCell.getRegrowthRate();
        double consumed = (tnext - tnow) * this.metRate;

        return (this.wealth + gathered - consumed);
    }

    private Cell findNextCell(int row, int col, int vision, double t)
    {
        Cell current;
        Cell nextCell = null;
        double cellDist = 0;

        // check up
        for (int i = 1; i <= vision; i++)
        {
            // landscape???
            int nextRow = (row - vision + NUM_ROWS - i) % NUM_ROWS;
            current = landscape.getCellAt(nextRow, col);

            if(current.getOccupied() == true)
            {
                continue;
            }

            if(current.getCurrentResource(t) == nextCell.getCurrentResource(t))
            {
                if(Math.abs( row - current.row ) < cellDist)
                {
                    nextCell = current;
                    cellDist = Math.abs( row - current.row );
                }
            }
            if(current.getCurrentResource(t) > nextCell.getCurrentResource(t))
            {
                nextCell = current;
                cellDist = Math.abs( row - current.row );
            }
        }
        // check down
        for (int i = 1; i <= vision; i++)
        {
            int nextRow = (row - vision + NUM_ROWS + i) % NUM_ROWS;
            current = landscape.getCellAt(nextRow, col);

            if(current.getOccupied() == true)
            {
                continue;
            }

            if(current.getCurrentResource(t) == nextCell.getCurrentResource(t))
            {
                if(Math.abs( row - current.row ) < cellDist)
                {
                    nextCell = current;
                    cellDist = =Math.abs( row - current.row );
                }
            }
            if(current.getCurrentResource(t) > nextCell.getCurrentResource(t))
            {
                nextCell = current;
                cellDist = Math.abs( row - current.row );
            }
        }
        // check left
        for (int i = 1; i <= vision; i++)
        {
            int nextCol = (col - vision + NUM_COLS - i) % NUM_COLS;
            current = landscape.getCellAt(row, nextCol);

            if(current.getOccupied() == true)
            {
                continue;
            }

            if(current.getCurrentResource(t) == nextCell.getCurrentResource(t))
            {
                if(Math.abs( col - current.col ) < cellDist)
                {
                    nextCell = current;
                    cellDist = Math.abs( col - current.col );
                }
            }
            if(current.getCurrentResource(t) > nextCell.getCurrentResource(t))
            {
                nextCell = current;
                cellDist = Math.abs( col - current.col );
            }
        }
        // check right
        for (int i = 1; i <= vision; i++)
        {
            int nextCol = (col - vision + NUM_COLS + i) % NUM_COLS;
            current = landscape.getCellAt(row, nextCol);

            if(current.getOccupied() == true)
            {
                continue;
            }

            if(current.getCurrentResource(t) == nextCell.getCurrentResource(t))
            {
                if(Math.abs( col - current.col ) < cellDist)
                {
                    nextCell = current;
                    cellDist = Math.abs( col - current.col );
                }
            }
            if(current.getCurrentResource(t) > nextCell.getCurrentResource(t))
            {
                nextCell = current;
                cellDist = Math.abs( col - current.col );
            }
        }
        if (nextCell == null)
        {
            Cell original = landscape.getCellAt(this.row, this.col);
            return original;
        }

        return nextCell;
    }
}

