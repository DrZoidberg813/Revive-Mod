package ru.ddr.revivemod.GUI;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;

public class ScrollGUI{
    public static void openItemGUI(Minecraft minecraft, InteractionHand interactionHand, Player player) {

        if (minecraft.level == null) {
            return;
        }

        if(player instanceof ServerPlayer){
            return;
        }

        minecraft.setScreen(new ScrollEditScreen(minecraft, player, interactionHand));
    }
}
