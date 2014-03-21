import java.util.*;

class Resolution
{
	public static void main(String [] args) throws Exception
	{
		LinkedList<Clause> clauses = new LinkedList<Clause>(); //the list of the clauses
		PriorityQueue<Integer> clauseTree = new PriorityQueue<Integer>();

		//Read files and add the corresponding clauses to the linkedlist
		//call the resolution function on each clause against all other clauses. start with the clause on the bottom. O(n^2) calls.
		//each resolution call may add new clauses to the linked list. to avoid redundant resolution calls and thus prevent infinite loops, use a stack to determine which clause will be calling the resolution function
		//To implement, compare the size of the new linkedlist to the old linkedlist. if the sizes are different (the new linkedlist has a new clause), then add the ID of the new clause to the stack
		//Example, before: <1 2 3>, after <1 2 3 4>. We would add 4 to the stack. Assuming 4 was added after resolving clause 3, the stack would be <4 2 1>, so we would resolve 4 next.
		//We stop when the resolution function returns "false", which means that the resolution found contradicting clauses. Otherwise, if the loop continues to completion, then every clause has been tested.
		//At this point, we return failure.
	}
}
