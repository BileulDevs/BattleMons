package com.darcosse.battlemons.mixin;

import com.cobblemon.mod.common.api.battles.interpreter.BattleContext;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleRegistry;
import com.cobblemon.mod.common.client.CobblemonClient;
import com.cobblemon.mod.common.client.gui.battle.BattleOverlay;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;


@Mixin(BattleOverlay.class)
public class RenderTerrains {

    private String terrain = "none";

    @Inject(method = "render", at = @At("TAIL"))
    private void findAndShowTerrain(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {

        this.terrain = "none";

        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        int iconSize = 32;
        int x = (screenWidth / 2) - (iconSize / 2);
        int y = screenHeight - 40 - iconSize;

        UUID playerUUID = Minecraft.getInstance().player.getUUID();
        PokemonBattle battle = BattleRegistry.INSTANCE.getBattleByParticipatingPlayerId(playerUUID);


        if(battle != null) {
            if(battle.getContextManager().get(BattleContext.Type.TERRAIN) != null) {
                if(!battle.getContextManager().get(BattleContext.Type.TERRAIN).isEmpty()) {
                    this.terrain = battle.getContextManager().get(BattleContext.Type.TERRAIN).iterator().next().getId();
                } else {
                    this.terrain = "none";
                }
            }
        }

        if(this.terrain != null && CobblemonClient.INSTANCE.getBattle() != null && !CobblemonClient.INSTANCE.getBattle().getMinimised() && CobblemonClient.INSTANCE.getBattle().getMustChoose()) {
            context.blit(
                    ResourceLocation.parse("battlemons:textures/gui/terrain/"+this.terrain+".png"),
                    46, screenHeight - 108,
                    0, 0,
                    iconSize, iconSize / 2,
                    iconSize, iconSize / 2
            );

        }
    }
}