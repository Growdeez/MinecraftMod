package com.example.firstblocks;

import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class ModParticleProviders {

    @SubscribeEvent
    public static void registerParticleProviders(
            RegisterParticleProvidersEvent event
    ) {
        event.registerSpriteSet(
                ModParticles.EARTH_WAND_PARTICLE.get(),
                EarthWandParticle.Provider::new
        );

        event.registerSpriteSet(
                ModParticles.EARTH_WAND_EXPLOSION_PARTICLE.get(),
                EarthWandExplosionParticle.Provider::new
        );

        event.registerSpriteSet(
                ModParticles.WHITE_SQUARE_PARTICLE.get(),
                WhiteSquareParticle.Provider::new
        );
    }
}