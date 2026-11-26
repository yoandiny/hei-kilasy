package hei.school;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SmsGradeNotifierTest {

    private final SmsGradeNotifier notifier = new SmsGradeNotifier();
    private PrintStream originalOut;

    @BeforeEach
    void captureOut() {
        originalOut = System.out;
    }

    @AfterEach
    void restoreOut() {
        System.setOut(originalOut);
    }

    @Test
    void sendGradeAvailable_logsExpectedFormat() {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));

        notifier.sendGradeAvailable("+261 34 00 000 01", "Tri à bulles", 14.4);

        String line = buffer.toString().trim();
        assertTrue(line.contains("[SMS] +261 34 00 000 01 : Votre note pour « Tri à bulles » est disponible : 14,4/20."));
    }

    @Test
    void sendGradeAvailable_rejectsMissingPhone() {
        assertThrows(IllegalArgumentException.class,
                () -> notifier.sendGradeAvailable("  ", "Tri à bulles", 16));
    }

    @Test
    void sendGradeAvailable_simulatedFailure_throwsWithoutLogging() {
        notifier.setSimulateFailure(true);
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        System.setOut(new PrintStream(buffer));

        RuntimeException error = assertThrows(RuntimeException.class,
                () -> notifier.sendGradeAvailable("+261 34 00 000 01", "Tri à bulles", 14.4));

        assertEquals("Échec simulé de l'envoi SMS", error.getMessage());
        assertTrue(buffer.toString().isBlank());
    }
}