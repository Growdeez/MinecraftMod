package com.example.firstblocks;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;

public class EarthWandDrawingEffect {

    private final ServerLevel level;

    private final double x;
    private final double y;
    private final double z;

    private int age = 0;

    // =========================
    // НАСТРОЙКИ РИСУНКА
    // =========================

    private final double circleRadius = 5.0;
    private final double circleCenterHeight = 14.0;

    private final double upperLineHeight = 6.0;

    private final double lineStep = 0.25;

    // Скорость построения рисунка
    private final double drawingSpeed = 0.8;

    // =========================
    // НАСТРОЙКИ ИСЧЕЗНОВЕНИЯ
    // =========================

    // Сколько тиков частица живёт полностью непрозрачной
    private final int particleFadeDelay = 30;

    // Сколько тиков занимает плавное исчезновение
    private final int particleFadeDuration = 20;

    // =========================

    // Случайный цвет всего конкретного рисунка
    private final float colorHue;

    private boolean finished = false;


    public EarthWandDrawingEffect(
            ServerLevel level,
            double x,
            double y,
            double z
    ) {
        this.level = level;
        this.x = x;
        this.y = y;
        this.z = z;

        // Каждый новый рисунок получает свой оттенок
        this.colorHue =
                RandomSource.create().nextFloat();
    }


    public void tick() {

        if (finished) {
            return;
        }

        age++;

        double previousHeight =
                (age - 1) * drawingSpeed;

        double currentHeight =
                age * drawingSpeed;

        drawNewParticles(
                previousHeight,
                currentHeight
        );

        // ============================================
        // Полная высота анимации
        // ============================================

        double circlesHeight =
                Math.PI * circleRadius;

        double upperLineStart =
                circleCenterHeight
                        + circlesHeight;

        double arcStartHeight =
                upperLineStart
                        + upperLineHeight;

        double totalHeight =
                arcStartHeight
                        + circleRadius;

        if (currentHeight >= totalHeight) {
            finished = true;
        }
    }


    private void drawNewParticles(
            double previousHeight,
            double currentHeight
    ) {

        double circleCenterY =
                y + circleCenterHeight;

        double leftCircleX =
                x - circleRadius;

        double rightCircleX =
                x + circleRadius;


        // ==================================================
        // 1. ЦЕНТРАЛЬНАЯ ЛИНИЯ
        // ==================================================

        double previousLineHeight =
                Math.min(
                        previousHeight,
                        circleCenterHeight
                );

        double currentLineHeight =
                Math.min(
                        currentHeight,
                        circleCenterHeight
                );

        drawNewLineParticles(
                previousLineHeight,
                currentLineHeight,
                x,
                y,
                z
        );


        // ==================================================
        // 2. КРУГИ
        // ==================================================

        if (currentHeight > circleCenterHeight) {

            double previousCircleHeight =
                    Math.max(
                            0.0,
                            previousHeight
                                    - circleCenterHeight
                    );

            double currentCircleHeight =
                    Math.min(
                            Math.PI * circleRadius,
                            currentHeight
                                    - circleCenterHeight
                    );

            double previousProgress =
                    previousCircleHeight
                            / (Math.PI * circleRadius);

            double currentProgress =
                    currentCircleHeight
                            / (Math.PI * circleRadius);

            drawNewCircleParticles(
                    leftCircleX,
                    circleCenterY,
                    previousProgress,
                    currentProgress,
                    0.0,
                    true
            );

            drawNewCircleParticles(
                    rightCircleX,
                    circleCenterY,
                    previousProgress,
                    currentProgress,
                    Math.PI,
                    false
            );
        }


        // ==================================================
        // 3. ВЕРХНИЕ ЛИНИИ
        // ==================================================

        double upperLineStart =
                circleCenterHeight
                        + Math.PI * circleRadius;

        if (currentHeight > upperLineStart) {

            double previousUpperHeight =
                    Math.max(
                            0.0,
                            previousHeight
                                    - upperLineStart
                    );

            double currentUpperHeight =
                    Math.min(
                            upperLineHeight,
                            currentHeight
                                    - upperLineStart
                    );

            drawNewLineParticles(
                    previousUpperHeight,
                    currentUpperHeight,
                    leftCircleX,
                    circleCenterY + circleRadius,
                    z
            );

            drawNewLineParticles(
                    previousUpperHeight,
                    currentUpperHeight,
                    rightCircleX,
                    circleCenterY + circleRadius,
                    z
            );
        }


        // ==================================================
        // 4. ВЕРХНИЙ ПОЛУКРУГ
        // ==================================================

        double arcStartHeight =
                upperLineStart
                        + upperLineHeight;

        if (currentHeight > arcStartHeight) {

            double previousArcHeight =
                    Math.max(
                            0.0,
                            previousHeight
                                    - arcStartHeight
                    );

            double currentArcHeight =
                    Math.min(
                            circleRadius,
                            currentHeight
                                    - arcStartHeight
                    );

            double previousProgress =
                    previousArcHeight
                            / circleRadius;

            double currentProgress =
                    currentArcHeight
                            / circleRadius;

            drawNewArcParticles(
                    previousProgress,
                    currentProgress
            );
        }
    }


