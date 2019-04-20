package my_Puzzle_Solver_Package;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class My_BNB_Functions
{
	// Constructor for Initialization
	public My_BNB_Functions(int[][] goal_Matrix)
	{
		this.goal_Key = Arrays.deepToString(goal_Matrix);
		this.puzzle_Size = goal_Matrix.length;
	}

	// Goal Key
	private String goal_Key;

	// Puzzle Size
	private int puzzle_Size;

	// Blank Tile Movement (Up, Down, Left, Right)
	private int[] row_Move = { -1, 1, 0, 0 };
	private int[] col_Move = { 0, 0, -1, 1 };

	private int calculate_Cost(int[][] curr_Matrix)
	{
		int sum_Manhattan = 0;

		for (int curr_Row = 0; curr_Row < puzzle_Size; curr_Row++)
		{
			for (int curr_Col = 0; curr_Col < puzzle_Size; curr_Col++)
			{
				int curr_Element = curr_Matrix[curr_Row][curr_Col];
				if (curr_Element != 0)
				{
					int target_X = (curr_Element - 1) / puzzle_Size;
					int target_Y = (curr_Element - 1) % puzzle_Size;
					int x_Dist = curr_Row - target_X;
					int y_Dist = curr_Col - target_Y;
					sum_Manhattan = sum_Manhattan + Math.abs(x_Dist) + Math.abs(y_Dist);
				}
			}
		}
		return sum_Manhattan;
	}

	private boolean valid_Move(int new_X, int new_Y)
	{
		return (new_X >= 0 && new_X < puzzle_Size && new_Y >= 0 && new_Y < puzzle_Size);
	}

	private LinkedList<My_Puzzle_Node> return_Child(My_Puzzle_Node min)
	{
		LinkedList<My_Puzzle_Node> child_List = new LinkedList<My_Puzzle_Node>();

		if (valid_Move(min.blank_X + row_Move[0], min.blank_Y + col_Move[0]))
		{
			My_Puzzle_Node child = new My_Puzzle_Node(min.curr_Matrix, min.blank_X, min.blank_Y,
					min.blank_X + row_Move[0], min.blank_Y + col_Move[0], min.node_Depth + 1, min, min.move_Path + "U");
			child.node_Cost = calculate_Cost(child.curr_Matrix);
			child_List.add(child);
		}

		if (valid_Move(min.blank_X + row_Move[1], min.blank_Y + col_Move[1]))
		{
			My_Puzzle_Node child = new My_Puzzle_Node(min.curr_Matrix, min.blank_X, min.blank_Y,
					min.blank_X + row_Move[1], min.blank_Y + col_Move[1], min.node_Depth + 1, min, min.move_Path + "D");
			child.node_Cost = calculate_Cost(child.curr_Matrix);
			child_List.add(child);
		}

		if (valid_Move(min.blank_X + row_Move[2], min.blank_Y + col_Move[2]))
		{
			My_Puzzle_Node child = new My_Puzzle_Node(min.curr_Matrix, min.blank_X, min.blank_Y,
					min.blank_X + row_Move[2], min.blank_Y + col_Move[2], min.node_Depth + 1, min, min.move_Path + "L");
			child.node_Cost = calculate_Cost(child.curr_Matrix);
			child_List.add(child);
		}

		if (valid_Move(min.blank_X + row_Move[3], min.blank_Y + col_Move[3]))
		{
			My_Puzzle_Node child = new My_Puzzle_Node(min.curr_Matrix, min.blank_X, min.blank_Y,
					min.blank_X + row_Move[3], min.blank_Y + col_Move[3], min.node_Depth + 1, min, min.move_Path + "R");
			child.node_Cost = calculate_Cost(child.curr_Matrix);
			child_List.add(child);
		}

		return child_List;
	}

	public My_Puzzle_Node solve_Puzzle_BNB(int[][] start_Mat, int blank_X, int blank_Y)
	{
		PriorityQueue<My_Puzzle_Node> node_Queue = new PriorityQueue<My_Puzzle_Node>(
				(node_A, node_B) -> (node_A.node_Cost + node_A.node_Depth) - (node_B.node_Cost + node_B.node_Depth));

		My_Puzzle_Node root_Node = new My_Puzzle_Node(start_Mat, blank_X, blank_Y, blank_X, blank_Y, 0, null, "");
		root_Node.node_Cost = calculate_Cost(start_Mat);

		HashMap<String, My_Puzzle_Node> visited_Node = new HashMap<String, My_Puzzle_Node>();

		node_Queue.add(root_Node);

		int node_Check_Count = 0;

		int curr_Depth = 0;
		int max_Depth = 0;

		while (!node_Queue.isEmpty())
		{
			My_Puzzle_Node curr_Min = node_Queue.poll();

			String curr_Key = Arrays.deepToString(curr_Min.curr_Matrix);

			My_Puzzle_Node temp_Node = (visited_Node.get(curr_Key));

			if (temp_Node == null)
			{
				visited_Node.put(curr_Key, curr_Min);
			}

			else
			{
				continue;
			}

			node_Check_Count++;

			int curr_Move_Length = curr_Min.move_Path.length();

			if (curr_Depth != curr_Move_Length)
			{
				curr_Depth = curr_Move_Length;

				if (curr_Depth > max_Depth)
				{
					max_Depth = curr_Depth;
				}
			}

			if (curr_Key.equals(goal_Key))
			{
				curr_Min.checking_Count = node_Check_Count;
				curr_Min.max_Depth = max_Depth;

				return curr_Min;
			}

			node_Queue.addAll(return_Child(curr_Min));
		}

		return null;
	}
}