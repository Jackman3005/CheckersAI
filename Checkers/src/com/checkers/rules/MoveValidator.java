package com.checkers.rules;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.checkers.model.CheckersPieceModel;
import com.checkers.model.PlayerToken;
import com.checkers.model.PossibleMove;

public class MoveValidator {

	public static PossibleMove validateAndReturnMove_InvalidMoveReturnsNull(
			List<CheckersPieceModel> allPiecesOnBoard,
			CheckersPieceModel pieceToMove, int newRowPosition,
			int newColumnPosition) {

		ArrayList<PossibleMove> allValidMoves = getAllValidMovesForASinglePiece(
				allPiecesOnBoard, pieceToMove);
		for (PossibleMove possibleMove : allValidMoves) {
			if (possibleMove.getNewColumnLocation() == newColumnPosition
					&& possibleMove.getNewRowLocation() == newRowPosition) {
				return possibleMove;
			}
		}
		return null;
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

		listOfValidMoves.addAll(getValidMovesTwoLocationsAway_JumpMoves(
				allPiecesOnBoard, checkersPiece, Integer.MIN_VALUE,
				Integer.MIN_VALUE));
		if (listOfValidMoves.size() == 0) {
			listOfValidMoves
					.addAll(getValidMovesWithinOneLocationAway_NonJumpMoves(
							allPiecesOnBoard, checkersPiece));
		}

		return listOfValidMoves;
	}

	private static List<PossibleMove> getValidMovesTwoLocationsAway_JumpMoves(
			List<CheckersPieceModel> allPiecesOnBoard,
			CheckersPieceModel checkersPiece, int rowPositionFromPreviousJump,
			int columnPositionFromPreviousJump) {
		ArrayList<PossibleMove> listOfValidMoves = new ArrayList<PossibleMove>();
		boolean northIsForward = checkersPiece.getPlayerToken().equals(
				PlayerToken.PLAYER);

		int northRow = checkersPiece.getRow() - 1;
		int southRow = checkersPiece.getRow() + 1;
		int westColumn = checkersPiece.getColumn() - 1;
		int eastColumn = checkersPiece.getColumn() + 1;

		if (northIsForward || checkersPiece.isKing()) {
			if (northRow - 1 != rowPositionFromPreviousJump
					&& westColumn - 1 != columnPositionFromPreviousJump) {
				CheckersPieceModel pieceAtNorthWestLocation = getPieceAtLocation(
						allPiecesOnBoard, northRow, westColumn);
				if (pieceAtNorthWestLocation != null
						&& !pieceAtNorthWestLocation.getPlayerToken().equals(
								checkersPiece.getPlayerToken())) {
					PossibleMove northWestJumpMove = checkForAndReturnValidMoveInAdjustmentDirection(
							allPiecesOnBoard, checkersPiece, -2, -2);
					if (northWestJumpMove != null) {
						listOfValidMoves
								.addAll(checkForMultipleJumpMovesAndReturn(
										allPiecesOnBoard, checkersPiece,
										pieceAtNorthWestLocation,
										northWestJumpMove, -2, -2));
					}
				}
			}
			if (northRow - 1 != rowPositionFromPreviousJump
					&& eastColumn + 1 != columnPositionFromPreviousJump) {
				CheckersPieceModel pieceAtNorthEastLocation = getPieceAtLocation(
						allPiecesOnBoard, northRow, eastColumn);
				if (pieceAtNorthEastLocation != null
						&& !pieceAtNorthEastLocation.getPlayerToken().equals(
								checkersPiece.getPlayerToken())) {
					PossibleMove northEastJumpMove = checkForAndReturnValidMoveInAdjustmentDirection(
							allPiecesOnBoard, checkersPiece, -2, 2);
					if (northEastJumpMove != null) {
						listOfValidMoves
								.addAll(checkForMultipleJumpMovesAndReturn(
										allPiecesOnBoard, checkersPiece,
										pieceAtNorthEastLocation,
										northEastJumpMove, -2, 2));
					}
				}
			}
		}
		if (!northIsForward || checkersPiece.isKing()) {
			if (southRow + 1 != rowPositionFromPreviousJump
					&& westColumn - 1 != columnPositionFromPreviousJump) {
				CheckersPieceModel pieceAtSouthWestLocation = getPieceAtLocation(
						allPiecesOnBoard, southRow, westColumn);
				if (pieceAtSouthWestLocation != null
						&& !pieceAtSouthWestLocation.getPlayerToken().equals(
								checkersPiece.getPlayerToken())) {
					PossibleMove southWestJumpMove = checkForAndReturnValidMoveInAdjustmentDirection(
							allPiecesOnBoard, checkersPiece, 2, -2);
					if (southWestJumpMove != null) {
						listOfValidMoves
								.addAll(checkForMultipleJumpMovesAndReturn(
										allPiecesOnBoard, checkersPiece,
										pieceAtSouthWestLocation,
										southWestJumpMove, 2, -2));
					}
				}
			}
			if (southRow + 1 != rowPositionFromPreviousJump
					&& eastColumn + 1 != columnPositionFromPreviousJump) {
				CheckersPieceModel pieceAtSouthEastLocation = getPieceAtLocation(
						allPiecesOnBoard, southRow, eastColumn);
				if (pieceAtSouthEastLocation != null
						&& !pieceAtSouthEastLocation.getPlayerToken().equals(
								checkersPiece.getPlayerToken())) {
					PossibleMove southEastJumpMove = checkForAndReturnValidMoveInAdjustmentDirection(
							allPiecesOnBoard, checkersPiece, 2, 2);
					if (southEastJumpMove != null) {
						listOfValidMoves
								.addAll(checkForMultipleJumpMovesAndReturn(
										allPiecesOnBoard, checkersPiece,
										pieceAtSouthEastLocation,
										southEastJumpMove, 2, 2));
					}
				}
			}
		}
		return listOfValidMoves;
	}

