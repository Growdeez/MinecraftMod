package com.example.firstblocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModParticles {

    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(
                    Registries.PARTICLE_TYPE,
                    FirstBlocks.MODID
            );

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> EARTH_WAND_PARTICLE =
            PARTICLE_TYPES.register(
                    "earth_wand_particle",
                    () -> new SimpleParticleType(false)
            );
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> EARTH_WAND_EXPLOSION_PARTICLE =
            PARTICLE_TYPES.register(
                    "earth_wand_explosion_particle",
                    () -> new SimpleParticleType(false)
            );

    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> WHITE_SQUARE_PARTICLE =
            PARTICLE_TYPES.register(
                    "white_square_particle",
                    () -> new SimpleParticleType(false)
            );
}