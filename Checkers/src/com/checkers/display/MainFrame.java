package com.checkers.display;

import javax.swing.JFrame;

import com.checkers.model.CheckersBoardModel;

public class MainFrame extends JFrame {

	public MainFrame(CheckersBoardModel checkersBoardModel) {
		int size = CheckersBoardPanel.BOARD_SIZE
				* CheckersBoardPanel.SQUARE_SIZE;
		this.setSize(size + 6, size + 28);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);

		CheckersBoardPanel checkerBoardPanel = new CheckersBoardPanel(
				checkersBoardModel);
		this.add(checkerBoardPanel);
	}

}
