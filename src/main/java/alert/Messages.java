package alert;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

/**
 * Classe utility per mostrare messaggi all'utente tramite finestre di dialogo.
 * Fornisce metodi statici per mostrare messaggi di errore, conferma,
 * informazione e avvertimento utilizzando la classe {@code Alert}.
 */
public class Messages {

    /**
     * Mostra un messaggio di errore.
     *
     * @param title   il titolo del messaggio
     * @param content il contenuto del messaggio
     */
    public static void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Mostra un messaggio di conferma.
     *
     * @param title   il titolo del messaggio
     * @param content il contenuto del messaggio
     * @return true se l'utente ha confermato, false altrimenti
     */
    public static boolean showConfirmation(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);

        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }

    /**
     * Mostra un messaggio di informazione.
     *
     * @param title   il titolo del messaggio
     * @param content il contenuto del messaggio
     */
    public static void showInfo(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * Mostra un messaggio di avvertimento.
     *
     * @param title   il titolo del messaggio
     * @param content il contenuto del messaggio
     */
    public static void showWarning(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
