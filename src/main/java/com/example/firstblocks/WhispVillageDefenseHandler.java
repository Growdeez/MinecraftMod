package com.example.firstblocks;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.villager.Villager;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;

public class WhispVillageDefenseHandler {

    private static final double DEFENSE_RADIUS = 16.0;

    @SubscribeEvent
    public static void onVillagerDamaged(
            LivingDamageEvent.Post event
    ) {
        // Реагируем только на потерю здоровья поселенцем.
        if (!(event.getEntity() instanceof Villager villager)) {
            return;
        }

        if (event.getHealthDamage() <= 0.0F) {
            return;
        }

        // Получаем игрока, ответственного за нанесение урона.
        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }

        // Не реагируем на игроков в креативе и режиме наблюдателя.
        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        // Работаем только на серверной стороне.
        if (!(villager.level() instanceof ServerLevel level)) {
            return;
        }

        // Все живые виспы поблизости начинают защищать поселенца.
        level.getEntitiesOfClass(
                Whisp.class,
                villager.getBoundingBox().inflate(DEFENSE_RADIUS),
                Whisp::isAlive
        ).forEach(whisp -> whisp.setTarget(player));
    }
}