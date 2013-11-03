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
	private final int startingRowLocation;
	private final int startingColumnLocation;

	public PossibleMove(CheckersPieceModel pieceToMove, int newRowLocation,
			int newColumnLocation) {
		this.pieceToMove = pieceToMove;
		this.startingRowLocation = pieceToMove.getRow();
		this.startingColumnLocation = pieceToMove.getColumn();
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

	public boolean isThisMoveAJump() {
		return this.piecesThatWillBeCaptured.size() > 0;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PossibleMove other = (PossibleMove) obj;
		if (this.intermediateLocations == null) {
			if (other.intermediateLocations != null)
				return false;
		} else if (!this.intermediateLocations
				.equals(other.intermediateLocations))
			return false;
		if (this.newColumnLocation != other.newColumnLocation)
			return false;
		if (this.newRowLocation != other.newRowLocation)
			return false;
		if (this.pieceToMove == null) {
			if (other.pieceToMove != null)
				return false;
		} else if (other.pieceToMove == null) {
			return false;
		} else if (this.pieceToMove.getId() != other.pieceToMove.getId())
			return false;
		if (this.piecesThatWillBeCaptured == null) {
			if (other.piecesThatWillBeCaptured != null)
				return false;
		} else if (!this.piecesThatWillBeCaptured
				.equals(other.piecesThatWillBeCaptured))
			return false;
		if (this.startingColumnLocation != other.startingColumnLocation)
			return false;
		if (this.startingRowLocation != other.startingRowLocation)
			return false;
		return true;
	}

}
