package com.example.sleeptracker;

import com.example.sleeptracker.analysis.AverageDurationAnalysis;
import com.example.sleeptracker.analysis.BadQualityAnalysis;
import com.example.sleeptracker.analysis.ChronotypeAnalysis;
import com.example.sleeptracker.analysis.MaxDurationAnalysis;
import com.example.sleeptracker.analysis.MinDurationAnalysis;
import com.example.sleeptracker.analysis.SleeplessNightsAnalysis;
import com.example.sleeptracker.analysis.TestSessions;
import com.example.sleeptracker.analysis.TotalSessionsAnalysis;
import com.example.sleeptracker.model.Chronotype;
import com.example.sleeptracker.model.SleepQuality;
import com.example.sleeptracker.model.SleepingSession;

import java.util.List;

import static com.example.sleeptracker.analysis.TestSessions.session;

public final class TestRunner {

    private static int passed;
    private static int failed;

    private TestRunner() {
    }

    public static void main(String[] args) {

        run(
                "total sessions #1",
                TestRunner::totalSessions1
        );

        run(
                "total sessions #2",
                TestRunner::totalSessions2
        );

        run(
                "min duration #1",
                TestRunner::minDuration1
        );

        run(
                "min duration #2",
                TestRunner::minDuration2
        );

        run(
                "max duration #1",
                TestRunner::maxDuration1
        );

        run(
                "max duration #2",
                TestRunner::maxDuration2
        );

        run(
                "average duration #1",
                TestRunner::averageDuration1
        );

        run(
                "average duration #2",
                TestRunner::averageDuration2
        );

        run(
                "bad quality #1",
                TestRunner::badQuality1
        );

        run(
                "bad quality #2",
                TestRunner::badQuality2
        );

        run(
                "sleepless #1",
                TestRunner::sleepless1
        );

        run(
                "sleepless #2",
                TestRunner::sleepless2
        );

        run(
                "sleepless #3",
                TestRunner::sleepless3
        );

        run(
                "sleepless #4",
                TestRunner::sleepless4
        );

        run(
                "sleepless #5",
                TestRunner::sleepless5
        );

        run(
                "chronotype #1",
                TestRunner::chronotype1
        );

        run(
                "chronotype #2",
                TestRunner::chronotype2
        );

        run(
                "chronotype #3",
                TestRunner::chronotype3
        );

        run(
                "chronotype #4",
                TestRunner::chronotype4
        );

        System.out.println();
        System.out.println(
                "Пройдено: " + passed
        );

        System.out.println(
                "Провалено: " + failed
        );

        if (failed > 0) {
            throw new AssertionError(
                    "Есть проваленные тесты"
            );
        }
    }

    private static void run(
            String name,
            Runnable test
    ) {
        try {
            test.run();

            passed++;

            System.out.println(
                    "PASS: " + name
            );

        } catch (Throwable error) {

            failed++;

            System.out.println(
                    "FAIL: " + name
            );

            error.printStackTrace(
                    System.out
            );
        }
    }

    private static void totalSessions1() {
        assertEquals(
                4L,
                new TotalSessionsAnalysis()
                        .apply(TestSessions.basic())
                        .getValue()
        );
    }

    private static void totalSessions2() {
        assertEquals(
                0L,
                new TotalSessionsAnalysis()
                        .apply(List.of())
                        .getValue()
        );
    }

    private static void minDuration1() {
        assertEquals(
                50L,
                new MinDurationAnalysis()
                        .apply(TestSessions.basic())
                        .getValue()
        );
    }

    private static void minDuration2() {
        List<SleepingSession> sessions =
                List.of(
                        session(
                                "01.10.25 22:00",
                                "02.10.25 06:00",
                                SleepQuality.GOOD
                        ),
                        session(
                                "02.10.25 22:00",
                                "03.10.25 05:00",
                                SleepQuality.GOOD
                        )
                );

        assertEquals(
                420L,
                new MinDurationAnalysis()
                        .apply(sessions)
                        .getValue()
        );
    }

    private static void maxDuration1() {
        assertEquals(
                585L,
                new MaxDurationAnalysis()
                        .apply(TestSessions.basic())
                        .getValue()
        );
    }

    private static void maxDuration2() {
        List<SleepingSession> sessions =
                List.of(
                        session(
                                "01.10.25 22:00",
                                "02.10.25 06:00",
                                SleepQuality.GOOD
                        ),
                        session(
                                "02.10.25 20:00",
                                "03.10.25 08:30",
                                SleepQuality.GOOD
                        )
                );

        assertEquals(
                750L,
                new MaxDurationAnalysis()
                        .apply(sessions)
                        .getValue()
        );
    }

    private static void averageDuration1() {
        assertEquals(
                396.25,
                new AverageDurationAnalysis()
                        .apply(TestSessions.basic())
                        .getValue()
        );
    }

    private static void averageDuration2() {
        List<SleepingSession> sessions =
                List.of(
                        session(
                                "01.10.25 22:00",
                                "02.10.25 06:00",
                                SleepQuality.GOOD
                        ),
                        session(
                                "02.10.25 22:00",
                                "03.10.25 06:00",
                                SleepQuality.GOOD
                        )
                );

        assertEquals(
                480.0,
                new AverageDurationAnalysis()
                        .apply(sessions)
                        .getValue()
        );
    }

    private static void badQuality1() {
        assertEquals(
                1L,
                new BadQualityAnalysis()
                        .apply(TestSessions.basic())
                        .getValue()
        );
    }

