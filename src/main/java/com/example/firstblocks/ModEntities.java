package com.example.firstblocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.Level;
import net.minecraft.world.entity.Mob;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(
                    Registries.ENTITY_TYPE,
                    FirstBlocks.MODID
            );

    public static final DeferredHolder<EntityType<?>, EntityType<EarthWandProjectile>> EARTH_WAND_PROJECTILE =
            ENTITY_TYPES.register(
                    "earth_wand_projectile",
                    () -> EntityType.Builder
                            .<EarthWandProjectile>of(
                                    EarthWandProjectile::new,
                                    MobCategory.MISC
                            )
                            .sized(0.25F, 0.25F)
                            .build(
                                    ResourceKey.create(
                                            Registries.ENTITY_TYPE,
                                            Identifier.fromNamespaceAndPath(
                                                    FirstBlocks.MODID,
                                                    "earth_wand_projectile"
                                            )
                                    )
                            )
            );

    public static final DeferredHolder<EntityType<?>, EntityType<Whisp>> WHISP =
            ENTITY_TYPES.register(
                    "whisp",
                    () -> EntityType.Builder
                            .<Whisp>of(Whisp::new, MobCategory.CREATURE)
                            .sized(0.5F, 0.5F)
                            .build(
                                    ResourceKey.create(
                                            Registries.ENTITY_TYPE,
                                            Identifier.fromNamespaceAndPath(
                                                    FirstBlocks.MODID,
                                                    "whisp"
                                            )
                                    )
                            )
            );

    public static final DeferredHolder<EntityType<?>, EntityType<WhispProjectile>> WHISP_PROJECTILE =
            ENTITY_TYPES.register(
                    "whisp_projectile",
                    () -> EntityType.Builder
                            .<WhispProjectile>of(
                                    WhispProjectile::new,
                                    MobCategory.MISC
                            )
                            .sized(0.25F, 0.25F)
                            .build(
                                    ResourceKey.create(
                                            Registries.ENTITY_TYPE,
                                            Identifier.fromNamespaceAndPath(
                                                    FirstBlocks.MODID,
                                                    "whisp_projectile"
                                            )
                                    )
                            )
            );
}