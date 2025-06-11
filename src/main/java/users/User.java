package users;

import java.util.Objects;

/**
 * Rappresenta un utente astratto all'interno del sistema.
 * Questa classe fornisce le basi comuni per tutti i tipi di utenti,
 * definendo attributi fondamentali come username e password,
 * oltre a funzionalità condivise come la validazione della password,
 * il controllo di uguaglianza e la generazione dell'hash code.
 *
 * La classe è astratta e deve essere estesa da classi concrete
 * che definiscono comportamenti e proprietà specifiche (es. Admin, Player).
 * 
 * @author Gruppo6
 */
public abstract class User {
    private final String username;  // Nome utente (immutabile)
    private final String password;  // Password utente (immutabile)

    /**
     * Costruisce un oggetto User con username e password specificati.
     * Inizializza gli attributi principali comuni a tutti gli utenti.
     *
     * @param username il nome utente
     * @param password la password associata all'utente
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Restituisce lo username dell'utente.
     *
     * @return lo username dell'utente
     */
    public String getUsername() {
        return username;
    }

    /**
     * Restituisce la password dell'utente.
     *
     * @return la password dell'utente
     */
    public String getPassword() {
        return password;
    }

    /**
     * Verifica se una password fornita corrisponde a quella dell'utente.
     *
     * @param input la password da verificare
     * @return true se la password è corretta, false altrimenti
     */
    public boolean checkPassword(String input) {
        return password.equals(input);
    }

    /**
     * Genera il codice hash per l'oggetto User basato sullo username.
     * Utile per l'utilizzo in strutture dati come hash map o set.
     *
     * @return il codice hash calcolato
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.username);
        return hash;
    }

    /**
     * Verifica l'uguaglianza tra due oggetti User in base allo username.
     *
     * @param obj l'oggetto da confrontare
     * @return true se i due utenti hanno lo stesso username, false altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        final User other = (User) obj;
        return Objects.equals(this.username, other.username);
    }    
}
