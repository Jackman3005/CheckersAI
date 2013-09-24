package com.checkers.rules;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import com.checkers.display.CheckersBoardPanel;
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
		boolean playerHasAJumpMoveAvailable = false;
		for (CheckersPieceModel checkersPiece : piecesToCheck) {
			ArrayList<PossibleMove> allValidMovesForASinglePiece = getAllValidMovesForASinglePiece(
					allPiecesOnBoard, checkersPiece);
			allValidMoves.addAll(allValidMovesForASinglePiece);
			for (PossibleMove possibleMove : allValidMovesForASinglePiece) {
				if (possibleMove.isThisMoveAJump()) {
					playerHasAJumpMoveAvailable = true;
				}
			}
		}
		if (playerHasAJumpMoveAvailable) {
			for (PossibleMove possibleMove : new ArrayList<PossibleMove>(
					allValidMoves)) {
				if (!possibleMove.isThisMoveAJump()) {
					allValidMoves.remove(possibleMove);
				}
			}
		}
		return allValidMoves;
	}

	public static ArrayList<PossibleMove> getAllValidMovesForASinglePiece(
			List<CheckersPieceModel> allPiecesOnBoard,
			CheckersPieceModel checkersPiece) {
		ArrayList<PossibleMove> listOfValidMoves = new ArrayList<PossibleMove>();

		// piece is removed from "allPiecesOnBoard" so that it cannot get in the
		// way of itself, This allows for a king to do a full circle capturing
		// four pieces and returning where it started
		allPiecesOnBoard.remove(checkersPiece);
		listOfValidMoves.addAll(getValidMovesTwoLocationsAway_JumpMoves(
				allPiecesOnBoard, checkersPiece, Integer.MIN_VALUE,
				Integer.MIN_VALUE, new ArrayList<CheckersPieceModel>()));
		allPiecesOnBoard.add(checkersPiece);
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
			int columnPositionFromPreviousJump,
			List<CheckersPieceModel> piecesAlreadyCaptured) {
		ArrayList<PossibleMove> listOfValidMoves = new ArrayList<PossibleMove>();
		boolean northIsForward = checkersPiece.getPlayerToken().equals(
				PlayerToken.PLAYER);

		int northRow = checkersPiece.getRow() - 1;
		int southRow = checkersPiece.getRow() + 1;
		int westColumn = checkersPiece.getColumn() - 1;
		int eastColumn = checkersPiece.getColumn() + 1;

		int northJumpRow = northRow - 1;
		int southJumpRow = southRow + 1;
		int westJumpColumn = westColumn - 1;
		int eastJumpColumn = eastColumn + 1;

		if (northIsForward || checkersPiece.isKing()) {
			if (northJumpRow != rowPositionFromPreviousJump
					|| westJumpColumn != columnPositionFromPreviousJump) {
				CheckersPieceModel pieceAtNorthWestLocation = getPieceAtLocation(
						allPiecesOnBoard, northRow, westColumn);
				if (pieceAtNorthWestLocation != null
						&& !pieceAtNorthWestLocation.getPlayerToken().equals(
								checkersPiece.getPlayerToken())) {
					if (!piecesAlreadyCaptured
							.contains(pieceAtNorthWestLocation)) {
						PossibleMove northWestJumpMove = checkForAndReturnValidMoveInAdjustmentDirection(
								allPiecesOnBoard, checkersPiece, -2, -2);
						if (northWestJumpMove != null) {
							piecesAlreadyCaptured.add(pieceAtNorthWestLocation);
							listOfValidMoves
									.addAll(checkForMultipleJumpMovesAndReturn(
											allPiecesOnBoard, checkersPiece,
											pieceAtNorthWestLocation,
											northWestJumpMove, -2, -2,
											piecesAlreadyCaptured));
						}
					}
				}
			}
			if (northJumpRow != rowPositionFromPreviousJump
					|| eastJumpColumn != columnPositionFromPreviousJump) {
				CheckersPieceModel pieceAtNorthEastLocation = getPieceAtLocation(
						allPiecesOnBoard, northRow, eastColumn);
				if (pieceAtNorthEastLocation != null
						&& !pieceAtNorthEastLocation.getPlayerToken().equals(
								checkersPiece.getPlayerToken())) {
					if (!piecesAlreadyCaptured
							.contains(pieceAtNorthEastLocation)) {
						PossibleMove northEastJumpMove = checkForAndReturnValidMoveInAdjustmentDirection(
								allPiecesOnBoard, checkersPiece, -2, 2);
						if (northEastJumpMove != null) {
							piecesAlreadyCaptured.add(pieceAtNorthEastLocation);
							listOfValidMoves
									.addAll(checkForMultipleJumpMovesAndReturn(
											allPiecesOnBoard, checkersPiece,
											pieceAtNorthEastLocation,
											northEastJumpMove, -2, 2,
											piecesAlreadyCaptured));
						}
					}
				}
			}
		}
		if (!northIsForward || checkersPiece.isKing()) {
			if (southJumpRow != rowPositionFromPreviousJump
					|| westJumpColumn != columnPositionFromPreviousJump) {
				CheckersPieceModel pieceAtSouthWestLocation = getPieceAtLocation(
						allPiecesOnBoard, southRow, westColumn);
				if (pieceAtSouthWestLocation != null
						&& !pieceAtSouthWestLocation.getPlayerToken().equals(
								checkersPiece.getPlayerToken())) {
					if (!piecesAlreadyCaptured
							.contains(pieceAtSouthWestLocation)) {
						PossibleMove southWestJumpMove = checkForAndReturnValidMoveInAdjustmentDirection(
								allPiecesOnBoard, checkersPiece, 2, -2);
						if (southWestJumpMove != null) {
							piecesAlreadyCaptured.add(pieceAtSouthWestLocation);
							listOfValidMoves
									.addAll(checkForMultipleJumpMovesAndReturn(
											allPiecesOnBoard, checkersPiece,
											pieceAtSouthWestLocation,
											southWestJumpMove, 2, -2,
											piecesAlreadyCaptured));
						}
					}
				}
			}

			if (southJumpRow != rowPositionFromPreviousJump
					|| eastJumpColumn != columnPositionFromPreviousJump) {
				CheckersPieceModel pieceAtSouthEastLocation = getPieceAtLocation(
						allPiecesOnBoard, southRow, eastColumn);
				if (pieceAtSouthEastLocation != null
						&& !pieceAtSouthEastLocation.getPlayerToken().equals(
								checkersPiece.getPlayerToken())) {
					if (!piecesAlreadyCaptured
							.contains(pieceAtSouthEastLocation)) {
						PossibleMove southEastJumpMove = checkForAndReturnValidMoveInAdjustmentDirection(
								allPiecesOnBoard, checkersPiece, 2, 2);
						if (southEastJumpMove != null) {
							piecesAlreadyCaptured.add(pieceAtSouthEastLocation);
							listOfValidMoves
									.addAll(checkForMultipleJumpMovesAndReturn(
											allPiecesOnBoard, checkersPiece,
											pieceAtSouthEastLocation,
											southEastJumpMove, 2, 2,
											piecesAlreadyCaptured));
						}
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
			int columnAdjustment, List<CheckersPieceModel> piecesAlreadyCaptured) {

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
				checkersPiece.getColumn(), piecesAlreadyCaptured);
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
						.contains(pieceBeingJumped_AKACaptured)) {
					continue;
				}
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

		boolean moveIsOnBoard = newRowLocation >= 0
				&& newRowLocation < CheckersBoardPanel.BOARD_SIZE
				&& newColumnLocation >= 0
				&& newColumnLocation < CheckersBoardPanel.BOARD_SIZE;

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

		CheckersPieceModel pieceAtLocation = getPieceAtLocation(
				allPiecesOnBoard, row, column);
		boolean locationIsEmpty = pieceAtLocation == null;
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
