package hei.school;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class ExerciseLatePenaltyTest {

    private static final LocalDate DEADLINE = LocalDate.of(2026, 11, 10);

    @Test
    void lateDaysStarted_sameDayAsDeadline_isZero() {
        assertEquals(0, Exercise.lateDaysStarted(DEADLINE, DEADLINE));
    }

    @Test
    void lateDaysStarted_beforeDeadline_isZero() {
        assertEquals(0, Exercise.lateDaysStarted(DEADLINE, DEADLINE.minusDays(1)));
    }

    @Test
    void lateDaysStarted_oneDayAfter_isOne() {
        assertEquals(1, Exercise.lateDaysStarted(DEADLINE, DEADLINE.plusDays(1)));
    }

    @Test
    void lateDaysStarted_countsCalendarDays() {
        assertEquals(3, Exercise.lateDaysStarted(DEADLINE, DEADLINE.plusDays(3)));
    }

    @Test
    void latePenaltyRate_scalesByTenPercentUpToFifty() {
        assertEquals(0, Exercise.latePenaltyRate(0));
        assertEquals(0.1, Exercise.latePenaltyRate(1), 1e-9);
        assertEquals(0.3, Exercise.latePenaltyRate(3), 1e-9);
        assertEquals(0.5, Exercise.latePenaltyRate(5), 1e-9);
        assertEquals(0.5, Exercise.latePenaltyRate(12), 1e-9);
    }

    @Test
    void gradeOn20_onTime_unchanged() {
        LocalDateTime onTime = DEADLINE.atTime(18, 0);
        assertEquals(16.0, Exercise.gradeOn20(16, 20, DEADLINE, onTime));
    }

    @Test
    void gradeOn20_oneDayLate_appliesTenPercent() {
        LocalDateTime late = DEADLINE.plusDays(1).atTime(2, 0);
        assertEquals(14.4, Exercise.gradeOn20(16, 20, DEADLINE, late));
    }

    @Test
    void gradeOn20_threeDaysLate_appliesThirtyPercent() {
        LocalDateTime late = DEADLINE.plusDays(3).atTime(10, 0);
        assertEquals(11.2, Exercise.gradeOn20(16, 20, DEADLINE, late));
    }

    @Test
    void gradeOn20_fiveDaysLate_capsAtFiftyPercent() {
        LocalDateTime late = DEADLINE.plusDays(5).atTime(12, 0);
        assertEquals(8.0, Exercise.gradeOn20(16, 20, DEADLINE, late));
    }

    @Test
    void gradeOn20_neverNegative() {
        LocalDateTime veryLate = DEADLINE.plusDays(20).atTime(8, 0);
        assertEquals(0.0, Exercise.gradeOn20(0, 20, DEADLINE, veryLate));
        assertEquals(1.0, Exercise.gradeOn20(2, 20, DEADLINE, veryLate));
        assertEquals(0.5, Exercise.gradeOn20(1, 20, DEADLINE, veryLate));
    }

    @Test
    void gradeOn20_respectsExerciseScale() {
        LocalDateTime late = DEADLINE.plusDays(1).atTime(9, 0);
        assertEquals(13.5, Exercise.gradeOn20(30, 40, DEADLINE, late));
    }
}