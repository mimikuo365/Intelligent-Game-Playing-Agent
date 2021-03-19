package student_player;

import java.util.ArrayList;

import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurCard;
import Saboteur.cardClasses.SaboteurTile;

public class PinkBoard
{
	private double[][] pinkBoard;	// Critical Area 10 <= x <= 14, 1 <= y <= 9

	// Path of different tiles
	private	int[][] full = {{1, 1 ,1}, {1, 1 ,1}, {1, 1 ,1}};
	private	int[][] notDown = {{1, 1 ,1}, {0, 1 ,1}, {1, 1 ,1}};
	private	int[][] notUp = {{1, 1 ,1}, {1, 1 ,0}, {1, 1 ,1}};
	private	int[][] notLeft = {{1, 0 ,1}, {1, 1 ,1}, {1, 1 ,1}};
	private	int[][] notRight = {{1, 1 ,1}, {1, 1 ,1}, {1, 0 ,1}};
	
	private int WIDTH = 9;
	private int HIGHT = 4;
	
	// For simulating the result
	private double myScore = 0;
	private double oppScore = 0;
	
	private void contentToProb()
	{
		// Convert the content of tiles to the probability that the tile is a nugget
		double left = pinkBoard[2][2];
		double central = pinkBoard[2][4];
		double right = pinkBoard[2][6];
		if (left == 2 || central == 2 || right == 2) // Known where is gold
		{
			if (left == 2)			left = 1;
			else 					left = 0;
			
			if (central == 2)	central = 1;
			else				central = 0;
			
			if (right == 2)		right = 1;
			else				right = 0;
		}
		else	// Evenly distribute the probability of nugget on unknown position
		{
			double total_unknown = left + central + right;
			left /= total_unknown;
			central /= total_unknown;
			right /= total_unknown;
		}
		
		pinkBoard[2][2] = left;
		pinkBoard[2][4] = central;
		pinkBoard[2][6] = right;
	}
	
	public PinkBoard(SaboteurBoardState boardState)
	{
		/*
		 * Extract the critical area (we call it Pink Zone) from the boardState,
		 * And create a small board to contain that area
		 * SaboteurBoardState boardState: current board state
		 */
		pinkBoard = new double[HIGHT][WIDTH];
		SaboteurTile[][] wholeBoard = boardState.getBoardForDisplay();
		
		// Select the area from the whole board
		int hight_start = 10, hight_end = 13;
		int width_start = 1,  width_end = 9;
		
		// Mapping from the whole board to the pink (smaller) board
		for (int h = hight_start, i = 0; h <= hight_end; h++, i++)
		{
			for (int w = width_start, j = 0; w <= width_end; w++, j++)
			{
				double value;
				double nothing = -1, gold = 2, goalTile = 1, others = 0;
				if (wholeBoard[h][w] == null)
				{
					value = nothing;
				}
				else if (wholeBoard[h][w].getName().contentEquals("Tile:nugget"))
				{
					value = gold;
				}
				else if (wholeBoard[h][w].getName().contentEquals("Tile:goalTile"))
				{
					value = goalTile;
				}
				else
				{
					value = others;
				}
				pinkBoard[i][j] = value;
			}
		}
		contentToProb();
	}
	
	private double evaGold(int x, int y, int[][] path, boolean change)
	{
		/*
		 * Count How many Gold it will get by placing a tile with path = 'int[][] path' on (x, y)
		 * int x: x position on the pink board
		 * int y: y position on the pink board
		 * int[] path: a 3x3 array, which is the config at point (x, y)
		 * boolean change: set to false if choose by default policy, true if tree policy
		 */
		double score = 0;
		if (path[1][2] == 1 && !outOfBound(x-1, y) && pinkBoard[x-1][y] > 0) // add up
		{
			score += pinkBoard[x-1][y];
			if (change == true)
				pinkBoard[x-1][y] = 0;
		}
		
		if (path[1][0] == 1 && !outOfBound(x+1, y) && pinkBoard[x+1][y] > 0) // add down
		{
			score += pinkBoard[x+1][y];
			if (change == true)
				pinkBoard[x+1][y] = 0;
		}
		
		if (path[0][1] == 1 && !outOfBound(x, y-1) && pinkBoard[x][y-1] > 0) // add left
		{
			score += pinkBoard[x][y-1];
			if (change == true)
				pinkBoard[x][y-1] = 0;
		}

		if (path[2][1] == 1 && !outOfBound(x, y+1) && pinkBoard[x][y+1] > 0) // add right
		{
			score += pinkBoard[x][y+1];
			if (change == true)
				pinkBoard[x][y+1] = 0;
		}
		return score;
	}
	
