package controller;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.animation.Timeline;
import service.Word;

import java.io.IOException;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * La classe QuestionsController gestisce l'interfaccia del quiz,
 * le interazioni dell'utente e coordina lo stato e il comportamento dell'applicazione quiz.
 * Fornisce funzionalità per avviare il quiz, gestire il timer, gestire
 * l'input dell'utente e visualizzare il feedback.
 * Questo controller è associato a un layout FXML di JavaFX.
 *
 * @author Gruppo6
 */
public class QuestionsController {

    /**
     * Etichetta nell'interfaccia utente che mostra la domanda
     * attualmente visualizzata all'utente durante il quiz.
     * Il testo di questa etichetta viene aggiornato dinamicamente per riflettere
     * il contenuto della domanda attiva.
     */
    @FXML
    private Label questionLabel;

    /**
     * Rappresenta il pulsante "Invia" nell'interfaccia utente del quiz.
     * Questo pulsante permette all'utente di inviare la risposta selezionata per la valutazione.
     * Quando cliccato, attiva il metodo {@link #onSubmitAnswer()},
     * che valida la risposta dell'utente e fornisce feedback.
     * Utilizzo: Il pulsante fa parte della vista del quiz e interagisce con i gestori di eventi
     * per elaborare la risposta dell'utente durante il flusso del quiz.
     */
    @FXML
    private Button submitButton;

    /**
     * Rappresenta il pulsante nell'interfaccia utente che permette all'utente di saltare la domanda corrente.
     * Quando cliccato, attiva l'azione per passare alla prossima domanda senza valutazione.
     * Questo pulsante offre agli utenti la possibilità di bypassare qualsiasi domanda a cui non desiderano rispondere.
     * È collegato al corrispondente metodo gestore di eventi nel controller.
     */
    @FXML
    private Button skipButton;

    /**
     * Il `feedbackLabel` è un componente dell'interfaccia utente utilizzato per visualizzare feedback all'utente
     * durante il processo del quiz. Questa etichetta fornisce informazioni testuali, come
     * se una risposta era corretta, errata o se un'azione non può essere eseguita.
     * Viene principalmente aggiornato in base alle interazioni dell'utente e alla logica di progressione del quiz.
     */
    @FXML
    private Label feedbackLabel;

    /**
     * Rappresenta l'etichetta nell'interfaccia utente che mostra il tempo rimanente
     * o il conto alla rovescia durante la sessione del quiz.
     * Questa etichetta viene aggiornata dinamicamente in base alla funzionalità del timer del quiz
     * ed è parte dell'interfaccia gestita da QuestionsController.
     */
    @FXML
    private Label timerLabel;

    /**
     * Rappresenta un pulsante radio nell'interfaccia del quiz che permette all'utente
     * di selezionare la prima opzione (opzione A) come risposta.
     * Questo componente è collegato all'elemento UI corrispondente nel file FXML.
     * Fa parte del gruppo di opzioni selezionabili per una domanda del quiz e
     * gestito dalla classe QuestionsController.
     */
    @FXML
    private RadioButton optionA;

    /**
     * Rappresenta un RadioButton nell'interfaccia del quiz etichettato come opzione B.
     * È progettato per permettere agli utenti di selezionare questa opzione come risposta
     * durante il quiz. Questo RadioButton fa parte di un gruppo di opzioni
     * visualizzate all'utente come possibili risposte a una domanda.
     * Questo campo è annotato con @FXML, indicando che è collegato a un
     * elemento definito nel file FXML associato e gestito dal framework JavaFX.
     */
    @FXML
    private RadioButton optionB;

    /**
     * Rappresenta la terza opzione selezionabile in una domanda a scelta multipla.
     * Questo pulsante radio permette all'utente di selezionare "Opzione C" come risposta.
     * Utilizzato nell'interfaccia del quiz gestita dal controller.
     */
    @FXML
    private RadioButton optionC;

