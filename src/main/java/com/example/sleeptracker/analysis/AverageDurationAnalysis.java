package com.example.sleeptracker.analysis;

import com.example.sleeptracker.model.SleepingSession;
import com.example.sleeptracker.SleepAnalysisResult;

import java.util.List;
import java.util.function.Function;

public class AverageDurationAnalysis
        implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        double result = sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .average()
                .orElse(0);

        return new SleepAnalysisResult(
                "Средняя продолжительность сессии (минут)",
                result
        );
    }
}