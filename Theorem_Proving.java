import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;


public class Theorem_Proving {

	public static void main(String[] args) throws IOException 
	{
		Set<Clause> clauses = new HashSet<Clause>();
		Set<String> stringSet = new HashSet<String>();
		LinkedList<Clause> knowledgeBase = new LinkedList<Clause>();
		
		//Reading and storing the clauses
		BufferedReader fileReader = new BufferedReader(new FileReader("task4.in"));
		
		String line = fileReader.readLine();
		
		while(line != null)
		{
			String sortedClauseString = sortClause(line);
			
			Clause c = new Clause(sortedClauseString);
			clauses.add(c);
			stringSet.add(sortedClauseString);
			knowledgeBase.add(c);
			
			line = fileReader.readLine();
		}
		
		//Clause [] clausesArray = clauses.toArray(new Clause[0]);
		//Clause c1 = clausesArray[1];
		//Clause c2 = clausesArray[2];
		
		//System.out.println(resolve(c1, c2));
		
		if(solveTheorem(clauses, stringSet,knowledgeBase))
		{
			
		}
		else 
		{
			System.out.println("No contradiction.");
		}
	}
	
	public static boolean solveTheorem(Set<Clause> clauses, Set<String> stringSet, LinkedList<Clause> knowledgeBase)
	{
	
		Set<Clause> newSet = new HashSet<Clause>();
		newSet.addAll(clauses);
		
		while(true)
		{
			Clause [] clausesArray = clauses.toArray(new Clause[0]);
			
			for(int clauseCount = 0; clauseCount < clausesArray.length; clauseCount++)
			{
				Clause c1 = knowledgeBase.get(clauseCount);
				
				for(int clauseCount2 = 0; clauseCount2 < clausesArray.length; clauseCount2++)
				{
					Clause c2 = knowledgeBase.get(clauseCount2);
					
					String resolvent = resolve(c1, c2);
					
					if(resolvent == null)
					{
						System.out.println("Contradiction reached.");
						System.out.println("Contradiction: " + c1.getClauseId() + " " + c2.getClauseId() + ".");
						printSolution(knowledgeBase, c1.getClauseId(), c2.getClauseId());
						
						return true;
					}
					
					if(!stringSet.contains(resolvent))
					{
						stringSet.add(resolvent);
						
						Clause c = new Clause(resolvent, clauseCount+1, clauseCount2+1);
						
						newSet.add(c);
						knowledgeBase.add(c);
					}
				}
			}
		
			if(clauses.containsAll(newSet))
			{	
				return false;
			}
			
			clauses.addAll(newSet);
		}
		
	}
	
	public static void printSolution(LinkedList<Clause> knowledgeBase, int p1, int p2)
	{
		Clause parentClause;
		Clause parentClause2;
		Set <Integer> seenInt = new HashSet<Integer>();
		int [] parents;
		
		//finding solution path
		Stack <Integer> stack = new Stack<Integer>();
		
		if(p1 != -1)
		{
			stack.push(p1);
			stack.push(p2);
			
			seenInt.add(p1);
			seenInt.add(p2);
		}
		
		
		while(!stack.isEmpty())
		{
			int nextClause = stack.pop()-1;
			
			if(nextClause >= 0)
			{
				parentClause = knowledgeBase.get(nextClause);
				parents = parentClause.getParents();
				
		//		parentClause.displayClause();
				
				if(parents[0] != -1)
				{
					seenInt.add(parents[0]);
					seenInt.add(parents[1]);
					stack.push(parents[0]);
					stack.push(parents[1]);
				}
			}
		}
		
		Object[] solutionTree = seenInt.toArray();
		
		Arrays.sort(solutionTree);
		
		for(int count = 0; count < solutionTree.length; count++)
		{
			int nextClause = (int)solutionTree[count]-1;
			
			if(nextClause >= 0)
			{
				Clause c = knowledgeBase.get(nextClause);
				c.displayClause();
			}
		}
	}
	
