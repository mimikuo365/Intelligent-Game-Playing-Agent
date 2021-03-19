package student_player;

import boardgame.Move;

import Saboteur.SaboteurPlayer;

import java.util.ArrayList;

import Saboteur.SaboteurBoardState;
import Saboteur.SaboteurMove;

/** A player file submitted by a student. */
public class StudentPlayer extends SaboteurPlayer {

    /**
     * You must modify this constructor to return your student number. This is
     * important, because this is what the code that runs the competition uses to
     * associate you with your agent. The constructor should do nothing else.
     */
    public StudentPlayer() {
        super("260938567");
    }

    /**
     * This is the primary method that you need to implement. The ``boardState``
     * object contains the current state of the game, which your agent must use to
     * make decisions.
     */
    public Move chooseMove(SaboteurBoardState boardState) {
    	ArrayList<SaboteurMove> AllLegalMoves = boardState.getAllLegalMoves();
		ArrayList<SaboteurMove> pink = new ArrayList<SaboteurMove>();
		ArrayList<SaboteurMove> map = new ArrayList<SaboteurMove>();
		ArrayList<SaboteurMove> drop = new ArrayList<SaboteurMove>();
		ArrayList<SaboteurMove> notPink = new ArrayList<SaboteurMove>();
		
		Move myMove = MyTools.classifyAndUnlock(AllLegalMoves, pink, map, drop, notPink);
		if (myMove != null)
			return myMove;
		myMove = MyTools.moveInPink(pink, boardState, "default");
		if (myMove != null)
			return myMove;
		myMove = MyTools.placeMap(map, boardState);
		if (myMove != null)
			return myMove;
		myMove = MyTools.drop(drop, "positive", boardState);
		if (myMove != null)
			return myMove;
		myMove = MyTools.moveOutPink(notPink, boardState);
		if (myMove != null)
			return myMove;
		myMove = MyTools.drop(drop, "negative", boardState);
		if (myMove != null)
			return myMove;
        return myMove;
    }
}