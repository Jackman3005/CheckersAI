package com.checkers.rules;

import java.util.ArrayList;
import java.util.List;

import com.checkers.model.CheckersPieceModel;
import com.checkers.model.PlayerToken;
import com.checkers.model.PossibleMove;

public class MoveValidator {

	public static boolean isMoveValid(
			List<CheckersPieceModel> allPiecesOnBoard,
			CheckersPieceModel pieceToMove, int newRowPosition,
			int newColumnPosition) {

		ArrayList<PossibleMove> allValidMoves = getAllValidMovesForASinglePiece(
				allPiecesOnBoard, pieceToMove);
		for (PossibleMove possibleMove : allValidMoves) {
			if (possibleMove.getEndingColumnLocation() == newColumnPosition
					&& possibleMove.getEndingRowLocation() == newRowPosition) {
				return true;
			}
		}
		return false;
	}

	public static List<PossibleMove> getAllValidMovesForPlayer(
			List<CheckersPieceModel> allPiecesOnBoard, PlayerToken player) {
		List<CheckersPieceModel> piecesToCheck = getAllPiecesThatThePlayerControls(
				allPiecesOnBoard, player);
		ArrayList<PossibleMove> allValidMoves = new ArrayList<PossibleMove>();
		for (CheckersPieceModel checkersPiece : piecesToCheck) {
			allValidMoves.addAll(getAllValidMovesForASinglePiece(
					allPiecesOnBoard, checkersPiece));

		}
		return allValidMoves;
	}

	public static ArrayList<PossibleMove> getAllValidMovesForASinglePiece(
			List<CheckersPieceModel> allPiecesOnBoard,
			CheckersPieceModel checkersPiece) {
		ArrayList<PossibleMove> listOfValidMoves = new ArrayList<PossibleMove>();

		listOfValidMoves.addAll(getValidMovesWithinRadius(allPiecesOnBoard,
				checkersPiece));

		return listOfValidMoves;
	}

	private static List<PossibleMove> getValidMovesWithinRadius(
			List<CheckersPieceModel> allPiecesOnBoard,
			CheckersPieceModel checkersPiece) {

		ArrayList<PossibleMove> listOfValidMoves = new ArrayList<PossibleMove>();
		boolean northIsForward = checkersPiece.getPlayerToken().equals(
				PlayerToken.PLAYER);
		if (northIsForward || checkersPiece.isKing()) {
			PossibleMove northWestMove = checkForAndReturnValidMoveInAdjustmentDirection(
					allPiecesOnBoard, checkersPiece, -1, -1);
			if (northWestMove != null)
				listOfValidMoves.add(northWestMove);

			PossibleMove northEastMove = checkForAndReturnValidMoveInAdjustmentDirection(
					allPiecesOnBoard, checkersPiece, -1, 1);
			if (northEastMove != null)
				listOfValidMoves.add(northEastMove);
		}
		if (!northIsForward || checkersPiece.isKing()) {
			PossibleMove southWestMove = checkForAndReturnValidMoveInAdjustmentDirection(
					allPiecesOnBoard, checkersPiece, 1, -1);
			if (southWestMove != null)
				listOfValidMoves.add(southWestMove);

			PossibleMove southEastMove = checkForAndReturnValidMoveInAdjustmentDirection(
					allPiecesOnBoard, checkersPiece, 1, 1);
			if (southEastMove != null)
				listOfValidMoves.add(southEastMove);
		}
		return listOfValidMoves;
	}

	private static PossibleMove checkForAndReturnValidMoveInAdjustmentDirection(
			List<CheckersPieceModel> allPiecesOnBoard,
			CheckersPieceModel checkersPiece, int rowAdjustment,
			int columnAdjustment) {
		int newRowLocation = checkersPiece.getRow() + rowAdjustment;
		int newColumnLocation = checkersPiece.getColumn() + columnAdjustment;

		boolean moveIsOnBoard = newRowLocation >= 0 && newRowLocation <= 7
				&& newColumnLocation >= 0 && newColumnLocation <= 7;

		if (moveIsOnBoard
				&& locationIsUnoccupied(allPiecesOnBoard, checkersPiece,
						newRowLocation, newColumnLocation)) {
			return new PossibleMove(checkersPiece, newRowLocation,
					newColumnLocation);
		}
		return null;
	}

	private static boolean locationIsUnoccupied(
			List<CheckersPieceModel> allPiecesOnBoard,
			CheckersPieceModel checkersPiece, int row, int column) {

		CheckersPieceModel northWestPiece = getPieceAtLocation(
				allPiecesOnBoard, row, column);
		boolean locationIsEmpty = northWestPiece == null;
		return locationIsEmpty;
	}

	private static CheckersPieceModel getPieceAtLocation(
			List<CheckersPieceModel> allPiecesOnBoard, int row, int column) {

		for (CheckersPieceModel checkersPieceModel : allPiecesOnBoard) {
			if (checkersPieceModel.getRow() == row) {
				if (checkersPieceModel.getColumn() == column) {
					return checkersPieceModel;
				}
			}
		}
		return null;
	}

	private static List<CheckersPieceModel> getAllPiecesThatThePlayerControls(
			List<CheckersPieceModel> allPiecesOnBoard, PlayerToken player) {
		List<CheckersPieceModel> piecesThatThePlayerControls = new ArrayList<CheckersPieceModel>();
		for (CheckersPieceModel checkersPiece : allPiecesOnBoard) {
			if (checkersPiece.getPlayerToken().equals(player)) {
				piecesThatThePlayerControls.add(checkersPiece);
			}
		}
		return piecesThatThePlayerControls;
	}
}
