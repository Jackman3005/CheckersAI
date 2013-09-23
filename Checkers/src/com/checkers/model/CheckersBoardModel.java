package com.checkers.model;

import java.util.ArrayList;
import java.util.List;

public class CheckersBoardModel {

	private ArrayList<CheckersPieceModel> piecesOnBoard;
	private final List<CheckersBoardObserverInterface> observers;

	public CheckersBoardModel() {
		initializeBoard();
		this.observers = new ArrayList<CheckersBoardObserverInterface>();
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

	public boolean capturePiece(CheckersPieceModel pieceToCapture) {
		pieceToCapture.capturePiece();
		boolean pieceWasOneOfTheOnesActuallyOnTheBoardAndNotACopy = this.piecesOnBoard
				.remove(pieceToCapture);
		if (pieceWasOneOfTheOnesActuallyOnTheBoardAndNotACopy) {
			notifyObserversThatTheBoardChanged();
		}
		return pieceWasOneOfTheOnesActuallyOnTheBoardAndNotACopy;
	}

	public boolean actuallyMovePiece(PossibleMove moveToMake) {
		CheckersPieceModel pieceToMove = moveToMake.getPieceToMove();
		int rowToMoveTo = moveToMake.getEndingRowLocation();
		pieceToMove.setRow(rowToMoveTo);
		pieceToMove.setColumn(moveToMake.getEndingColumnLocation());

		if (rowToMoveTo == 0
				&& pieceToMove.getPlayerToken().equals(PlayerToken.PLAYER)) {
			pieceToMove.kingMe();
		} else if (rowToMoveTo == 7
				&& pieceToMove.getPlayerToken().equals(PlayerToken.OPPONENT)) {
			pieceToMove.kingMe();
		}

		notifyObserversThatTheBoardChanged();
		return this.piecesOnBoard.contains(pieceToMove);
	}

	public static List<CheckersPieceModel> emulateMovingAPieceAndReturnCopyOfPiecesOnBoard(
			List<CheckersPieceModel> allPiecesOnBoard,
			PossibleMove moveToEmulate) {
		List<CheckersPieceModel> copyOfPiecesOnBoardWithMoveEmulated = new ArrayList<CheckersPieceModel>();
		for (CheckersPieceModel checkersPieceModel : allPiecesOnBoard) {
			if (checkersPieceModel.equals(moveToEmulate.getPieceToMove())) {
				CheckersPieceModel movedCheckerPieceCopy = new CheckersPieceModel(
						moveToEmulate.getEndingRowLocation(),
						moveToEmulate.getEndingColumnLocation(), moveToEmulate
								.getPieceToMove().getPlayerToken());
				copyOfPiecesOnBoardWithMoveEmulated.add(movedCheckerPieceCopy);
			} else {
				// We may not actually have to make a copy of the pieces that
				// do not move. Depends on how we implement our AI. If we don't
				// need to copy unmoved pieces then we could save a lot of
				// memory by not copying them lol

				CheckersPieceModel copyOfCheckersPieceModel = CheckersPieceModel
						.copy(checkersPieceModel);
				copyOfPiecesOnBoardWithMoveEmulated
						.add(copyOfCheckersPieceModel);
			}
		}

		return copyOfPiecesOnBoardWithMoveEmulated;
	}

	private void notifyObserversThatTheBoardChanged() {
		for (CheckersBoardObserverInterface observer : this.observers) {
			observer.boardChanged();
		}
	}

	public void addObserver(CheckersBoardObserverInterface observer) {
		this.observers.add(observer);
	}

	public void removeObserver(CheckersBoardObserverInterface observer) {
		this.observers.remove(observer);
	}

	public List<CheckersPieceModel> getPiecesOnBoard() {
		return this.piecesOnBoard;
	}
}
