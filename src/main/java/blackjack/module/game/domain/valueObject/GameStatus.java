package blackjack.module.game.domain.valueObject;

public enum GameStatus {
    ACTIVE,
    FINISHED;

    public boolean isFinished() {
        return this == FINISHED;
    }
}
