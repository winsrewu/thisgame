package org.jawbts.thisgame.mixin;import net.minecraft.network.MessageType;import net.minecraft.server.PlayerManager;import net.minecraft.server.network.ServerPlayerEntity;import net.minecraft.text.Text;import net.minecraft.text.TranslatableText;import org.jawbts.thisgame.GameManager;import org.spongepowered.asm.mixin.Mixin;import org.spongepowered.asm.mixin.injection.At;import org.spongepowered.asm.mixin.injection.Inject;import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

                    import java.util.UUID;
                    import java.util.function.Function;

                    @Mixin(PlayerManager.class)
            public class PlayerManagerMixin {
        @Inject(
                method = "broadcast(Lnet/minecraft/text/Text;Ljava/util/function/Function;Lnet/minecraft/network/MessageType;Ljava/util/UUID;)V",
                            at = @At("HEAD")
                        )
                        private void broadcast(Text serverMessage, Function<ServerPlayerEntity, Text> playerMessageFactory, MessageType playerMessageType, UUID sender, CallbackInfo ci) {
                    if (serverMessage instanceof TranslatableText text) {if (text.getKey().equals("chat.type.text") && text.getArgs().length > 0 && text.getArgs()[1] instanceof String) {
                                    if (((String) text.getArgs()[1]).equalsIgnoreCase("this game"))
                        GameManager.getInstance().startGame(((PlayerManager) (Object) this).getPlayer(sender));}}}}
