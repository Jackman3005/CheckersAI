package com.checkers.artificialIntelligence;

import java.util.HashMap;
import java.util.List;

import com.checkers.display.CheckersBoardPanel;
import com.checkers.model.CheckersPieceModel;
import com.checkers.model.PlayerToken;

public class CheckersPieceLocationValueCalculator {
	public static CheckersPieceLocationValueCalculator singleton = new CheckersPieceLocationValueCalculator();
	private final HashMap<Integer, HashMap<Integer, Integer>> mapOfLocationValues;

	private CheckersPieceLocationValueCalculator() {
		int numberOfColumnsAndRows = CheckersBoardPanel.BOARD_SIZE;
		this.mapOfLocationValues = new HashMap<Integer, HashMap<Integer, Integer>>();

		initializeMapWithValues(numberOfColumnsAndRows,
				this.mapOfLocationValues);

	}

	public int getValueOfBoardFromPerspectiveOfPlayer(
			List<CheckersPieceModel> allCheckersPieces, PlayerToken player) {
		int valueOfPlayersPieces = 0;
		int valueOfOpponentsPieces = 0;

		for (CheckersPieceModel checkersPiece : allCheckersPieces) {
			if (checkersPiece.getPlayerToken().equals(player)) {
				valueOfPlayersPieces += piecesValueAtLocation(checkersPiece);
			} else {
				valueOfOpponentsPieces += piecesValueAtLocation(checkersPiece);
			}
		}

		return valueOfPlayersPieces - valueOfOpponentsPieces;
	}

	public int piecesValueAtLocation(CheckersPieceModel checkersPiece) {
		int piecesBaseValue = checkersPiece.isKing() ? 5 : 3;
		Integer valueOfLocation = this.mapOfLocationValues.get(
				checkersPiece.getRow()).get(checkersPiece.getColumn());

		return piecesBaseValue * valueOfLocation;
	}

	private void initializeMapWithValues(int numberOfColumnsAndRows,
			HashMap<Integer, HashMap<Integer, Integer>> mapOfLocationValues) {
		for (int i = 0; i < numberOfColumnsAndRows; i++) {
			HashMap<Integer, Integer> mapRepresentingARowOnTheCheckerBoard = new HashMap<Integer, Integer>();
			mapOfLocationValues.put(i, mapRepresentingARowOnTheCheckerBoard);
		}

		mapOfLocationValues.get(0).put(1, 12);
		mapOfLocationValues.get(0).put(3, 12);
		mapOfLocationValues.get(0).put(5, 12);
		mapOfLocationValues.get(0).put(7, 8);

		mapOfLocationValues.get(1).put(0, 10);
		mapOfLocationValues.get(1).put(2, 10);
		mapOfLocationValues.get(1).put(4, 10);
		mapOfLocationValues.get(1).put(6, 10);

		mapOfLocationValues.get(2).put(1, 10);
		mapOfLocationValues.get(2).put(3, 12);
		mapOfLocationValues.get(2).put(5, 12);
		mapOfLocationValues.get(2).put(7, 9);

		mapOfLocationValues.get(3).put(0, 9);
		mapOfLocationValues.get(3).put(2, 12);
		mapOfLocationValues.get(3).put(4, 12);
		mapOfLocationValues.get(3).put(6, 10);

		mapOfLocationValues.get(4).put(1, 10);
		mapOfLocationValues.get(4).put(3, 12);
		mapOfLocationValues.get(4).put(5, 12);
		mapOfLocationValues.get(4).put(7, 9);

		mapOfLocationValues.get(5).put(0, 9);
		mapOfLocationValues.get(5).put(2, 10);
		mapOfLocationValues.get(5).put(4, 10);
		mapOfLocationValues.get(5).put(6, 10);

		mapOfLocationValues.get(6).put(1, 10);
		mapOfLocationValues.get(6).put(3, 10);
		mapOfLocationValues.get(6).put(5, 10);
		mapOfLocationValues.get(6).put(7, 10);

		mapOfLocationValues.get(7).put(0, 8);
		mapOfLocationValues.get(7).put(2, 12);
		mapOfLocationValues.get(7).put(4, 12);
		mapOfLocationValues.get(7).put(6, 12);
	}
}
