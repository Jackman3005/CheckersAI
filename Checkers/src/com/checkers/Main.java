package com.checkers;

import com.checkers.display.MainFrame;
import com.checkers.model.CheckersBoardModel;

public class Main {

	public static void main(String[] args) {
		CheckersBoardModel checkersBoardModel = new CheckersBoardModel();

		MainFrame checkerBoardFrame = new MainFrame(checkersBoardModel);
		checkerBoardFrame.setVisible(true);
	}

}
