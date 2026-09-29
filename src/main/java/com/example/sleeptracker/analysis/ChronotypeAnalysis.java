package com.example.sleeptracker.analysis;

import com.example.sleeptracker.model.Chronotype;
import com.example.sleeptracker.model.SleepingSession;
import com.example.sleeptracker.SleepAnalysisResult;

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

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

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
        LocalTime start = session.getSleepStart().toLocalTime();
        LocalTime wake = session.getWakeUp().toLocalTime();

        return !start.isAfter(LocalTime.of(12, 0))
                || wake.isBefore(LocalTime.of(12, 0));
    }

    private boolean isOwl(SleepingSession session) {
        LocalTime start = session.getSleepStart().toLocalTime();
        LocalTime wake = session.getWakeUp().toLocalTime();

        return start.isAfter(OWL_START)
                && wake.isAfter(OWL_WAKE);
    }

    private boolean isLark(SleepingSession session) {
        LocalTime start = session.getSleepStart().toLocalTime();
        LocalTime wake = session.getWakeUp().toLocalTime();

        return start.isBefore(LARK_START)
                && wake.isBefore(LARK_WAKE);
    }
}