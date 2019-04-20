package my_Puzzle_Solver_Package;

public class My_Puzzle_Node
{

	public My_Puzzle_Node parent_Node;
	public int[][] curr_Matrix;

	// Blank Tile Coordinates
	public int blank_X, blank_Y;

	// Movement Cost
	public int node_Cost;

	// Current_Depth
	public int node_Depth;

	// Blank Tile Movement Path
	public String move_Path;

	// Node Check Count
	public int checking_Count;

	// Max Depth Accessed
	public int max_Depth;

	// Public Constructor
	public My_Puzzle_Node(int[][] curr_Mat, int old_X, int old_Y, int new_X, int new_Y, int curr_Depth,
			My_Puzzle_Node parent_In, String curr_Move)
	{
		this.parent_Node = parent_In;
		this.curr_Matrix = new int[curr_Mat.length][];
		for (int i = 0; i < curr_Mat.length; i++)
		{
			this.curr_Matrix[i] = curr_Mat[i].clone();
		}

		// Move Blank Tile
		this.curr_Matrix[old_X][old_Y] = this.curr_Matrix[new_X][new_Y];
		this.curr_Matrix[new_X][new_Y] = 0;

		this.node_Cost = Integer.MAX_VALUE;
		this.node_Depth = curr_Depth;
		this.blank_X = new_X;
		this.blank_Y = new_Y;

		this.move_Path = curr_Move;
	}
}