package com.arnav.cartographersgrief.block;

import com.arnav.cartographersgrief.registry.ModBlockEntityTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GriefSkullBlock extends BlockWithEntity {
    public static final MapCodec<GriefSkullBlock> CODEC = createCodec(GriefSkullBlock::new);

    public GriefSkullBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GriefSkullBlockEntity(pos, state);
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if (!world.isClient()) {
            BlockEntity be = world.getBlockEntity(pos);
            if (be instanceof GriefSkullBlockEntity skull) {
                long day = skull.getDeathTick() / 24000L + 1;
                player.sendMessage(Text.literal(
                    "§5[Cartographer] §r" + skull.getPlayerName() +
                    " §7died here on §5Day " + day + "§7 — " + skull.getDeathMessage()
                ), false);
            }
        }
        return ActionResult.SUCCESS;
    }
}
