package com.checkers.ArtificialIntelligence;

import java.util.List;
import java.util.ArrayList;

import com.checkers.model.CheckersPieceModel;
import com.checkers.model.PlayerToken;
import com.checkers.model.PossibleMove;
import com.checkers.rules.MoveValidator;

public class SmartMoveMaker {

	public PossibleMove getBestMoveToMakeForPlayer(
			List<CheckersPieceModel> allCheckersPieces, PlayerToken playerToken) {
		
		ArrayList<PossibleMove> AllJumps= new ArrayList<PossibleMove>(AllOfThisPlayersJumps(allCheckersPieces, playerToken));
		PossibleMove bestMove= null;
		if (AllJumps.size()>0){
			
			bestMove=AllJumps.get((int)((Math.random()*(AllJumps.size()+1))));
			
		}
		
		else {
			List<PossibleMove> allValidMovesForPlayer = MoveValidator.getAllValidMovesForPlayer(allCheckersPieces, playerToken);
			bestMove=allValidMovesForPlayer.get((int)((Math.random()*(AllJumps.size()+1))));
			
		}
			
			
		return bestMove;
	}
	
	
	
	public ArrayList<PossibleMove> AllOfThisPlayersJumps(
			List<CheckersPieceModel> allCheckersPieces, PlayerToken playerToken){
		
		List<PossibleMove> allValidMovesForPlayer = MoveValidator.getAllValidMovesForPlayer(allCheckersPieces, playerToken);
		ArrayList<PossibleMove> allJumpsForPlayer=new ArrayList<PossibleMove>();
		
		for (int i=0;i<allValidMovesForPlayer.size();i++){
			if (allValidMovesForPlayer.get(i).isThisMoveAJump())
				allJumpsForPlayer.add(allValidMovesForPlayer.get(i));				
		}
		
		
		return allJumpsForPlayer;
	}
	
	
}
