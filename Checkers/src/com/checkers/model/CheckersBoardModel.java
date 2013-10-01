package com.checkers.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CheckersBoardModel {

	private ArrayList<CheckersPieceModel> piecesOnBoard;
	private final List<CheckersBoardObserverInterface> observers;
	private final Stack<UndoableMove> undoMoveStack;
	private int[][] spaceNotation; 

	public CheckersBoardModel() {
		initializeBoard();
		initializeNotation();
		this.observers = new ArrayList<CheckersBoardObserverInterface>();
		this.undoMoveStack = new Stack<UndoableMove>();
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

	private void initializeNotation(){
		spaceNotation=new int[8][8];
		int spaceNumber=1;
		for (int column=0; column<8;column++){
			for (int row =0; row<8; row++){
				spaceNotation[column][row]=0;
				if((column+row)%2==1){
					spaceNotation[column][row]=spaceNumber;
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
		System.out.print(getNotation(pieceToMove)+"-");
		int rowToMoveTo = moveToMake.getNewRowLocation();

		boolean pieceShouldBecomeKing = false;
		if (rowToMoveTo == 0
				&& pieceToMove.getPlayerToken().equals(PlayerToken.PLAYER)) {
			pieceShouldBecomeKing = true;
		} else if (rowToMoveTo == 7
				&& pieceToMove.getPlayerToken().equals(PlayerToken.OPPONENT)) {
			pieceShouldBecomeKing = true;
		}
		UndoableMove undoableMoveToRevertThisMove = new UndoableMove(
				moveToMake, pieceToMove.getRow(), pieceToMove.getColumn(),
				pieceShouldBecomeKing);

		if (pieceShouldBecomeKing) {
			pieceToMove.kingMe();
		}
		pieceToMove.setRow(rowToMoveTo);
		pieceToMove.setColumn(moveToMake.getNewColumnLocation());
		System.out.println(getNotation(pieceToMove)+", ");
		for (CheckersPieceModel checkersPiece : moveToMake
				.getPiecesThatWillBeCaptured()) {
			capturePiece(checkersPiece);
		}

		this.undoMoveStack.push(undoableMoveToRevertThisMove);
		notifyObserversThatTheBoardChanged();
		return this.piecesOnBoard.contains(pieceToMove);
	}

	public UndoableMove undoLastMove() {
		if (!this.undoMoveStack.isEmpty()) {
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
		for (CheckersPieceModel checkersPieceModel : allPiecesOnBoard) {
			if (checkersPieceModel.equals(moveToEmulate.getPieceToMove())) {
				CheckersPieceModel movedCheckerPieceCopy = new CheckersPieceModel(
						moveToEmulate.getNewRowLocation(),
						moveToEmulate.getNewColumnLocation(), moveToEmulate
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
	
	public int getNotation(CheckersPieceModel piece){
				
		return spaceNotation[piece.getRow()][piece.getColumn()];
	}
	
	public CheckersPieceModel getPieceFromNotation(int spaceNumber){
		for (int column=0; column<8;column++){
			for (int row =0; row<8; row++){
				if(spaceNumber==spaceNotation[column][row]){
					List <CheckersPieceModel> listOfPieces =this.getPiecesOnBoard();
					for (int i=0; i<listOfPieces.size(); i++){
						if((listOfPieces.get(i).getColumn()==column)&&
								(listOfPieces.get(i).getRow()==row))
							return listOfPieces.get(i);
					}
				}					
			}
		}
		return null;
	}
	
	
}
