package users;

/**
 * Rappresenta i possibili ruoli che un utente può avere all'interno del sistema.
 * Questa enumerazione viene utilizzata per distinguere tra utenti amministratori
 * e utenti regolari, permettendo di applicare permessi e funzionalità differenti
 * in base al ruolo.
 *
 * I ruoli definiti sono:
 * - ADMIN: Utente con privilegi amministrativi (ad esempio, gestione utenti o contenuti).
 * - BASE: Utente standard, con accesso limitato alle funzionalità del sistema.
 */
public enum Role {
    ADMIN, // Ruolo amministratore
    BASE   // Ruolo base, ovvero utente normale
}