	private ArrayList<int[]> availablePos(int x, int y, int[][] path, int rec)
	{
		/*
		 * Search possible next move close to (x, y)
		 * int x: x position on the pink board
		 * int y: y position on the pink board
		 * int[] path: a 3x3 array, which is the config at point (x, y)
		 * int rec: the depth of search, avoid too many recursion 
		 */
		ArrayList<int[]> pos = new ArrayList<int[]>();
		
		if (rec < 0)	// avoid stack overflow
			return pos;
		
		if (path[1][2] == 1 && !outOfBound(x-1, y) ) // add up
			if (pinkBoard[x-1][y] == 0)
				pos.addAll(availablePos(x-1, y, notDown, rec-1));
			else
				pos.add(new int[]{x-1, y});
		
		if (path[1][0] == 1 && !outOfBound(x+1, y) ) // add down
			if (pinkBoard[x+1][y] == 0)
				pos.addAll(availablePos(x+1, y, notUp, rec-1));
			else
				pos.add(new int[]{x+1, y});
		
		if (path[0][1] == 1 && !outOfBound(x, y-1) ) // add left
			if (pinkBoard[x][y-1] == 0)
				pos.addAll(availablePos(x, y-1, notRight, rec-1));
			else
				pos.add(new int[]{x, y-1});

		if (path[2][1] == 1 && !outOfBound(x, y+1) ) // add right
			if (pinkBoard[x][y+1] == 0)
				pos.addAll(availablePos(x, y+1, notLeft, rec-1));
			else
				pos.add(new int[]{x, y+1});
		
		return pos;
	}
	
	private boolean simuEnd()
	{
		return myScore + oppScore >= 0.9;
	}
	
 	private boolean outOfBound(int x, int y)
	{
 		/*
		 * Check whether (x, y) is in pink board
		 * int x: x position on the pink board
		 * int y: y position on the pink board
		 */
		return !((0 <= x && x < HIGHT) && (0 <= y && y < WIDTH));
	}
	
 	public void printBoard()
	{
		for (int i = 0; i < HIGHT; i++)
		{
			for (int j = 0; j < WIDTH; j++)
			{
				System.out.print("|" + pinkBoard[i][j] + "|	");
			}
			System.out.println("		" + i);
		}
	}

 	private int[] optMove(ArrayList<int[]> availablePos)
 	{
 		double max = -1;
 		int optMove = -1;
 		boolean change = false;
 		
 		for (int i = 0; i < availablePos.size(); i++)
 		{
 			int x = availablePos.get(i)[0];
 			int y = availablePos.get(i)[1];
 			double cur = evaGold(x, y, full, change);
 			
 			if (cur > max)
 			{
 				max = cur;
 				optMove = i;
 			}
 		}
 		
 		if (optMove != -1)
 		{
 			int x = availablePos.get(optMove)[0];
 			int y = availablePos.get(optMove)[1];
 			int[] pos = {x, y};
 			return pos;
 		}
 		else
 			return null;
 	}

 	public boolean brokenPath(SaboteurMove myMove)
 	{
 		int[][] path = ((SaboteurTile) myMove.getCardPlayed()).getPath();
 		int numOfOne = 0;
 		for (int i = 0; i < 3; i++)
 			for (int j = 0; j < 3; j++)
 				numOfOne += path[i][j];
 		
 		if (numOfOne >= 3 && path[1][1] == 1)
 			return false;
 		else
 			return true;
 	}
 	
 	public double evaInPink(SaboteurMove myMove, boolean enterPink)
 	{
 		/*
 		 * Evaluate a move categorised as inPink by function in baseFunctions
 		 * SaboteurMove myMove: a move to be evaluated
 		 * boolean enterPink: indicate whether there is at least a tile in our hand can be placed in pink zone
 		 */
        SaboteurCard card = myMove.getCardPlayed();
        
        // Handle lock
        if (card.getName().equals("Malus"))
        {
        	if (enterPink)  return 2;
        	else     return -1;
        }
 		boolean isBroken = brokenPath(myMove);
 		
 		if (isBroken)
 			return -1;
 		else
 		{
 			boolean change = true;
 			int x = myMove.getPosPlayed()[0] - 10;
 			int y = myMove.getPosPlayed()[1] - 1;
 			int[][] path = ((SaboteurTile) myMove.getCardPlayed()).getPath(); 
 			myScore += evaGold(x, y, path, change);
 			
 			int rec = 3;
 			ArrayList<int[]> availablePos = availablePos(x, y, path, rec);
 			int[] pos = optMove(availablePos); 
 			if (pos != null)
 			{
 				x = pos[0]; y =pos[1];
 				oppScore += evaGold(x, y, full, change);
 			}
 			
 			availablePos = availablePos(x, y, full, rec);
 			pos = optMove(availablePos);
 			if (pos != null)
 			{
 				x = pos[0]; y =pos[1];
 				myScore += evaGold(x, y, full, change);
 			}
 			
 			
 			availablePos = availablePos(x, y, path, rec);
 			pos = optMove(availablePos);
 			if (pos != null)
 			{
 				x = pos[0]; y =pos[1];
 				oppScore += evaGold(x, y, full, change);
 			}
 			
 			return myScore - oppScore;
 		}
 	}
}
