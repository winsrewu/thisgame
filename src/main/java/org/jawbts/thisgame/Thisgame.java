package org.jawbts.thisgame;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.util.ActionResult;

public class Thisgame implements ModInitializer {
    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        ServerTickEvents.END_SERVER_TICK.register((MinecraftServer) -> {
            GameManager.getInstance().tick();
        });
        ServerTickEvents.START_WORLD_TICK.register((MinecraftServer) -> {
            Rocket.tickAll();
        });
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClient) return ActionResult.PASS;
            GameManager.getInstance().onPlayerHitEvent(player, entity);
            return ActionResult.PASS;
        });
        UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClient) return ActionResult.PASS;
            GameManager.getInstance().onPlayerUseEvent(player, entity);
            return ActionResult.PASS;
        });
    }
}