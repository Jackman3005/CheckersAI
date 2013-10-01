package com.checkers.artificialIntelligence;

import java.util.ArrayList;
import java.util.List;

import com.checkers.model.CheckersBoardModel;
import com.checkers.model.CheckersPieceModel;
import com.checkers.model.PlayerToken;
import com.checkers.model.PossibleMove;
import com.checkers.rules.MoveValidator;

public class SmartMoveMaker {

	public static PossibleMove getBestMoveToMakeForPlayer(
			List<CheckersPieceModel> allCheckersPieces, PlayerToken playerToken) {
		List<PossibleMove> allValidMovesForPlayer = MoveValidator
				.getAllValidMovesForPlayer(allCheckersPieces, playerToken);
		PossibleMove bestPossibleMove = null;
		int bestBoardValue = Integer.MIN_VALUE;
		for (PossibleMove possibleMove : allValidMovesForPlayer) {
			List<CheckersPieceModel> possibleBoardLayout = CheckersBoardModel
					.emulateMovingAPieceAndReturnCopyOfPiecesOnBoard(
							allCheckersPieces, possibleMove);
			int valueOfBoardAfterSevenMoreTurns = getValueOfMoveUsingMinMaxSearchForTheSpecifiedNumberOfTurns(
					possibleBoardLayout, getOppositePlayerToken(playerToken),
					7, false);
			if (valueOfBoardAfterSevenMoreTurns > bestBoardValue) {
				bestBoardValue = valueOfBoardAfterSevenMoreTurns;
				bestPossibleMove = possibleMove;
			}
		}
		return bestPossibleMove;

	}

	private static int getValueOfMoveUsingMinMaxSearchForTheSpecifiedNumberOfTurns(
			List<CheckersPieceModel> possibleBoardLayout,
			PlayerToken playerToken, int numberOfTurnsToCheck,
			boolean isPlayersTurn) {
		List<PossibleMove> allValidMovesForPlayer = MoveValidator
				.getAllValidMovesForPlayer(possibleBoardLayout, playerToken);

		List<CheckersPieceModel> bestBoardLayoutForPlayer = new ArrayList<CheckersPieceModel>();
		int bestValueForBoard = Integer.MIN_VALUE;
		for (PossibleMove possibleMove : allValidMovesForPlayer) {
			List<CheckersPieceModel> newPossibleBoardLayout = CheckersBoardModel
					.emulateMovingAPieceAndReturnCopyOfPiecesOnBoard(
							possibleBoardLayout, possibleMove);
			int valueOfBoardAfterPossibleMove = CheckersPieceLocationValueCalculator.singleton
					.getValueOfBoardFromPerspectiveOfPlayer(
							newPossibleBoardLayout, playerToken);
			if (valueOfBoardAfterPossibleMove > bestValueForBoard) {
				bestValueForBoard = valueOfBoardAfterPossibleMove;
				bestBoardLayoutForPlayer = newPossibleBoardLayout;
			}
		}

		if (numberOfTurnsToCheck > 1) {
			PlayerToken playerForNextTurn = getOppositePlayerToken(playerToken);
			return getValueOfMoveUsingMinMaxSearchForTheSpecifiedNumberOfTurns(
					bestBoardLayoutForPlayer, playerForNextTurn,
					--numberOfTurnsToCheck, !isPlayersTurn);
		} else {
			PlayerToken playerOfLastTurn = isPlayersTurn ? playerToken
					: getOppositePlayerToken(playerToken);
			return CheckersPieceLocationValueCalculator.singleton
					.getValueOfBoardFromPerspectiveOfPlayer(
							bestBoardLayoutForPlayer, playerOfLastTurn);
		}

	}

	private static PlayerToken getOppositePlayerToken(PlayerToken playerToken) {
		return playerToken.equals(PlayerToken.BOTTOM_PLAYER) ? PlayerToken.TOP_PLAYER
				: PlayerToken.BOTTOM_PLAYER;
	}

}
