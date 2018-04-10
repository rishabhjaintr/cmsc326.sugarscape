import java.util.Random;
// find unoccuppied cell with highest currentRes

// row - FOV + NUM_ROWS % NUM_ROWS for donut view. Same for column.
public class Agent
{
    private int id;   // identifier for the agent
    private int row;
    private int col;
    private int vision; // how many cells in any one direction can be seen
    private double wealth;   
    private double metRate;
    private int maxAge;

    private double t_move;
    private double t_death;

    protected static Landscape landscape = null;
    protected static int NUM_ROWS;
    protected static int NUM_COLS;
    protected static int maxAgentID;

    private static int getUniform(int min, int max)
    {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }
    
    public Agent(int id, int r, int c)
    {
        this.id = id;
        this.row = r;
        this.col = c;
        this.vision = getUniform(1, 6);
        this.maxAge = getUniform(60, 100);
        this.metRate = getUniform(1, 4);
        this.wealth = getUniform(5, 25);
        maxAgentID++;
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
    public double getMaxAge()
    {
        return this.maxAge;
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
    public void setLocation(int row, int col)
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
    public void setMaxAge(int a)
    {
        this.maxAge = a;
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
            // schedule a death at the moment the wealth dropped below 0 
            // compute slope
            double slope_m = (this.wealth - nextWealth)/(t - tnext);
            // compute y-intercept
            double c = this.wealth - (slope_m * t);
            // compute time of death
            double nextDeath = ((-1)*c) / slope_m;
            this.setDeathTime(nextDeath);
        }

        return nextCell;
    }

    public void death(double t)
    {
        this.setLocation(null, null);
        landscape.getCellAt(this.row, this.col).setOccupied(false);

        // return list of empty cells
        Cell[] emptyList = new Cell[NUM_ROWS*NUM_COLS];
        int index = 0;
        for (int i = 0; i < NUM_ROWS; i++)
        {
            for (int j = 0; j < NUM_COLS; j++)
            {
                if (landscape.getCellAt(i, j).getOccupied() == false)
                {
                    emptyList[index] = landscape.getCellAt(i, j);
                    index++;
                }
            }
        }

        // choose a number between 1 and length at random
        int length = index;
        int randomindex = getUniform(0, index);
        int newRow = emptylist[randomIndex].row;
        int newCol = emptylist[randomindex].col;
        // that cell is the new location for our new agent
        Agent x = new Agent(maxAgentID+1, newRow, newCol);

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
