package hei.school;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.InOrder;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@ExtendWith(MockitoExtension.class)
class SubmissionControllerGradeSmsTest {

    @InjectMocks
    private SubmissionController controller;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private SmsGradeNotifier smsGradeNotifier;

    private Map<String, Object> sampleRow() {
        Map<String, Object> row = new HashMap<>();
        row.put("max_points", BigDecimal.valueOf(20));
        row.put("deadline", Date.valueOf(LocalDate.of(2026, 11, 10)));
        row.put("submitted_at", Timestamp.valueOf(LocalDateTime.of(2026, 11, 9, 12, 0)));
        row.put("full_name", "Alice");
        row.put("email", "alice@etu.kilasy.test");
        row.put("phone", "+261 34 00 000 01");
        row.put("title", "Tri à bulles");
        return row;
    }

    @Test
    void grade_whenSmsFails_stillPublishesGradeAndSendsEmail() {
        when(jdbcTemplate.queryForList(anyString(), eq(5L))).thenReturn(List.of(sampleRow()));
        when(jdbcTemplate.update(anyString(), any(), any(), anyLong())).thenReturn(1);
        doThrow(new RuntimeException("Échec simulé de l'envoi SMS"))
                .when(smsGradeNotifier)
                .sendGradeAvailable(anyString(), anyString(), anyDouble());

        ResponseEntity<?> response = controller.grade(5L, Map.of("points", 16, "feedback", "OK"));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(16.0, body.get("rawPoints"));
        assertEquals(16.0, body.get("grade"));
        assertEquals("Échec simulé de l'envoi SMS", body.get("smsError"));

        InOrder order = inOrder(mailSender, smsGradeNotifier);
        order.verify(mailSender).send(any(SimpleMailMessage.class));
        order.verify(smsGradeNotifier).sendGradeAvailable(eq("+261 34 00 000 01"), eq("Tri à bulles"), eq(16.0));
    }

    @Test
    void grade_whenSmsSucceeds_hasNoSmsError() {
        when(jdbcTemplate.queryForList(anyString(), eq(5L))).thenReturn(List.of(sampleRow()));
        when(jdbcTemplate.update(anyString(), any(), any(), anyLong())).thenReturn(1);

        ResponseEntity<?> response = controller.grade(5L, Map.of("points", 16));

        assertEquals(HttpStatus.OK, response.getStatusCode());
        @SuppressWarnings("unchecked")
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(16.0, body.get("grade"));
        assertNull(body.get("smsError"));
        verify(smsGradeNotifier).sendGradeAvailable(eq("+261 34 00 000 01"), eq("Tri à bulles"), eq(16.0));
    }
}