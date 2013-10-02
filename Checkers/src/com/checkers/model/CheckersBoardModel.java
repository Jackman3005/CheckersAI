package com.checkers.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CheckersBoardModel {

	private ArrayList<CheckersPieceModel> piecesOnBoard;
	private final List<CheckersBoardObserverInterface> observers;
	private final Stack<UndoableMove> undoMoveStack;
	private int[][] spaceNotation;
	private int turnCount;
	private int lastTurnThatAPieceWasCaptured;

	public CheckersBoardModel() {
		initializeBoard();
		initializeNotation();
		this.observers = new ArrayList<CheckersBoardObserverInterface>();
		this.undoMoveStack = new Stack<UndoableMove>();
		this.turnCount = 0;
		this.lastTurnThatAPieceWasCaptured = 0;
	}

	private void initializeBoard() {
		this.piecesOnBoard = new ArrayList<CheckersPieceModel>();
		for (int column = 0; column < 8; column++) {
			for (int row = 0; row < 3; row++) {
				if ((column + row) % 2 == 1) {
					this.piecesOnBoard.add(new CheckersPieceModel(row, column,
							PlayerToken.TOP_PLAYER));
				}
			}
			for (int row = 5; row < 8; row++) {
				if ((column + row) % 2 == 1) {
					this.piecesOnBoard.add(new CheckersPieceModel(row, column,
							PlayerToken.BOTTOM_PLAYER));
				}
			}
		}
	}

	private void initializeNotation() {
		this.spaceNotation = new int[8][8];
		int spaceNumber = 1;
		for (int column = 0; column < 8; column++) {
			for (int row = 0; row < 8; row++) {
				this.spaceNotation[column][row] = 0;
				if ((column + row) % 2 == 1) {
					this.spaceNotation[column][row] = spaceNumber;
					spaceNumber++;
				}

			}
		}
	}

	public boolean capturePiece(CheckersPieceModel pieceToCapture) {
		pieceToCapture.capturePiece();
		boolean pieceWasOneOfTheOnesActuallyOnTheBoardAndNotACopy = this.piecesOnBoard
				.remove(pieceToCapture);
		notifyObserversThatTheBoardChanged();
		return pieceWasOneOfTheOnesActuallyOnTheBoardAndNotACopy;
	}

	public boolean actuallyMovePiece(PossibleMove moveToMake) {
		if (moveToMake == null)
			return false;
		CheckersPieceModel pieceToMove = moveToMake.getPieceToMove();
		System.out.print(pieceToMove.getPlayerToken() + ":\t");
		System.out.print(getNotation(moveToMake.getPieceToMove()) + "-");
		int rowToMoveTo = moveToMake.getNewRowLocation();
		List<Point> intermediateLocations = moveToMake
				.getIntermediateLocations();
		for (Point point : intermediateLocations) {
			System.out.print(this.spaceNotation[point.y][point.x] + "-");
		}
		boolean pieceShouldBecomeKing = shouldPieceBecomeKing(
				pieceToMove.getPlayerToken(), rowToMoveTo);
		UndoableMove undoableMoveToRevertThisMove = new UndoableMove(
				moveToMake, pieceToMove.getRow(), pieceToMove.getColumn(),
				pieceShouldBecomeKing);

		if (pieceShouldBecomeKing) {
			pieceToMove.kingMe();
		}
		pieceToMove.setRow(rowToMoveTo);
		pieceToMove.setColumn(moveToMake.getNewColumnLocation());
		System.out.println(getNotation(pieceToMove));
		for (CheckersPieceModel checkersPiece : moveToMake
				.getPiecesThatWillBeCaptured()) {
			capturePiece(checkersPiece);
			this.lastTurnThatAPieceWasCaptured = this.turnCount;
		}

		this.undoMoveStack.push(undoableMoveToRevertThisMove);
		this.turnCount++;
		notifyObserversThatTheBoardChanged();
		notifyObserversThatATurnHasEnded();
		return this.piecesOnBoard.contains(pieceToMove);

	}

	private static boolean shouldPieceBecomeKing(PlayerToken player,
			int rowToMoveTo) {
		if (rowToMoveTo == 0 && player.equals(PlayerToken.BOTTOM_PLAYER)) {
			return true;
		} else if (rowToMoveTo == 7 && player.equals(PlayerToken.TOP_PLAYER)) {
			return true;
		}
		return false;
	}

	public UndoableMove undoLastMove() {
		if (!this.undoMoveStack.isEmpty()) {
			this.turnCount--;
			UndoableMove lastMoveMade = this.undoMoveStack.pop();

			PossibleMove moveToUndo = lastMoveMade.getMoveToUndo();
			CheckersPieceModel pieceToMoveBack = moveToUndo.getPieceToMove();
			pieceToMoveBack.setRow(lastMoveMade.getOldPieceRowLocation());
			pieceToMoveBack.setColumn(lastMoveMade.getOldPieceColumnLocation());

			for (CheckersPieceModel capturedPiece : moveToUndo
					.getPiecesThatWillBeCaptured()) {
				capturedPiece.undoCapturePiece();
				this.piecesOnBoard.add(capturedPiece);
			}

			if (lastMoveMade.pieceWasKingedDuringMove()) {
				pieceToMoveBack.undoKingMe();
			}
			notifyObserversThatTheBoardChanged();
			return lastMoveMade;
		}
		return null;
	}

	public static List<CheckersPieceModel> emulateMovingAPieceAndReturnCopyOfPiecesOnBoard(
			List<CheckersPieceModel> allPiecesOnBoard,
			PossibleMove moveToEmulate) {
		List<CheckersPieceModel> copyOfPiecesOnBoardWithMoveEmulated = new ArrayList<CheckersPieceModel>();
		List<CheckersPieceModel> piecesThatWillBeCapturedBecauseOfThisMove = new ArrayList<CheckersPieceModel>();
		for (CheckersPieceModel checkersPieceModel : allPiecesOnBoard) {
			if (checkersPieceModel.equals(moveToEmulate.getPieceToMove())) {
				PlayerToken playerToken = moveToEmulate.getPieceToMove()
						.getPlayerToken();
				CheckersPieceModel movedCheckerPieceCopy = new CheckersPieceModel(
						moveToEmulate.getNewRowLocation(),
						moveToEmulate.getNewColumnLocation(), playerToken);
				if (shouldPieceBecomeKing(playerToken,
						moveToEmulate.getNewRowLocation())
						|| moveToEmulate.getPieceToMove().isKing()) {
					movedCheckerPieceCopy.kingMe();
				}
				piecesThatWillBeCapturedBecauseOfThisMove.addAll(moveToEmulate
						.getPiecesThatWillBeCaptured());
				copyOfPiecesOnBoardWithMoveEmulated.add(movedCheckerPieceCopy);
			}
		}
		for (CheckersPieceModel checkersPieceModel : allPiecesOnBoard) {
			if (!checkersPieceModel.equals(moveToEmulate.getPieceToMove())
					&& !piecesThatWillBeCapturedBecauseOfThisMove
							.contains(checkersPieceModel)) {
				CheckersPieceModel copyOfCheckersPieceModel_MayBeUneccessary = CheckersPieceModel
						.copy(checkersPieceModel);
				copyOfPiecesOnBoardWithMoveEmulated
						.add(copyOfCheckersPieceModel_MayBeUneccessary);
			}
		}

		return copyOfPiecesOnBoardWithMoveEmulated;
	}

	private void notifyObserversThatTheBoardChanged() {
		for (CheckersBoardObserverInterface observer : this.observers) {
			observer.boardChanged();
		}
	}

	private void notifyObserversThatATurnHasEnded() {
		for (CheckersBoardObserverInterface observer : this.observers) {
			observer.turnHasEnded();
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

	public int getNotation(CheckersPieceModel piece) {

		return this.spaceNotation[piece.getRow()][piece.getColumn()];
	}

	public int getNotation(int row, int column) {
		return this.spaceNotation[row][column];
	}

	public CheckersPieceModel getPieceFromNotation(int spaceNumber) {
		for (int column = 0; column < 8; column++) {
			for (int row = 0; row < 8; row++) {
				if (spaceNumber == this.spaceNotation[column][row]) {
					List<CheckersPieceModel> listOfPieces = this
							.getPiecesOnBoard();
					for (int i = 0; i < listOfPieces.size(); i++) {
						if ((listOfPieces.get(i).getColumn() == column)
								&& (listOfPieces.get(i).getRow() == row))
							return listOfPieces.get(i);
					}
				}
			}
		}
		return null;
	}

	public int getLastTurnThatAPieceWasCaptured() {
		return this.lastTurnThatAPieceWasCaptured;
	}

	public int getTurnCount() {
		return this.turnCount;
	}

}
