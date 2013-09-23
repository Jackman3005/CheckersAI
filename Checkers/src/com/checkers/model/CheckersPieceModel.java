package com.checkers.model;

public class CheckersPieceModel {
	private final PlayerToken playerToken;
	private int row;
	private int column;
	private boolean isKing;
	private boolean isCaptured;

	public CheckersPieceModel(int row, int column, PlayerToken player) {
		this.playerToken = player;
		this.row = row;
		this.column = column;
		this.isKing = false;
		this.isCaptured = false;
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
		this.isCaptured = true;
	}

	public boolean isCaptured() {
		return this.isCaptured;
	}

	public static CheckersPieceModel copy(CheckersPieceModel checkersPieceModel) {
		CheckersPieceModel copyOfCheckersPiece = new CheckersPieceModel(
				checkersPieceModel.getRow(), checkersPieceModel.getColumn(),
				checkersPieceModel.getPlayerToken());
		if (checkersPieceModel.isKing()) {
			copyOfCheckersPiece.kingMe();
		}
		if (checkersPieceModel.isCaptured()) {
			copyOfCheckersPiece.capturePiece();
		}
		return copyOfCheckersPiece;
	}

}
