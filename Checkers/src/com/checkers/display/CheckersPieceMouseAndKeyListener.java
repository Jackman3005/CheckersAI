package com.checkers.display;

import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import com.checkers.model.CheckersBoardModel;
import com.checkers.model.CheckersPieceModel;
import com.checkers.model.PossibleMove;
import com.checkers.rules.MoveValidator;
//import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

final class CheckersPieceMouseAndKeyListener implements MouseListener,
		MouseMotionListener, KeyListener {

	private final CheckersBoardPanel checkerBoardPanel;
	private final ArrayList<CheckersPieceGui> pieces;
	private CheckersPieceGui currentlySelectedPiece;
	private CheckersPieceGui currentlySelectedPiece_WithRightClick;
	private Point mouseStartingPoint;
	private final CheckersBoardModel checkersBoardModel;
	private char currentKeyPressed;

	public CheckersPieceMouseAndKeyListener(
			CheckersBoardPanel checkerBoardPanel,
			ArrayList<CheckersPieceGui> checkerPieces,
			CheckersBoardModel checkersBoardModel) {
		this.checkerBoardPanel = checkerBoardPanel;
		this.pieces = checkerPieces;
		this.checkersBoardModel = checkersBoardModel;
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		if (this.currentlySelectedPiece != null) {

			Point newBoardLocation = getBoardLocation(arg0.getPoint());
			List<PossibleMove> moves = MoveValidator
					.getAllvalidMovesToLocation_ReturnsEmptyListIfNonFound(
							this.checkersBoardModel.getPiecesOnBoard(),
							this.currentlySelectedPiece.getModel(),
							newBoardLocation.y, newBoardLocation.x);
			if (moves.size() == 1) {
				this.checkersBoardModel.actuallyMovePiece(moves.get(0));
			} else if (moves.size() > 1) {
				try {
					String stringOfPressedNumber = new String(
							new char[] { this.currentKeyPressed });
					int moveToChoose = Integer.parseInt(stringOfPressedNumber);
					if (moveToChoose <= moves.size()) {
						this.checkersBoardModel.actuallyMovePiece(moves
								.get(moveToChoose - 1));
					}
				} catch (Exception e) {
				}
			}

			this.currentlySelectedPiece.setSelected(false);
		} else if (this.currentlySelectedPiece_WithRightClick != null) {
			Point newBoardLocation = getBoardLocation(arg0.getPoint());
			PossibleMove moveThatCanBeInvalid = new PossibleMove(
					this.currentlySelectedPiece_WithRightClick.getModel(),
					newBoardLocation.y, newBoardLocation.x);
			this.checkersBoardModel.actuallyMovePiece(moveThatCanBeInvalid);
		}
		this.checkerBoardPanel.repaint();
	}

	@Override
	public void mousePressed(MouseEvent click) {
		Point boardLocation = getBoardLocation(click.getPoint());

		int rightClick = MouseEvent.BUTTON3;
		if (click.getButton() == rightClick) {
			this.currentlySelectedPiece = null;
			this.currentlySelectedPiece_WithRightClick = getPieceAtLocation(boardLocation);
		} else {
			this.currentlySelectedPiece_WithRightClick = null;
			this.currentlySelectedPiece = getPieceAtLocation(boardLocation);
			if (this.currentlySelectedPiece != null) {
				this.mouseStartingPoint = click.getPoint();
				this.currentlySelectedPiece.setSelected(true);
			}
		}
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		if (this.currentlySelectedPiece != null) {
			Point currentPoint = arg0.getPoint();
			int deltaX = this.mouseStartingPoint.x - currentPoint.x;
			int deltaY = this.mouseStartingPoint.y - currentPoint.y;
			Point startingPoint = getScreenLocationOfPiece(this.currentlySelectedPiece);
			this.currentlySelectedPiece.setDisplayX(startingPoint.x - deltaX);
			this.currentlySelectedPiece.setDisplayY(startingPoint.y - deltaY);
			this.checkerBoardPanel.repaint();
		}
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
	}

	private CheckersPieceGui getPieceAtLocation(Point boardLocation) {
		for (CheckersPieceGui piece : this.pieces) {
			CheckersPieceModel model = piece.getModel();
			if (model.getColumn() == boardLocation.x) {
				if (model.getRow() == boardLocation.y) {
					if (!model.isCaptured()) {
						return piece;
					}
				}
			}
		}
		return null;
	}

	private Point getScreenLocationOfPiece(CheckersPieceGui piece) {
		int screenX = piece.getModel().getColumn()
				* CheckersBoardPanel.SQUARE_SIZE;
		int screenY = piece.getModel().getRow()
				* CheckersBoardPanel.SQUARE_SIZE;

		return new Point(screenX, screenY);
	}

	private Point getBoardLocation(Point point) {
		int screenX = point.x;
		int screenY = point.y;
		int boardX = screenX / CheckersBoardPanel.SQUARE_SIZE;
		int boardY = screenY / CheckersBoardPanel.SQUARE_SIZE;
		Point boardLocation = new Point(boardX, boardY);
		return boardLocation;
	}

	@Override
	public void keyPressed(KeyEvent keyPress) {
		if (keyPress.isControlDown() && keyPress.getKeyCode() == 90) {
			this.checkersBoardModel.undoLastMove();
		} else {
			this.currentKeyPressed = keyPress.getKeyChar();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		this.currentKeyPressed = '\0';
	}

	@Override
	public void keyTyped(KeyEvent keyPress) {

		if (this.currentlySelectedPiece_WithRightClick != null) {
			if (keyPress.getKeyChar() == 'k') {
				CheckersPieceModel model = this.currentlySelectedPiece_WithRightClick
						.getModel();
				if (model.isKing()) {
					model.undoKingMe();
				} else {
					model.kingMe();
				}
			}
			if (keyPress.getKeyChar() == 'c') {
				this.checkersBoardModel
						.capturePiece(this.currentlySelectedPiece_WithRightClick
								.getModel());
			}
			this.checkerBoardPanel.repaint();
		}
	}
}