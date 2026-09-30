package com.example.sleeptracker;

import com.example.sleeptracker.model.SleepQuality;
import com.example.sleeptracker.model.SleepingSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class SleepLogReader {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");

    public List<SleepingSession> read(String filePath) throws IOException {
        try (var lines = Files.lines(Path.of(filePath))) {
            return lines
                    .filter(line -> !line.isBlank())
                    .map(this::parseLine)
                    .collect(Collectors.toList());
        }
    }

    private SleepingSession parseLine(String line) {
        String[] parts = line.split(";");

        if (parts.length != 3) {
            throw new IllegalArgumentException(
                    "Неверный формат строки: " + line
            );
        }

        LocalDateTime sleepStart =
                LocalDateTime.parse(parts[0], FORMATTER);

        LocalDateTime wakeUp =
                LocalDateTime.parse(parts[1], FORMATTER);

        SleepQuality quality =
                SleepQuality.valueOf(
                        parts[2].trim().toUpperCase()
                );

        return new SleepingSession(
                sleepStart,
                wakeUp,
                quality
        );
    }
}

