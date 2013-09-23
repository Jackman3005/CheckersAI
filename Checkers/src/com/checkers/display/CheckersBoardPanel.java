package com.checkers.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.checkers.model.CheckersBoardModel;
import com.checkers.model.CheckersBoardObserverInterface;
import com.checkers.model.CheckersPieceModel;

public class CheckersBoardPanel extends JPanel {

	private final class CheckersBoardRepaintingObserver implements
			CheckersBoardObserverInterface {
		@Override
		public void boardChanged() {
			CheckersBoardPanel.this.repaintBoardBecauseModelChanged();
		}
	}

	public static final int BOARD_SIZE = 8;
	public static final int SQUARE_SIZE = 80;
	private static final Color COLOR_ONE = Color.RED;
	private static final Color COLOR_TWO = Color.BLACK;
	private final ArrayList<CheckersPieceGui> checkersGuiPieces;
	private final CheckersBoardModel checkersBoardModel;

	public CheckersBoardPanel(CheckersBoardModel checkersBoardModel) {
		this.checkersBoardModel = checkersBoardModel;
		this.setFocusable(true);
		this.checkersGuiPieces = new ArrayList<CheckersPieceGui>();
		buildGuiPiecesBasedOnModel();

		CheckersPieceMouseAndKeyListener checkerPieceMouseAndKeyListener = new CheckersPieceMouseAndKeyListener(
				this, this.checkersGuiPieces, checkersBoardModel);
		this.addMouseListener(checkerPieceMouseAndKeyListener);
		this.addMouseMotionListener(checkerPieceMouseAndKeyListener);
		this.addKeyListener(checkerPieceMouseAndKeyListener);

		this.checkersBoardModel
				.addObserver(new CheckersBoardRepaintingObserver());
	}

	private void buildGuiPiecesBasedOnModel() {
		this.checkersGuiPieces.clear();
		CheckersPieceValidMovesHighlighter validMovesHighlighter = new CheckersPieceValidMovesHighlighter(
				this.checkersBoardModel);
		for (CheckersPieceModel pieceModel : this.checkersBoardModel
				.getPiecesOnBoard()) {
			this.checkersGuiPieces.add(new CheckersPieceGui(pieceModel,
					validMovesHighlighter));
		}
	}

	private void repaintBoardBecauseModelChanged() {
		buildGuiPiecesBasedOnModel();
		this.revalidate();
		this.repaint();
	}

	@Override
	public void paint(Graphics graphics) {
		Graphics2D graphics2D = (Graphics2D) graphics;
		for (int row = 0; row < BOARD_SIZE; row++) {
			for (int column = 0; column < BOARD_SIZE; column++) {
				Color colorToDraw = (row + column) % 2 == 0 ? COLOR_ONE
						: COLOR_TWO;
				graphics2D.setColor(colorToDraw);
				graphics2D.fillRect(SQUARE_SIZE * column, SQUARE_SIZE * row,
						SQUARE_SIZE, SQUARE_SIZE);
			}
		}

		for (CheckersPieceGui piece : this.checkersGuiPieces) {
			piece.draw(graphics2D);
		}

		for (CheckersPieceGui piece : this.checkersGuiPieces) {
			if (piece.isSelected()) {
				piece.draw(graphics2D);
			}
		}

	}
}
