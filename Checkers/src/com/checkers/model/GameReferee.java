package com.checkers.model;

import java.util.ArrayList;
import java.util.Stack;

public class GameReferee {

	private final CheckersBoardModel checkersBoardModel;

	public GameReferee(CheckersBoardModel checkersBoardModel) {
		this.checkersBoardModel = checkersBoardModel;
	}

	public void howWeDoing(Stack<UndoableMove> previousMoves,
			PossibleMove moveToCheck) {
		// ArrayList<UndoableMove> listOfPreviousMoves =
		// copyStackToList(previousMoves);

		// Something about checking to see if you've done the same move 3 times
		// in a row
		// int numberOfMoves = listOfPreviousMoves.size();
		// UndoableMove lastMoveMade = listOfPreviousMoves.get(numberOfMoves -
		// 1);
		//
		// for (int i = numberOfMoves - 2; i > numberOfMoves - 3; i--) {
		// UndoableMove move = listOfPreviousMoves.get(i);
		// UndoableMove previous = listOfPreviousMoves.get(i - 1);
		// undoableMove.getMoveToUndo();
		// }
		if (this.checkersBoardModel.getTurnCount()
				- this.checkersBoardModel.getLastTurnThatAPieceWasCaptured() > 24) {
			System.out.println("Hey Bud Game Ova. No captures in 25 moves.");
		}
	}

	private ArrayList<UndoableMove> copyStackToList(
			Stack<UndoableMove> previousMoves) {
		ArrayList<UndoableMove> listOfPreviousMoves = new ArrayList<UndoableMove>();
		for (int i = 0; i < previousMoves.size(); i++) {
			listOfPreviousMoves.add(previousMoves.get(i));
		}
		return listOfPreviousMoves;
	}
}