    /**
     * Rappresenta la quarta opzione di risposta in una domanda a scelta multipla per l'interfaccia del quiz.
     * Questo pulsante radio permette all'utente di selezionare l'opzione D come risposta scelta.
     * Questo componente fa parte dell'interfaccia utente gestita da `QuestionsController` e
     * interagisce con il gruppo di risposte per permettere solo un'opzione selezionata alla volta.
     * Il pulsante radio viene inizializzato insieme ad altri componenti UI e viene abilitato o disabilitato
     * in base allo stato del quiz.
     */
    @FXML
    private RadioButton optionD;

    /**
     * Rappresenta un ToggleGroup utilizzato per gestire la mutua esclusività di un gruppo
     * di pulsanti toggle (ad esempio, RadioButton) per la selezione delle risposte nell'interfaccia del quiz.
     * Questo assicura che solo un'opzione di risposta possa essere selezionata alla volta.
     */
    private ToggleGroup answerGroup;

    /**
     * Lista di parole utilizzate per generare le domande del quiz.
     */
    private List<Word> wordList;

    /**
     * Rappresenta il testo della risposta corretta per la domanda corrente.
     * Questa variabile viene utilizzata per memorizzare e confrontare la risposta corretta con
     * la risposta selezionata dall'utente durante il quiz.
     */
    private String correctAnswerText;

    /**
     * Rappresenta l'identificatore univoco per un documento specifico utilizzato nel contesto
     * della gestione del quiz o delle operazioni relative ai documenti all'interno dell'applicazione.
     * Questa variabile viene principalmente utilizzata per associare azioni, come il caricamento o
     * l'elaborazione, al documento corretto in base al suo ID assegnato. L'ID del documento
     * dovrebbe essere un valore intero univoco fornito dal sistema o dall'utente.
     */
    private int documentId;

    /**
     * Gestisce lo stato corrente della sessione di gioco.
     * Questo campo mantiene un riferimento all'istanza singleton di {@code GameSessionManagement},
     * responsabile per coordinare il ciclo di vita della sessione di gioco, recuperare i dati della sessione,
     * e mantenere i dettagli specifici della sessione.
     * L'istanza {@code session} viene utilizzata in vari metodi nella classe {@code QuestionsController}
     * per gestire e interagire con la sessione di gioco, come recuperare lo stato corrente
     * della sessione e applicare configurazioni specifiche per il quiz.
     */
    private final service.GameSessionManagement session = service.GameSessionManagement.getInstance();

    /**
     * Rappresenta il numero massimo di domande che verranno visualizzate o poste
     * durante la sessione del quiz. Questo valore limita il totale delle domande che un utente può
     * incontrare in una singola sessione del quiz.
     */
    private int maxQuestions;

    /**
     * Un'istanza di {@link PauseTransition} utilizzata per gestire la tempistica delle singole domande nel quiz.
     * Questo timer è responsabile per controllare la durata assegnata per rispondere a ogni domanda e può attivare azioni specifiche quando il tempo scade.
     */
    private PauseTransition questionTimer;

    /**
     * Rappresenta la durata del tempo concesso per rispondere a una singola domanda nel quiz.
     * Questa variabile viene utilizzata per controllare l'intervallo di timeout, assicurando che l'utente abbia un tempo limitato per rispondere a ogni domanda.
     */
    private Duration questionTimeout;

    /**
     * Rappresenta una Timeline JavaFX utilizzata per gestire e animare
     * il timer del conto alla rovescia durante il quiz. Questa Timeline è responsabile
     **/
    private Timeline countdownTimeline;

    /**
     * Contiene il tempo rimanente in secondi per il quiz o la domanda corrente.
     * Questa variabile viene principalmente utilizzata per tracciare e gestire il timer del conto alla rovescia che
     * limita il tempo che un utente può impiegare per rispondere a una domanda o completare il quiz.
     * Viene aggiornata dinamicamente per riflettere la durata rimanente in tempo reale.
     */
    private int remainingTimeSeconds;

