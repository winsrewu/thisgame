package org.jawbts.thisgame;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.Random;

public class GameManager {
    private static final GameManager INSTANCE = new GameManager();
    private static final Random rm = new Random();
    private BasicGame curGame;
    private MinecraftServer curServer;

    public static GameManager getInstance() {
        return INSTANCE;
    }

    public BasicGame getCurGame() {
        return curGame;
    }

    public MinecraftServer getCurServer() {
        return curServer;
    }

    public void tick() {
        if (curGame == null) return;
        curGame.tick();
    }

    public void startGame(PlayerEntity player) {
        if (curGame != null) {
            player.sendMessage(new LiteralText("不行, 你已经在玩一个游戏了!"), false);
            return;
        }
        curServer = player.getServer();
        switch (rm.nextInt(3)) {
            case 0 -> curGame = new WhoStoleMyDiamondsGame(player);
            case 1 -> curGame = new JumpWithMeGame(player);
            case 2 -> {
                player.sendMessage(new LiteralText("上天去吧啊哈哈哈哈!"), false);
                new Rocket(player);
            }
        }
        curGame.onGameStart();
    }

    public void endGame() {
        curGame = null;
    }

    public void onPlayerHitEvent(PlayerEntity player, Entity hit) {
        if (curGame instanceof WhoStoleMyDiamondsGame) {
            ((WhoStoleMyDiamondsGame) curGame).sbOnHit(hit);
        }
    }

    public void onPlayerUseEvent(PlayerEntity player, Entity hit) {
        if (player instanceof ServerPlayerEntity && player.getMainHandStack().isOf(Items.FLINT_AND_STEEL)
                && player.isSneaking()) {
            new Rocket(hit);
        }

        if (curGame instanceof WhoStoleMyDiamondsGame) {
            ((WhoStoleMyDiamondsGame) curGame).sbOnUse(hit);
        }
    }
}
