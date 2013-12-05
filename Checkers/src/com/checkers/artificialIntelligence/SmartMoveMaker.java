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
			List<CheckersPieceModel> allCheckersPieces,
			PlayerToken playerToken, List<PossibleMove> allPreviousMovesMade) {
		List<PossibleMove> allValidMovesForPlayer = MoveValidator
				.getAllValidMovesForPlayer(allCheckersPieces, playerToken);
		int bestBoardValue = Integer.MIN_VALUE;
		// System.out.println("PlayerToMove: " + playerToken);
		List<PossibleMove> listOfMovesEqualInWeight = new ArrayList<PossibleMove>();
		for (PossibleMove possibleMove : allValidMovesForPlayer) {
			if (numberOfTimesThisMoveHasAlreadyBeenMadeByThisPieceRecently(
					allPreviousMovesMade, possibleMove) > 2) {
				continue;
				// Skip checking this move because it has already been made a
				// few times.
				// We don't want to continue repeating the same move pattern
			}
			/*
			 * An idea on fixing the double-jump issue
			 * boolean doubleJumpInTreePlayer=false;
			 * boolean doubleJumpInTreeeOponent=false;
			 * if (doubleJumpInTreeOponent) continue; //don't let it make this move
			 * if (doubleJumpInTreePlayer) {
			 * 		//Put Something here to rate this move higher
			 * }
			 *  //Will need to put the flags to true if a double jump was made
			 *  
			 */
			List<CheckersPieceModel> possibleBoardLayout = CheckersBoardModel
					.emulateMovingAPieceAndReturnCopyOfPiecesOnBoard(
							allCheckersPieces, possibleMove);
			// testOutputMethod(possibleMove);
			int valueOfBoardAfterSevenMoreTurns = getValueOfMoveUsingMinMaxSearchForTheSpecifiedNumberOfTurns(
					new ArrayList<PossibleMove>(), possibleBoardLayout,
					getOppositePlayerToken(playerToken), 7, false);
			if (valueOfBoardAfterSevenMoreTurns > bestBoardValue) {
				listOfMovesEqualInWeight.clear();
				listOfMovesEqualInWeight.add(possibleMove);
				bestBoardValue = valueOfBoardAfterSevenMoreTurns;
			} else if (valueOfBoardAfterSevenMoreTurns == bestBoardValue) {
				listOfMovesEqualInWeight.add(possibleMove);
			}
		}
		int numberOfEquallyGoodMoves = listOfMovesEqualInWeight.size();
		if (numberOfEquallyGoodMoves > 0) {
			int moveToGet = (int) (Math.random() * numberOfEquallyGoodMoves);
			return listOfMovesEqualInWeight.get(moveToGet);//CHanged from 0 -Patrick
		}
		System.out.println(playerToken + " has Lost.");
		return null;

	}

	private static int getValueOfMoveUsingMinMaxSearchForTheSpecifiedNumberOfTurns(
			ArrayList<PossibleMove> movesMade,
			List<CheckersPieceModel> possibleBoardLayout,
			PlayerToken playerToken, int numberOfTurnsToCheck,
			boolean isPlayersTurn) {
		List<CheckersPieceModel> bestBoardLayoutForPlayer = new ArrayList<CheckersPieceModel>();

		List<PossibleMove> allValidMovesForPlayer = MoveValidator
				.getAllValidMovesForPlayer(possibleBoardLayout, playerToken);

		if (allValidMovesForPlayer.size() == 0) {
			return isPlayersTurn ? Integer.MIN_VALUE : Integer.MAX_VALUE;
		}

		int bestValueForBoard = Integer.MIN_VALUE;
		PossibleMove bestMoveForPlayer = null;
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
				bestMoveForPlayer = possibleMove;
			}
		}
		movesMade.add(bestMoveForPlayer);
		if (numberOfTurnsToCheck > 1 && bestValueForBoard != Integer.MIN_VALUE) {
			PlayerToken playerForNextTurn = getOppositePlayerToken(playerToken);
			return getValueOfMoveUsingMinMaxSearchForTheSpecifiedNumberOfTurns(
					movesMade, bestBoardLayoutForPlayer, playerForNextTurn,
					--numberOfTurnsToCheck, !isPlayersTurn);
		} else {
			PlayerToken playerWeAreTryingToHelp = isPlayersTurn ? playerToken
					: getOppositePlayerToken(playerToken);
			int valueOfBoardFromPerspectiveOfPlayer = CheckersPieceLocationValueCalculator.singleton
					.getValueOfBoardFromPerspectiveOfPlayer(
							bestBoardLayoutForPlayer, playerWeAreTryingToHelp);
			if (!isPlayersTurn && bestValueForBoard == Integer.MIN_VALUE) {
				valueOfBoardFromPerspectiveOfPlayer = Integer.MAX_VALUE;
			}
			// System.out.println("ValueOfPossibleTurn: "
			// + valueOfBoardFromPerspectiveOfPlayer + " for player: "
			// + playerWeAreTryingToHelp);
			return valueOfBoardFromPerspectiveOfPlayer;
		}

	}

	private static int numberOfTimesThisMoveHasAlreadyBeenMadeByThisPieceRecently(
			List<PossibleMove> allPreviousMovesMade, PossibleMove possibleMove) {
		int numberOfMovesBackToCheck = 16;

		int numberOfTimesThisMoveHasAlreadyBeenMade = 0;
		for (int i = allPreviousMovesMade.size() - 1; i > 0
				&& i >= allPreviousMovesMade.size() - numberOfMovesBackToCheck; i--) {
			if (allPreviousMovesMade.get(i).equals(possibleMove)) {
				numberOfTimesThisMoveHasAlreadyBeenMade++;
			}
		}
		return numberOfTimesThisMoveHasAlreadyBeenMade;
	}

	private static PlayerToken getOppositePlayerToken(PlayerToken playerToken) {
		return playerToken.equals(PlayerToken.BOTTOM_PLAYER) ? PlayerToken.TOP_PLAYER
				: PlayerToken.BOTTOM_PLAYER;
	}

	private static void testOutputMethod(PossibleMove possibleMove) {
		CheckersBoardModel deleteMe = new CheckersBoardModel();
		int pieceToMove = deleteMe.getNotation(possibleMove.getPieceToMove()
				.getRow(), possibleMove.getPieceToMove().getColumn());
		int locationToMoveTo = deleteMe.getNotation(
				possibleMove.getNewRowLocation(),
				possibleMove.getNewColumnLocation());
		System.out.println("Move Being Investigated: " + pieceToMove + "-"
				+ locationToMoveTo);
	}

}
