import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

class Resolution
{
	public static void main(String [] args) throws Exception
	{
		LinkedList<Clause> clauses = new LinkedList<Clause>(); //the list of the clauses
		PriorityQueue<Integer> clauseTree = new PriorityQueue<Integer>();

		//Read files and add the corresponding clauses to the linkedlist
		//call the resolution function on each clause against all other clauses above it. start with the clause on the bottom. O(n^2) calls.
		//each resolution call may add new clauses to the linked list. to avoid redundant resolution calls and thus prevent infinite loops, use a stack to determine which clause will be calling the resolution function next
		//To implement, compare the size of the new linkedlist to the old linkedlist. if the sizes are different (the new linkedlist has a new clause), then add the ID of the new clause(s) to the stack
		//Example, linkedlist - before: <1 2 3>, after <1 2 3 4>. We would add 4 to the stack. Assuming 4 was added after resolving clause 3, the stack would be <4 2 1>, so we would resolve 4 next.
		//We stop when the resolution function returns "false", which means that the resolution found contradicting clauses. Otherwise, if the loop continues to completion, then every clause has been tested.
		//At this point, we return failure.
		
		BufferedReader fileReader = new BufferedReader(new FileReader("simpletest.txt"));
		
		String line = fileReader.readLine();
		int clauseCount = 1;
		
		while(line != null)
		{
			String[] literals = line.split(" ");
			boolean[] literalValuesNegated = new boolean[literals.length];
			
			for(int literalCount = 0; literalCount < literals.length; literalCount++)
			{
				if(literals[literalCount].charAt(0) == '~')
				{
					literals[literalCount] = literals[literalCount].substring(1);
					literalValuesNegated[literalCount] = true;
				}
				else
				{
					literalValuesNegated[literalCount] = false;
				}
			}
		
			clauses.add(new Clause(clauseCount, literals, literalValuesNegated));
			clauseCount++;
			line = fileReader.readLine();
		}

		
		/*String[] testString1 = {"z", "y", "x"};
		String[] testString2 = {"y", "z", "x"};
		boolean[] testBool1 = {true, false, true};
		boolean[] testBool2 = {true, true, true};
		Clause testClause1 = new Clause(1, testString1, testBool1);
		Clause testClause2 = new Clause(2, testString2, testBool2);
		testClause1.resolution(testClause2, clauses); 
		Clause resultClause = clauses.getFirst();
		resultClause.outputClause();
		String[] testString3 = {"z"};
		String[] testString4 = {"z"};
		boolean[] testBool3 = {false};
		boolean[] testBool4 = {true};
		Clause testClause3 = new Clause(3, testString3, testBool3);
		Clause testClause4 = new Clause(4, testString4, testBool4);
		testClause3.resolution(testClause4, clauses);
		resultClause = clauses.get(1);
		resultClause.outputClause();
		System.out.println("End");*/
		
		/*String[] testString1 = {"z", "y", "x"};
		String[] testString2 = {"y", "z", "x"};
		boolean[] testBool1 = {true, false, true};
		boolean[] testBool2 = {true, true, true};
		clauses.add(new Clause(1, testString1, testBool1));
		clauses.add(new Clause(2, testString2, testBool2));*/
		
		/*String[] testString1 = {"z"};
		String[] testString2 = {"z"};
		boolean[] testBool1 = {true};
		boolean[] testBool2 = {false};
		clauses.add(new Clause(1, testString1, testBool1));
		clauses.add(new Clause(2, testString2, testBool2));*/
		
		if(!resolve(clauses))
		{
			System.out.println("True. Reached a contradiction.");
			printProofTree(clauses.getLast(), clauses);
		}
	}
	
	public static boolean resolve(LinkedList<Clause> clauses)
	{
		//Storing onto the stack to verify clauses in reverse order
		Stack<Clause> expansionStack = new Stack<Clause>();
		
		for(int count = 0; count < clauses.size(); count++)
		{
			expansionStack.add(clauses.get(count));
		}
		
		while(!expansionStack.isEmpty()) //Until the stack is empty
		{

			Clause lastClause = expansionStack.pop();
			boolean added = false;
			
			for(int clauseCount = 0; clauseCount < lastClause.getClauseID()-1; clauseCount++)
			{
				//If any new clauses are added since last execution
				//if(!clauses.getLast().equals(lastClause))
				{
					//break;
				}
				
				Clause tempClause = clauses.get(clauseCount);
				System.out.print("Expanding: ");
				lastClause.outputClause();
				System.out.print("Against: ");
				tempClause.outputClause();
				
				int numClausesBeforeExpansion = clauses.size();
				
				boolean result = lastClause.resolution(tempClause, clauses);		
				
				if(!result)
				{
					return false;
				}
				
				int numClausesAfterExpansion = clauses.size();
				
				//System.out.println(numClausesAfterExpansion - numClausesBeforeExpansion); //Size does not change
				
				//If new clauses are added, expand the newly added clause before expanding any other clause
				if(numClausesAfterExpansion - numClausesBeforeExpansion > 0)
				{
					System.out.println("Clause " + clauses.size() + " is created.");
					expansionStack.push(clauses.getLast());
				}
			}
			
		}
		
		return true;
	}

	public static void printProofTree(Clause finalClause, LinkedList<Clause> clauseList)
	{
		PriorityQueue<Integer> proofTree = new PriorityQueue<Integer>(); //Will be used to order the ancestors of the finalClause for output
		LinkedList<Clause> treeQueue = new LinkedList<Clause>(); //Will take in the ancestors of the finalClause
		int[] parentIDs;

		treeQueue.add(finalClause);
		while(!treeQueue.isEmpty())
		{
			Clause polledClause = treeQueue.poll();
			proofTree.add(polledClause.getClauseID());
			parentIDs = polledClause.getParentIDs();
			if(parentIDs[0] != -1) //if one parent exists, the other must exist and we add the parents to the queue
			{
				treeQueue.add(clauseList.get(parentIDs[0]-1)); //add the first parent to the queue
				treeQueue.add(clauseList.get(parentIDs[1]-1)); //add the second parent to the queue
			}
		}
		

		//output all the clauses in the proof tree
		while(proofTree.peek() != null)
		{
			clauseList.get(proofTree.poll()-1).outputClause();
		}
		System.out.println("Size of final clause set:   " + clauseList.size());
	}
}
