package com.checkers.model;

import java.util.Timer;
import java.util.TimerTask;

import com.checkers.artificialIntelligence.SmartMoveMaker;

public class AutomatedCheckersGame {

	private final class MoveMakingObserver implements
			CheckersBoardObserverInterface {
		@Override
		public void boardChanged() {
		}

		@Override
		public void turnHasEnded() {
			if (AutomatedCheckersGame.this.playerToMakeMovesFor != null) {
				if (AutomatedCheckersGame.this.theAutomatedPlayerStartedOnAnEvenTurn
						&& AutomatedCheckersGame.this.checkersBoardModel
								.getTurnCount() % 2 == 0) {
					if (!AutomatedCheckersGame.this.makeMoveForPlayer()) {
						stopMakingMovesForPlayerWhenItsTheirTurn();
					}
				} else if (!AutomatedCheckersGame.this.theAutomatedPlayerStartedOnAnEvenTurn
						&& AutomatedCheckersGame.this.checkersBoardModel
								.getTurnCount() % 2 != 0) {
					if (!AutomatedCheckersGame.this.makeMoveForPlayer()) {
						stopMakingMovesForPlayerWhenItsTheirTurn();
					}
				}
			}
		}
	}

	private final class EmulatedCheckersGameMoveMakerTask extends TimerTask {
		boolean opponentMadeLastMove = false;

		@Override
		public void run() {
			PlayerToken playerToMove = this.opponentMadeLastMove ? PlayerToken.BOTTOM_PLAYER
					: PlayerToken.TOP_PLAYER;
			this.opponentMadeLastMove = playerToMove
					.equals(PlayerToken.TOP_PLAYER);

			PossibleMove bestMoveToMakeForPlayer = SmartMoveMaker
					.getBestMoveToMakeForPlayer(
							AutomatedCheckersGame.this.checkersBoardModel
									.getPiecesOnBoard(), playerToMove,
							AutomatedCheckersGame.this.checkersBoardModel
									.getAllMovesMade());
			boolean aCheckersPieceWasMovedThisTurn = AutomatedCheckersGame.this.checkersBoardModel
					.actuallyMovePiece(bestMoveToMakeForPlayer);
			int numberOfTurnsSinceAPieceWasCapture = AutomatedCheckersGame.this.checkersBoardModel
					.getTurnCount()
					- AutomatedCheckersGame.this.checkersBoardModel
							.getLastTurnThatAPieceWasCaptured();
			if (!aCheckersPieceWasMovedThisTurn
					|| numberOfTurnsSinceAPieceWasCapture > 50) {
				this.cancel();
			}
		}
	}

	private final CheckersBoardModel checkersBoardModel;
	private final Timer MOVE_MAKER = new Timer();
	private final int TIME_BETWEEN_TURNS = 200;
	private PlayerToken playerToMakeMovesFor = null;
	private boolean theAutomatedPlayerStartedOnAnEvenTurn;

	public AutomatedCheckersGame(CheckersBoardModel checkersBoardModel) {
		this.checkersBoardModel = checkersBoardModel;
		this.checkersBoardModel.addObserver(new MoveMakingObserver());

	}

	public void startFullyEmulatedGame() {
		EmulatedCheckersGameMoveMakerTask moveMakerTask = new EmulatedCheckersGameMoveMakerTask();
		this.MOVE_MAKER.schedule(moveMakerTask, 0, this.TIME_BETWEEN_TURNS);
	}

	public void startMakingMovesForPlayerWhenItsTheirTurn(
			PlayerToken playerToMakeMovesFor) {
		this.playerToMakeMovesFor = playerToMakeMovesFor;
		this.theAutomatedPlayerStartedOnAnEvenTurn = this.checkersBoardModel
				.getTurnCount() % 2 == 0;
		makeMoveForPlayer();
	}

	private boolean makeMoveForPlayer() {
		PossibleMove bestMoveToMakeForPlayer = SmartMoveMaker
				.getBestMoveToMakeForPlayer(
						AutomatedCheckersGame.this.checkersBoardModel
								.getPiecesOnBoard(),
						AutomatedCheckersGame.this.playerToMakeMovesFor,
						this.checkersBoardModel.getAllMovesMade());
		return AutomatedCheckersGame.this.checkersBoardModel
				.actuallyMovePiece(bestMoveToMakeForPlayer);
	}

	public void stopMakingMovesForPlayerWhenItsTheirTurn() {
		this.playerToMakeMovesFor = null;
	}

}
