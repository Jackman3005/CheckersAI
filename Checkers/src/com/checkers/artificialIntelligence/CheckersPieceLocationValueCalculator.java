package com.checkers.artificialIntelligence;

import java.util.HashMap;
import java.util.List;

import com.checkers.display.CheckersBoardPanel;
import com.checkers.model.CheckersPieceModel;
import com.checkers.model.PlayerToken;

public class CheckersPieceLocationValueCalculator {
	public static CheckersPieceLocationValueCalculator singleton = new CheckersPieceLocationValueCalculator();
	private final HashMap<Integer, HashMap<Integer, Integer>> mapOfLocationValues;
	private final HashMap<Integer, HashMap<Integer, Integer>> mapOfLoactionValuesForKing;
	private CheckersPieceLocationValueCalculator() {
		int numberOfColumnsAndRows = CheckersBoardPanel.BOARD_SIZE;
		this.mapOfLocationValues = new HashMap<Integer, HashMap<Integer, Integer>>();
		this.mapOfLoactionValuesForKing=new HashMap<Integer, HashMap<Integer, Integer>>();
		initializeMapWithValues(numberOfColumnsAndRows,
				this.mapOfLocationValues);
		initializeMapWithValuesForKing(numberOfColumnsAndRows,
				this.mapOfLoactionValuesForKing);

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
		Integer valueOfLocation;
		int piecesBaseValue = checkersPiece.isKing() ? 50 : 10;
		if (checkersPiece.isKing()){
			valueOfLocation = this.mapOfLoactionValuesForKing.get(
					checkersPiece.getRow()).get(checkersPiece.getColumn());
		}
		else valueOfLocation = this.mapOfLocationValues.get(
				checkersPiece.getRow()).get(checkersPiece.getColumn());

		return piecesBaseValue + valueOfLocation;
	}

	private void initializeMapWithValues(int numberOfColumnsAndRows,
			HashMap<Integer, HashMap<Integer, Integer>> mapOfLocationValues) {
		for (int i = 0; i < numberOfColumnsAndRows; i++) {
			HashMap<Integer, Integer> mapRepresentingARowOnTheCheckerBoard = new HashMap<Integer, Integer>();
			mapOfLocationValues.put(i, mapRepresentingARowOnTheCheckerBoard);
		}

		mapOfLocationValues.get(0).put(1, 4);
		mapOfLocationValues.get(0).put(3, 4);
		mapOfLocationValues.get(0).put(5, 4);
		mapOfLocationValues.get(0).put(7, 4);

		mapOfLocationValues.get(1).put(0, 4);
		mapOfLocationValues.get(1).put(2, 3);
		mapOfLocationValues.get(1).put(4, 3);
		mapOfLocationValues.get(1).put(6, 3);

		mapOfLocationValues.get(2).put(1, 3);
		mapOfLocationValues.get(2).put(3, 2);
		mapOfLocationValues.get(2).put(5, 2);
		mapOfLocationValues.get(2).put(7, 4);

		mapOfLocationValues.get(3).put(0, 4);
		mapOfLocationValues.get(3).put(2, 2);
		mapOfLocationValues.get(3).put(4, 1);
		mapOfLocationValues.get(3).put(6, 3);

		mapOfLocationValues.get(4).put(1, 3);
		mapOfLocationValues.get(4).put(3, 1);
		mapOfLocationValues.get(4).put(5, 2);
		mapOfLocationValues.get(4).put(7, 4);

		mapOfLocationValues.get(5).put(0, 4);
		mapOfLocationValues.get(5).put(2, 2);
		mapOfLocationValues.get(5).put(4, 2);
		mapOfLocationValues.get(5).put(6, 3);

		mapOfLocationValues.get(6).put(1, 3);
		mapOfLocationValues.get(6).put(3, 3);
		mapOfLocationValues.get(6).put(5, 3);
		mapOfLocationValues.get(6).put(7, 4);

		mapOfLocationValues.get(7).put(0, 4);
		mapOfLocationValues.get(7).put(2, 4);
		mapOfLocationValues.get(7).put(4, 4);
		mapOfLocationValues.get(7).put(6, 4);
	}
	
	private void initializeMapWithValuesForKing(int numberOfColumnsAndRows,
			HashMap<Integer, HashMap<Integer, Integer>> mapOfLocationValues) {
		for (int i = 0; i < numberOfColumnsAndRows; i++) {
			HashMap<Integer, Integer> mapRepresentingARowOnTheCheckerBoard = new HashMap<Integer, Integer>();
			mapOfLoactionValuesForKing.put(i, mapRepresentingARowOnTheCheckerBoard);
		}

		mapOfLoactionValuesForKing.get(0).put(1, 1);
		mapOfLoactionValuesForKing.get(0).put(3, 1);
		mapOfLoactionValuesForKing.get(0).put(5, 1);
		mapOfLoactionValuesForKing.get(0).put(7, 1);

		mapOfLoactionValuesForKing.get(1).put(0, 1);
		mapOfLoactionValuesForKing.get(1).put(2, 2);
		mapOfLoactionValuesForKing.get(1).put(4, 2);
		mapOfLoactionValuesForKing.get(1).put(6, 2);

		mapOfLoactionValuesForKing.get(2).put(1, 2);
		mapOfLoactionValuesForKing.get(2).put(3, 3);
		mapOfLoactionValuesForKing.get(2).put(5, 3);
		mapOfLoactionValuesForKing.get(2).put(7, 1);

		mapOfLoactionValuesForKing.get(3).put(0, 1);
		mapOfLoactionValuesForKing.get(3).put(2, 3);
		mapOfLoactionValuesForKing.get(3).put(4, 4);
		mapOfLoactionValuesForKing.get(3).put(6, 2);

		mapOfLoactionValuesForKing.get(4).put(1, 2);
		mapOfLoactionValuesForKing.get(4).put(3, 4);
		mapOfLoactionValuesForKing.get(4).put(5, 3);
		mapOfLoactionValuesForKing.get(4).put(7, 1);

		mapOfLoactionValuesForKing.get(5).put(0, 1);
		mapOfLoactionValuesForKing.get(5).put(2, 3);
		mapOfLoactionValuesForKing.get(5).put(4, 3);
		mapOfLoactionValuesForKing.get(5).put(6, 2);

		mapOfLoactionValuesForKing.get(6).put(1, 2);
		mapOfLoactionValuesForKing.get(6).put(3, 2);
		mapOfLoactionValuesForKing.get(6).put(5, 2);
		mapOfLoactionValuesForKing.get(6).put(7, 1);

		mapOfLoactionValuesForKing.get(7).put(0, 1);
		mapOfLoactionValuesForKing.get(7).put(2, 1);
		mapOfLoactionValuesForKing.get(7).put(4, 1);
		mapOfLoactionValuesForKing.get(7).put(6, 1);
	}
}
