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
                getFirstPotentialNight(sessions);

        LocalDate lastNight =
                getLastPotentialNight(sessions);

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
            List<SleepingSession> sessions) {

        return sessions.stream()
                .map(session -> {
                    LocalDate date =
                            session.getSleepStart().toLocalDate();

                    LocalTime time =
                            session.getSleepStart().toLocalTime();

                    if (time.isAfter(LocalTime.NOON)) {
                        return date.plusDays(1);
                    }

                    return date;
                })
                .min(LocalDate::compareTo)
                .orElseThrow();
    }

    private LocalDate getLastPotentialNight(
            List<SleepingSession> sessions) {

        return sessions.stream()
                .map(session -> {
                    LocalDate date =
                            session.getWakeUp().toLocalDate();

                    LocalTime time =
                            session.getWakeUp().toLocalTime();

                    if (time.isBefore(LocalTime.NOON)) {
                        return date;
                    }

                    return date.minusDays(1);
                })
                .max(LocalDate::compareTo)
                .orElseThrow();
    }

    private boolean hasSleepDuringNight(
            List<SleepingSession> sessions,
            LocalDate night) {

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

