package ru.ddr.revivemod.GUI;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.glfw.GLFW;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class ScrollEditScreen extends Screen {
    private final Player player;
    private final InteractionHand hand;
    private final Minecraft minecraft;
    private final Screen lastscreen;
    private EditBox nameEdit;
    private Button buttonRevive;
    public static final ResourceLocation SCROLL_BACKGROUND_LOCATION = new ResourceLocation("rm:textures/gui/revive_scroll_texture.png");

    protected ScrollEditScreen(Minecraft minecraft, Player player, InteractionHand interactionHand) {
        super(Component.literal(""));
        this.player = player;
        this.minecraft = minecraft;
        this.hand = interactionHand;
        this.lastscreen = minecraft.screen;
    }

    public void init() {
        int i = (this.width - 245) / 2;
        this.nameEdit = new EditBox(this.font, i + 94, 158, 100, 20, Component.translatable("addServer.enterName"));
        this.nameEdit.setBordered(false);
        this.nameEdit.setFocused(true);
        this.nameEdit.setTextColor(0xbc8864);
        this.addWidget(this.nameEdit);
        this.buttonRevive = this.addRenderableWidget(Button.builder(Component.literal(this.player.getName().getString()), (press) -> this.onPress()).bounds(i + 74, 192, 100, 20).build());
        this.buttonRevive.setAlpha(0);
        this.buttonRevive.setFGColor(0xbc8864);
    }

    public void render(PoseStack poseStack, int p_98105_, int p_98106_, float p_98107_){
        this.renderBackground(poseStack);
        RenderSystem.setShaderTexture(0, SCROLL_BACKGROUND_LOCATION);
        int i = (this.width - 245) / 2;
        blit(poseStack, i, 0, 0, 0, 245, 245);
        this.nameEdit.render(poseStack, p_98105_, p_98106_, p_98107_);
        this.buttonRevive.render(poseStack, p_98105_, p_98106_, p_98107_);
        super.render(poseStack, p_98105_, p_98106_, p_98107_);
    }

    private void onPress(){
        String name = this.nameEdit.getValue();
        PlayerList serverPlayerList = this.minecraft.getSingleplayerServer().getPlayerList();

        ServerPlayer playerToRevive = serverPlayerList.getPlayerByName(name);

        if (playerToRevive == null){
            return;
        }

        if(Objects.equals(playerToRevive.getName().toString(), this.player.getName().toString())) {
            return;
        }

        playerToRevive.setPos(new Vec3(this.player.getX(),this.player.getY(),this.player.getZ()));
        playerToRevive.setGameMode(GameType.DEFAULT_MODE);
        this.minecraft.setScreen(this.lastscreen);
        serverPlayerList.getPlayerByName(this.player.getName().getString()).setItemInHand(this.hand, ItemStack.EMPTY);
    }

    public boolean charTyped(char p_98085_, int p_98086_) {
        if (super.charTyped(p_98085_, p_98086_)) {
            return true;
        } else if (SharedConstants.isAllowedChatCharacter(p_98085_)) {
            this.nameEdit.insertText(Character.toString(p_98085_));
            return true;
        } else {
            return false;
        }
    }

    public boolean keyPressed(int p_98100_, int p_98101_, int p_98102_) {
        if (super.keyPressed(p_98100_, p_98101_, p_98102_)) {
            return true;
        } else {
            return this.scrollKeyPressed(p_98100_);
        }
    }

    private boolean scrollKeyPressed(int key) {
        if (key == GLFW.GLFW_KEY_BACKSPACE) {
                this.nameEdit.deleteChars(-1);
                return true;
            }
        if (key == GLFW.GLFW_KEY_ENTER){
            this.onPress();
            return true;
        }
        return false;
    }
}
