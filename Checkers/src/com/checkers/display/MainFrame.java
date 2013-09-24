package com.checkers.display;

import javax.swing.JFrame;

import com.checkers.model.CheckersBoardModel;

public class MainFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public MainFrame(CheckersBoardModel checkersBoardModel) {
		int size = CheckersBoardPanel.BOARD_SIZE
				* CheckersBoardPanel.SQUARE_SIZE;
		this.setSize(size + 16, size + 62);
		this.setResizable(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setFocusable(false);
		CheckersBoardPanel checkerBoardPanel = new CheckersBoardPanel(
				checkersBoardModel);

		CheckersOptionsMenu checkersOptionsMenu = new CheckersOptionsMenu(
				checkersBoardModel);
		setJMenuBar(checkersOptionsMenu);
		this.add(checkerBoardPanel);

	}

}
