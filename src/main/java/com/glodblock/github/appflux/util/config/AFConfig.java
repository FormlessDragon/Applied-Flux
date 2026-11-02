package com.glodblock.github.appflux.util.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import java.io.File;

public final class AFConfig {

    private static int fluxPerByte = 1024 * 1024 * 4;
    private static long fluxAccessorIO = 0L;
    private static boolean selfCharge = false;
    private static boolean pullFE = false;

    private AFConfig() {
    }

    public static void load(File file) {
        Configuration config = new Configuration(file);
        try {
            config.load();
            fluxPerByte = config.getInt("amount", Configuration.CATEGORY_GENERAL, fluxPerByte, 1, Integer.MAX_VALUE, "FE stored per byte.");
            Property ioLimit = config.get(Configuration.CATEGORY_GENERAL, "io_limit", Long.toString(fluxAccessorIO), "The I/O limit of Flux Accessor. 0 means no limitation.");
            fluxAccessorIO = parseLongCompat(ioLimit.getString(), fluxAccessorIO);
            if (fluxAccessorIO < 0) {
                fluxAccessorIO = 0;
            }
            ioLimit.set(Long.toString(fluxAccessorIO));
            selfCharge = config.getBoolean("enable", Configuration.CATEGORY_GENERAL, selfCharge, "Allow Flux Accessor to charge ME network with stored FE.");
            pullFE = config.getBoolean("enable_FE_pull", Configuration.CATEGORY_GENERAL, pullFE, "Allow ME Import Bus to pull FE.");
        } finally {
            if (config.hasChanged()) {
                config.save();
            }
        }
    }

    public static int getFluxPerByte() {
        return fluxPerByte;
    }

    public static long getFluxAccessorIO() {
        return fluxAccessorIO <= 0 ? Long.MAX_VALUE : fluxAccessorIO;
    }

    public static boolean selfCharge() {
        return selfCharge;
    }

    public static boolean pullFE() {
        return pullFE;
    }

    private static long parseLongCompat(String value, long fallback) {
        if (value == null) {
            return fallback;
        }
        String trimmed = value.trim();
        try {
            return Long.parseLong(trimmed);
        } catch (NumberFormatException ignored) {
            try {
                return (long) Double.parseDouble(trimmed);
            } catch (NumberFormatException ignoredToo) {
                return fallback;
            }
        }
    }
}
