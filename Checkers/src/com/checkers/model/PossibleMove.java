package com.checkers.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class PossibleMove {

	private final CheckersPieceModel pieceToMove;
	private final int newRowLocation;
	private final int newColumnLocation;
	private final List<CheckersPieceModel> piecesThatWillBeCaptured = new ArrayList<CheckersPieceModel>();
	private final List<Point> intermediateLocations = new ArrayList<Point>();

	public PossibleMove(CheckersPieceModel pieceToMove, int newRowLocation,
			int newColumnLocation) {
		this.pieceToMove = pieceToMove;
		this.newRowLocation = newRowLocation;
		this.newColumnLocation = newColumnLocation;
	}

	public int getNewRowLocation() {
		return this.newRowLocation;
	}

	public CheckersPieceModel getPieceToMove() {
		return this.pieceToMove;
	}

	public int getNewColumnLocation() {
		return this.newColumnLocation;
	}

	public List<CheckersPieceModel> getPiecesThatWillBeCaptured() {
		return this.piecesThatWillBeCaptured;
	}

	public void addPieceThatWillBeCaptured(
			CheckersPieceModel pieceThatWillBeTaken) {
		this.piecesThatWillBeCaptured.add(pieceThatWillBeTaken);
	}

	public void addIntermediateLocation(int rowLocationOfIntermediateLocation,
			int columnLocationOfIntermediateLocation) {
		this.intermediateLocations.add(new Point(
				columnLocationOfIntermediateLocation,
				rowLocationOfIntermediateLocation));
	}

	public List<Point> getIntermediateLocations() {
		return this.intermediateLocations;
	}
	
	public boolean isThisMoveAJump(){
		if (this.piecesThatWillBeCaptured.size()>0)
			return true;
		else
			return false;
		
	}

}
