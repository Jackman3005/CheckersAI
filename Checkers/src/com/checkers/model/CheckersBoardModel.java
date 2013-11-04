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
	private final GameReferee gameReferee;
	private boolean shouldShowNotation_ShouldBeOnADifferentModelButImLazy = false;

	public CheckersBoardModel() {
		initializeBoard();
		initializeNotation();
		this.observers = new ArrayList<CheckersBoardObserverInterface>();
		this.undoMoveStack = new Stack<UndoableMove>();
		this.turnCount = 0;
		this.lastTurnThatAPieceWasCaptured = 0;
		this.gameReferee = new GameReferee(this);
	}

	public void setShouldShowNotation(boolean shouldShowNotation) {
		this.shouldShowNotation_ShouldBeOnADifferentModelButImLazy = shouldShowNotation;
		notifyObserversThatTheBoardChanged();
	}

	public boolean shouldShowNotationOnBoard() {
		return this.shouldShowNotation_ShouldBeOnADifferentModelButImLazy;
	}

	private void initializeBoard() {
		this.piecesOnBoard = new ArrayList<CheckersPieceModel>();
		int pieceId = 0;
		for (int column = 0; column < 8; column++) {
			for (int row = 0; row < 3; row++) {
				if ((column + row) % 2 == 1) {
					this.piecesOnBoard.add(new CheckersPieceModel(pieceId++,
							row, column, PlayerToken.TOP_PLAYER));
				}
			}
			for (int row = 5; row < 8; row++) {
				if ((column + row) % 2 == 1) {
					this.piecesOnBoard.add(new CheckersPieceModel(pieceId++,
							row, column, PlayerToken.BOTTOM_PLAYER));
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

	public void resetBoard() {
		// This method still blows apparantly.
		while (undoLastMove() != null) {
		}
		this.turnCount = 0;
		this.lastTurnThatAPieceWasCaptured = 0;
		notifyObserversThatTheBoardChanged();
	}

	public boolean actuallyMovePiece(PossibleMove moveToMake) {
		if (moveToMake == null)
			return false;
		CheckersPieceModel pieceToMove = moveToMake.getPieceToMove();
		printOutMoveNotationToConsole(moveToMake);

		int rowToMoveTo = moveToMake.getNewRowLocation();
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
		for (CheckersPieceModel checkersPiece : moveToMake
				.getPiecesThatWillBeCaptured()) {
			capturePiece(checkersPiece);
			this.lastTurnThatAPieceWasCaptured = this.turnCount;
		}

		this.undoMoveStack.push(undoableMoveToRevertThisMove);
		this.turnCount++;
		notifyObserversThatTheBoardChanged();
		notifyObserversThatATurnHasEnded();

		this.gameReferee.howWeDoing(this.undoMoveStack, moveToMake);

		return this.piecesOnBoard.contains(pieceToMove);
	}

	private void printOutMoveNotationToConsole(PossibleMove moveToMake) {
		CheckersPieceModel pieceToMove = moveToMake.getPieceToMove();
		String moveOrJumpNotation;
		if (moveToMake.getPiecesThatWillBeCaptured().isEmpty()) {
			moveOrJumpNotation = "-";
		} else {
			moveOrJumpNotation = "x";
		}

		System.out.print(pieceToMove.getPlayerToken() + ":\t");
		System.out.print(getNotation(pieceToMove) + moveOrJumpNotation);

		List<Point> intermediateLocations = moveToMake
				.getIntermediateLocations();

		for (Point point : intermediateLocations) {
			System.out.print(this.spaceNotation[point.y][point.x]
					+ moveOrJumpNotation);
		}
		System.out.println(getNotation(moveToMake.getNewRowLocation(),
				moveToMake.getNewColumnLocation()));
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
						moveToEmulate.getPieceToMove().getId(),
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
				copyOfPiecesOnBoardWithMoveEmulated.add(checkersPieceModel);
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

		return getNotation(piece.getRow(), piece.getColumn());
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

	public List<PossibleMove> getAllMovesMade() {
		ArrayList<PossibleMove> listToReturn = new ArrayList<PossibleMove>();
		for (int i = 0; i < this.undoMoveStack.size(); i++) {
			listToReturn.add(this.undoMoveStack.get(i).getMoveToUndo());
		}
		return listToReturn;
	}

}
