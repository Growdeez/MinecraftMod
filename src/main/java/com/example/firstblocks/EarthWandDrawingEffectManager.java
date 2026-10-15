package com.example.firstblocks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

public class EarthWandDrawingEffectManager {

    private static final List<EarthWandDrawingEffect> EFFECTS =
            new ArrayList<>();

    public static void addEffect(EarthWandDrawingEffect effect) {
        EFFECTS.add(effect);
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {

        Iterator<EarthWandDrawingEffect> iterator =
                EFFECTS.iterator();

        while (iterator.hasNext()) {

            EarthWandDrawingEffect effect = iterator.next();

            effect.tick();

            if (effect.isFinished()) {
                iterator.remove();
            }
        }
    }
}