package com.checkers.display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

import com.checkers.model.CheckersPieceModel;
import com.checkers.model.PlayerToken;

public class CheckersPieceGui {
	private static final Color PLAYER_COLOR = new Color(232, 170, 26);
	private static final Color OPPONENT_COLOR = new Color(118, 214, 197);
	private int displayY;
	private int displayX;
	private boolean isSelected;
	private final CheckersPieceModel pieceModel;
	private final CheckersPieceValidMovesHighlighter validMovesHighlighter;

	public CheckersPieceGui(CheckersPieceModel pieceModel,
			CheckersPieceValidMovesHighlighter validMovesHighlighter) {
		this.pieceModel = pieceModel;
		this.validMovesHighlighter = validMovesHighlighter;
		this.isSelected = false;
		setDisplayLocationBasedOnModel();
	}

	public void draw(Graphics2D graphics2D) {
		int size = CheckersBoardPanel.SQUARE_SIZE;
		if (this.isSelected) {
			graphics2D.setColor(Color.YELLOW);
			graphics2D.setStroke(new BasicStroke(4));
			graphics2D.drawOval(this.displayX, this.displayY, size, size);
			this.validMovesHighlighter.drawValidMovesForCheckersPiece(
					graphics2D, this.pieceModel);
		} else {
			setDisplayLocationBasedOnModel();
		}
		Color pieceColor = this.pieceModel.getPlayerToken().equals(
				PlayerToken.PLAYER) ? PLAYER_COLOR : OPPONENT_COLOR;
		graphics2D.setColor(pieceColor);
		graphics2D.fillOval(this.displayX, this.displayY, size, size);
		if (this.pieceModel.isKing()) {
			graphics2D.drawImage(Images.singleton.crown, this.displayX,
					this.displayY, size, size, null);
		}
	}

	public void setDisplayX(int screenX) {
		this.displayX = screenX;
	}

	public void setDisplayY(int screenY) {
		this.displayY = screenY;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public CheckersPieceModel getModel() {
		return this.pieceModel;
	}

	private void setDisplayLocationBasedOnModel() {
		this.displayX = this.pieceModel.getColumn()
				* CheckersBoardPanel.SQUARE_SIZE;
		this.displayY = this.pieceModel.getRow()
				* CheckersBoardPanel.SQUARE_SIZE;
	}

	public boolean isSelected() {
		// TODO Auto-generated method stub
		return this.isSelected;
	}
}
