package com.example.firstblocks;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;

public class EarthWandExplosionParticle extends FlameParticle {

    // =========================
    // НАСТРОЙКИ ЧАСТИЦЫ
    // =========================

    private static final int FADE_DURATION = 20;

    private static final int MIN_LIFETIME = 1;


    // =========================
    // ДАННЫЕ КОНКРЕТНОЙ ЧАСТИЦЫ
    // =========================

    private final float baseHue;

    // Через сколько тиков начать исчезновение
    private final int fadeDelay;

    // Через сколько тиков после создания
    // разрешить переливание цвета
    private final int colorDelay;


    protected EarthWandExplosionParticle(
            ClientLevel level,
            double x,
            double y,
            double z,
            double xd,
            double yd,
            double zd,
            TextureAtlasSprite sprite
    ) {

        /*
         * Мы передаём 0 скорости в super,
         * потому что xd/yd/zd здесь являются
         * не скоростью, а нашими данными.
         */
        super(
                level,
                x,
                y,
                z,
                0.0,
                0.0,
                0.0,
                sprite
        );

        // xAux
        this.baseHue =
                (float) Math.max(
                        0.0,
                        Math.min(1.0, xd)
                );

        // yAux
        this.fadeDelay =
                Math.max(
                        0,
                        (int) Math.round(yd)
                );

        // zAux
        this.colorDelay =
                Math.max(
                        0,
                        (int) Math.round(zd)
                );


        // Частица живёт достаточно долго,
        // чтобы дождаться fadeDelay и затем
        // плавно исчезнуть.
        this.lifetime =
                Math.max(
                        MIN_LIFETIME,
                        this.fadeDelay
                                + FADE_DURATION
                );


        this.scale(1.2F);

        this.setParticleSpeed(
                0.0,
                0.0,
                0.0
        );

        this.setColorForHue(
                this.baseHue
        );

        this.setAlpha(1.0F);
    }


    @Override
    public void tick() {

        super.tick();


        // ==================================================
        // 1. ЦВЕТ
        // ==================================================

        if (this.age < this.colorDelay) {

            // Пока рисунок ещё строится —
            // цвет полностью статичный.
            setColorForHue(
                    this.baseHue
            );

        } else {

            // После завершения построения
            // начинаем переливание.

            int animationAge =
                    this.age
                            - this.colorDelay;

            float progress =
                    (float) animationAge
                            / 200.0F;

            float hue =
                    this.baseHue
                            + progress;

            hue =
                    hue
                            - (float) Math.floor(hue);

            setColorForHue(hue);
        }


        // ==================================================
        // 2. ИСЧЕЗНОВЕНИЕ
        // ==================================================

        if (this.age < this.fadeDelay) {

            // Полностью видимая частица
            setAlpha(1.0F);

            return;
        }


        int fadeAge =
                this.age
                        - this.fadeDelay;


        float fadeProgress =
                (float) fadeAge
                        / (float) FADE_DURATION;


        fadeProgress =
                Math.max(
                        0.0F,
                        Math.min(
                                1.0F,
                                fadeProgress
                        )
                );


        float alpha =
                1.0F - fadeProgress;


        setAlpha(alpha);
    }


    // ==================================================
    // HSV → RGB
    // ==================================================

    private void setColorForHue(
            float hue
    ) {

        float r;
        float g;
        float b;


        float h =
                hue * 6.0F;

        int sector =
                (int) h;

        float fraction =
                h - sector;

        float q =
                1.0F - fraction;

        float t =
                fraction;


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


        this.setColor(
                r,
                g,
                b
        );
    }


    // ==================================================
    // PROVIDER
    // ==================================================

    public static class Provider
            implements ParticleProvider<SimpleParticleType> {

        private final SpriteSet sprite;


        public Provider(
                SpriteSet sprite
        ) {
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

            return new EarthWandExplosionParticle(
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