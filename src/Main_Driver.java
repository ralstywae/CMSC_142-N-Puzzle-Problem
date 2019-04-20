import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.swing.JFileChooser;

import my_Puzzle_Solver_Package.My_BFS_Functions;
import my_Puzzle_Solver_Package.My_BNB_Functions;
import my_Puzzle_Solver_Package.My_Puzzle_Node;

public class Main_Driver
{

	public static void main(String[] args) throws IOException
	{
		JFileChooser file_Selector = new JFileChooser();
		file_Selector.setMultiSelectionEnabled(true);
		file_Selector.showOpenDialog(null);

		File[] my_Files = file_Selector.getSelectedFiles();

		if (my_Files == null || my_Files.length == 0)
		{
			System.out.println("Invalid");
			System.exit(0);
		}

		LinkedList<String> input_List = new LinkedList<String>();

		for (File curr_File : my_Files)
		{
			Path my_Path = Paths.get(curr_File.getAbsolutePath());

			Scanner my_Scan = new Scanner(my_Path);

			while (my_Scan.hasNextLine())
			{
				input_List.add(my_Scan.nextLine());
			}

			my_Scan.close();
		}

		PrintWriter my_Exp_Writer = new PrintWriter("Experiment_Data.txt");

		System.out.println("Program Running...");
		for (String curr_String : input_List)
		{
			my_Exp_Writer.println("||| Input: " + curr_String + " |||");
			my_Exp_Writer.println("==========");

			String[] string_Arr = curr_String.replaceAll("[^0-9]", "-").split("-");

			int puzzle_Size = (int) Math.sqrt(string_Arr.length);

			int[][] curr_Matrix = new int[puzzle_Size][puzzle_Size];
			int[][] goal_Matrix = new int[puzzle_Size][puzzle_Size];

			int blank_X = 0;
			int blank_Y = 0;

			int curr_Count = 0;

			for (int row_Count = 0; row_Count < puzzle_Size; row_Count++)
			{
				for (int col_Count = 0; col_Count < puzzle_Size; col_Count++)
				{
					curr_Matrix[row_Count][col_Count] = Integer.parseInt(string_Arr[curr_Count]);

					if (Integer.parseInt(string_Arr[curr_Count]) == 0)
					{
						blank_X = row_Count;
						blank_Y = col_Count;
					}

					goal_Matrix[row_Count][col_Count] = (curr_Count + 1);

					curr_Count++;
				}

			}

			goal_Matrix[puzzle_Size - 1][puzzle_Size - 1] = 0;

			if (is_Solvable(curr_Matrix))
			{
				run_BNB(curr_Matrix, goal_Matrix, blank_X, blank_Y, my_Exp_Writer);
				my_Exp_Writer.println("----------");
				run_BFS(curr_Matrix, goal_Matrix, blank_X, blank_Y, my_Exp_Writer);
			}

			else
			{
				my_Exp_Writer.println("Invalid Input");
			}
			my_Exp_Writer.println("==========\n");
		}

		System.out.println("Program Ended...");
		my_Exp_Writer.close();
	}

	private static void run_BNB(int[][] curr_Matrix, int[][] goal_Matrix, int blank_X, int blank_Y,
			PrintWriter file_Writer)
	{
		My_BNB_Functions my_BNB_Call = new My_BNB_Functions(goal_Matrix);

		long start_Time = System.nanoTime();
		My_Puzzle_Node solution_Node = my_BNB_Call.solve_Puzzle_BNB(curr_Matrix, blank_X, blank_Y);
		long end_Time = System.nanoTime();

		long total_Time = (end_Time - start_Time);

		file_Writer.println("Algorithm: Branch-and-Bound Algorithm");
		file_Writer.println("Blank Tile Movement: " + Arrays.toString(solution_Node.move_Path.toCharArray()));
		file_Writer.println("Running Time: " + format_Run_Time((long) total_Time));
		file_Writer.println("Node Checks: " + solution_Node.checking_Count);
		file_Writer.println("Max Depth: " + solution_Node.max_Depth);
		file_Writer.println("Solution Depth: " + solution_Node.node_Depth);

	}

	private static void run_BFS(int[][] curr_Matrix, int[][] goal_Matrix, int blank_X, int blank_Y,
			PrintWriter file_Writer)
	{
		My_BFS_Functions my_BFS_Call = new My_BFS_Functions(goal_Matrix);

		long start_Time = System.nanoTime();
		My_Puzzle_Node solution_Node = my_BFS_Call.solve_Puzzle_BFS(curr_Matrix, blank_X, blank_Y);
		long end_Time = System.nanoTime();

		long total_Time = (end_Time - start_Time);

		file_Writer.println("Algorithm: Breadth-First Search Algorithm");
		file_Writer.println("Blank Tile Movement: " + Arrays.toString(solution_Node.move_Path.toCharArray()));
		file_Writer.println("Running Time: " + format_Run_Time((long) total_Time));
		file_Writer.println("Node Checks: " + solution_Node.checking_Count);
		file_Writer.println("Max Depth: " + solution_Node.max_Depth);
		file_Writer.println("Solution Depth: " + solution_Node.node_Depth);

	}

	private static boolean is_Solvable(int[][] mat_In)
	{
		int curr_Count = 0;
		List<Integer> number_List = new ArrayList<Integer>();

		for (int curr_Row = 0; curr_Row < mat_In.length; curr_Row++)
		{
			for (int curr_Col = 0; curr_Col < mat_In.length; curr_Col++)
			{
				number_List.add(mat_In[curr_Row][curr_Col]);
			}
		}

		Integer[] number_Array = new Integer[number_List.size()];
		number_List.toArray(number_Array);

		for (int curr_Row = 0; curr_Row < number_Array.length - 1; curr_Row++)
		{
			for (int curr_Col = curr_Row + 1; curr_Col < number_Array.length; curr_Col++)
			{
				if (number_Array[curr_Row] != 0 && number_Array[curr_Col] != 0
						&& number_Array[curr_Row] > number_Array[curr_Col])
				{
					curr_Count++;
				}
			}
		}

		return (curr_Count % 2 == 0);
	}

	private static String format_Run_Time(long nano_Input)
	{
		String outString = ""; // java.util.concurrent.TimeUnit;
		long in_Days = TimeUnit.NANOSECONDS.toDays(nano_Input);
		long in_Hours = TimeUnit.NANOSECONDS.toHours(nano_Input)
				- TimeUnit.DAYS.toHours(TimeUnit.NANOSECONDS.toDays(nano_Input));
		long in_Minutes = TimeUnit.NANOSECONDS.toMinutes(nano_Input)
				- TimeUnit.HOURS.toMinutes(TimeUnit.NANOSECONDS.toHours(nano_Input));
		long in_Seconds = TimeUnit.NANOSECONDS.toSeconds(nano_Input)
				- TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(nano_Input));

		double nano_Time = (double) nano_Input / 1000000000;

		BigDecimal decimal_Format = new BigDecimal(Double.toString(nano_Time)).setScale(10, RoundingMode.HALF_UP);
		String text_Decimal = decimal_Format.toPlainString();
		String seconds_Fraction = text_Decimal.substring(text_Decimal.indexOf('.') + 1, text_Decimal.length());

		if (in_Days == 0)
		{
			outString = String.format("%02d:%02d:%02d", in_Hours, in_Minutes, in_Seconds) + "." + seconds_Fraction;
		}

		else
		{
			outString = String.format("%dd %02d:%02d:%02d", in_Days, in_Hours, in_Minutes, in_Seconds) + "."
					+ seconds_Fraction;
		}

		return outString;
	}

}
