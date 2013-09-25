package com.checkers.display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import com.checkers.model.CheckersPieceModel;
import com.checkers.model.CheckersPieceObserverInterface;
import com.checkers.model.PlayerToken;

public class CheckersPieceGui {
	private final class CheckersPieceCaptureAnimationObserver implements
			CheckersPieceObserverInterface {
		@Override
		public void pieceCaptured() {
			CheckersPieceGui.this.captureAnimator
					.animateCapture(CheckersPieceGui.this);
		}

		@Override
		public void pieceUnCaptured() {
			int squareSize = CheckersBoardPanel.SQUARE_SIZE;
			CheckersPieceGui.this.displayHieght = squareSize;
			CheckersPieceGui.this.displayWidth = squareSize;
			CheckersPieceGui.this.displayX = squareSize
					* CheckersPieceGui.this.pieceModel.getColumn();
			CheckersPieceGui.this.displayY = squareSize
					* CheckersPieceGui.this.pieceModel.getRow();
		}
	}

	private static final Color PLAYER_COLOR = new Color(232, 170, 26);
	private static final Color OPPONENT_COLOR = new Color(118, 214, 197);
	private int displayY;
	private int displayX;
	private boolean isSelected;
	private final CheckersPieceModel pieceModel;
	private final CheckersPieceValidMovesHighlighter validMovesHighlighter;
	private final CheckersPieceCaptureAnimationDisplayer captureAnimator;
	private int displayWidth;
	private int displayHieght;

	public CheckersPieceGui(CheckersPieceModel pieceModel,
			CheckersPieceValidMovesHighlighter validMovesHighlighter,
			CheckersPieceCaptureAnimationDisplayer captureAnimator) {
		this.pieceModel = pieceModel;
		this.validMovesHighlighter = validMovesHighlighter;
		this.captureAnimator = captureAnimator;
		this.isSelected = false;
		this.displayWidth = CheckersBoardPanel.SQUARE_SIZE;
		this.displayHieght = CheckersBoardPanel.SQUARE_SIZE;

		this.pieceModel
				.addObserver(new CheckersPieceCaptureAnimationObserver());

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
		} else if (!this.getModel().isCaptured()) {
			setDisplayLocationBasedOnModel();
		}
		BufferedImage pieceImage = Images.singleton.crown;
		if (this.pieceModel.getPlayerToken().equals(PlayerToken.OPPONENT)) {
			pieceImage = this.pieceModel.isKing() ? Images.singleton.blackKingPiece
					: Images.singleton.blackPiece;
		} else {
			pieceImage = this.pieceModel.isKing() ? Images.singleton.redKingPiece
					: Images.singleton.redPiece;
		}
		graphics2D.drawImage(pieceImage, this.displayX, this.displayY,
				this.displayWidth, this.displayHieght, null);
	}

	public void setDisplayX(int screenX) {
		this.displayX = screenX;
	}

	public void setDisplayY(int screenY) {
		this.displayY = screenY;
	}

	public int getDisplayX() {
		return this.displayX;
	}

	public int getDisplayY() {
		return this.displayY;
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

	public void setDisplayHieght(int displayHieght) {
		this.displayHieght = displayHieght;
	}

	public void setDisplayWidth(int displayWidth) {
		this.displayWidth = displayWidth;
	}
}
