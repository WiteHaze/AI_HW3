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

		String[] testString1 = {"z", "y", "x"};
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
		System.out.println("End");
	}
}
