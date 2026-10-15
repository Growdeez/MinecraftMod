package com.example.firstblocks;

import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class EarthWandParticle extends FlameParticle {

    protected EarthWandParticle(
            ClientLevel level,
            double x,
            double y,
            double z,
            double xd,
            double yd,
            double zd,
            TextureAtlasSprite sprite
    ) {
        super(level, x, y, z, xd, yd, zd, sprite);
        this.scale(2.5F);
        this.setColor(0.2F, 1.0F, 0.2F);
        this.lifetime = 6;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;

        public Provider(SpriteSet sprite) {
            this.sprite = sprite;
        }

        @Override
        public Particle createParticle(
                SimpleParticleType options,
                ClientLevel level,
                double x,
                double y,
                double z,
                double xAux,
                double yAux,
                double zAux,
                RandomSource random
        ) {
            return new EarthWandParticle(
                    level,
                    x,
                    y,
                    z,
                    xAux,
                    yAux,
                    zAux,
                    this.sprite.get(random)
            );
        }
    }
}