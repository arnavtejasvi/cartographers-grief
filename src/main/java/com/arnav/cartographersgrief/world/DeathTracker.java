package com.arnav.cartographersgrief.world;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeathTracker extends PersistentState {
    private static final String KEY = "cartographers_grief_deaths";
    private static final Type<DeathTracker> TYPE = new Type<>(
        DeathTracker::new,
        (nbt, registries) -> fromNbt(nbt),
        null
    );

    private final List<DeathRecord> records = new ArrayList<>();

    public static DeathTracker get(ServerWorld world) {
        PersistentStateManager mgr = world.getPersistentStateManager();
        return mgr.getOrCreate(TYPE, KEY);
    }

    public void add(DeathRecord record) {
        records.add(record);
        markDirty();
    }

    public void remove(BlockPos pos) {
        records.removeIf(r -> r.pos().equals(pos));
        markDirty();
    }

    public List<DeathRecord> getAll() {
        return Collections.unmodifiableList(records);
    }

    @Override
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registries) {
        NbtList list = new NbtList();
        for (DeathRecord r : records) {
            NbtCompound entry = new NbtCompound();
            entry.putLong("pos", r.pos().asLong());
            entry.putLong("tick", r.tick());
            entry.putString("message", r.message());
            entry.putString("player", r.player());
            list.add(entry);
        }
        nbt.put("records", list);
        return nbt;
    }

    private static DeathTracker fromNbt(NbtCompound nbt) {
        DeathTracker tracker = new DeathTracker();
        NbtList list = nbt.getList("records", NbtElement.COMPOUND_TYPE);
        for (int i = 0; i < list.size(); i++) {
            NbtCompound entry = list.getCompound(i);
            tracker.records.add(new DeathRecord(
                BlockPos.fromLong(entry.getLong("pos")),
                entry.getLong("tick"),
                entry.getString("message"),
                entry.getString("player")
            ));
        }
        return tracker;
    }
}
