package student_player;

import Saboteur.SaboteurMove;

public class baseFunction {
	
	public static Boolean isPink(SaboteurMove move)
    {
    	Boolean isPink = false;
    	if (isLock(move))
    		isPink = true;
    	else if (move.getCardPlayed().getName().split(":")[0].equals("Tile"))
    	{
    		int[] location = move.getPosPlayed();
    		int x = location[0];
    		int y = location[1];
    		      
    		if (10 <= x && x <= 13 && 1 <= y && y <= 9)
    		     isPink = true;
    	}
    	
    	return isPink;
    }
	
	public static Boolean isLock(SaboteurMove move)
    {
    	Boolean isLock = false;
    	if (move.getCardPlayed().getName().equals("Malus"))
    		isLock = true;
    	return isLock;
    }
    
    public static Boolean isUnlock(SaboteurMove move)
    {
    	Boolean isUnlock = false;
    	if (move.getCardPlayed().getName().equals("Bonus"))
    		isUnlock = true;
    	return isUnlock;
    }
    
    public static Boolean isMap(SaboteurMove move)
    {
    	Boolean isMap = false;
    	if (move.getCardPlayed().getName().equals("Map"))
    		isMap = true;
    	return isMap;
    }
    
    public static Boolean isDrop(SaboteurMove move)
    {
    	Boolean isDrop = false;
    	if (move.getCardPlayed().getName().equals("Drop"))
    	{
    		isDrop = true;
    	}
    	return isDrop;
    }
   
    public static Boolean notPink(SaboteurMove move)
    {
    	Boolean notPink = false;
    	notPink = !(isPink(move));
    	return notPink;
    }
}
