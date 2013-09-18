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

		boolean isMovingToABlackTile = (newRowPosition + newColumnPosition) % 2 != 0;

		int currentRowPosition = pieceToMove.getRow();
		int currentColumnPosition = pieceToMove.getColumn();

		int rowMovement = Math.abs(newRowPosition - currentRowPosition);
		int columnMovement = Math
				.abs(newColumnPosition - currentColumnPosition);

		boolean isMovingOnlyOneSpaceAway_ThisIsAnUnfinishedRule = columnMovement <= 1
				&& rowMovement <= 1;

		return isMovingToABlackTile
				&& isMovingOnlyOneSpaceAway_ThisIsAnUnfinishedRule;
	}

	public static List<PossibleMove> getAllValidMovesForPlayer(
			List<CheckersPieceModel> allPiecesOnBoard, PlayerToken player) {
		List<CheckersPieceModel> piecesToCheck = getAllPiecesThatThePlayerControls(
				allPiecesOnBoard, player);
		ArrayList<PossibleMove> allValidMoves = new ArrayList<PossibleMove>();
		for (CheckersPieceModel checkersPiece : piecesToCheck) {
			allValidMoves.addAll(getAllValidMovesForASinglePiece(allPiecesOnBoard,
					player, checkersPiece));

		}
		return allValidMoves;
	}

	public static ArrayList<PossibleMove> getAllValidMovesForASinglePiece(
			List<CheckersPieceModel> allPiecesOnBoard, PlayerToken player,
			CheckersPieceModel checkersPiece) {
		int row = checkersPiece.getRow();
		int column = checkersPiece.getColumn();

		boolean northIsForward = player.equals(PlayerToken.PLAYER);
		ArrayList<PossibleMove> listOfValidMoves = new ArrayList<PossibleMove>();

		if (northIsForward || checkersPiece.isKing()) {
			int northWestRow = row - 1;
			int northWestColumn = column - 1;
			int northEastRow = row - 1;
			int northEastColumn = column + 1;

			CheckersPieceModel northWestPiece = getPieceAtLocation(
					allPiecesOnBoard, northWestRow, northWestColumn);
			CheckersPieceModel northEastPiece = getPieceAtLocation(
					allPiecesOnBoard, northEastRow, northEastColumn);
			boolean northEastLocationIsEmpty = northEastPiece == null;
			boolean northWestLocationIsEmpty = northWestPiece == null;

			if (northWestLocationIsEmpty) {
				listOfValidMoves.add(new PossibleMove(checkersPiece,
						northWestRow, northWestColumn));
			}
			if (northEastLocationIsEmpty) {
				listOfValidMoves.add(new PossibleMove(checkersPiece,
						northEastRow, northEastColumn));
			}
		}
		if (!northIsForward || checkersPiece.isKing()) {
			int southWestRow = row - 1;
			int southWestColumn = column - 1;
			int southEastRow = row - 1;
			int southEastColumn = column + 1;

			CheckersPieceModel southWestPiece = getPieceAtLocation(
					allPiecesOnBoard, southWestRow, southWestColumn);
			CheckersPieceModel southEastPiece = getPieceAtLocation(
					allPiecesOnBoard, southEastRow, southEastColumn);
			boolean northEastLocationIsEmpty = southEastPiece == null;
			boolean northWestLocationIsEmpty = southWestPiece == null;

			if (northWestLocationIsEmpty) {
				listOfValidMoves.add(new PossibleMove(checkersPiece,
						southWestRow, southWestColumn));
			}
			if (northEastLocationIsEmpty) {
				listOfValidMoves.add(new PossibleMove(checkersPiece,
						southEastRow, southEastColumn));
			}
		}
		return listOfValidMoves;
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
