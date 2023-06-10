package ru.ddr.revivemod.GUI;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.BookViewScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class ScrollEditScreen extends Screen {
    private final Player player;
    private final InteractionHand hand;
    private final Minecraft minecraft;
    private final Screen lastscreen;
    private EditBox nameEdit;
    private Button buttonRevive;

    protected ScrollEditScreen(Minecraft minecraft, Player player, InteractionHand interactionHand) {
        super(Component.literal(""));
        this.player = player;
        this.minecraft = minecraft;
        this.hand = interactionHand;
        this.lastscreen = minecraft.screen;
    }

    public void init() {
        this.nameEdit = new EditBox(this.font, this.width / 2 - 100, 66, 200, 20, Component.translatable("addServer.enterName"));
        this.addWidget(this.nameEdit);
        this.buttonRevive = this.addRenderableWidget(Button.builder(Component.literal("Revive"), (press) -> this.onPress()).bounds(this.width / 2 - 100, this.height / 4 + 96 + 18, 200, 20).build());
    }

    public void render(PoseStack poseStack, int p_98105_, int p_98106_, float p_98107_){
        this.renderBackground(poseStack);
        RenderSystem.setShaderTexture(0, BookViewScreen.BOOK_LOCATION);
        this.nameEdit.render(poseStack, p_98105_, p_98106_, p_98107_);
        this.buttonRevive.render(poseStack, p_98105_, p_98106_, p_98107_);
        super.render(poseStack, p_98105_, p_98106_, p_98107_);
    }

    private void onPress(){
        String name = this.nameEdit.getValue();

        ServerPlayer playerToRevive = this.minecraft.getSingleplayerServer().getPlayerList().getPlayerByName(name);

        if (playerToRevive == null){
            return;
        }

        if(Objects.equals(playerToRevive.getName().toString(), this.player.getName().toString())) {
            return;
        }

        playerToRevive.setPos(new Vec3(this.player.getX(),this.player.getY(),this.player.getZ()));
        playerToRevive.setGameMode(GameType.DEFAULT_MODE);
        this.player.setItemInHand(this.hand, ItemStack.EMPTY);
        this.minecraft.setScreen(this.lastscreen);
    }

}