	private static List<PossibleMove> checkForMultipleJumpMovesAndReturn(
			List<CheckersPieceModel> allPiecesOnBoard,
			CheckersPieceModel checkersPiece,
			CheckersPieceModel pieceBeingJumped_AKACaptured,
			PossibleMove previousJumpMove, int rowAdjustment,
			int columnAdjustment) {

		List<PossibleMove> listOfValidJumpMoves = new ArrayList<PossibleMove>();

		CheckersPieceModel tempCheckersPiece = new CheckersPieceModel(
				checkersPiece.getRow() + rowAdjustment,
				checkersPiece.getColumn() + columnAdjustment,
				checkersPiece.getPlayerToken());
		if (checkersPiece.isKing()) {
			tempCheckersPiece.kingMe();
		}
		List<PossibleMove> additionalJumps = getValidMovesTwoLocationsAway_JumpMoves(
				allPiecesOnBoard, tempCheckersPiece, checkersPiece.getRow(),
				checkersPiece.getColumn());
		if (additionalJumps.size() > 0)
			for (PossibleMove possibleMove : additionalJumps) {
				PossibleMove multiJumpMove = new PossibleMove(checkersPiece,
						possibleMove.getNewRowLocation(),
						possibleMove.getNewColumnLocation());
				multiJumpMove.addIntermediateLocation(
						previousJumpMove.getNewRowLocation(),
						previousJumpMove.getNewColumnLocation());
				multiJumpMove
						.addPieceThatWillBeCaptured(pieceBeingJumped_AKACaptured);
				List<CheckersPieceModel> piecesThatHaveBeenCaptured = possibleMove
						.getPiecesThatWillBeCaptured();
				if (piecesThatHaveBeenCaptured
						.contains(pieceBeingJumped_AKACaptured))
					continue;
				for (CheckersPieceModel jumpedPieces : piecesThatHaveBeenCaptured) {
					multiJumpMove.addPieceThatWillBeCaptured(jumpedPieces);
				}
				for (Point intermediateLocation : possibleMove
						.getIntermediateLocations()) {
					multiJumpMove.addIntermediateLocation(
							intermediateLocation.y, intermediateLocation.x);
				}
				listOfValidJumpMoves.add(multiJumpMove);
			}
		else {
			previousJumpMove
					.addPieceThatWillBeCaptured(pieceBeingJumped_AKACaptured);
			listOfValidJumpMoves.add(previousJumpMove);
		}
		return listOfValidJumpMoves;
	}

	private static List<PossibleMove> getValidMovesWithinOneLocationAway_NonJumpMoves(
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
