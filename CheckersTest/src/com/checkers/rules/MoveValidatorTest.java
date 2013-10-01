package com.checkers.rules;

import java.awt.Point;
import java.util.ArrayList;

import org.junit.Test;

import com.TestUtils.AssertHelper;
import com.checkers.model.CheckersPieceModel;
import com.checkers.model.PlayerToken;
import com.checkers.model.PossibleMove;

public class MoveValidatorTest extends AssertHelper {

	@Test
	public void testAllValidMovesForSinglePiece_WillOnlyReturnJumpMovesIfTheyAreAvailable() {

		ArrayList<CheckersPieceModel> allPiecesOnBoard = new ArrayList<CheckersPieceModel>();

		CheckersPieceModel pieceToCheck = new CheckersPieceModel(2, 2,
				PlayerToken.TOP_PLAYER);
		CheckersPieceModel pieceToCapture = new CheckersPieceModel(3, 3,
				PlayerToken.BOTTOM_PLAYER);

		allPiecesOnBoard.add(pieceToCheck);
		allPiecesOnBoard.add(pieceToCapture);
		ArrayList<CheckersPieceModel> expectedPiecesToCapture = new ArrayList<CheckersPieceModel>();
		expectedPiecesToCapture.add(pieceToCapture);

		ArrayList<PossibleMove> validMoves = MoveValidator
				.getAllValidMovesForASinglePiece(allPiecesOnBoard, pieceToCheck);

		assertEquals(1, validMoves.size());
		checkPossibleMove(validMoves.get(0), pieceToCheck, 4, 4,
				new ArrayList<Point>(), expectedPiecesToCapture);
	}

	@Test
	public void testAllValidMovesForSinglePiece_KingsCanCaptureFourPiecesAndEndUpAtTheirStartingLocation()
			throws Exception {
		ArrayList<CheckersPieceModel> allPiecesOnBoard = new ArrayList<CheckersPieceModel>();

		CheckersPieceModel pieceToCheck = new CheckersPieceModel(4, 3,
				PlayerToken.BOTTOM_PLAYER);
		pieceToCheck.kingMe();
		CheckersPieceModel pieceToCapture1 = new CheckersPieceModel(3, 2,
				PlayerToken.TOP_PLAYER);
		CheckersPieceModel pieceToCapture2 = new CheckersPieceModel(1, 2,
				PlayerToken.TOP_PLAYER);
		CheckersPieceModel pieceToCapture3 = new CheckersPieceModel(3, 4,
				PlayerToken.TOP_PLAYER);
		CheckersPieceModel pieceToCapture4 = new CheckersPieceModel(1, 4,
				PlayerToken.TOP_PLAYER);

		allPiecesOnBoard.add(pieceToCheck);
		allPiecesOnBoard.add(pieceToCapture1);
		allPiecesOnBoard.add(pieceToCapture2);
		allPiecesOnBoard.add(pieceToCapture3);
		allPiecesOnBoard.add(pieceToCapture4);
		ArrayList<CheckersPieceModel> expectedPiecesToCapture = new ArrayList<CheckersPieceModel>();
		expectedPiecesToCapture.add(pieceToCapture1);
		expectedPiecesToCapture.add(pieceToCapture2);
		expectedPiecesToCapture.add(pieceToCapture3);
		expectedPiecesToCapture.add(pieceToCapture4);

		ArrayList<Point> expectedIntermediateLocations1 = new ArrayList<Point>();
		expectedIntermediateLocations1.add(new Point(1, 2));
		expectedIntermediateLocations1.add(new Point(3, 0));
		expectedIntermediateLocations1.add(new Point(5, 2));
		ArrayList<Point> expectedIntermediateLocations2 = new ArrayList<Point>();
		expectedIntermediateLocations2.add(new Point(5, 2));
		expectedIntermediateLocations2.add(new Point(3, 0));
		expectedIntermediateLocations2.add(new Point(1, 2));

		ArrayList<PossibleMove> validMoves = MoveValidator
				.getAllValidMovesForASinglePiece(allPiecesOnBoard, pieceToCheck);

		assertEquals(1, validMoves.size());
		checkPossibleMove(validMoves.get(0), pieceToCheck, 4, 3,
				expectedIntermediateLocations1, expectedPiecesToCapture);
		// checkPossibleMove(validMoves.get(1), pieceToCheck, 4, 3,
		// expectedIntermediateLocations2, expectedPiecesToCapture);
	}

	private void checkPossibleMove(PossibleMove moveToCheck,
			CheckersPieceModel pieceBeingMoved, int expectedColumnLocation,
			int expectedRowLocation,
			ArrayList<Point> expectedIntermediateLocations,
			ArrayList<CheckersPieceModel> expectedPiecesToCapture) {

		assertSame(pieceBeingMoved, moveToCheck.getPieceToMove());
		assertEquals(expectedRowLocation, moveToCheck.getNewRowLocation());
		assertEquals(expectedColumnLocation, moveToCheck.getNewColumnLocation());

		assertCollectionsAreEqual(expectedIntermediateLocations,
				moveToCheck.getIntermediateLocations());
		assertCollectionsAreEqual(expectedPiecesToCapture,
				moveToCheck.getPiecesThatWillBeCaptured());
	}
}
