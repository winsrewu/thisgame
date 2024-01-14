package org.jawbts.thisgame;
import net.minecraft.block.Blocks;import net.minecraft.entity.Entity;import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;import net.minecraft.entity.effect.StatusEffectInstance;import net.minecraft.entity.effect.StatusEffects;import net.minecraft.item.ItemStack;import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;import net.minecraft.world.explosion.Explosion;import net.minecraft.world.explosion.ExplosionBehavior;
import java.util.ArrayList;import java.util.Iterator;import java.util.List;import java.util.Random;
public class Rocket {
    private static final List<Rocket> rocketList = new ArrayList<>();private static final Random rm = new Random();protected Entity passenger;protected ServerWorld world = null;protected List<ServerPlayerEntity> viewers = null;protected int nextStageCount = 100;protected LaunchStage curStage = LaunchStage.PREPARE;private Vec3d playerVec = Vec3d.ZERO;
    Rocket(Entity passenger) {this.passenger = passenger;if (passenger.getEntityWorld().getPlayers().get(0) instanceof ServerPlayerEntity) {world = ((ServerPlayerEntity) passenger.getEntityWorld().getPlayers().get(0)).getServerWorld();
            if (passenger.getEntityWorld() instanceof ServerWorld) viewers = ((ServerWorld) passenger.getEntityWorld()).getPlayers();}if (world == null || viewers == null) {return;}
        rocketList.add(this);if (passenger.getArmorItems() instanceof DefaultedList<ItemStack>) {((DefaultedList<ItemStack>) passenger.getArmorItems()).set(3, new ItemStack(Items.GLASS));}
    }public static void tickAll() {Iterator<Rocket> iterator = rocketList.iterator();while (iterator.hasNext()) {Rocket next = iterator.next();if (next.curStage == LaunchStage.END) {iterator.remove();} else {next.tick();}}}
    private <T extends net.minecraft.particle.ParticleEffect> void spawnParticles(T particle, double x, double y, double z, double deltaX, double deltaY, double deltaZ, double speed) {
        for (ServerPlayerEntity viewer : viewers) {world.spawnParticles(viewer, particle, true, x, y - 6, z, 10, deltaX, deltaY, deltaZ, speed);}}
    private <T extends net.minecraft.particle.ParticleEffect> void spawnParticles(T particle, double x, double y, double z, double deltaX, double deltaY, double deltaZ) {spawnParticles(particle, x, y, z, deltaX, deltaY, deltaZ, 1);}
    private void addVec(Entity entity, double x, double y, double z) {if (entity instanceof ServerPlayerEntity) {playerVec = playerVec.add(x, y, z);} else {entity.addVelocity(x, y, z);}}
    private BlockPos getTopPos(World world, BlockPos blockPos) {for (int y = 1; y < 256; y++) {if (world.getBlockState(new BlockPos(blockPos.getX(), y, blockPos.getZ())).isAir()) {return new BlockPos(blockPos.getX(), y, blockPos.getZ());}}
        return new BlockPos(blockPos.getX(), 255, blockPos.getZ());}public void tick() {
        if (world.isClient() || !passenger.isAlive() || !passenger.isLiving()) return;if (curStage == LaunchStage.END) {
            return;}if (nextStageCount-- <= 0) {curStage = curStage.nextStage();if (curStage == LaunchStage.END) return;nextStageCount = curStage.getNextStageCount();}switch (curStage) {case PREPARE -> {
                if (world.getTime() % 2 == 0) {passenger.setPos(passenger.getX() + 0.01, passenger.getY(), passenger.getZ());
                } else {passenger.setPos(passenger.getX() - 0.01, passenger.getY(), passenger.getZ());}
                if (passenger instanceof LivingEntity) {((LivingEntity) passenger).addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 60, 5));}}
            case FIRE -> {world.createExplosion(null, DamageSource.explosion((LivingEntity) passenger), (ExplosionBehavior) null,
                    passenger.getX(), passenger.getY() - 20, passenger.getZ(), 10.0F, true, Explosion.DestructionType.BREAK);spawnParticles(ParticleTypes.SMOKE, passenger.getX(), passenger.getY() - 0.5, passenger.getZ(), 0, -1, 0);for (int i = 0; i < 5; i++) {spawnParticles(ParticleTypes.LAVA, passenger.getX() + 0.5, passenger.getY() - 0.5, passenger.getZ() + 0.5, rm.nextFloat() / 2.0F, -1, rm.nextFloat() / 2.0F);
                    spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, passenger.getX() + 0.5, passenger.getY() - 0.5, passenger.getZ() + 0.5, rm.nextFloat() * 10.0F, -1, rm.nextFloat() * 10.0F, 0.001);
                    spawnParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, passenger.getX() + 0.5, passenger.getY() - 0.5, passenger.getZ() + 0.5, rm.nextFloat() * 10.0F, -1, rm.nextFloat() * 10.0F, 0.001);}
                addVec(passenger, 0, 0.0201, 0);}case LOW_SPEED -> {
                spawnParticles(ParticleTypes.LARGE_SMOKE, passenger.getX(), passenger.getY() - 0.5, passenger.getZ(), 0, -1, 0, 0.1);for (int i = 0; i < 5; i++) {
                    spawnParticles(ParticleTypes.LAVA, passenger.getX() + 0.5, passenger.getY() - 0.5, passenger.getZ() + 0.5, rm.nextFloat() / 2.0F, -1, rm.nextFloat() / 2.0F);}
                addVec(passenger, 0, 0.025, 0);for (int i = -1; i <= 1; i++) {for (int j = -1; j <= 1; j++) {world.breakBlock(passenger.getBlockPos().add(i, 3, j), true);}}}
            case HIGH_SPEED -> {for (int i = 0; i < 5; i++) {spawnParticles(ParticleTypes.LAVA, passenger.getX() + 0.5, passenger.getY() - 0.5, passenger.getZ() + 0.5, rm.nextFloat() / 100.0F, -1, rm.nextFloat() / 100.0F, 0.8);}
                addVec(passenger, 0, 0.05, 0);for (int i = -1; i <= 1; i++) {for (int j = -1; j <= 1; j++) {world.breakBlock(passenger.getBlockPos().add(i, 2, j), true);}}if (passenger.getY() > 500) {
                    curStage = curStage.nextStage();}}case FLOAT -> {curStage = curStage.nextStage();}case RETURN -> {if (passenger.getVelocity().getY() > -10 && playerVec.getY() > -10) addVec(passenger, 0, -1, 0);
                if (passenger.getY() < 500) {for (int i = 0; i < 2; i++) {passenger.getEntityWorld().setBlockState(getTopPos(passenger.getEntityWorld(), passenger.getBlockPos()), Blocks.COBWEB.getDefaultState());}curStage = curStage.nextStage();nextStageCount = curStage.getNextStageCount();}}}if (passenger instanceof ServerPlayerEntity) {passenger.requestTeleport(passenger.getX() + playerVec.getX(), passenger.getY() + playerVec.getY(), passenger.getZ() + playerVec.getZ());}}
    public enum LaunchStage {
        PREPARE(100), FIRE(100), LOW_SPEED(600), HIGH_SPEED(Integer.MAX_VALUE), FLOAT(Integer.MAX_VALUE), RETURN((Integer.MAX_VALUE)), HIT_GROUND(20), END(Integer.MAX_VALUE);
        private static final LaunchStage[] values = values();private final int nextStageCount;LaunchStage(int nextStageCount) {this.nextStageCount = nextStageCount;}public int getNextStageCount() {return nextStageCount;
        }public LaunchStage nextStage() {return (this.ordinal() >= values.length - 1) ? END : values[this.ordinal() + 1];}}}
