package ru.ddr.revivemod.Items;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import ru.ddr.revivemod.GUI.ScrollGUI;

public class ReviveScrollItem extends Item{
    public ReviveScrollItem(Properties p_41383_) {
        super(p_41383_);
    }

    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        Minecraft minecraft = Minecraft.getInstance();
        ScrollGUI.openItemGUI(minecraft, interactionHand, player);

        return InteractionResultHolder.sidedSuccess(itemStack, level.isClientSide());
    }

}
