package com.arnav.cartographersgrief.item;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class MemoryOrbItem extends Item {

    private static final String TAG_X   = "MemX";
    private static final String TAG_Y   = "MemY";
    private static final String TAG_Z   = "MemZ";
    private static final String TAG_DIM = "MemDim";

    public MemoryOrbItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);

        if (world.isClient()) return TypedActionResult.success(stack);

        NbtComponent nbtComponent = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
        NbtCompound nbt = nbtComponent.copyNbt();
        String currentDim = world.getRegistryKey().getValue().toString();

        if (!nbt.contains(TAG_X)) {
            BlockPos pos = player.getBlockPos();
            nbt.putInt(TAG_X, pos.getX());
            nbt.putInt(TAG_Y, pos.getY());
            nbt.putInt(TAG_Z, pos.getZ());
            nbt.putString(TAG_DIM, currentDim);
            stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(nbt));
            player.sendMessage(Text.literal(
                "§5[Cartographer] §7Memory captured at §5" + pos.getX() + ", " + pos.getY() + ", " + pos.getZ() + "§7."
            ), false);
        } else {
            int mx = nbt.getInt(TAG_X);
            int my = nbt.getInt(TAG_Y);
            int mz = nbt.getInt(TAG_Z);
            String dim = nbt.getString(TAG_DIM);

            BlockPos here = player.getBlockPos();
            double dist = Math.sqrt(
                Math.pow(here.getX() - mx, 2) +
                Math.pow(here.getY() - my, 2) +
                Math.pow(here.getZ() - mz, 2)
            );

            String dimNote = dim.equals(currentDim) ? "" : " §c(different dimension)";
            player.sendMessage(Text.literal(
                "§5[Cartographer] §7Memory: §5" + mx + ", " + my + ", " + mz +
                "§7 — §5" + (int) dist + " §7blocks away" + dimNote
            ), false);
        }

        return TypedActionResult.success(stack);
    }
}
