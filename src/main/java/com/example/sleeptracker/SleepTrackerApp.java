package com.example.sleeptracker;

import com.example.sleeptracker.analysis.*;
import com.example.sleeptracker.model.SleepingSession;

import java.io.IOException;
import java.util.List;
import java.util.function.Function;

public class SleepTrackerApp {

    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println(
                    "Использование: java " +
                            "ru.yandex.practicum.sleeptracker.SleepTrackerApp " +
                            "<путь-к-файлу>"
            );
            return;
        }

        SleepLogReader reader = new SleepLogReader();

        List<SleepingSession> sessions =
                reader.read(args[0]);

        List<Function<List<SleepingSession>, SleepAnalysisResult>>
                analyses = List.of(
                new TotalSessionsAnalysis(),
                new MinDurationAnalysis(),
                new MaxDurationAnalysis(),
                new AverageDurationAnalysis(),
                new BadQualityAnalysis(),
                new SleeplessNightsAnalysis(),
                new ChronotypeAnalysis()
        );

        analyses.stream()
                .map(analysis -> analysis.apply(sessions))
                .forEach(result ->
                        System.out.println(
                                result.getDescription()
                                        + ": "
                                        + result.getValue()
                        )
                );
    }
}