package org.jawbts.thisgame;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.Random;

public class JumpWithMeGame extends BasicGame {
    private static final Random rm = new Random();
    private boolean jump = false;
    private int startCD = 50, failTime = 0, winTime = 0;
    private VillagerEntity v1;

    JumpWithMeGame(PlayerEntity player) {
        super(player);
        v1 = new VillagerEntity(EntityType.VILLAGER, player.getEntityWorld());
        v1.setCustomName(new LiteralText(Thiss.generateNewName()));
    }

    @Override
    public void onGameStart() {
        player.sendMessage(new LiteralText("跟着" + Thiss.getName(v1) + "一起跳!"), false);
        player.sendMessage(new LiteralText("和我一起跳是"), false);
        v1.setPos(player.getX(), player.getY() + 1, player.getZ());
        player.getEntityWorld().spawnEntity(v1);
    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();
        v1.kill();
    }

    @Override
    public void tick() {
        if (player.getEntityWorld().isClient()) return;
        if (startCD > 0) startCD--;
        if (!v1.isAlive()) {
            player.sendMessage(new LiteralText("有this不见了! 游戏结束!"), false);
            onGameEnd();
            return;
        }
        if (startCD != 0) return;
        if (player.getEntityWorld().getTime() % 20 == 19 && jump) {
            if (player.isOnGround()) {
                player.sendMessage(Thiss.saySth(v1, "Missed! (" + ++failTime + "/5)"), false);
                if (failTime == 5) {
                    player.sendMessage(Thiss.saySth(v1, "啊哈! 你输了! 才跳了" + winTime + "个!"), false);
                    onGameEnd();
                }
            } else {
                player.sendMessage(Thiss.saySth(v1, "Good Job!"), false);
                winTime++;
            }
            jump = false;
        }
        if (player.getEntityWorld().getTime() % 20 == 0 && v1.isOnGround() && rm.nextBoolean()) {
            v1.addVelocity(0, 0.5, 0);
            jump = true;
        }
    }
}
