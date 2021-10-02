package dev.nickrobson.minecraft.playeranalytics.forge.listener;

import net.minecraftforge.eventbus.api.Event;

public final class ForgeEventUtil {
    private ForgeEventUtil() {}

    static boolean isEventCancelled(Event event) {
        return event.isCancelable() && event.isCanceled()
                || event.hasResult() && event.getResult() == Event.Result.DENY;
    }
}
