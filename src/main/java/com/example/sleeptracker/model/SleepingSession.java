package com.example.sleeptracker.model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SleepingSession {

    private final LocalDateTime sleepStart;
    private final LocalDateTime wakeUp;
    private final SleepQuality quality;

    public SleepingSession(
            LocalDateTime sleepStart,
            LocalDateTime wakeUp,
            SleepQuality quality) {

        if (wakeUp.isBefore(sleepStart)) {
            throw new IllegalArgumentException(
                    "Время пробуждения не может быть раньше времени засыпания"
            );
        }

        this.sleepStart = sleepStart;
        this.wakeUp = wakeUp;
        this.quality = quality;
    }

    public LocalDateTime getSleepStart() {
        return sleepStart;
    }

    public LocalDateTime getWakeUp() {
        return wakeUp;
    }

    public SleepQuality getQuality() {
        return quality;
    }

    public long getDurationMinutes() {
        return Duration.between(sleepStart, wakeUp).toMinutes();
    }

    @Override
    public String toString() {
        return "SleepingSession{" +
                "sleepStart=" + sleepStart +
                ", wakeUp=" + wakeUp +
                ", quality=" + quality +
                '}';
    }
}