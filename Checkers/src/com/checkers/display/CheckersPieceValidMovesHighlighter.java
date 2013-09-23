package com.checkers.display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import com.checkers.model.CheckersBoardModel;
import com.checkers.model.CheckersPieceModel;
import com.checkers.model.PossibleMove;
import com.checkers.rules.MoveValidator;

public class CheckersPieceValidMovesHighlighter {

	private final CheckersBoardModel checkersBoardModel;

	public CheckersPieceValidMovesHighlighter(
			CheckersBoardModel checkersBoardModel) {
		this.checkersBoardModel = checkersBoardModel;
	}

	public void drawValidMovesForCheckersPiece(Graphics2D graphics2D,
			CheckersPieceModel checkersPiece) {
		graphics2D.setColor(Color.YELLOW);
		graphics2D.setStroke(new BasicStroke(4));
		int size = CheckersBoardPanel.SQUARE_SIZE;

		ArrayList<PossibleMove> allValidMoves = MoveValidator
				.getAllValidMovesForASinglePiece(
						this.checkersBoardModel.getPiecesOnBoard(),
						checkersPiece);

		for (PossibleMove possibleMove : allValidMoves) {
			graphics2D.drawOval(possibleMove.getEndingColumnLocation() * size,
					possibleMove.getEndingRowLocation() * size, size, size);
		}

	}
}
