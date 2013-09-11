package com.checkers.model;

public class CheckersPieceModel {
	private final PlayerToken playerToken;
	private int row;
	private int column;
	private boolean isKing;

	public CheckersPieceModel(int row, int column, PlayerToken player) {
		this.playerToken = player;
		this.row = row;
		this.column = column;
		this.isKing = false;
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

	public void setColumn(int column) {
		this.column = column;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public void kingMe() {
		this.isKing = true;
	}

	public boolean isKing() {
		return this.isKing;
	}

}
