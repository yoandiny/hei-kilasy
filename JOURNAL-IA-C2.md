# Journal IA – Kilasy (TD C2 – notification SMS)

Outil utilisé : Cursor (Composer), dans Cursor IDE.

## Changement : notification SMS à la publication d'une note

### 1. 2026-11-26

- **Demande** : SMS simulé en log console à la publication ; logique isolée pour futur provider ; échec SMS sans bloquer mail ni réponse 200.
- **Proposition** : Classe `SmsGradeNotifier` + appel dans `PUT /submissions/{id}/grade` après l'email, dans un `try/catch` avec champ `smsError` si besoin.
- **Décision** : Conservée.
- **Pourquoi** : Un seul point à modifier pour Twilio/autre ; le contrôleur reste inchangé côté flux principal.
- **Vérification** : `PUT /submissions/{id}/grade` → log `[SMS] +261…` ; `mvn test` (`SmsGradeNotifierTest`, `SubmissionControllerGradeSmsTest`).
- **Simulation échec** : `kilasy.sms.simulate-failure=true` dans `application.properties` pour tests manuels ; tests unitaires avec `setSimulateFailure` et Mockito.

## Bilan

- Fichiers modifiés : `SmsGradeNotifier.java`, `SubmissionController.java`, `application.properties`, `SmsGradeNotifierTest.java`, `SubmissionControllerGradeSmsTest.java`, `JOURNAL-IA-C2.md`.