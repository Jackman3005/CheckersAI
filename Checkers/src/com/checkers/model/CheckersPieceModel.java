package com.checkers.model;

import java.util.ArrayList;

public class CheckersPieceModel {
	private final PlayerToken playerToken;
	private int row;
	private int column;
	private boolean isKing;
	private boolean isCaptured;
	private final ArrayList<CheckersPieceObserverInterface> observers;
	private final int id;

	public CheckersPieceModel(int id, int row, int column, PlayerToken player) {
		this.id = id;
		this.playerToken = player;
		this.row = row;
		this.column = column;
		this.isKing = false;
		this.isCaptured = false;
		this.observers = new ArrayList<CheckersPieceObserverInterface>();
	}

	public int getColumn() {
		return this.column;
	}

	public int getRow() {
		return this.row;
	}

	public PlayerToken getPlayerToken() {
		return this.playerToken;
	}

	void setColumn(int column) {
		this.column = column;
	}

	void setRow(int row) {
		this.row = row;
	}

	public void kingMe() {
		this.isKing = true;
	}

	public boolean isKing() {
		return this.isKing;
	}

	void capturePiece() {
		notifyObserversPieceWasCaptured();
		this.isCaptured = true;
	}

	public boolean isCaptured() {
		return this.isCaptured;
	}

	public void undoCapturePiece() {
		this.isCaptured = false;
		notifyObserversPieceWasUnCaptured();
	}

	public void undoKingMe() {
		this.isKing = false;
	}

	public void addObserver(CheckersPieceObserverInterface observer) {
		this.observers.add(observer);
	}

	public void removeObserver(CheckersPieceObserverInterface observer) {
		this.observers.remove(observer);
	}

	public int getId() {
		return this.id;
	}

	private void notifyObserversPieceWasCaptured() {
		for (CheckersPieceObserverInterface observer : this.observers) {
			observer.pieceCaptured();
		}
	}

	private void notifyObserversPieceWasUnCaptured() {
		for (CheckersPieceObserverInterface observer : this.observers) {
			observer.pieceUnCaptured();
		}
	}

	@Override
	public String toString() {
		return this.playerToken + " Piece at Row: " + this.row + " Col: "
				+ this.column;
	}
}
