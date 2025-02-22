package com.darcosse.battlemons.mixin;

import com.cobblemon.mod.common.api.battles.interpreter.BattleContext;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleRegistry;
import com.cobblemon.mod.common.client.gui.battle.BattleOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.client.DeltaTracker;

import java.util.UUID;


@Mixin(BattleOverlay.class)
public class RenderWeather {

    private String weather = " no ";

    @Inject(method = "render", at = @At("TAIL"))
    private void findAndShowWeather(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {

        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        int x = screenWidth / 2 - 100;
        int y = screenHeight / 5 - 10;

        UUID playerUUID = Minecraft.getInstance().player.getUUID();
        PokemonBattle battle = BattleRegistry.INSTANCE.getBattleByParticipatingPlayerId(playerUUID);


        if(battle != null) {
            if(battle.getContextManager().get(BattleContext.Type.WEATHER) != null) {
                this.weather = battle.getContextManager().get(BattleContext.Type.WEATHER).iterator().next().getId();
            } else {
                this.weather = "No weather";
            }
        }

        context.drawString(
                Minecraft.getInstance().font,
                Component.literal("Weather : " + this.weather),
                x, y, 0xFFFFFF
        );

    }
}