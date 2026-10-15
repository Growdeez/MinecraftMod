package com.example.firstblocks;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;


public class EarthWandItem extends Item {

    public EarthWandItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {


        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        }

        Projectile.spawnProjectileFromRotation(
                EarthWandProjectile::new,
                serverLevel,
                player.getItemInHand(hand),
                player,
                0.0F,
                1.5F,
                0.0F
        );

        return InteractionResult.SUCCESS;
    }
}
