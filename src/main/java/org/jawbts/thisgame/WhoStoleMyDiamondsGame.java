package org.jawbts.thisgame;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;

import java.util.Objects;
import java.util.Random;

public class WhoStoleMyDiamondsGame extends BasicGame {
    private static final Random rm = new Random();
    private final VillagerEntity v1, v2, v3;
    private final int robberId;
    private int liarWho;
    private String s1, s2, s3;

    WhoStoleMyDiamondsGame(PlayerEntity player) {
        super(player);
        v1 = new VillagerEntity(EntityType.VILLAGER, player.getEntityWorld());
        v2 = new VillagerEntity(EntityType.VILLAGER, player.getEntityWorld());
        v3 = new VillagerEntity(EntityType.VILLAGER, player.getEntityWorld());
        v1.setCustomName(new LiteralText(Thiss.generateNewName()));
        v2.setCustomName(new LiteralText(Thiss.generateNewName()));
        v3.setCustomName(new LiteralText(Thiss.generateNewName()));
        robberId = v1.getId();
        liarWho = rm.nextInt(3);
        if (liarWho == 0) {
            s1 = tellLie();
            s2 = sayTruth();
            s3 = sayTruth();
        }
        if (liarWho == 1) {
            s2 = tellLie();
            s1 = sayTruth();
            s3 = sayTruth();
        }
        if (liarWho == 2) {
            s3 = tellLie();
            s1 = sayTruth();
            s2 = sayTruth();
        }
        s1 += ' ';
        s2 += ' ';
        s3 += ' ';
        liarWho = rm.nextInt(3);
        if (liarWho == 0) {
            s1 += tellLie();
            s2 += sayTruth();
            s3 += sayTruth();
        }
        if (liarWho == 1) {
            s2 += tellLie();
            s1 += sayTruth();
            s3 += sayTruth();
        }
        if (liarWho == 2) {
            s3 += tellLie();
            s1 += sayTruth();
            s2 += sayTruth();
        }
    }

    private VillagerEntity getLiar() {
        if (liarWho == 0) return v1;
        if (liarWho == 1) return v2;
        return v3;
    }

    private VillagerEntity getNotLiarA() {
        if (liarWho == 0) return v2;
        return v1;
    }

    private VillagerEntity getNotLiarB() {
        if (liarWho == 0) return v3;
        if (liarWho == 1) return v3;
        return v2;
    }

    private String sayTruth() {
        switch (rm.nextInt(5)) {
            case 0 -> {
                return Thiss.getName(v1) + "是小偷!";
            }
            case 1 -> {
                return Thiss.getName(v2) + "不是小偷!";
            }
            case 2 -> {
                return Thiss.getName(v3) + "不是小偷!";
            }
            case 3 -> {
                return Thiss.getName(getLiar()) + "在说谎!";
            }
            case 4 -> {
                return Thiss.getName(getNotLiarA()) + "不在说谎!";
            }
        }
        return Thiss.getName(getNotLiarB()) + "不在说谎!";
    }

    private String tellLie() {
        switch (rm.nextInt(5)) {
            case 0 -> {
                return Thiss.getName(v1) + "不是小偷!";
            }
            case 1 -> {
                return Thiss.getName(v2) + "是小偷!";
            }
            case 2 -> {
                return Thiss.getName(v3) + "是小偷!";
            }
            case 3 -> {
                return Thiss.getName(getLiar()) + "不在说谎!";
            }
            case 4 -> {
                return Thiss.getName(getNotLiarA()) + "在说谎!";
            }
        }
        return Thiss.getName(getNotLiarB()) + "不在说谎!";
    }

    @Override
    public void onGameStart() {
        player.sendMessage(new LiteralText("询问每个村民, 然后找出(打一下它)小偷! 注意! 一共有一句话是谎话!"), false);
        player.sendMessage(new LiteralText("谁偷了我的钻石是"), false);
        v1.setPos(player.getX(), player.getY() + 1, player.getZ());
        v2.setPos(player.getX(), player.getY() + 1, player.getZ());
        v3.setPos(player.getX(), player.getY() + 1, player.getZ());
        player.getEntityWorld().spawnEntity(v1);
        player.getEntityWorld().spawnEntity(v2);
        player.getEntityWorld().spawnEntity(v3);
    }

    @Override
    public void onGameEnd() {
        super.onGameEnd();
        v1.kill();
        v2.kill();
        v3.kill();
    }

    @Override
    public void tick() {
        if (!v1.isAlive() || !v2.isAlive() || !v3.isAlive()) {
            player.sendMessage(new LiteralText("有this不见了! 游戏结束!"), false);
            onGameEnd();
        }
    }

    public void sbOnHit(Entity entity) {
        if (entity.getId() != v1.getId() && entity.getId() != v2.getId() && entity.getId() != v3.getId()) return;
        if (entity.getId() == robberId) {
            player.sendMessage(new LiteralText("你赢了 =) ! 小偷是" + Objects.requireNonNull(v1.getCustomName()).asString() + "!"), false);
        } else {
            player.sendMessage(new LiteralText("你输了 =( ! 小偷是" + Objects.requireNonNull(v1.getCustomName()).asString() + "!"), false);
        }
        onGameEnd();
    }

    public void sbOnUse(Entity entity) {
        if (entity.getId() == v1.getId()) {
            player.sendMessage(Thiss.saySth(v1, s1), false);
        }
        if (entity.getId() == v2.getId()) {
            player.sendMessage(Thiss.saySth(v2, s2), false);
        }
        if (entity.getId() == v3.getId()) {
            player.sendMessage(Thiss.saySth(v3, s3), false);
        }
    }
}
