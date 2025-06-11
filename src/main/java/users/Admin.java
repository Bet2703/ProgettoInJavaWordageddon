package users;

/**
 * Rappresenta un utente di tipo Admin, estendendo la classe base User.
 * In aggiunta agli attributi e metodi della classe User, questa classe
 * introduce un campo per indicare se l'utente ha privilegi amministrativi.
 * 
 * Autore: Gruppo6
 */
public class Admin extends User {

    /**
     * Indica se l'utente possiede privilegi da amministratore.
     * Serve a distinguere tra utenti normali e amministratori.
     */
    private boolean isAdmin;

    /**
     * Costruttore della classe Admin.
     * Inizializza un utente amministratore con username, password e stato admin.
     *
     * @param username il nome utente
     * @param password la password dell'utente
     * @param isAdmin  true se l'utente ha privilegi da amministratore, false altrimenti
     */
    public Admin(String username, String password, boolean isAdmin) {
        super(username, password); // Chiamata al costruttore della superclasse User
        this.isAdmin = isAdmin;
    }

    /**
     * Restituisce true se l'utente ha privilegi da amministratore.
     *
     * @return true se è admin, false altrimenti
     */
    public boolean isIsAdmin() {
        return isAdmin;
    }

    /**
     * Imposta lo stato amministrativo dell'utente.
     *
     * @param isAdmin nuovo valore booleano che indica se l'utente è admin
     */
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
