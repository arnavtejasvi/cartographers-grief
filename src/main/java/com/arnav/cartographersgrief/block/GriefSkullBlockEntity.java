package com.arnav.cartographersgrief.block;

import com.arnav.cartographersgrief.registry.ModBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

public class GriefSkullBlockEntity extends BlockEntity {
    private long deathTick = 0L;
    private String deathMessage = "unknown causes";
    private String playerName = "Someone";

    public GriefSkullBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntityTypes.GRIEF_SKULL_ENTITY, pos, state);
    }

    public void setData(long deathTick, String deathMessage, String playerName) {
        this.deathTick = deathTick;
        this.deathMessage = deathMessage;
        this.playerName = playerName;
        markDirty();
    }

    public long getDeathTick()    { return deathTick; }
    public String getDeathMessage() { return deathMessage; }
    public String getPlayerName() { return playerName; }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.writeNbt(nbt, registries);
        nbt.putLong("deathTick", deathTick);
        nbt.putString("deathMessage", deathMessage);
        nbt.putString("playerName", playerName);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        super.readNbt(nbt, registries);
        deathTick = nbt.getLong("deathTick");
        deathMessage = nbt.getString("deathMessage");
        playerName = nbt.getString("playerName");
    }
}
