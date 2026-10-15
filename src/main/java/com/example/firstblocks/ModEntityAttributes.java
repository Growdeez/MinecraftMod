package com.example.firstblocks;

import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.Mob;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public class ModEntityAttributes {

    @SubscribeEvent
    public static void createAttributes(EntityAttributeCreationEvent event) {
        event.put(
                ModEntities.WHISP.get(),
                Mob.createMobAttributes()
                        .add(Attributes.MAX_HEALTH, 24.0)
                        .add(Attributes.MOVEMENT_SPEED, 0.1)
                        .add(Attributes.FLYING_SPEED, 0.1)
                        .add(Attributes.FOLLOW_RANGE, 24.0)
                        .build()
        );
    }
}