package com.checkers.display;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.checkers.model.CheckersBoardModel;
import com.checkers.model.CheckersPieceModel;

public class CheckersBoardPanel extends JPanel {

	public static final int BOARD_SIZE = 8;
	public static final int SQUARE_SIZE = 80;
	private static final Color COLOR_ONE = Color.RED;
	private static final Color COLOR_TWO = Color.BLACK;
	private final ArrayList<CheckersPieceGui> checkerPieces;

	public CheckersBoardPanel(CheckersBoardModel checkersBoardModel) {
		this.checkerPieces = new ArrayList<CheckersPieceGui>();
		for (CheckersPieceModel pieceModel : checkersBoardModel
				.getPiecesOnBoard()) {
			this.checkerPieces.add(new CheckersPieceGui(pieceModel));
		}

		CheckersPieceMouseListener checkerPieceMouseListener = new CheckersPieceMouseListener(
				this, this.checkerPieces);
		this.addMouseListener(checkerPieceMouseListener);
		this.addMouseMotionListener(checkerPieceMouseListener);
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

		for (CheckersPieceGui piece : this.checkerPieces) {
			piece.draw(graphics2D);
		}

		for (CheckersPieceGui piece : this.checkerPieces) {
			if (piece.isSelected()) {
				piece.draw(graphics2D);
			}
		}

	}
}
