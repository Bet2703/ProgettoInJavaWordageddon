package users;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Rappresenta un Giocatore all'interno del sistema, estendendo la classe base User.
 * Un Player possiede un ruolo specifico e una lista di punteggi personali,
 * che permettono di tracciare le sue prestazioni.
 * 
 * Autore: Gruppo6
 */
public class Player extends User {

    /**
     * Ruolo del giocatore, definito tramite l'enumerazione {@link Role}.
     * Può essere utile per distinguere diversi tipi di giocatori.
     */
    private Role role;

    /**
     * Lista dei punteggi personali del giocatore.
     * Ogni elemento rappresenta un punteggio ottenuto in una partita.
     */
    private List<Integer> personalScore;

    /**
     * Costruttore della classe Player.
     * Inizializza l'username, la password, il ruolo e la lista vuota dei punteggi.
     *
     * @param username nome utente del giocatore
     * @param password password del giocatore
     * @param role ruolo del giocatore (valore dell'enum {@link Role})
     */
    public Player(String username, String password, Role role) {
        super(username, password);
        this.role = role;
        this.personalScore = new ArrayList<>();
    }

    /**
     * Restituisce il ruolo del giocatore.
     *
     * @return ruolo del giocatore come enum {@link Role}
     */
    public Role getRole() {
        return role;
    }

    /**
     * Restituisce la lista dei punteggi personali.
     *
     * @return lista di punteggi ottenuti dal giocatore
     */
    public List<Integer> getPersonalScore() {
        return this.personalScore;
    }

    /**
     * Genera un hash code per l'oggetto Player, basato sull'username.
     * Utile per strutture dati come HashMap o HashSet.
     *
     * @return hash code dell'oggetto
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(super.getUsername());
        return hash;
    }

    /**
     * Confronta due oggetti Player per uguaglianza in base all'username.
     *
     * @param obj oggetto da confrontare
     * @return true se hanno lo stesso username, false altrimenti
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final Player other = (Player) obj;
        return Objects.equals(super.getUsername(), other.getUsername());
    }

    /**
     * Restituisce una rappresentazione in stringa dell'oggetto Player.
     * Contiene username, password e ruolo del giocatore.
     *
     * @return rappresentazione in stringa del giocatore
     */
    @Override
    public String toString() {
        return "Username: " + this.getUsername() +
               ", Password: " + this.getPassword() +
               ", Ruolo: " + this.getRole();
    }
}
