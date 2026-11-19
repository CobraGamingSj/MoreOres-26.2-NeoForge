package org.cobra.moreores.util;

import net.neoforged.neoforge.transfer.fluid.FluidResource;

public class FluidConversion {
    private FluidResource variant;
    private int amount;

    public FluidConversion(FluidResource variant, int amount) {
        this.variant = variant;
        this.amount = amount;
    }

    public FluidResource getVariant() {
        return variant;
    }

    public int getAmount() {
        return amount;
    }

    public int setAmount(int amount) {
        return this.amount = amount;
    }

    public static int convertDropletsToMb(int drops) {
        return (drops / 81);
    }

    public static int convertMbToDroplets(int milliBuckets) {
        return milliBuckets * 81;
    }
}