    /**
     * Inizializza i componenti e le impostazioni del controller.
     * Questo metodo configura il `ToggleGroup` per le opzioni di risposta (optionA, optionB, optionC, optionD)
     * per assicurare che solo un'opzione possa essere selezionata alla volta.
     * Viene chiamato automaticamente durante il caricamento del file FXML.
     */
    @FXML
    public void initialize() {
        answerGroup = new ToggleGroup();
        Stream.of(optionA, optionB, optionC, optionD).forEach(rb -> rb.setToggleGroup(answerGroup));
    }

    /**
     * Avvia il gioco inizializzando le configurazioni necessarie per la sessione
     * e caricando la prima domanda. Recupera le parole in base all'ID del documento fornito
     * e determina il livello di difficoltà per impostare il timeout della domanda.
     * Se l'ID del documento non è valido o non ci sono abbastanza parole, fornisce
     * un feedback appropriato all'utente.
     *
     * @param documentId l'ID del documento utilizzato per caricare le parole per il gioco
     */
    @FXML
    public void startGame(int documentId) {
        this.documentId = documentId;
        wordList = service.QuestionGenerator.getWords(documentId);

        if (wordList == null) {
            feedbackLabel.setText("Documento non valido o insufficiente.");
            disableInteraction();
            return;
        }

        if (wordList.size() < 4) {
            feedbackLabel.setText("Non ci sono abbastanza parole per generare una domanda.");
            return;
        }

        String difficulty = LevelsController.getDifficulty();
        session.startSession(session.getCurrentPlayer(), documentId, difficulty);
        maxQuestions = session.getMaxQuestions();

        switch (difficulty.toUpperCase()) {
            case "EASY":
                questionTimeout = Duration.seconds(30);
                break;
            case "MEDIUM":
                questionTimeout = Duration.seconds(20);
                break;
            case "HARD":
                questionTimeout = Duration.seconds(10);
                break;
        }

        loadNextQuestion(maxQuestions);
    }

    /**
     * Disabilita l'interazione dell'utente con gli elementi chiave dell'interfaccia utente.
     * Questo metodo impedisce l'interazione disabilitando i seguenti componenti dell'interfaccia utente:
     * - Il pulsante "Invia"
     * - Il pulsante "Salta"
     * - Le opzioni di risposta (optionA, optionB, optionC e optionD)
     * Tipicamente, questo metodo viene utilizzato per impedire l'interazione dell'utente durante azioni
     * come l'elaborazione di una risposta o il passaggio alla domanda successiva.
     */
    private void disableInteraction() {
        submitButton.setDisable(true);
        skipButton.setDisable(true);
        optionA.setDisable(true);
        optionB.setDisable(true);
        optionC.setDisable(true);
        optionD.setDisable(true);
    }

    /**
     * Carica la prossima domanda nella sessione del quiz o conclude la sessione se tutte le domande
     * sono state risposte o il timer scade.
     *
     * Questo metodo:
     * - Reimposta l'etichetta di feedback e prepara l'interfaccia utente per una nuova domanda.
     * - Verifica se il numero massimo di domande è stato risposto:
     *   - Se tutte le domande sono state risposte, mostra il punteggio finale, salva la sessione,
     *     disabilita l'interazione e conclude il quiz.
     *   - Altrimenti, genera e visualizza la prossima domanda e le sue opzioni.
     * - Gestisce il timer del conto alla rovescia per la domanda e aggiorna l'etichetta del timer.
     * - Elabora l'evento quando il timer scade, fornendo feedback e programmando
     *   il caricamento della prossima domanda.
     *
     * @param maxQuestions il numero totale di domande consentite nella sessione.
     */
    private void loadNextQuestion(int maxQuestions) {
        feedbackLabel.setStyle("-fx-text-fill: black;");
        feedbackLabel.setText("");

        if (session.getQuestionsAnswered() >= maxQuestions) {
            feedbackLabel.setText("Hai completato il quiz! Punteggio: " + session.getScore());
            session.saveSession();
            disableInteraction();
            concludeQuiz();
            return;
        }

        service.Question question = service.QuestionGenerator.generateNextQuestion(wordList, session.getQuestionsAnswered());

        questionLabel.setText(question.getQuestionText());
        correctAnswerText = question.getCorrectAnswer();

        setOptions(question.getOptions());

        answerGroup.selectToggle(null);

        if (questionTimer != null) {
            questionTimer.stop();
        }

        remainingTimeSeconds = (int) questionTimeout.toSeconds();

        countdownTimeline = new Timeline(new javafx.animation.KeyFrame(Duration.seconds(1), e -> {
            remainingTimeSeconds--;
            timerLabel.setText("Tempo Rimasto: " + remainingTimeSeconds + " secondi");
            if (remainingTimeSeconds <= 0) {
                countdownTimeline.stop();
                feedbackLabel.setStyle("-fx-text-fill: red;");
                feedbackLabel.setText("Tempo scaduto! La risposta corretta era: " + correctAnswerText);
                session.recordAnswer(false);
                setInteractionEnabled(false);

                PauseTransition pause = new PauseTransition(Duration.seconds(2));
                pause.setOnFinished(ev -> {
                    setInteractionEnabled(true);
                    loadNextQuestion(maxQuestions);
                });
                pause.play();
            }
        }));
        countdownTimeline.setCycleCount(remainingTimeSeconds);
        countdownTimeline.play();

        timerLabel.setText("Tempo Rimasto: " + remainingTimeSeconds + " secondi");
        setInteractionEnabled(true);
    }

