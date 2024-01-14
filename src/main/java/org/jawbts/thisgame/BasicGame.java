package org.jawbts.thisgame;

import net.minecraft.entity.player.PlayerEntity;

public abstract class BasicGame {
    protected PlayerEntity player;

    BasicGame(PlayerEntity player) {
        this.player = player;
    }

    public PlayerEntity getPlayer() {
        return player;
    }

    abstract public void onGameStart();

    public void onGameEnd() {
        GameManager.getInstance().endGame();
    }

    abstract public void tick();
}
