package com.checkers.model;

public class PossibleMove {

	private final CheckersPieceModel pieceToMove;
	private final int newRowLocation;
	private final int newColumnLocation;

	public PossibleMove(CheckersPieceModel pieceToMove, int newRowLocation,
			int newColumnLocation) {
		this.pieceToMove = pieceToMove;
		this.newRowLocation = newRowLocation;
		this.newColumnLocation = newColumnLocation;

	}

	public int getNewRowLocation() {
		return this.newRowLocation;
	}

	public CheckersPieceModel getPieceToMove() {
		return this.pieceToMove;
	}

	public int getNewColumnLocation() {
		return this.newColumnLocation;
	}

}