    private static void badQuality2() {
        List<SleepingSession> sessions =
                List.of(
                        session(
                                "01.10.25 22:00",
                                "02.10.25 06:00",
                                SleepQuality.BAD
                        ),
                        session(
                                "02.10.25 22:00",
                                "03.10.25 06:00",
                                SleepQuality.BAD
                        )
                );

        assertEquals(
                2L,
                new BadQualityAnalysis()
                        .apply(sessions)
                        .getValue()
        );
    }

    /*
     * Две ночи без сна между двумя сессиями.
     */
    private static void sleepless1() {
        List<SleepingSession> sessions =
                List.of(
                        session(
                                "01.10.25 23:00",
                                "02.10.25 07:00",
                                SleepQuality.GOOD
                        ),
                        session(
                                "04.10.25 23:00",
                                "05.10.25 07:00",
                                SleepQuality.GOOD
                        )
                );

        assertEquals(
                2L,
                new SleeplessNightsAnalysis()
                        .apply(sessions)
                        .getValue()
        );
    }

    /*
     * 07:00-11:00 не пересекает 00:00-06:00.
     */
    private static void sleepless2() {
        List<SleepingSession> sessions =
                List.of(
                        session(
                                "01.10.25 07:00",
                                "01.10.25 11:00",
                                SleepQuality.GOOD
                        )
                );

        assertEquals(
                1L,
                new SleeplessNightsAnalysis()
                        .apply(sessions)
                        .getValue()
        );
    }

    /*
     * 02:00-07:00 — ночной сон.
     */
    private static void sleepless3() {
        List<SleepingSession> sessions =
                List.of(
                        session(
                                "01.10.25 02:00",
                                "01.10.25 07:00",
                                SleepQuality.GOOD
                        )
                );

        assertEquals(
                0L,
                new SleeplessNightsAnalysis()
                        .apply(sessions)
                        .getValue()
        );
    }

    /*
     * Переход через октябрь -> ноябрь.
     */
    private static void sleepless4() {
        List<SleepingSession> sessions =
                List.of(
                        session(
                                "31.10.25 23:00",
                                "01.11.25 07:00",
                                SleepQuality.GOOD
                        ),
                        session(
                                "03.11.25 23:00",
                                "04.11.25 07:00",
                                SleepQuality.GOOD
                        )
                );

        assertEquals(
                2L,
                new SleeplessNightsAnalysis()
                        .apply(sessions)
                        .getValue()
        );
    }

    /*
     * Первая сессия после 12:00.
     * Следующая ночь потенциально относится
     * к периоду, но лог заканчивается раньше неё.
     */
    private static void sleepless5() {
        List<SleepingSession> sessions =
                List.of(
                        session(
                                "01.10.25 14:00",
                                "01.10.25 15:00",
                                SleepQuality.GOOD
                        )
                );

        assertEquals(
                0L,
                new SleeplessNightsAnalysis()
                        .apply(sessions)
                        .getValue()
        );
    }

    private static void chronotype1() {
        List<SleepingSession> sessions =
                List.of(
                        session(
                                "01.10.25 23:30",
                                "02.10.25 10:00",
                                SleepQuality.GOOD
                        ),
                        session(
                                "02.10.25 23:30",
                                "03.10.25 10:00",
                                SleepQuality.GOOD
                        ),
                        session(
                                "03.10.25 23:45",
                                "04.10.25 10:30",
                                SleepQuality.GOOD
                        )
                );

        assertEquals(
                Chronotype.OWL,
                new ChronotypeAnalysis()
                        .apply(sessions)
                        .getValue()
        );
    }

    private static void chronotype2() {
        List<SleepingSession> sessions =
                List.of(
                        session(
                                "01.10.25 21:30",
                                "02.10.25 06:30",
                                SleepQuality.GOOD
                        ),
                        session(
                                "02.10.25 21:00",
                                "03.10.25 06:00",
                                SleepQuality.GOOD
                        ),
                        session(
                                "03.10.25 21:45",
                                "04.10.25 06:45",
                                SleepQuality.GOOD
                        )
                );

        assertEquals(
                Chronotype.LARK,
                new ChronotypeAnalysis()
                        .apply(sessions)
                        .getValue()
        );
    }

    private static void chronotype3() {
        List<SleepingSession> sessions =
                List.of(
                        session(
                                "01.10.25 22:30",
                                "02.10.25 08:00",
                                SleepQuality.GOOD
                        ),
                        session(
                                "02.10.25 22:30",
                                "03.10.25 08:00",
                                SleepQuality.GOOD
                        )
                );

        assertEquals(
                Chronotype.PIGEON,
                new ChronotypeAnalysis()
                        .apply(sessions)
                        .getValue()
        );
    }

    /*
     * 02:00-05:00 — ночная сессия,
     * несмотря на одинаковую дату.
     *
     * По правилам классификации:
     * 02:00 < 22:00 и 05:00 < 07:00,
     * поэтому эта ночь является жаворонком.
     *
     * Дневная сессия 14:00-15:00 игнорируется.
     */
    private static void chronotype4() {
        List<SleepingSession> sessions =
                List.of(
                        session(
                                "01.10.25 02:00",
                                "01.10.25 05:00",
                                SleepQuality.GOOD
                        ),
                        session(
                                "01.10.25 14:00",
                                "01.10.25 15:00",
                                SleepQuality.GOOD
                        )
                );

        assertEquals(
                Chronotype.LARK,
                new ChronotypeAnalysis()
                        .apply(sessions)
                        .getValue()
        );
    }

    private static void assertEquals(
            Object expected,
            Object actual
    ) {
        if (!java.util.Objects.equals(
                expected,
                actual
        )) {
            throw new AssertionError(
                    "Ожидалось: " +
                            expected +
                            ", получено: " +
                            actual
            );
        }
    }
}