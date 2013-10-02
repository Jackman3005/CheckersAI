package com.checkers.display;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
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
				graphics2D.drawOval(intermediateLocation.x * size + 10,
						intermediateLocation.y * size + 10, size - 20,
						size - 20);
			}

			graphics2D.setStroke(basicStroke);
			graphics2D.drawOval(possibleMove.getNewColumnLocation() * size,
					possibleMove.getNewRowLocation() * size, size, size);
			checkIfMoreThanOneMoveCanGetToLocationAndDrawThatNumber(graphics2D,
					allValidMoves, possibleMove);

		}

	}

	private void checkIfMoreThanOneMoveCanGetToLocationAndDrawThatNumber(
			Graphics2D graphics2D, ArrayList<PossibleMove> allValidMoves,
			PossibleMove possibleMove) {
		int numberOfOtherMovesThatEndInSameLocationAsThisMove = numberOfOtherMovesThatEndInSameLocationAsThisMove(
				allValidMoves, possibleMove);
		if (numberOfOtherMovesThatEndInSameLocationAsThisMove > 1) {
			char[] numberToDraw = String.valueOf(
					numberOfOtherMovesThatEndInSameLocationAsThisMove)
					.toCharArray();
			drawStringInCenterOfSquare(graphics2D, numberToDraw,
					possibleMove.getNewColumnLocation(),
					possibleMove.getNewRowLocation());
		}
	}

	private void drawStringInCenterOfSquare(Graphics2D graphics2D,
			char[] numberToDraw, int column, int row) {
		int size = CheckersBoardPanel.SQUARE_SIZE;
		Font defaultFont = graphics2D.getFont();
		Font fontToUse = new Font(defaultFont.getFontName(),
				defaultFont.getStyle(), 36);
		graphics2D.setFont(fontToUse);
		FontMetrics fontMetrics = graphics2D.getFontMetrics();
		int widthOfStringToDraw = fontMetrics.charsWidth(numberToDraw, 0,
				numberToDraw.length);
		int heightOfStringToDraw = fontMetrics.getHeight();
		graphics2D.drawChars(numberToDraw, 0, numberToDraw.length, column
				* size + size / 2 - (widthOfStringToDraw / 2), row * size
				+ size / 2 + heightOfStringToDraw / 4);
	}

	private int numberOfOtherMovesThatEndInSameLocationAsThisMove(
			ArrayList<PossibleMove> allValidMoves, PossibleMove possibleMove) {
		int count = 0;

		for (PossibleMove otherMove : allValidMoves) {
			if (otherMove.getNewColumnLocation() == possibleMove
					.getNewColumnLocation())
				if (otherMove.getNewRowLocation() == possibleMove
						.getNewRowLocation()) {
					count++;
				}
		}
		return count;
	}
}
