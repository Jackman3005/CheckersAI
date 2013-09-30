package com.checkers.display;

import java.util.Timer;
import java.util.TimerTask;

public class CheckersPieceCaptureAnimationDisplayer {
	private final class WidthShrinker extends TimerTask {
		private final CheckersPieceGui pieceToShrink;
		private int currentOvalWidth;

		public WidthShrinker(CheckersPieceGui checkersPiece, int squareSize) {
			this.pieceToShrink = checkersPiece;
			// TODO Auto-generated constructor stub
			this.currentOvalWidth = squareSize;

		}

		@Override
		public void run() {
			if (this.currentOvalWidth >= 15) {
				this.pieceToShrink.setDisplayWidth(this.currentOvalWidth);
				this.currentOvalWidth -= 2;
				this.pieceToShrink
						.setDisplayX(this.pieceToShrink.getDisplayX() + 1);
				CheckersPieceCaptureAnimationDisplayer.this.checkersBoardPanel
						.repaint();
			} else {
				this.cancel();
			}

		}
	}

	private final class HeightShrinker extends TimerTask {
		private final CheckersPieceGui pieceToShrink;
		private int currentOvalHeight;

		public HeightShrinker(CheckersPieceGui checkersPiece, int squareSize) {
			this.pieceToShrink = checkersPiece;
			this.currentOvalHeight = squareSize;

		}

		@Override
		public void run() {
			if (this.currentOvalHeight >= 0) {
				this.pieceToShrink.setDisplayHieght(this.currentOvalHeight);
				this.currentOvalHeight -= 2;
				this.pieceToShrink
						.setDisplayY(this.pieceToShrink.getDisplayY() + 1);
				CheckersPieceCaptureAnimationDisplayer.this.checkersBoardPanel
						.repaint();
			} else {
				this.cancel();
			}

		}
	}

	private final CheckersBoardPanel checkersBoardPanel;

	public CheckersPieceCaptureAnimationDisplayer(
			CheckersBoardPanel checkersBoardPanel) {
		this.checkersBoardPanel = checkersBoardPanel;

	}

	public void animateCapture(CheckersPieceGui checkersPiece) {
		Timer animator = new Timer();
		TimerTask animatedStep1 = new WidthShrinker(checkersPiece,
				CheckersBoardPanel.SQUARE_SIZE);
		TimerTask animatedStep2 = new HeightShrinker(checkersPiece,
				CheckersBoardPanel.SQUARE_SIZE);

		int timeBetweenEachStepInAnimation1 = 5;
		int timeTakenToRunAnimation1 = (CheckersBoardPanel.SQUARE_SIZE - 15)
				* timeBetweenEachStepInAnimation1;

		animator.schedule(animatedStep1, 0, timeBetweenEachStepInAnimation1);
		animator.schedule(animatedStep2, timeTakenToRunAnimation1, 1);
	}
}
