package com.checkers.display;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import com.checkers.model.CheckersBoardModel;

final class CheckersPieceMouseListener implements MouseListener,
		MouseMotionListener {

	private final CheckersBoardPanel checkerBoardPanel;
	private final ArrayList<CheckersPieceGui> pieces;
	private CheckersPieceGui currentlySelectedPiece;
	private Point mouseStartingPoint;
	private final CheckersBoardModel checkersBoardModel;

	public CheckersPieceMouseListener(CheckersBoardPanel checkerBoardPanel,
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
			CheckersPieceGui pieceAtNewLocation = getPieceAtLocation(newBoardLocation);
			boolean locationIsEmpty = pieceAtNewLocation == null;
			if (locationIsEmpty) {
				this.checkersBoardModel.movePiece(
						this.currentlySelectedPiece.getModel(),
						newBoardLocation.y, newBoardLocation.x);
			}
			this.currentlySelectedPiece.setSelected(false);
			this.checkerBoardPanel.repaint();
		}
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		Point boardLocation = getBoardLocation(arg0.getPoint());
		this.currentlySelectedPiece = getPieceAtLocation(boardLocation);
		if (this.currentlySelectedPiece != null) {
			this.mouseStartingPoint = arg0.getPoint();
			this.currentlySelectedPiece.setSelected(true);
			if (arg0.getClickCount() > 1) {
				this.currentlySelectedPiece.getModel().kingMe();
			}
			this.checkerBoardPanel.repaint();
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
			if (piece.getModel().getColumn() == boardLocation.x) {
				if (piece.getModel().getRow() == boardLocation.y) {
					return piece;
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
}