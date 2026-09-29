package com.example.sleeptracker.analysis;

import com.example.sleeptracker.SleepAnalysisResult;
import com.example.sleeptracker.model.SleepingSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

public class SleeplessNightsAnalysis
        implements Function<List<SleepingSession>, SleepAnalysisResult> {

    private static final LocalTime NIGHT_EVENING_START =
            LocalTime.of(18, 0);

    private static final LocalTime NIGHT_MORNING_END =
            LocalTime.of(6, 0);

    @Override
    public SleepAnalysisResult apply(
            List<SleepingSession> sessions) {

        if (sessions.isEmpty()) {
            return new SleepAnalysisResult(
                    "Количество бессонных ночей",
                    0L
            );
        }

        LocalDate firstNight =
                getFirstPotentialNight(sessions.get(0));

        LocalDate lastNight =
                getLastPotentialNight(
                        sessions.get(sessions.size() - 1)
                );

        if (firstNight.isAfter(lastNight)) {
            return new SleepAnalysisResult(
                    "Количество бессонных ночей",
                    0L
            );
        }

        long nights = ChronoUnit.DAYS.between(
                firstNight,
                lastNight
        ) + 1;

        long sleeplessNights = IntStream
                .rangeClosed(0, (int) nights - 1)
                .mapToObj(firstNight::plusDays)
                .filter(night ->
                        !hasSleepDuringNight(
                                sessions,
                                night
                        ))
                .count();

        return new SleepAnalysisResult(
                "Количество бессонных ночей",
                sleeplessNights
        );
    }

    private LocalDate getFirstPotentialNight(
            SleepingSession firstSession) {

        LocalDate date =
                firstSession
                        .getSleepStart()
                        .toLocalDate();

        LocalTime time =
                firstSession
                        .getSleepStart()
                        .toLocalTime();

        /*
         * Если первая сессия начинается после полудня,
         * следующая ночь ещё не началась в логе.
         *
         * Например:
         * 01.10 14:00 -> 15:00
         * первая потенциальная ночь — 02.10.
         */
        if (time.isAfter(LocalTime.NOON)) {
            return date.plusDays(1);
        }

        return date;
    }

    private LocalDate getLastPotentialNight(
            SleepingSession lastSession) {

        LocalDate date =
                lastSession
                        .getWakeUp()
                        .toLocalDate();

        LocalTime time =
                lastSession
                        .getWakeUp()
                        .toLocalTime();

        /*
         * Если пробуждение произошло до полудня,
         * эта дата относится к последней ночи.
         *
         * Например:
         * 01.10 23:00 -> 02.10 07:00
         * последняя потенциальная ночь — 02.10.
         */
        if (time.isBefore(LocalTime.NOON)) {
            return date;
        }

        /*
         * Если человек проснулся после полудня,
         * ночь этой даты уже не рассматриваем.
         */
        return date.minusDays(1);
    }

    private boolean hasSleepDuringNight(
            List<SleepingSession> sessions,
            LocalDate night) {

        /*
         * Ночь с датой night:
         *
         * вечер предыдущего дня: 18:00 -> 00:00
         * утро текущего дня:      00:00 -> 06:00
         *
         * Например, ночь 02.10:
         *
         * 01.10 18:00 ---------------- 02.10 06:00
         */
        LocalDateTime nightStart =
                LocalDateTime.of(
                        night.minusDays(1),
                        NIGHT_EVENING_START
                );

        LocalDateTime nightEnd =
                LocalDateTime.of(
                        night,
                        NIGHT_MORNING_END
                );

        return sessions.stream()
                .anyMatch(session ->
                        intersects(
                                session,
                                nightStart,
                                nightEnd
                        ));
    }

    private boolean intersects(
            SleepingSession session,
            LocalDateTime intervalStart,
            LocalDateTime intervalEnd) {

        return session
                .getSleepStart()
                .isBefore(intervalEnd)
                && session
                .getWakeUp()
                .isAfter(intervalStart);
    }
}
