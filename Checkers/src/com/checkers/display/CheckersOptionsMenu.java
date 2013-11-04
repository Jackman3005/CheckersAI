package com.checkers.display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

import com.checkers.model.AutomatedCheckersGame;
import com.checkers.model.CheckersBoardModel;
import com.checkers.model.PlayerToken;

public class CheckersOptionsMenu extends JMenuBar {
	private final class ShowLocationNotationActionListener implements
			ActionListener {

		private final JCheckBoxMenuItem showNotationCheckbox;

		public ShowLocationNotationActionListener(
				JCheckBoxMenuItem showNotationCheckbox) {
			this.showNotationCheckbox = showNotationCheckbox;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			CheckersOptionsMenu.this.checkersBoardModel
					.setShouldShowNotation(this.showNotationCheckbox
							.isSelected());
		}
	}

	private final class ResetBoardActionListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			CheckersOptionsMenu.this.checkersBoardModel.resetBoard();
		}
	}

	private final class EmulateGameActionListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			AutomatedCheckersGame automatedCheckersGame = new AutomatedCheckersGame(
					CheckersOptionsMenu.this.checkersBoardModel);
			automatedCheckersGame.startFullyEmulatedGame();
		}
	}

	private final class PerformMovesPlayerActionListener implements
			ActionListener {
		private final PlayerToken playerToMakeMovesFor;
		private final JCheckBoxMenuItem checkBox;
		private final AutomatedCheckersGame automatedCheckersGame;

		PerformMovesPlayerActionListener(PlayerToken playerToMakeMovesFor,
				JCheckBoxMenuItem checkBox) {
			this.playerToMakeMovesFor = playerToMakeMovesFor;
			this.checkBox = checkBox;
			this.automatedCheckersGame = new AutomatedCheckersGame(
					CheckersOptionsMenu.this.checkersBoardModel);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (this.checkBox.isSelected()) {
				this.automatedCheckersGame
						.startMakingMovesForPlayerWhenItsTheirTurn(this.playerToMakeMovesFor);
			} else {
				this.automatedCheckersGame
						.stopMakingMovesForPlayerWhenItsTheirTurn();

			}
		}
	}

	private static final long serialVersionUID = 1L;
	private final CheckersBoardModel checkersBoardModel;

	public CheckersOptionsMenu(CheckersBoardModel checkersBoardModel) {
		this.checkersBoardModel = checkersBoardModel;

		JMenu aIMenu = new JMenu("Artificial Intelligence");
		aIMenu.setMnemonic('a');

		JCheckBoxMenuItem automateTopPlayersMoves = createAutomatePlayerMovesCheckbox(
				PlayerToken.TOP_PLAYER, "Automate Top Player");
		JCheckBoxMenuItem automateBottomPlayersMoves = createAutomatePlayerMovesCheckbox(
				PlayerToken.BOTTOM_PLAYER, "Automate Bottom Player");
		JMenuItem emulateGame = createEmulateGameMenuItem();
		JMenuItem resetBoardMenuItem = createResetBoardMenuItem();
		JCheckBoxMenuItem showNotationCheckBox = createShowNotationCheckBox();

		aIMenu.add(automateTopPlayersMoves);
		aIMenu.add(automateBottomPlayersMoves);
		aIMenu.add(emulateGame);
		aIMenu.add(new JSeparator());
		aIMenu.add(resetBoardMenuItem);
		aIMenu.add(showNotationCheckBox);
		this.add(aIMenu);

	}

	private JMenuItem createResetBoardMenuItem() {
		JMenuItem resetBoard = new JMenuItem("Reset Board");
		resetBoard.addActionListener(new ResetBoardActionListener());
		resetBoard.setMnemonic('r');
		return resetBoard;
	}

	private JCheckBoxMenuItem createAutomatePlayerMovesCheckbox(
			PlayerToken playerToken, String menuItemName) {
		JCheckBoxMenuItem checkBoxForAutomatingPlayer = new JCheckBoxMenuItem(
				menuItemName);
		checkBoxForAutomatingPlayer
				.addActionListener(new PerformMovesPlayerActionListener(
						playerToken, checkBoxForAutomatingPlayer));

		return checkBoxForAutomatingPlayer;
	}

	private JCheckBoxMenuItem createShowNotationCheckBox() {
		JCheckBoxMenuItem checkBoxForShowingLocationNotation = new JCheckBoxMenuItem(
				"Show Location Notation");
		checkBoxForShowingLocationNotation
				.addActionListener(new ShowLocationNotationActionListener(
						checkBoxForShowingLocationNotation));

		return checkBoxForShowingLocationNotation;

	}

	private JMenuItem createEmulateGameMenuItem() {
		JMenuItem emulateGame = new JMenuItem("Emulate Game");
		emulateGame.addActionListener(new EmulateGameActionListener());
		emulateGame.setMnemonic('e');
		return emulateGame;
	}

}
