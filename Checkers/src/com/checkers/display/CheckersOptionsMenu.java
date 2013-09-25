package com.checkers.display;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.checkers.ArtificialIntelligence.SmartMoveMaker;
import com.checkers.model.CheckersBoardModel;
import com.checkers.model.PlayerToken;
import com.checkers.model.PossibleMove;

public class CheckersOptionsMenu extends JMenuBar {
	private final class EmulateGameActionListener implements ActionListener {
		private final class EmulatedCheckersGameMoveMakerTask extends TimerTask {

			boolean opponentMadeLastMove = false;

			@Override
			public void run() {
				CheckersBoardModel model = CheckersOptionsMenu.this.checkersBoardModel;
				PlayerToken playerToMove = this.opponentMadeLastMove ? PlayerToken.PLAYER
						: PlayerToken.OPPONENT;
				this.opponentMadeLastMove = playerToMove
						.equals(PlayerToken.OPPONENT);

				PossibleMove bestMoveToMakeForPlayer = SmartMoveMaker
						.getBestMoveToMakeForPlayer(model.getPiecesOnBoard(),
								playerToMove);
				boolean aCheckersPieceWasMovedThisTurn = model
						.actuallyMovePiece(bestMoveToMakeForPlayer);
				if (!aCheckersPieceWasMovedThisTurn) {
					this.cancel();
				}
			}
		}

		private final Timer MOVE_MAKER = new Timer();
		private final int TIME_BETWEEN_TURNS = 400;

		@Override
		public void actionPerformed(ActionEvent arg0) {
			EmulatedCheckersGameMoveMakerTask moveMakerTask = new EmulatedCheckersGameMoveMakerTask();
			this.MOVE_MAKER.schedule(moveMakerTask, 0, this.TIME_BETWEEN_TURNS);
		}
	}

	private final class PerformComputerMoveActionListener implements
			ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			CheckersBoardModel model = CheckersOptionsMenu.this.checkersBoardModel;
			PossibleMove bestMoveToMakeForOpponent = SmartMoveMaker
					.getBestMoveToMakeForPlayer(model.getPiecesOnBoard(),
							PlayerToken.OPPONENT);
			model.actuallyMovePiece(bestMoveToMakeForOpponent);
		}
	}

	private static final long serialVersionUID = 1L;
	private final CheckersBoardModel checkersBoardModel;

	public CheckersOptionsMenu(CheckersBoardModel checkersBoardModel) {
		this.checkersBoardModel = checkersBoardModel;

		JMenu aIMenu = new JMenu("Artificial Intelligence");
		aIMenu.setMnemonic('a');

		JMenuItem performComputerMove = createPerformComputerMoveMenuItem();
		JMenuItem emulateGame = createEmulateGameMenuItem();

		aIMenu.add(performComputerMove);
		aIMenu.add(emulateGame);
		this.add(aIMenu);

	}

	private JMenuItem createEmulateGameMenuItem() {
		JMenuItem emulateGame = new JMenuItem("Emulate Game");
		emulateGame.addActionListener(new EmulateGameActionListener());
		emulateGame.setMnemonic('e');
		return emulateGame;
	}

	private JMenuItem createPerformComputerMoveMenuItem() {
		JMenuItem performComputerMove = new JMenuItem("Perform Computer Move");
		performComputerMove
				.addActionListener(new PerformComputerMoveActionListener());
		performComputerMove.setMnemonic('m');
		return performComputerMove;
	}
}
