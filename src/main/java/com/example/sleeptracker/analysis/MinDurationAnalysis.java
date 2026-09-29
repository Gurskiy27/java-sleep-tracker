package com.example.sleeptracker.analysis;

import com.example.sleeptracker.model.SleepingSession;
import com.example.sleeptracker.SleepAnalysisResult;

import java.util.List;
import java.util.function.Function;

public class MinDurationAnalysis
        implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        long result = sessions.stream()
                .mapToLong(SleepingSession::getDurationMinutes)
                .min()
                .orElse(0);

        return new SleepAnalysisResult(
                "Минимальная продолжительность сессии (минут)",
                result
        );
    }
}