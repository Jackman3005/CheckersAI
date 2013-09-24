package com.checkers.ArtificialIntelligence;

import java.util.List;

import com.checkers.model.CheckersPieceModel;
import com.checkers.model.PlayerToken;
import com.checkers.model.PossibleMove;
import com.checkers.rules.MoveValidator;

public class SmartMoveMaker {

	public static PossibleMove getBestMoveToMakeForPlayer(
			List<CheckersPieceModel> allCheckersPieces, PlayerToken playerToken) {
		List<PossibleMove> allValidMovesForPlayer = MoveValidator
				.getAllValidMovesForPlayer(allCheckersPieces, playerToken);

		if (allValidMovesForPlayer.isEmpty())
			return null;

		return allValidMovesForPlayer
				.get((int) ((Math.random() * (allValidMovesForPlayer.size()))));

	}

	// Hey, I moved your code for requiring jump moves if they are available
	// into the MoveValidator.
	//
	//
	// ArrayList<PossibleMove> allJumps = new ArrayList<PossibleMove>(
	// allOfThisPlayersJumps(allCheckersPieces, playerToken));
	// PossibleMove bestMove = null;
	// if (allJumps.size() > 0) {
	//
	// bestMove = allJumps
	// .get((int) ((Math.random() * (allJumps.size() + 1))));
	//
	// }
	//
	// else {
	// List<PossibleMove> allValidMovesForPlayer = MoveValidator
	// .getAllValidMovesForPlayer(allCheckersPieces, playerToken);
	// bestMove = allValidMovesForPlayer
	// .get((int) ((Math.random() * (allJumps.size() + 1))));
	//
	// }
	//
	// return bestMove;

	// public static ArrayList<PossibleMove> allOfThisPlayersJumps(
	// List<CheckersPieceModel> allCheckersPieces, PlayerToken playerToken) {
	//
	// List<PossibleMove> allValidMovesForPlayer = MoveValidator
	// .getAllValidMovesForPlayer(allCheckersPieces, playerToken);
	// ArrayList<PossibleMove> allJumpsForPlayer = new
	// ArrayList<PossibleMove>();
	//
	// for (int i = 0; i < allValidMovesForPlayer.size(); i++) {
	// if (allValidMovesForPlayer.get(i).isThisMoveAJump())
	// allJumpsForPlayer.add(allValidMovesForPlayer.get(i));
	// }
	//
	// return allJumpsForPlayer;
	// }

}
