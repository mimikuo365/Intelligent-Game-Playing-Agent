package student_player;
import java.util.ArrayList;

import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;
import Saboteur.cardClasses.SaboteurCard;
import boardgame.Move;

public class MyTools {
    
    public static Move classifyAndUnlock(	ArrayList<SaboteurMove> AllLegalMoves,
    										ArrayList<SaboteurMove> pink,
    										ArrayList<SaboteurMove> map,
    										ArrayList<SaboteurMove> drop,
    										ArrayList<SaboteurMove> notPink	)
    {
    	/*
    	 * if (unlock is legal)
    	 * 		return unlock
    	 * else
    	 * 		classify all moves
    	 * 		return NULL
    	 */
    	
    	SaboteurMove unlock = null;
    	for (int i = 0; i < AllLegalMoves.size(); i++)
    	{
    		SaboteurMove currentMove = AllLegalMoves.get(i);
    		if (baseFunction.isUnlock(currentMove))
    		{
    			unlock = currentMove;
    			break;
    		}
    		else if (baseFunction.isPink(currentMove))
    			pink.add(currentMove);
    		else if (baseFunction.isMap(currentMove))
    			map.add(currentMove);
    		else if (baseFunction.isDrop(currentMove))
    			drop.add(currentMove);
    		else if (baseFunction.notPink(currentMove))
    			notPink.add(currentMove);
    	}
    	return unlock;
    }
    
    public static Boolean checkIfLockInPink(ArrayList<SaboteurMove> pink)
    {
    	Boolean enterPink = false;
    	for (int i = 0; i < pink.size() && enterPink == false; i++)
    	{
    		SaboteurMove currentMove = pink.get(i);
    		String idx = currentMove.getCardPlayed().getName().split(":")[0];
    		if (idx.equals("Tile"))
    			enterPink = true;
    	}
    	return enterPink;
    }
    
    
    public static Move moveInPink( ArrayList<SaboteurMove> pink, SaboteurBoardState boardState, String policy)
    {
    	// to do: consider how to handle lock
    	/*
    	 * find the step with highest utility
    	 * if the utility > 0
    	 * 		return the step
    	 * else
    	 * 		return null
    	 */
    	
    	Boolean enterPink = checkIfLockInPink(pink);
    	
    	double max_utility = 0.0;
    	SaboteurMove myMove = null;
    	for (int i = 0; i < pink.size(); i++)
    	{
    		SaboteurMove currentMove = pink.get(i);
    		
    		PinkBoard pinkBoard = new PinkBoard(boardState);
    		
    		int myID = boardState.getTurnPlayer();
    		double x = Math.random() * (boardState.getNbMalus(1 - myID) * 0.25 + 0.2) - 0.1;
    	    double current_utility = pinkBoard.evaInPink(currentMove, enterPink);
    	    current_utility += x;
    	       
    		if (current_utility > max_utility)
    		{
    			max_utility = current_utility;
    			myMove = currentMove;
    		}
    	}
    	return myMove;
    }
    
    public static Move placeMap( ArrayList<SaboteurMove> map, SaboteurBoardState boardState ) 
    {
    	/*
    	 * choose map in sequence left -> right -> centre
    	 * return null if no map
    	 */
    	
    	int max_utility = 0;
    	SaboteurMove myMove = null;
    	for (int i = 0; i < map.size(); i++)
    	{
    		SaboteurMove currentMove = map.get(i);
    		int current_utility = Evaluation.evaMap(currentMove, boardState);
    		if (current_utility > max_utility)
    		{
    			max_utility = current_utility;
    			myMove = currentMove;
    		}
    	}
    	return myMove;
    }
    
    public static int countNumBonusDrop(ArrayList<SaboteurMove> drop)
    {
    	int num_Bonus = 0;
    	for (int i = 0; i < drop.size(); i++)
    		if (drop.get(i).getCardPlayed().getName().equals("Bonus"))
    			num_Bonus++;
    	return num_Bonus;
    }
    
    public static int decreaseBonusDrop(SaboteurMove drop, int num_Bonus)
    {
    	if (drop.getCardPlayed().getName().equals("Bonus"))
			num_Bonus--;
    	return num_Bonus;
    }
    
    public static Move drop( ArrayList<SaboteurMove> drop, String condition, SaboteurBoardState boardState )
    {
    	/*
    	 * find the step with highest utility
    	 * if the utility > 0
    	 * 		return the step
    	 * else if (condition == 'negative')
    	 * 		return the step
    	 * else
    	 * 		return null
    	 */

    	SaboteurMove myMove = null;
    	int max_utility;
    	if (condition.equals("positive"))
    		max_utility = 0;
    	else // if (condition.equals("negative"))
    		max_utility = -100;
    	
    	int num_Bonus = countNumBonusDrop(drop);
    	
    	for (int i = 0; i < drop.size(); i++)
    	{
    		SaboteurMove currentMove = drop.get(i);
    		int current_utility = Evaluation.evaDrop(currentMove, boardState, num_Bonus, max_utility);
    		decreaseBonusDrop(currentMove,num_Bonus);
    		
    		if (current_utility > max_utility)
    		{
    			max_utility = current_utility;
    			myMove = currentMove;
    		}
    	}
    	return myMove;
    }
       
    public static Move moveOutPink ( ArrayList<SaboteurMove> outPink, SaboteurBoardState boardState )
    {	
    	SaboteurMove myMove = Evaluation.evaOutPink(outPink, boardState);
    	return myMove;
    }
}