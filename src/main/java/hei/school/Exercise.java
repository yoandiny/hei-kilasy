package hei.school;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Exercise {

    /** Jours de retard entamés après l'échéance (0 si rendu à temps ou le jour même). */
    public static int lateDaysStarted(LocalDate deadline, LocalDate submittedDate) {
        if (!submittedDate.isAfter(deadline)) {
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(deadline, submittedDate);
    }

    /** Pénalité sur la note, entre 0 et 0,5 (−10 % par jour entamé, plafonnée à −50 %). */
    public static double latePenaltyRate(int lateDaysStarted) {
        if (lateDaysStarted <= 0) {
            return 0;
        }
        return Math.min(0.5, lateDaysStarted * 0.10);
    }

    /** Note sur 20 à partir des points bruts, avec pénalité de retard le cas échéant (≥ 0). */
    public static double gradeOn20(double rawPoints, double maxPoints, LocalDate deadline, LocalDateTime submittedAt) {
        double base = Math.round(rawPoints / maxPoints * 20 * 10) / 10.0;
        int days = lateDaysStarted(deadline, submittedAt.toLocalDate());
        if (days == 0) {
            return base;
        }
        double penalized = base * (1 - latePenaltyRate(days));
        penalized = Math.max(0, penalized);
        return Math.round(penalized * 10) / 10.0;
    }

    private Long id;
    private Long teacherId;
    private String title;
    private String description;
    private double maxPoints;
    private LocalDate deadline;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getMaxPoints() {
        return maxPoints;
    }

    public void setMaxPoints(double maxPoints) {
        this.maxPoints = maxPoints;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
