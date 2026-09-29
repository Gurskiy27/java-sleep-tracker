package com.example.sleeptracker.analysis;

import com.example.sleeptracker.model.SleepQuality;
import com.example.sleeptracker.model.SleepingSession;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public final class TestSessions {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern(
                    "dd.MM.yy HH:mm"
            );

    private TestSessions() {
    }

    public static SleepingSession session(
            String start,
            String end,
            SleepQuality quality
    ) {
        return new SleepingSession(
                LocalDateTime.parse(
                        start,
                        FORMATTER
                ),
                LocalDateTime.parse(
                        end,
                        FORMATTER
                ),
                quality
        );
    }

    public static List<SleepingSession> basic() {
        return List.of(
                session(
                        "01.10.25 22:15",
                        "02.10.25 08:00",
                        SleepQuality.GOOD
                ),
                session(
                        "02.10.25 23:00",
                        "03.10.25 08:00",
                        SleepQuality.NORMAL
                ),
                session(
                        "03.10.25 14:30",
                        "03.10.25 15:20",
                        SleepQuality.NORMAL
                ),
                session(
                        "03.10.25 23:30",
                        "04.10.25 06:20",
                        SleepQuality.BAD
                )
        );
    }
}
