package com.darcosse.battlemons.mixin;

import com.cobblemon.mod.common.api.battles.interpreter.BattleContext;
import com.cobblemon.mod.common.api.battles.model.PokemonBattle;
import com.cobblemon.mod.common.battles.BattleRegistry;
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

import java.util.*;

import com.darcosse.battlemons.utils.Hazards;


@Mixin(BattleOverlay.class)
public class RenderHazards {

    private String hazards1 = "";
    private String hazards2 = "";

    private List<String> hazardsListSide1 = new LinkedList<>();
    private List<String> hazardsListSide2 = new LinkedList<>();

    private ArrayList<Hazards> countHazards(List<String> hazardList) {
        Map<String, Integer> hazardCountMap = new HashMap<>();

        for (String hazard : hazardList) {
            hazardCountMap.put(hazard, hazardCountMap.getOrDefault(hazard, 0) + 1);
        }

        ArrayList<Hazards> hazards = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : hazardCountMap.entrySet()) {
            hazards.add(new Hazards(entry.getKey(), entry.getValue()));
        }

        hazards.sort(Comparator.comparing(h -> h.count));

        return hazards;
    }


    @Inject(method = "render", at = @At("TAIL"))
    private void findAndShowHazards(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci) {

        this.hazardsListSide1.clear();
        this.hazardsListSide2.clear();

        int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        int x = screenWidth / 2 - 100;
        int y = screenHeight / 5 + 25;

        UUID playerUUID = Minecraft.getInstance().player.getUUID();
        PokemonBattle battle = BattleRegistry.INSTANCE.getBattleByParticipatingPlayerId(playerUUID);


        if(battle != null) {
            if(battle.getSide1().getContextManager().get(BattleContext.Type.HAZARD) != null) {
                this.hazards1 = battle.getSide1().getContextManager().get(BattleContext.Type.HAZARD).toString();
            } else {
                this.hazards1 = "No Hazards";
            }

            if(battle.getSide2().getContextManager().get(BattleContext.Type.HAZARD) != null) {
                this.hazardsListSide2.clear();
                battle.getSide2().getContextManager().get(BattleContext.Type.HAZARD).iterator().forEachRemaining(hazard -> {
                    this.hazardsListSide2.add(hazard.getId());
                });
            } else {
                this.hazards2 = "No Hazards";
            }
        }


        for(int i = 0; i < this.countHazards(this.hazardsListSide2).size(); ++i) {
            Hazards h = this.countHazards(this.hazardsListSide2).get(i);

            context.blit(
                    ResourceLocation.parse("battlemons:textures/gui/hazard/repeat_ball.png"),
                    x,
                    y,
                    16,
                    16,
                    16,
                    16

            );

            context.drawString(
                    Minecraft.getInstance().font,
                    Component.literal("Hazards S2 : "+h.name+", count: "+h.count),
                    x, y+(i*25), 0xFFFFFF
            );
        }

    }
}