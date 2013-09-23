package com.checkers.model;

public class UndoableMove {
	private final PossibleMove moveToUndo;
	private final int oldPieceRowLocation;
	private final int oldPieceColumnLocation;
	private final boolean pieceWasKingedDuringMove;

	public UndoableMove(PossibleMove moveToUndo, int oldPieceRowLocation,
			int oldPieceColumnLocation, boolean pieceWasKingedDuringMove) {
		this.moveToUndo = moveToUndo;
		this.oldPieceRowLocation = oldPieceRowLocation;
		this.oldPieceColumnLocation = oldPieceColumnLocation;
		this.pieceWasKingedDuringMove = pieceWasKingedDuringMove;
	}

	public PossibleMove getMoveToUndo() {
		return this.moveToUndo;
	}

	public int getOldPieceColumnLocation() {
		return this.oldPieceColumnLocation;
	}

	public int getOldPieceRowLocation() {
		return this.oldPieceRowLocation;
	}

	public boolean pieceWasKingedDuringMove() {
		return this.pieceWasKingedDuringMove;
	}

}
