
package com.example.firstblocks;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.renderer.state.level.QuadParticleRenderState;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.joml.Quaternionf;

public class WhiteSquareParticle extends FlameParticle {

    private final int face;
    private final Quaternionf faceRotation;


    protected WhiteSquareParticle(
            ClientLevel level,
            double x,
            double y,
            double z,
            int face,
            TextureAtlasSprite sprite,
            RandomSource random
    ) {
        super(level, x, y, z, 0.0, 0.0, 0.0, sprite);

        this.face = face;
        this.faceRotation = getRotationForFace(face);

        this.lifetime = 20;
        this.setParticleSpeed(0.0, 0.0, 0.0);

        this.setColorForHue(random.nextFloat());

        this.setAlpha(1.0F);
        this.scale(0.5F);
    }

    private static Quaternionf getRotationForFace(int face) {
        return switch (face) {
            case 0 -> new Quaternionf().rotateX((float) -Math.PI / 2.0F); // Верх
            case 1 -> new Quaternionf().rotateX((float) Math.PI / 2.0F);  // Низ
            case 2 -> new Quaternionf().rotateY((float) Math.PI);         // Север
            case 3 -> new Quaternionf();                                  // Юг
            case 4 -> new Quaternionf().rotateY((float) Math.PI / 2.0F);  // Восток
            case 5 -> new Quaternionf().rotateY((float) -Math.PI / 2.0F); // Запад
            default -> new Quaternionf();
        };
    }

    private void setColorForHue(float hue) {
        float r;
        float g;
        float b;

        float h = hue * 6.0F;
        int sector = (int) h;

        float fraction = h - sector;
        float q = 1.0F - fraction;
        float t = fraction;

        switch (sector % 6) {
            case 0 -> {
                r = 1.0F;
                g = t;
                b = 0.0F;
            }
            case 1 -> {
                r = q;
                g = 1.0F;
                b = 0.0F;
            }
            case 2 -> {
                r = 0.0F;
                g = 1.0F;
                b = t;
            }
            case 3 -> {
                r = 0.0F;
                g = q;
                b = 1.0F;
            }
            case 4 -> {
                r = t;
                g = 0.0F;
                b = 1.0F;
            }
            default -> {
                r = 1.0F;
                g = 0.0F;
                b = q;
            }
        }

        this.setColor(r, g, b);
    }

    @Override
    public void extract(
            QuadParticleRenderState state,
            Camera camera,
            float partialTickTime
    ) {
        this.extractRotatedQuad(
                state,
                camera,
                this.faceRotation,
                partialTickTime
        );
    }

    @Override
    public int getLightCoords(float partialTick) {
        return 15728880;
    }

    @Override
    public float getQuadSize(float partialTick) {
        return 0.08F;
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
            int face = (int) xAux;

            return new WhiteSquareParticle(
                    level,
                    x,
                    y,
                    z,
                    face,
                    this.sprite.get(random),
                    random
            );
        }
    }
}
