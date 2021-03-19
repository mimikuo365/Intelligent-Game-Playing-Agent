package student_player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import Saboteur.SaboteurBoardState;
import Saboteur.cardClasses.SaboteurTile;

public class Connectivity {
	
	public static boolean findBreakPoints(SaboteurBoardState boardState, int[] curLoc, int[] start, ArrayList<int[]> output)
	{
		// return true if there is a path
		// if there are broken tiles, they are saved in output
        ArrayList<int[]> brokeTile1 = new ArrayList<>(); 
		boolean found = findPath(boardState, start, curLoc, brokeTile1);
		if (!found)
			return false;
		else if (brokeTile1.size() != 0)
		{
	        ArrayList<int[]> brokeTile2 = new ArrayList<>(); 
			findPath(boardState, curLoc, start, brokeTile2);
			ArrayList<int[]> tmp = intersect(brokeTile1, brokeTile2);
			output.addAll(tmp);
		}
		return true;
	}
	
	public static ArrayList<int[]> intersect(ArrayList<int[]> A, ArrayList<int[]> B)
	{
		ArrayList<int[]> output = new ArrayList<int[]>();
		for (int[] a : A)
			for (int[] b : B)
				if (a[0] == b[0] && a[1] == b[1])
					output.add(b);
		return output;
	}

	public static boolean findPath(SaboteurBoardState boardState, int[] curLoc, int[] start, ArrayList<int[]> brokeTile)
    {
		// return true if a path is found
		SaboteurTile[][] myBoard = boardState.getHiddenBoard();
        ArrayList<int[]> neighbors = new ArrayList<>(); 

        ArrayList<int[]> visited = new ArrayList<int[]>(); 
        visited.add(start);
        addNeighbor(myBoard, start, neighbors, visited);
        
        while(neighbors.size() > 0){        		
        	int[] tmpPos = neighbors.remove(0);

            if(Arrays.equals(curLoc,tmpPos))
                return true;
            
            if (Broken(tmpPos, myBoard))
            	if(!included(brokeTile, tmpPos))
            			brokeTile.add(tmpPos);
            visited.add(tmpPos);
            addNeighbor(myBoard, tmpPos,neighbors,visited);
        }
        return false;
    }
    
    public static void addNeighbor(SaboteurTile[][] myBoard, int[] pos, ArrayList<int[]> neighbors, ArrayList<int[]> visited)
    {
        int[][] index = {{0, 1},{0, -1},{1, 0},{-1, 0}};
        int i = pos[0];
        int j = pos[1];
        for (int m = 0; m < 4; m++) 
        {
            if ((0 <= i + index[m][0]) && (i + index[m][0] < 14) 
             && (0 <= j + index[m][1]) && (j + index[m][1] < 14)) 
            { 
                int[] tmpPos = new int[]{i + index[m][0], j + index[m][1]};
                
                if (!included(visited, tmpPos))
                {

                	if (myBoard[tmpPos[0]][tmpPos[1]] != null) 
                	{
                		if (!myBoard[tmpPos[0]][tmpPos[1]].getName().equals("Tile:4")
                		&& !myBoard[tmpPos[0]][tmpPos[1]].getName().equals("Tile:4_flip")
                		&& !myBoard[tmpPos[0]][tmpPos[1]].getName().equals("Tile:12")
                		&& !myBoard[tmpPos[0]][tmpPos[1]].getName().equals("Tile:12_flip")
                		&& !included(neighbors, tmpPos))
                			neighbors.add(tmpPos);
                	}
                }
            }
        }
    }
  
    public static boolean included(ArrayList<int[]> a,int[] o)
    { 
        if (o == null) 
        {
            for (int i = 0; i < a.size(); i++) 
                if (a.get(i) == null)
                    return true;
        } 
        else 
        {
            for (int i = 0; i < a.size(); i++) 
                if (Arrays.equals(o, a.get(i)))
                    return true;
        }
        return false;
    }
	   
    public static boolean Broken(int[] tmpPos, SaboteurTile[][] myBoard)
    {
    	// cards that are broken tile
    	String idx = myBoard[tmpPos[0]][tmpPos[1]].getName().split(":")[1];
    	if (idx.equals("1") || idx.equals("2") || idx.equals("3") 
    	 || idx.equals("4") || idx.equals("11") || idx.equals("12")
    	 || idx.equals("13") || idx.equals("14") || idx.equals("15")
    	 || idx.equals("1_flip") || idx.equals("2_flip") || idx.equals("3_flip")
    	 || idx.equals("4_flip") || idx.equals("11_flip") || idx.equals("12_flip")
    	 || idx.equals("13_flip") || idx.equals("14_flip") || idx.equals("15_flip"))
    		return true;
    	else
    		return false;
    }
}
