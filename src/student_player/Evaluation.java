package student_player;
import java.util.ArrayList;
import java.util.Arrays;

import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurCard;
import Saboteur.cardClasses.SaboteurTile;

public class Evaluation 
{
	public static int evaMap(SaboteurMove move, SaboteurBoardState boardState)
	{
		/*
	 	Check goalTile from left to right
		 */
		int utility = -100;
		SaboteurTile[][] currentBoard = boardState.getBoardForDisplay();
		int[] loc = move.getPosPlayed(); 
		if (currentBoard[12][3].getName().equals("Tile:goalTile") && loc[1] == 3)
			utility = 3;
		else if (currentBoard[12][3].getName().equals("Tile:goalTile") && loc[1] == 5)
			utility = 2;
		else if (currentBoard[12][3].getName().equals("Tile:goalTile") && loc[1] == 5)
			utility = 1;
		return utility;
	}
	
	public static int evaDrop(SaboteurMove move, SaboteurBoardState boardState, int num_Bonus, int max_U) 
	{
		/*
 		Drop Card in Drop1 (100%) if 
 		i. 	It is broken.
 		ii. If we have more than 2 Bonus.
 		
 		Drop Card in Drop2 using defined heuristic value
		 */
	    int utility = -100;
	    ArrayList<SaboteurCard> myCards = boardState.getCurrentPlayerCards();
	    SaboteurCard curCard = myCards.get(move.getPosPlayed()[0]);
	    if (curCard.getName().equals("Destroy"))
	    		utility = -1;
	    else if (curCard.getName().equals("Bonus"))
	    {
	    	if ((max_U != 0) || num_Bonus > 2)
	    		utility = 4;
	    }
	    else if (curCard.getName().equals("Map"))
	    {
	    		utility = 2;
	    }
	    else if (curCard.getName().split(":")[0].equals("Tile"))
	    {
	    	String idx = curCard.getName().split(":")[1];
	    	
	    	// broken card
		    if (idx.equals("1") || idx.equals("2") || idx.equals("3") 
		     || idx.equals("4") || idx.equals("11") || idx.equals("12")
		     || idx.equals("13") || idx.equals("14") || idx.equals("15")
		     || idx.equals("1_flip") || idx.equals("2_flip") || idx.equals("3_flip")
		     || idx.equals("4_flip") || idx.equals("11_flip") || idx.equals("12_flip")
		     || idx.equals("13_flip") || idx.equals("14_flip") || idx.equals("15_flip"))
		    	utility = 3;
		    
		    // L card
		    else if (idx.equals("5") || idx.equals("7") 
		    	  || idx.equals("5_flip") || idx.equals("7_flip")) 
		   		utility = -1;
		    
		    // line card
		    else if (idx.equals("0") || idx.equals("10")
			      || idx.equals("0_flip") || idx.equals("10_flip"))
		   		utility = -2;
		    
		    // -| card
		   	else if (idx.equals("6") || idx.equals("9")
			      || idx.equals("6_flip") || idx.equals("9_flip"))
	    		utility = -3;	    
		    
		    // cross card
		   	else if (idx.equals("8") || idx.equals("8_flip"))
		   		utility = -4; 
	    }
    	return utility;
	}

    public static int getNumDestroy(SaboteurBoardState boardState)
    {
    	/*
			Count # of destroy card in hand
    	 */
    	int num = 0;
    	ArrayList<SaboteurCard> myCards = boardState.getCurrentPlayerCards();
    	for (int i = 0; i < myCards.size(); i++)
    		if (myCards.get(i).getName().equals("Destroy"))
    			num++;
    	return num;
    }
    
    public static int utilityOfCard(SaboteurMove move, int num_break)
    {
    	/*
			Utility of card calculated using 
				i. 	num_break (cost)
				ii. heuristic (depended by move)
    	 */
    	int utility = 0;
    	int base = 0;
    	if (num_break == 0)			base = 8;
    	else if (num_break == 1)	base = 4;
    	else if (num_break == 2)	base = 0;
    	if (move.getCardPlayed().getName().split(":")[0].equals("Tile"))
    	{
    		String idx = move.getCardPlayed().getName().split(":")[1];
   		    if (idx.equals("5") || idx.equals("7") // L card
   		     || idx.equals("5_flip") || idx.equals("7_flip")) 
   		    		utility = 4;
   		    else if (idx.equals("0") || idx.equals("0_flip"))  // line card
   		    		utility = 3;
   		   	else if (idx.equals("6") || idx.equals("9")  // -| card
   			      || idx.equals("6_flip") || idx.equals("9_flip"))
   		    		utility = 2;
   		   	else if (idx.equals("8") || idx.equals("8_flip"))  // cross card
   		    		utility = 1;
   		   	else if (idx.equals("10") || idx.equals("10_flip"))
   		   			utility = 0;
	    	utility += base;
    	}
    	return utility;
    }
    
	public static int findBottom(ArrayList<SaboteurMove> outPink)
    {
		/*
		Find the maximum x value available in OutPink category
		 */
    	int max_x = 0;
    	for (int i = 0; i < outPink.size(); i++)
    	{
    		int cur_x = outPink.get(i).getPosPlayed()[0];
    		if (cur_x > max_x)
    			max_x = cur_x;
    	}
    	return max_x;
    }
   
