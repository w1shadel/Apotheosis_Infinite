package com.maxwell.apotheosis_morepower.mixin;

import dev.shadowsoffire.placebo.util.StepFunction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = StepFunction.class, remap = false)
public class StepFunctionMixin {
    @Shadow
    @Final
    protected float min;
    @Shadow
    @Final
    protected float step;
    @Shadow
    @Final
    protected int steps;

    /**
     * @author Maxwell
     * @reason レベル 1.0 (100%) を超えても線形に数値が上がり続けるように制限を撤廃する
     */
    @Overwrite
    public float get(float level) {
        return this.min + (level * (float) this.steps * this.step);
    }
}