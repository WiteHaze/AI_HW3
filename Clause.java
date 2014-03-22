import java.util.*;

class Clause
{
	private int ID; //ID of this clause. Would be the order of the clause in the input file
	private String[] literal; //list of literals in this clause
	private boolean[] negated; //negated[i] is true when literal[i] is negated
	private int[] parents = {-1, -1}; //if this clause was derived, then this field would have non -1 parents

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
	
	//Takes in another clause and if it can be resolved, then it will add the produced clause to the clauseList. Returns true if there was no contradiction. False if there was.
	public boolean resolution(Clause otherClause, LinkedList<Clause> clauseList)
	{
		for(int i = 0; i < literal.length; i++) //Loop through all the literals in this clause
		{
			for(int j = 0; j < otherClause.literal.length; j++) //Loop through all the literals in the otherClause
			{
				if(literal[i].equals(otherClause.literal[j]) && negated[i] != otherClause.negated[j]) //if the literals are the same and the negation is different, then they can be resolution'd
				{
					if(literal.length == 1 && otherClause.literal.length == 1) //If boths lengths are 1, then there is a contradiction
					{
						//Add dummy clause to clauseList so that it outputs "False" when the outputClause function is called
						String[] dummyString = {"False"};
						boolean[] dummyNegated = {false};
						int[] newParents = {ID, otherClause.ID};
						Clause dummyClause = new Clause(clauseList.size() + 1, dummyString, dummyNegated, newParents);
						clauseList.add(dummyClause);

						return false;
					}

					//Check for repeating literals and tautology
					boolean isTaut = false; //is true if the clause is a tautology ( ~a V a )
					int numOfRepeatingLit = 0;
					for(int k = 0; k < literal.length && !isTaut; k++)
					{
						for(int l = 0; l < otherClause.literal.length && !isTaut; l++)
						{
							if(literal[k].equals(otherClause.literal[l]) && k != i && l != j) //if the literals are the same and they are not the literals we are removing
							{
								if(negated[k] != otherClause.negated[l]) //if the negations are different, then we have a tautology
								{
									isTaut = true;
									continue; //will result in breaking out of the loops
								}
								numOfRepeatingLit++;
								otherClause.literal[l] = ""; //Set the literal to "" so it does not get added in the later loop
							}
						}
					}
					
					if(!isTaut) //if the resulting new clause is not a tautology
					{
						//Add the literals from both clauses to the new clause
						String[] newLiteral = new String[literal.length + otherClause.literal.length - 2 - numOfRepeatingLit]; //the new clause will have literals of both minus the removed literal from both and repeating literals
						boolean[] newNegated = new boolean[negated.length + otherClause.negated.length - 2 - numOfRepeatingLit];
						int newLiteralIndex = 0;

						for(int k = 0; k < literal.length; k++)
						{
							if(k != i) //k is not the index of the removed literal
							{
								newLiteral[newLiteralIndex] = literal[k];
								newNegated[newLiteralIndex] = negated[k];
								newLiteralIndex++;
							}
						}
						for(int k = 0; k < otherClause.literal.length; k++)
						{
							if(k != j && otherClause.literal[k] != "") //k is not the index of the removed literal and the literal is not indicated as repeating
							{
								newLiteral[newLiteralIndex] = otherClause.literal[k];
								newNegated[newLiteralIndex] = otherClause.negated[k];
								newLiteralIndex++;
							}
						}
						//Create a new clause object and add it to the passed in linked list
						int newParents[] = {ID, otherClause.ID};
						Clause newClause = new Clause(clauseList.size() + 1, newLiteral, newNegated, newParents);
						clauseList.add(newClause);
					}
				}
			}
		}

		return true;
	}
	
	//Prints to stdout the clause in the format specified by the assignment
	public void outputClause()
	{
		String outputStr = "";
		outputStr += (ID + ".  ");
		for(int i = 0; i < literal.length; i++)
		{
			if(negated[i])
			{
				outputStr += "~";
			}
			outputStr += (literal[i] + " ");
		}
		outputStr += "  {";
		if(parents[0] != -1) //if this clause has parents
		{
			outputStr += parents[0] + ", " + parents[1];
		}
		outputStr += "}";

		System.out.println(outputStr);
	}
	
	//returns clause ID
	public int getClauseID()
	{
		return this.ID;
	}
}
