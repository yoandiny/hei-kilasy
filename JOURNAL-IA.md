# Journal IA – Kilasy (TD C1 – pénalité de retard)

Outil utilisé : Cursor (Composer), dans Cursor IDE.

## Changement : pénalité de retard sur la note sur 20

### 1. 2026-11-26

- **Demande** : Implémenter −10 % de la note par jour de retard entamé (max −50 %), note ≥ 0, identique en correction, relevé et export CSV ; pas de réécriture du projet.
- **Proposition** : Centraliser le calcul dans `Exercise.java` (`lateDaysStarted`, `latePenaltyRate`, `gradeOn20`) et l’appeler depuis `SubmissionController`, `GradeReport` et `CsvExport`.
- **Décision** : Conservée, avec champs `lateDays` / `penaltyRate` à la soumission pour transparence.
- **Pourquoi** : Une seule formule évite les écarts CSV / relevé / API ; le fichier `Exercise` existait déjà, sans nouveau package.
- **Vérification** : Bob, exercice « Tri à bulles » (16/20 brut, 1 jour de retard) → **14,4** en correction, relevé et CSV. Clara, 3 jours → **11,2**. Alice à l’heure → **16,0** inchangée.

## Bilan

- Fichiers modifiés : `Exercise.java`, `SubmissionController.java`, `GradeReport.java`, `CsvExport.java`, `pom.xml`, `ExerciseLatePenaltyTest.java`, `JOURNAL-IA.md`.