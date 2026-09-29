package com.example.sleeptracker.analysis;

import com.example.sleeptracker.model.SleepQuality;
import com.example.sleeptracker.model.SleepingSession;
import com.example.sleeptracker.SleepAnalysisResult;

import java.util.List;
import java.util.function.Function;

public class BadQualityAnalysis
        implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {

        long result = sessions.stream()
                .filter(session -> session.getQuality() == SleepQuality.BAD)
                .count();

        return new SleepAnalysisResult(
                "Количество сессий с плохим качеством сна",
                result
        );
    }
}