    /**
     * Imposta il testo per le opzioni di risposta disponibili per una domanda.
     * Questo metodo assegna la lista fornita di testi delle opzioni alle rispettive
     * opzioni di risposta (optionA, optionB, optionC e optionD).
     *
     * @param options una lista di stringhe che rappresentano il testo per le opzioni.
     *                 Questa lista deve contenere almeno quattro elementi dove:
     *                 - Il primo elemento imposta il testo per optionA.
     *                 - Il secondo elemento imposta il testo per optionB.
     *                 - Il terzo elemento imposta il testo per optionC.
     *                 - Il quarto elemento imposta il testo per optionD.
     *                 Se la lista contiene meno di quattro elementi, può verificarsi un'eccezione.
     */
    private void setOptions(List<String> options) {
        List<RadioButton> optionsButtons = Arrays.asList(optionA, optionB, optionC, optionD);
        IntStream.range(0, optionsButtons.size())
                .forEach(i -> optionsButtons.get(i).setText(options.get(i)));
    }

    /**
     * Gestisce l'invio di una risposta durante il quiz.
     * Questo metodo valuta la risposta selezionata dall'utente, fornisce un feedback appropriato,
     * disabilita le interazioni fino alla transizione alla prossima domanda e programma il
     * caricamento della prossima domanda dopo un breve ritardo. Se il timer della domanda è attivo, viene fermato.
     */
    @FXML
    private void onSubmitAnswer() {
        Optional<RadioButton> selectedOpt = Stream.of(optionA, optionB, optionC, optionD)
                .filter(RadioButton::isSelected)
                .findFirst();

        if (!selectedOpt.isPresent()) {
            feedbackLabel.setText("Seleziona una risposta prima di inviare.");
            return;
        }

        String selectedText = selectedOpt.get().getText();
        boolean isCorrect = selectedText.equals(correctAnswerText);
        session.recordAnswer(isCorrect);

        Map<Boolean, String> messages = new HashMap<>();
        messages.put(true, "Risposta corretta!");
        messages.put(false, "Risposta sbagliata. La risposta corretta era: " + correctAnswerText);

        Map<Boolean, String> styles = new HashMap<>();
        styles.put(true, "-fx-text-fill: green;");
        styles.put(false, "-fx-text-fill: red;");

        feedbackLabel.setText(messages.get(isCorrect));
        feedbackLabel.setStyle(styles.get(isCorrect));

        setInteractionEnabled(false);

        PauseTransition pause = new PauseTransition(Duration.seconds(1));
        pause.setOnFinished(e -> {
            setInteractionEnabled(false);
            loadNextQuestion(maxQuestions);
        });

        if (countdownTimeline != null) {
            countdownTimeline.stop();
        }

        pause.play();
    }

