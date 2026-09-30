package com.example.sleeptracker.analysis;

import com.example.sleeptracker.model.SleepingSession;
import com.example.sleeptracker.SleepAnalysisResult;

import java.util.List;
import java.util.function.Function;

public class TotalSessionsAnalysis
        implements Function<List<SleepingSession>, SleepAnalysisResult> {

    @Override
    public SleepAnalysisResult apply(List<SleepingSession> sessions) {
        return new SleepAnalysisResult(
                "Количество сессий сна",
                (long) sessions.size()
        );
    }
}