	public static boolean goDown(SaboteurBoardState boardState, String curN, int[] cPos)
	{
		/*
		Check if the card is leading a path going down
		 */
		SaboteurTile[][] myBoard = boardState.getHiddenBoard();
		if (curN.split(":")[0].equals("Tile"))
		{
			String idx = curN.split(":")[1];
			int x = cPos[0];
			int y = cPos[1];

			if (x+1 <= 14 && y+1 <= 14 && x-1 >= 0 && y-1 >= 0)
			{
				if ((idx.equals("5") && myBoard[x][y+1] == null) 
				 || (idx.equals("5_flip") && myBoard[x-1][y] == null )
				 || (idx.equals("7") && myBoard[x-1][y] == null )
				 || (idx.equals("7_flip") && myBoard[x][y-1] == null )
				 || (idx.equals("9") && (myBoard[x][y+1] == null && myBoard[x][y-1] == null))
				 || (idx.equals("9_flip") && myBoard[x-1][y] == null ))
					return false;
			}
		}
		return true;
	}
	
	public static int calValue(SaboteurBoardState boardState, SaboteurMove move, int numDestroy, ArrayList<int[]> breakPoints)
	{
		int current_utility = 0;
		String curN = move.getCardPlayed().getName();
		int[] curPos = move.getPosPlayed();
		int[] start = new int[]{5,5};
		if (goDown(boardState, curN, curPos))
		{
			boolean findPath = Connectivity.findBreakPoints(boardState, curPos, start, breakPoints);
			if (findPath)
			{
				if (numDestroy - breakPoints.size() >= -1)  
					current_utility = utilityOfCard(move, breakPoints.size());
				else
					current_utility = -10;
			}
		}
		return current_utility;
	}
	
	public static int minBreak(SaboteurMove move, SaboteurBoardState boardState) 
	{
		SaboteurTile[][] myBoard = boardState.getHiddenBoard();
		int[] loc = move.getPosPlayed();
		int[][] side = new int[][] {{-1,0},{1,0},{0,-1},{0,1}};
		int min = 100;
		for (int i = 0; i < 4; i++)
		{
			
			int tmp_m = 100;
			int[] tmp = new int[2];
			tmp[0] = side[i][0]+loc[0];
			tmp[1] = side[i][1]+loc[1];
			if (tmp[1] < 14 && tmp[0] < 14 && tmp[1] >= 0 && tmp[0] >= 0)  //////// changed this 
			{
				if (myBoard[tmp[0]][tmp[1]] != null)
				{
					ArrayList<int[]> tmpBreakPoints = new ArrayList<int[]>();
					if (Connectivity.findBreakPoints(boardState, loc, new int[] {5,5}, tmpBreakPoints))
						tmp_m = tmpBreakPoints.size();
					if (min > tmp_m)
						min = tmp_m;
				}
			}
		}
		return min;
	}
	
	@SuppressWarnings("unchecked")
	public static SaboteurMove evaOutPink(ArrayList<SaboteurMove> outPink, SaboteurBoardState boardState)
	{
		int current_utility = 0;
		int max_utility = 0;
		SaboteurMove myMove = null;
		
		ArrayList<SaboteurMove> placeTile = new ArrayList<SaboteurMove>();
		ArrayList<SaboteurMove> destroyTile = new ArrayList<SaboteurMove>();
		int max_x = findBottom(outPink);
		int numDestroy = getNumDestroy(boardState);
		
		// Category cards into 2 piles
		for (int i = 0; i < outPink.size(); i++)
    	{
			if (outPink.get(i).getCardPlayed().getName().split(":")[0].equals("Tile"))
				placeTile.add(outPink.get(i));
			else if (outPink.get(i).getCardPlayed().getName().split(":")[0].equals("Destroy"))
				destroyTile.add(outPink.get(i));
    	}

		// Check "Tile" category first
		ArrayList<int[]> breakPoints = new ArrayList<int[]>();
		int tmp_x = max_x;
		while (myMove == null && placeTile.size() != 0 && max_x >= 0 && (max_x - tmp_x) <= 2) // changed this place
		{
			for (int i = 0; i < placeTile.size(); i++)
			{
				SaboteurMove move = placeTile.get(i);
				if (move.getPosPlayed()[0] == tmp_x)
				{
					placeTile.remove(i);
					ArrayList<int[]> tmpBreakPoints = new ArrayList<int[]>();
					current_utility = calValue(boardState, move, numDestroy, tmpBreakPoints);
					if (current_utility > max_utility)
				    {
						breakPoints = (ArrayList<int[]>) tmpBreakPoints.clone();
				    	max_utility = current_utility;
				    	myMove = move;
				    	
				    }
				}
			}
			tmp_x--;
		}

		if (myMove != null)
		{
			if (numDestroy == 0 || breakPoints.size() == 0)
			{
				return myMove;
			}
			else
			{
				int[] loc = breakPoints.get(0);
				int i = 0;
					
				for (; i < destroyTile.size(); i++)
				{
					if (loc[0] == destroyTile.get(i).getPosPlayed()[0] 
							&& loc[1] == destroyTile.get(i).getPosPlayed()[1])
						return destroyTile.get(i);
				}
			}
		}
		
		// Check "Destroy" category second
		int b = 4;
		tmp_x = max_x;
		while (myMove == null && tmp_x >= 0 && destroyTile.size() != 0)
		{
			for (int i = 0; i < destroyTile.size(); i++)
			{
				SaboteurMove move = destroyTile.get(i);
				if (move.getPosPlayed()[0] == tmp_x)
				{
					int tmpb = minBreak(move, boardState);
					if (tmpb == 0)
						myMove = move;
				}
				destroyTile.remove(i);
			}
			tmp_x--;
		}
		return myMove;
	}
}