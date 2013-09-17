package com.checkers.model;

import java.util.ArrayList;

import com.checkers.rules.MoveValidator;

public class CheckersBoardModel {

	private ArrayList<CheckersPieceModel> piecesOnBoard;

	public CheckersBoardModel() {
		initializeBoard();
	}

	private void initializeBoard() {
		this.piecesOnBoard = new ArrayList<CheckersPieceModel>();
		for (int column = 0; column < 8; column++) {
			for (int row = 0; row < 3; row++) {
				if ((column + row) % 2 == 1) {
					this.piecesOnBoard.add(new CheckersPieceModel(row, column,
							PlayerToken.OPPONENT));
				}
			}
			for (int row = 5; row < 8; row++) {
				if ((column + row) % 2 == 1) {
					this.piecesOnBoard.add(new CheckersPieceModel(row, column,
							PlayerToken.PLAYER));
				}
			}
		}
	}

	public boolean movePiece(CheckersPieceModel pieceToMove,
			int newRowPosition, int newColumnPosition) {
		boolean moveValid = MoveValidator.isMoveValid(this.piecesOnBoard,
				pieceToMove, newRowPosition, newColumnPosition);
		if (moveValid) {
			pieceToMove.setRow(newRowPosition);
			pieceToMove.setColumn(newColumnPosition);
			return true;
		}
		return false;
	}

	public ArrayList<CheckersPieceModel> getPiecesOnBoard() {
		return this.piecesOnBoard;
	}
}
