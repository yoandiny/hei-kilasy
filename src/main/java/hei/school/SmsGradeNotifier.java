package hei.school;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Envoi SMS lors de la publication d'une note.
 * Seule cette classe devra être adaptée lorsqu'un fournisseur SMS sera branché.
 */
@Component
public class SmsGradeNotifier {

    private boolean simulateFailure;

    /** Active un échec volontaire (tests manuels : kilasy.sms.simulate-failure=true). */
    @Value("${kilasy.sms.simulate-failure:false}")
    public void setSimulateFailure(boolean simulateFailure) {
        this.simulateFailure = simulateFailure;
    }

    /**
     * @throws IllegalArgumentException si le numéro est absent ou vide
     * @throws RuntimeException si l'envoi échoue côté fournisseur (à venir)
     */
    public void sendGradeAvailable(String phone, String exerciseTitle, double gradeOn20) {
        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Numéro de téléphone manquant");
        }
        String gradeFormatted = String.format("%.1f", gradeOn20).replace('.', ',');
        String body = "Votre note pour « " + exerciseTitle + " » est disponible : " + gradeFormatted + "/20.";
        if (simulateFailure) {
            throw new RuntimeException("Échec simulé de l'envoi SMS");
        }
        System.out.println("[SMS] " + phone.trim() + " : " + body);
    }
}