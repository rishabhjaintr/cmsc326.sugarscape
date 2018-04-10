public class Cell
{
	private double capacity;
	private boolean occupied;
	private double currentResource;
	// use this instead of currentResource ( (t - timeLastDepleted) * regrowthRate)
	private double timeLastDepleted;
	private double regrowthRate;
	public int row;
	public int col;
	
	public Cell()
	{
		//
	}
	
	public Cell(double cap, double regrowth)
	{
		this.capacity = cap;
		this.regrowth = regrowth;
	}
	public Cell(double cap)
	{
		this.capacity = cap;
	}
	
	//accessor methods
	public double getCapacity()
	{
		return this.capacity;
	}
	public double getRegrowthRate()
	{
		return this.regrowthRate;
	}
	public boolean getOccupied()
	{
		return this.occupied;
	}
	public double getCurrentResource(double t)
	{
		return ((t - this.timeLastDepleted) * this.regrowthRate);
	}
	public double getTimeLastDepleted()
	{
		return this.timeLastDepleted;
	}
	
	//mutator methods
	public void setOccupied(boolean b)
	{
		this.occupied = b;
	}
	public void setCapacity(double cap)
	{
		this.capacity = cap;
	}
	public void setRegrowthRate(double r)
	{
		this.regrowthRate = r;
	}
	public void setCurrentResource(double r)
	{
		this.currentResource = r;
	}
	public void setTimeLastDepleted(double t)
	{
		this.timeLastDepleted = t
	}
}