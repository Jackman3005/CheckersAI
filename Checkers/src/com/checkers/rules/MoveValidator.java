package com.checkers.rules;

import java.util.List;

import com.checkers.model.CheckersPieceModel;

public class MoveValidator {

	public static boolean isMoveValid(
			List<CheckersPieceModel> allPiecesOnBoard,
			CheckersPieceModel pieceToMove, int newRowPosition,
			int newColumnPosition) {

		boolean isMovingToABlackTile = (newRowPosition + newColumnPosition) % 2 != 0;

		int currentRowPosition = pieceToMove.getRow();
		int currentColumnPosition = pieceToMove.getColumn();

		int rowMovement = Math.abs(newRowPosition - currentRowPosition);
		int columnMovement = Math
				.abs(newColumnPosition - currentColumnPosition);

		boolean isMovingOnlyOneSpaceAway_ThisIsAnUnfinishedRule = columnMovement <= 1
				&& rowMovement <= 1;

		return isMovingToABlackTile && isMovingOnlyOneSpaceAway_ThisIsAnUnfinishedRule;
	}
}
