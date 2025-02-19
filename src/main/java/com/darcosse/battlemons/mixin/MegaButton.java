package com.darcosse.battlemons.mixin;

import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.gui.battle.BattleOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.DeltaTracker;



@Mixin(BattleOverlay.class)
public class MegaButton {

    @Inject(method = "render", at = @At("TAIL"))
    private void addMegaButton(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {

        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        int x = screenWidth / 2 - 100;
        int y = screenHeight / 5 - 5;

        context.drawString(
                Minecraft.getInstance().font,
                Component.literal("Your Pokémon: " + CobblemonClient.INSTANCE.getBattle().getSide1().getActors().getFirst().getActivePokemon().getFirst().getBattlePokemon().getSpecies().getName()),
                x, y, 0xFFFFFF, false
        );
        context.drawString(
                Minecraft.getInstance().font,
                Component.literal("Opponent: " + CobblemonClient.INSTANCE.getBattle().getSide2().getActors().getFirst().getActivePokemon().getFirst().getBattlePokemon().getSpecies().getName()),
                x, y + 15, 0xFF5555, false
        );
    }
}