    /**
     * Abilita o disabilita l'interazione dell'utente con specifici componenti dell'interfaccia utente.
     * Questo metodo viene utilizzato per controllare se l'utente può interagire con
     * elementi interattivi come pulsanti e opzioni durante il quiz.
     *
     * @param enabled un valore booleano che indica se abilitare o disabilitare
     *                l'interazione. Se impostato a true, gli elementi saranno abilitati.
     *                Se impostato a false, gli elementi saranno disabilitati.
     */
    private void setInteractionEnabled(boolean enabled) {
        List<javafx.scene.control.Control> controls = Arrays.asList(submitButton, skipButton, optionA, optionB, optionC, optionD);
        controls.forEach(c -> c.setDisable(!enabled));
    }

    /**
     * Gestisce l'evento attivato quando il pulsante "Salta" viene cliccato durante il quiz.
     *
     * Questo metodo esegue le seguenti operazioni:
     * 1. Registra la domanda saltata come non risposta correttamente chiamando `session.recordAnswer(false)`.
     * 2. Ferma il timer del conto alla rovescia se è attivo per prevenire ulteriori aggiornamenti.
     * 3. Verifica se il numero massimo di domande (`maxQuestions`) è stato risposto:
     *    - Se tutte le domande sono state risposte, mostra il punteggio finale utilizzando `feedbackLabel`,
     *      salva la sessione utilizzando `session.saveSession()`, disabilita l'interazione dell'utente
     *      chiamando `disableInteraction()`, e termina il quiz utilizzando `concludeQuiz()`.
     *    - Se ci sono domande rimanenti, carica la prossima domanda chiamando
     *      `loadNextQuestion(maxQuestions)`.
     */
    @FXML
    public void onSkipQuestion() {
        session.recordAnswer(false);

        if (countdownTimeline != null) {
            countdownTimeline.stop();
        }

        if (session.getQuestionsAnswered() >= maxQuestions) {
            feedbackLabel.setText("Hai completato il quiz! Punteggio: " + session.getScore());
            session.saveSession();
            disableInteraction();
            concludeQuiz();
            return;
        }

        loadNextQuestion(maxQuestions);
    }

    /**
     * Conclude la sessione corrente del quiz mostrando la schermata dei risultati
     * e chiudendo la finestra corrente delle domande.
     * Questo metodo è responsabile per:
     * - Caricare la schermata dei risultati dal file FXML.
     * - Passare il numero di risposte corrette al ResultsController.
     * - Creare e visualizzare un nuovo stage per mostrare i risultati.
     * - Chiudere lo stage corrente che ospita le domande del quiz.
     * - Mostrare un messaggio di errore tramite `feedbackLabel` in caso di eccezione.
     * Il metodo interagisce con il `ResultsController` per impostare il numero
     * di risposte corrette per la sessione e utilizza componenti JavaFX come `FXMLLoader`
     * e `Stage` per la gestione dell'interfaccia utente.
     * Eventuali errori riscontrati durante il caricamento o la visualizzazione della schermata
     * dei risultati vengono catturati e gestiti registrando l'eccezione e aggiornando
     * il `feedbackLabel` con un messaggio di errore comprensibile all'utente.
     */
    private void concludeQuiz() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ResultView.fxml"));
            Parent root = loader.load();
            ResultsController resultsController = loader.getController();
            resultsController.setCorrectAnswers(session.getCorrectAnswers());
            resultsController.setTotalAnswers(session.getQuestionsAnswered());

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Risultati del Quiz");
            stage.show();

            Stage currentStage = (Stage) questionLabel.getScene().getWindow();
            currentStage.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Recupera l'ID del documento associato alla sessione corrente o alla configurazione.
     *
     * @return l'ID del documento come intero
     */
    public int getDocumentId() {
        return documentId;
    }

    /**
     * Imposta l'ID del documento per la sessione o la configurazione.
     *
     * @param id l'ID del documento da assegnare
     */
    @FXML
    public void setDocumentId(int id) {
        this.documentId = id;
    }
}