package com.checkers.display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

import com.checkers.model.CheckersBoardModel;
import com.checkers.model.CheckersPieceModel;
import com.checkers.model.PossibleMove;
import com.checkers.rules.MoveValidator;

public class CheckersPieceValidMovesHighlighter {

	private final CheckersBoardModel checkersBoardModel;
	final static float dash1[] = { 7.0f };
	final static BasicStroke dashed = new BasicStroke(4.0f,
			BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash1, 0.0f);

	public CheckersPieceValidMovesHighlighter(
			CheckersBoardModel checkersBoardModel) {
		this.checkersBoardModel = checkersBoardModel;
	}

	public void drawValidMovesForCheckersPiece(Graphics2D graphics2D,
			CheckersPieceModel checkersPiece) {
		graphics2D.setColor(Color.YELLOW);
		BasicStroke basicStroke = new BasicStroke(4);
		int size = CheckersBoardPanel.SQUARE_SIZE;

		ArrayList<PossibleMove> allValidMoves = MoveValidator
				.getAllValidMovesForASinglePiece(
						this.checkersBoardModel.getPiecesOnBoard(),
						checkersPiece);

		for (PossibleMove possibleMove : allValidMoves) {
			graphics2D.setStroke(dashed);
			for (Point intermediateLocation : possibleMove
					.getIntermediateLocations()) {
				graphics2D.drawOval(intermediateLocation.x * size,
						intermediateLocation.y * size, size, size);
			}

			graphics2D.setStroke(basicStroke);
			graphics2D.drawOval(possibleMove.getNewColumnLocation() * size,
					possibleMove.getNewRowLocation() * size, size, size);

		}

	}
}
