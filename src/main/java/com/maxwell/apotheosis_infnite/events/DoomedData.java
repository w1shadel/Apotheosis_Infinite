package com.maxwell.apotheosis_infnite.events;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class DoomedData extends SavedData {
    private boolean isDoomed = false;

    public boolean isDoomed() { return isDoomed; }
    public void setDoomed(boolean doomed) { this.isDoomed = doomed; this.setDirty(); }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("ApothInfiniteDoomed", this.isDoomed);
        return tag;
    }

    public static DoomedData load(CompoundTag tag) {
        DoomedData data = new DoomedData();
        data.isDoomed = tag.getBoolean("ApothInfiniteDoomed");
        return data;
    }

    public static DoomedData get(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(DoomedData::load, DoomedData::new, "apoth_infinite_doomed");
    }
}