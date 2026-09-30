package com.example.sleeptracker.analysis;

import com.example.sleeptracker.SleepAnalysisResult;
import com.example.sleeptracker.model.Chronotype;
import com.example.sleeptracker.model.SleepingSession;

import java.time.LocalTime;
import java.util.List;
import java.util.function.Function;

public class ChronotypeAnalysis
        implements Function<List<SleepingSession>, SleepAnalysisResult> {

    private static final LocalTime OWL_START =
            LocalTime.of(23, 0);

    private static final LocalTime OWL_WAKE =
            LocalTime.of(9, 0);

    private static final LocalTime LARK_START =
            LocalTime.of(22, 0);

    private static final LocalTime LARK_WAKE =
            LocalTime.of(7, 0);

    private static final LocalTime NOON =
            LocalTime.of(12, 0);

    @Override
    public SleepAnalysisResult apply(
            List<SleepingSession> sessions) {

        long larks = sessions.stream()
                .filter(this::isNightSession)
                .filter(this::isLark)
                .count();

        long owls = sessions.stream()
                .filter(this::isNightSession)
                .filter(this::isOwl)
                .count();

        long pigeons = sessions.stream()
                .filter(this::isNightSession)
                .filter(session ->
                        !isLark(session) && !isOwl(session))
                .count();

        Chronotype result;

        if (larks > owls && larks > pigeons) {
            result = Chronotype.LARK;
        } else if (owls > larks && owls > pigeons) {
            result = Chronotype.OWL;
        } else {
            result = Chronotype.PIGEON;
        }

        return new SleepAnalysisResult(
                "Хронотип пользователя",
                result
        );
    }

    private boolean isNightSession(SleepingSession session) {
        LocalTime start =
                session.getSleepStart().toLocalTime();

        LocalTime wake =
                session.getWakeUp().toLocalTime();

        return !start.isAfter(NOON)
                || wake.isBefore(NOON);
    }

    private boolean isOwl(SleepingSession session) {
        LocalTime start =
                session.getSleepStart().toLocalTime();

        LocalTime wake =
                session.getWakeUp().toLocalTime();

        int normalizedStart =
                normalizeStartMinutes(start);

        int owlStart =
                minutes(OWL_START);

        /*
         * Если начало сна после полуночи,
         * границу 23:00 тоже переносим
         * на следующий условный день.
         */
        if (start.isBefore(NOON)) {
            owlStart += 24 * 60;
        }

        return normalizedStart > owlStart
                && minutes(wake) > minutes(OWL_WAKE);
    }

    private boolean isLark(SleepingSession session) {
        LocalTime start =
                session.getSleepStart().toLocalTime();

        LocalTime wake =
                session.getWakeUp().toLocalTime();

        int normalizedStart =
                normalizeStartMinutes(start);

        int larkStart =
                minutes(LARK_START);

        /*
         * Для сна после полуночи сравниваем
         * с 22:00 предыдущего условного дня.
         *
         * 02:00 -> 26:00
         * 22:00 -> 22:00
         *
         * Поэтому для жаворонка здесь нельзя
         * просто сравнивать нормализованные
         * значения напрямую.
         */
        if (start.isBefore(NOON)) {
            return minutes(start) < minutes(LARK_WAKE)
                    && minutes(wake) < minutes(LARK_WAKE);
        }

        return normalizedStart < larkStart
                && minutes(wake) < minutes(LARK_WAKE);
    }

    private int normalizeStartMinutes(LocalTime time) {
        int minutes = minutes(time);

        if (time.isBefore(NOON)) {
            return minutes + 24 * 60;
        }

        return minutes;
    }

    private int minutes(LocalTime time) {
        return time.getHour() * 60 + time.getMinute();
    }
}

