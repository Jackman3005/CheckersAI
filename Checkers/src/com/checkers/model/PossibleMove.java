package com.checkers.model;

public class PossibleMove {

	private final CheckersPieceModel pieceToMove;
	private final int newRowLocation;
	private final int newColumnLocation;
	private CheckersPieceModel pieceThatWillBeTaken = null;

	public PossibleMove(CheckersPieceModel pieceToMove, int newRowLocation,
			int newColumnLocation, CheckersPieceModel pieceThatWillBeTaken) {
		this(pieceToMove, newRowLocation, newColumnLocation);
		this.pieceThatWillBeTaken = pieceThatWillBeTaken;
	}

	public PossibleMove(CheckersPieceModel pieceToMove, int newRowLocation,
			int newColumnLocation) {
		this.pieceToMove = pieceToMove;
		this.newRowLocation = newRowLocation;
		this.newColumnLocation = newColumnLocation;
	}

	public int getEndingRowLocation() {
		return this.newRowLocation;
	}

	public CheckersPieceModel getPieceToMove() {
		return this.pieceToMove;
	}

	public int getEndingColumnLocation() {
		return this.newColumnLocation;
	}

	public boolean willTakeAnEnemyPiece() {
		return this.pieceThatWillBeTaken != null;
	}

	public CheckersPieceModel getPieceThatWillBeTaken() {
		return this.pieceThatWillBeTaken;
	}

}
