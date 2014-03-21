import java.util.*;

class Clause
{
	int ID; //ID of this clause. Would be the order of the clause in the input file
	String[] literal; //list of literals in this clause
	boolean[] negated; //negated[i] is true when literal[i] is negated
	int[] parents = {-1, -1}; //if this clause was derived, then this field would have non -1 parents

	public Clause(int ID, String[] literal, boolean[] negated)
	{
		this.ID = ID;
		this.literal = literal;
		this.negated = negated;
	}

	public Clause(int ID, String[] literal, boolean[] negated, int[] parents)
	{
		this.ID = ID;
		this.literal = literal;
		this.negated = negated;
		this.parents = parents;
	}
	
	//Takes in another clause and if it can be resolved, then it will add the produced clause to the clauseList
	/*
	public boolean resolution(Clause otherClause, LinkedList<Clause> clauseList)
	{

	}
	*/
}