    // ==================================================
    // НОВЫЕ ТОЧКИ ПРЯМОЙ ЛИНИИ
    // ==================================================

    private void drawNewLineParticles(
            double previousHeight,
            double currentHeight,
            double startX,
            double startY,
            double startZ
    ) {

        if (currentHeight <= previousHeight) {
            return;
        }

        int firstIndex;

        if (previousHeight <= 0.0) {
            firstIndex = 0;
        } else {
            firstIndex =
                    (int) Math.floor(
                            previousHeight / lineStep
                    ) + 1;
        }

        int lastIndex =
                (int) Math.floor(
                        currentHeight / lineStep
                );

        for (int i = firstIndex;
             i <= lastIndex;
             i++) {

            double offset =
                    i * lineStep;

            spawnParticle(
                    startX,
                    startY + offset,
                    startZ
            );
        }
    }


    // ==================================================
    // НОВЫЕ ТОЧКИ КРУГА
    // ==================================================

    private void drawNewCircleParticles(
            double centerX,
            double centerY,
            double previousProgress,
            double currentProgress,
            double startAngle,
            boolean clockwise
    ) {

        int particleCount = 32;

        int previousCount =
                (int) (
                        particleCount
                                * previousProgress
                );

        int currentCount =
                (int) (
                        particleCount
                                * currentProgress
                );

        double direction =
                clockwise
                        ? 1.0
                        : -1.0;

        for (int i = previousCount;
             i < currentCount;
             i++) {

            double angle =
                    startAngle
                            + direction
                            * (
                            2.0
                                    * Math.PI
                                    * i
                                    / particleCount
                    );

            double xOffset =
                    Math.cos(angle)
                            * circleRadius;

            double yOffset =
                    Math.sin(angle)
                            * circleRadius;

            spawnParticle(
                    centerX + xOffset,
                    centerY + yOffset,
                    z
            );
        }
    }


    // ==================================================
    // НОВЫЕ ТОЧКИ ВЕРХНЕГО ПОЛУКРУГА
    // ==================================================

    private void drawNewArcParticles(
            double previousProgress,
            double currentProgress
    ) {

        int particleCount = 32;

        int previousCount =
                (int) (
                        particleCount
                                * previousProgress
                );

        int currentCount =
                (int) (
                        particleCount
                                * currentProgress
                );

        double arcCenterX = x;

        double arcCenterY =
                y
                        + circleCenterHeight
                        + circleRadius
                        + upperLineHeight;

        for (int i = previousCount;
             i < currentCount;
             i++) {

            double angle =
                    Math.PI
                            - (
                            Math.PI
                                    * i
                                    / particleCount
                    );

            double particleX =
                    arcCenterX
                            + Math.cos(angle)
                            * circleRadius;

            double particleY =
                    arcCenterY
                            + Math.sin(angle)
                            * circleRadius;

            spawnParticle(
                    particleX,
                    particleY,
                    z
            );
        }
    }


    // ==================================================
    // СОЗДАНИЕ ОДНОЙ ЧАСТИЦЫ
    // ==================================================

    private void spawnParticle(
            double particleX,
            double particleY,
            double particleZ
    ) {

        /*
         * xSpeed -> hue
         * ySpeed -> задержка до начала исчезновения
         * zSpeed -> задержка до начала переливания цвета
         */

        double circlesHeight =
                Math.PI * circleRadius;

        double upperLineStart =
                circleCenterHeight
                        + circlesHeight;

        double arcStartHeight =
                upperLineStart
                        + upperLineHeight;

        double totalHeight =
                arcStartHeight
                        + circleRadius;

        int totalDrawingTicks =
                (int) Math.ceil(
                        totalHeight / drawingSpeed
                );

        // Сколько тиков осталось до полного
        // завершения построения рисунка.
        int colorDelay =
                Math.max(
                        0,
                        totalDrawingTicks - age
                );

        level.sendParticles(
                ModParticles.EARTH_WAND_EXPLOSION_PARTICLE.get(),

                particleX,
                particleY,
                particleZ,

                // ВАЖНО:
                // count = 0 позволяет передать
                // xSpeed/ySpeed/zSpeed напрямую
                0,

                // xDist -> hue
                colorHue,

                // yDist -> fade delay
                particleFadeDelay,

                // zDist
                colorDelay,

                // speed
                1.0
        );
    }


    public boolean isFinished() {
        return finished;
    }
}