	public static String sortClause(String clause)
	{	
		String sortedClause = "";
		
		String [] literalsArray = clause.split(" ");
		
		if(literalsArray.length == 0)
		{
			literalsArray = new String[1];
			literalsArray[0] = clause;
		}
		
		Set <String> negatedStrings = new HashSet<String>();
		
		for(int literalCount = 0; literalCount < literalsArray.length; literalCount++)
		{
			if(literalsArray[literalCount].charAt(0) == '~')
			{
				literalsArray[literalCount] = literalsArray[literalCount].substring(1);
				negatedStrings.add(literalsArray[literalCount]);
			}
		}
		
		Arrays.sort(literalsArray);
		
		for(int literalCount = 0; literalCount < literalsArray.length; literalCount++)
		{
			if(negatedStrings.contains(literalsArray[literalCount]))
			{
				String neg = "~";
				literalsArray[literalCount] = neg + literalsArray[literalCount];
			}
			
			if(literalCount > 0)
			{
				sortedClause += " ";
			}
			
			sortedClause += literalsArray[literalCount];
			
		}
		
		return sortedClause;
	}
	
	public static String resolve (Clause clause1, Clause clause2)
	{
		String [] clause1Literals = clause1.getLiterals();
		String [] clause2Literals = clause2.getLiterals();
		String result = "";
		Set <String> stringsDoNotInclude = new HashSet<String>();
		int spaceCount = 0;
		
		for(int literalCount = 0; literalCount < clause1Literals.length; literalCount++)
		{
			
			String literal = clause1Literals[literalCount];
			String negLiteral;
			
			
			//Finding the negation of the literal.
			if(literal.charAt(0) == '~')
			{
				negLiteral = literal.substring(1);
			}
			else
			{
				negLiteral = "~";
				negLiteral += literal;
			}
			
			boolean contradiction = false;
			
			//Check if negation of the literal occurs in clause2		
			for(int literalCount2 = 0; literalCount2 < clause2Literals.length && !contradiction; literalCount2++)
			{
				String tempString = clause2Literals[literalCount2];
				
				if(negLiteral.equals(tempString))
				{
					contradiction = true;
					stringsDoNotInclude.add(tempString);
				}
			}
			
			if(!contradiction)
			{
				if(spaceCount != 0)
				{
					result += " ";
				}
				
				spaceCount++;
				result += literal;
				stringsDoNotInclude.add(literal);
			}
		
		}
		
		//Making sure to include all the strings in clause2
		for(int clauseCount = 0; clauseCount < clause2Literals.length; clauseCount++)
		{
			if(!stringsDoNotInclude.contains(clause2Literals[clauseCount]))
			{
				if(spaceCount != 0)
				{
					result += " ";
				}
				
				spaceCount++;
				result += clause2Literals[clauseCount];
			}
		}
		
		if(!result.equals(""))
		{
			return sortClause(result);
		}
		else
		{
			return null;
		}
	}
}

class Clause
{
	private String clauseString;
	private String [] literals;
	private int clauseId;
	private int [] parents;
	
	private static int clausesCount = 1;
	
	Clause(String clauseString)
	{
		this.clauseString = clauseString;
		this.clauseId = clausesCount;
		clausesCount++;
		literals = clauseString.split(" ");
		
		parents = new int[2];
		this.parents[0] = -1; //does not have a parent
		this.parents[1] = -1; //does not have a parent
		
		//this.displayClause();
	}
	
	Clause(String clauseString, int p1, int p2)
	{
		this.clauseString = clauseString;
		this.clauseId = clausesCount;
		clausesCount++;
		literals = clauseString.split(" ");
		
		this.parents = new int[2];
		this.parents[0] = p1;
		this.parents[1] = p2;
		
		//this.displayClause();
	}
	
	int [] getParents()
	{
		return parents;
	}
	
	void displayClause()
	{
		System.out.print(this.clauseId + ". ");
		
		for(int literalCount = 0; literalCount < literals.length; literalCount++)
		{
			System.out.print(literals[literalCount] + " "); 
		}
		
		System.out.print("{" + (parents[0]) + " ," + (parents[1]) + " }");
		System.out.println();
	}
	
	String[] getLiterals()
	{
		return literals;
	}
	
	int getClauseId()
	{
		return this.clauseId;
	}
}

