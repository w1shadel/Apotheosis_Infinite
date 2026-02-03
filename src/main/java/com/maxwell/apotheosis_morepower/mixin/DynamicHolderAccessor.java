package com.maxwell.apotheosis_morepower.mixin;

import dev.shadowsoffire.placebo.reload.DynamicHolder;
import dev.shadowsoffire.placebo.reload.DynamicRegistry;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(value = DynamicHolder.class, remap = false)
public interface DynamicHolderAccessor {
    @Invoker("<init>")
    static <T> DynamicHolder<T> create(DynamicRegistry<? super T> registry, ResourceLocation id) {
        throw new UnsupportedOperationException();
    }
}