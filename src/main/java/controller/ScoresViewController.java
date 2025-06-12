package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import model.GameSession;
import management.DocumentsManagement;

/**
 * Gestisce l'evento di navigazione di ritorno al menu principale.
 * Tipicamente invocato quando viene cliccato il pulsante "Back to Menu".
 * Chiama il metodo {@code changeWindow()} nella classe {@code ParentMenu}
 * per passare alla scena o finestra appropriata che rappresenta il menu principale.
 */
public class ScoresViewController implements Initializable {

    /**
     * Rappresenta un pulsante nell'interfaccia ScoresViewController che permette
     * all'utente di tornare al menu principale. Questo pulsante è associato
     * al metodo {@code handleBackToMenu}, che viene attivato quando il pulsante viene cliccato.
     *
     * Serve come elemento dell'interfaccia utente per passare dalla vista
     * dei punteggi personali alla schermata del menu principale.
     */
    @FXML
    private Button backToMenuButton;

    /**
     * Un'enumerazione che rappresenta le modalità di ordinamento utilizzate all'interno del
     * ScoresViewController. L'ordinamento può essere basato sul punteggio
     * delle partite o sulla data.
     *
     * Questa enum è principalmente utilizzata per definire il comportamento
     * di ordinamento quando si visualizzano e organizzano i dati nelle tabelle associate.
     */
    private enum SortMode{
        BY_SCORE, BY_DATE
    }

    /**
     * Rappresenta la modalità di ordinamento corrente utilizzata nel ScoresViewController.
     * L'ordinamento può essere per punteggio o per data, come definito dall'enumerazione SortMode.
     *
     * Questa variabile determina come i dati delle sessioni di gioco sono organizzati e visualizzati
     * nelle tabelle associate ai diversi livelli di difficoltà. Viene aggiornata dinamicamente
     * in base alle interazioni dell'utente, come il clic sul pulsante di ordinamento.
     */
    private SortMode currentSort = SortMode.BY_SCORE;

    /**
     * Il sortButton è una variabile d'istanza privata annotata con {@code @FXML},
     * che rappresenta un pulsante nell'interfaccia utente responsabile di attivare la funzionalità di ordinamento
     * dei dati delle sessioni di gioco visualizzati nelle tabelle dei punteggi. L'interazione con questo pulsante
     * tipicamente avvia il processo di alternanza tra le modalità di ordinamento, come ordinare
     * per punteggio o per data.
     */
    @FXML
    private Button sortButton;

    /**
     * Rappresenta un componente TabPane utilizzato per gestire e visualizzare multiple tab
     * corrispondenti a diversi livelli di difficoltà (es. Facile, Medio, Difficile).
     * Ogni tab può contenere una TableView che mostra le sessioni di gioco e i punteggi
     * per il relativo livello di difficoltà. Questo elemento dell'interfaccia fa parte del
     * ScoresViewController, permettendo all'utente di navigare tra
     * le diverse tabelle dei punteggi specifiche per difficoltà.
     *
     * Il componente `difficultyTabs` è collegato al file FXML tramite l'annotazione `@FXML`
     * e viene caricato automaticamente durante l'inizializzazione del controller.
     */
    @FXML
    private TabPane difficultyTabs;

    /**
     * Rappresenta la tab "Facile" nell'interfaccia di selezione della difficoltà del ScoresViewController.
     * Questa tab permette all'utente di visualizzare e interagire con i dati delle sessioni di gioco filtrati per il livello di difficoltà "Facile".
     * Fa parte di un pannello a tab che organizza i punteggi di gioco per difficoltà.
     */
    @FXML
    private Tab easySelector;

    /**
     * Rappresenta una tabella che mostra i dati delle sessioni di gioco per il livello di difficoltà "Facile".
     * Questa tabella fa parte dell'interfaccia utente controllata dal {@code ScoresViewController}.
     *
     * La {@code easyTable} è popolata con istanze di {@code model.GameSession} che rappresentano
     * le sessioni di gioco completate con difficoltà facile. Ogni riga corrisponde a una singola sessione,
     * mostrando dati come il titolo della sessione, il punteggio e il timestamp. La tabella supporta funzionalità
     * di ordinamento e interazione definite nel controller.
     *
     * L'iniezione FXML è utilizzata per questo campo per collegarlo al componente TableView corrispondente
     * definito nel file FXML caricato dal {@code ScoresViewController}.
     */
    @FXML
    private TableView<GameSession> easyTable;

    /**
     * Rappresenta la colonna della tabella nella tabella dei punteggi di difficoltà "Facile" che mostra
     * i titoli dei documenti associati alle sessioni di gioco.
     *
     * Questa colonna è configurata per estrarre e visualizzare le informazioni sul titolo
     * dall'oggetto {@link GameSession}, specificamente come valore String.
     * Il titolo corrisponde al documento associato alla sessione di gioco.
     *
     * Fa parte dell'interfaccia utente gestita dalla classe {@code ScoresViewController}
     * ed è utilizzata per presentare i dati in un formato tabellare strutturato per la difficoltà "Facile".
     */
    @FXML
    private TableColumn<GameSession, String> easyTitleColumn;

    /**
     * Rappresenta la colonna "Punteggio" nella tabella che mostra le sessioni di gioco per il livello di difficoltà "Facile".
     * Questa colonna è utilizzata per visualizzare i punteggi ottenuti dai giocatori durante le sessioni di gioco categorizzate come "Facile".
     *
     * La colonna recupera e mostra valori interi corrispondenti alla proprietà `score` degli
     * oggetti {@link GameSession}.
     *
     * Questo campo è annotato con `@FXML` per indicare la sua associazione con il layout UI
     * definito nel corrispondente file FXML per il ScoresViewController.
     */
    @FXML
    private TableColumn<GameSession, Integer> easyScoreColumn;

    /**
     * Rappresenta la colonna della tabella nella tabella dei punteggi di difficoltà "Facile" che mostra
     * la data e l'ora associata a ciascuna sessione di gioco.
     *
     * Ogni cella in questa colonna riflette il timestamp di una specifica sessione di gioco,
     * indicando quando la sessione è avvenuta. I dati per questa colonna sono presi dal
     * campo `timestamp` dei corrispondenti oggetti {@link GameSession}.
     */
    @FXML
    private TableColumn<GameSession, String> easyDateColumn;

    /**
     * Rappresenta l'elemento Tab per il livello di difficoltà "Medio" nell'interfaccia utente.
     * Questa tab permette agli utenti di navigare e visualizzare i punteggi di difficoltà media.
     * Fa parte del sistema di navigazione a tab basato sulla difficoltà nella vista dei punteggi.
     */
    @FXML
    private Tab mediumSelector;

    /**
     * Rappresenta la vista tabella responsabile della visualizzazione dei dati delle sessioni di gioco
     * filtrati per il livello di difficoltà "Medio" nell'interfaccia utente dell'applicazione.
     * Ogni voce nella tabella corrisponde a un oggetto {@link GameSession}.
     *
     * Questa tabella è popolata dinamicamente con i dati delle sessioni di gioco di difficoltà "Media"
     * attraverso la logica dell'applicazione e supporta funzionalità come l'ordinamento e
     * l'interazione come configurato nel controller.
     *
     * La tabella è principalmente gestita dalla classe {@code ScoresViewController}
     * e interagisce con metodi focalizzati sul caricamento, ordinamento e visualizzazione delle sessioni di gioco
     * basate sulla difficoltà selezionata.
     */
    @FXML
    private TableView<GameSession> mediumTable;

    /**
     * Rappresenta una TableColumn nell'interfaccia utente responsabile della visualizzazione del titolo
     * dei documenti associati alle sessioni di gioco presenti nella tabella di difficoltà "Medio".
     *
     * Questa colonna è configurata per mostrare valori stringa recuperati usando l'ID del documento dai
     * dati della sessione di gioco. Fa parte della mediumTable ed è popolata dinamicamente quando
     * viene visualizzata la tabella dei punteggi di difficoltà media.
     *
     * Utilizzato da metodi come {@code setupTableColumns} per definire le factory dei valori delle celle
     * per una corretta visualizzazione dei dati ed è popolato con dati da {@code mediumSessions}.
     *
     * I dati visualizzati in questa colonna sono associati a entità {@link GameSession}.
     */
    @FXML
    private TableColumn<GameSession, String> mediumTitleColumn;

    /**
     * Rappresenta la TableColumn per visualizzare il punteggio delle sessioni di gioco con difficoltà "Medio".
     * Questa colonna è configurata per mostrare i valori interi dei punteggi memorizzati negli oggetti {@link GameSession}.
     * Fa parte della mediumTable nel ScoresViewController, che mostra
     * tutti i dati delle sessioni di gioco di difficoltà media.
     */
    @FXML
    private TableColumn<GameSession, Integer> mediumScoreColumn;

    /**
     * Rappresenta la colonna della tabella utilizzata per visualizzare le informazioni sulla data delle sessioni di gioco
     * all'interno della tabella dei punteggi di difficoltà "Medio" nell'interfaccia utente. È configurata
     * per gestire e mostrare dati relativi alla data per le sessioni di gioco appartenenti al livello
     * di difficoltà medio.
     *
     * I valori della colonna dovrebbero mostrare il timestamp associato a ogni sessione di gioco,
     * riflettendo quando la sessione è avvenuta. Questa informazione è legata alla proprietà `timestamp`
     * dell'oggetto {@link GameSession}.
     *
     * Questo campo è annotato con {@code @FXML} per indicare che è definito nel relativo
     * file FXML e viene iniettato a runtime dal framework JavaFX.
     */
    @FXML
    private TableColumn<GameSession, String> mediumDateColumn;

    /**
     * Rappresenta il componente Tab utilizzato per visualizzare la tabella dei punteggi di difficoltà "Difficile".
     * Associato ai dati di difficoltà "Difficile", questa tab fornisce un'interfaccia per
     * visualizzare informazioni sulle sessioni di gioco, come punteggi, titoli e timestamp, per
     * sessioni giocate al livello più difficile del gioco.
     *
     * Questa Tab fa parte del difficultyTabs nel ScoresViewController e,
     * quando selezionata, interagisce con la hardTable per mostrare i dati corrispondenti.
     */
    @FXML
    private Tab hardSelector;

    /**
     * Rappresenta la vista tabella utilizzata per visualizzare i dati delle sessioni di gioco per il livello di difficoltà "Difficile".
     * Questa tabella è popolata con istanze di {@link GameSession}, contenenti dettagli
     * come il titolo del documento, il punteggio e la data per le sessioni di gioco categorizzate come "Difficile".
     * I dati sono caricati dinamicamente, visualizzati e ordinati all'interno della tabella in base alle interazioni dell'utente.
     */
    @FXML
    private TableView<GameSession> hardTable;

    /**
     * Rappresenta la colonna "Titolo" nella tabella di livello di difficoltà "Difficile" all'interno della vista dei punteggi personali.
     * Questa colonna mostra i titoli dei documenti associati alle sessioni di gioco memorizzate nella categoria di difficoltà "Difficile".
     * La colonna è legata alla proprietà title della classe {@code GameSession}.
     */
    @FXML
    private TableColumn<GameSession, String> hardTitleColumn;

    /**
     * Rappresenta la colonna della tabella per visualizzare i punteggi di difficoltà 'Difficile' nell'interfaccia utente.
     * Questa colonna fa parte della `hardTable` nel `ScoresViewController` ed è legata alla
     * proprietà `score` della classe `GameSession`. Mostra i punteggi interi ottenuti dai giocatori
     * durante sessioni impostate al livello di sfida "Difficile".
     *
     * La colonna è configurata come parte della configurazione della tabella per recuperare e visualizzare dati relativi ai punteggi
     * per sessioni di gioco con difficoltà "Difficile", fornendo una rappresentazione visiva all'interno dell'interfaccia.
     */
    @FXML
    private TableColumn<GameSession, Integer> hardScoreColumn;

    /**
     * Rappresenta la colonna "Data" nella tabella che mostra i dati delle sessioni di gioco per il livello di difficoltà "Difficile".
     * Questa colonna è configurata per visualizzare il timestamp di ogni sessione di gioco, formattato come stringa.
     *
     * È associata alla tabella di difficoltà difficile ed è popolata con dati dalla lista {@code hardSessions}.
     * I timestamp corrispondono alla data e ora in cui le sessioni sono avvenute.
     */
    @FXML
    private TableColumn<GameSession, String> hardDateColumn;

    /**
     * Memorizza una lista di sessioni di gioco specificamente filtrate o categorizzate sotto il livello di difficoltà "Facile".
     * Ogni sessione nella lista è rappresentata da un'istanza di {@code model.GameSession}.
     * La lista è osservabile, permettendo all'interfaccia utente di riflettere automaticamente i cambiamenti alla lista.
     * È utilizzata nel contesto della classe {@code ScoresViewController} per gestire e visualizzare
     * dati delle sessioni di gioco relativi alla difficoltà "Facile" nella relativa tabella dei punteggi.
     */
    private ObservableList<GameSession> easySessions = FXCollections.observableArrayList();

    /**
     * Una collezione di sessioni di gioco osservabili corrispondenti al livello di difficoltà "Medio".
     * Questa lista è utilizzata per gestire e visualizzare i dati delle sessioni di gioco di difficoltà media nell'interfaccia utente.
     *
     * La lista è inizializzata come una lista osservabile vuota e può essere popolata dinamicamente
     * con istanze di {@code model.GameSession} a seconda della logica di recupero e visualizzazione dati dell'applicazione.
     */
    private ObservableList<GameSession> mediumSessions = FXCollections.observableArrayList();

    /**
     * Una lista osservabile di sessioni di gioco filtrate per il livello di difficoltà "Difficile".
     * Questa lista è specificamente progettata per memorizzare e gestire oggetti GameSession
     * che rappresentano sessioni di gioco giocate a difficoltà "Difficile".
     * I dati sono gestiti dinamicamente, permettendo aggiornamenti in tempo reale e
     * riflettendo cambiamenti nel set di dati sottostante.
     *
     * Questo campo è utilizzato principalmente per popolare e visualizzare la tabella dei punteggi
     * di difficoltà "Difficile" nell'interfaccia utente.
     */
    private ObservableList<GameSession> hardSessions = FXCollections.observableArrayList();


    // Dati Classifica Globale

    @FXML
    private TabPane globalDifficultyTabs;

    @FXML
    private Tab globalEasySelector;

    @FXML
    private TableView<GameSession> globalEasyTable;

    @FXML
    private TableColumn<GameSession, String> globalEasyUsernameColumn;

    @FXML
    private TableColumn<GameSession, String> globalEasyTitleColumn;

    @FXML
    private TableColumn<GameSession, Integer> globalEasyScoreColumn;

    @FXML
    private TableColumn<GameSession, String> globalEasyDateColumn;

    @FXML
    private Tab globalMediumSelector;

    @FXML
    private TableView<GameSession> globalMediumTable;

    @FXML
    private TableColumn<GameSession, String> globalMediumUsernameColumn;

    @FXML
    private TableColumn<GameSession, String> globalMediumTitleColumn;

    @FXML
    private TableColumn<GameSession, Integer> globalMediumScoreColumn;

    @FXML
    private TableColumn<GameSession, String> globalMediumDateColumn;

    @FXML
    private Tab globalHardSelector;

    @FXML
    private TableView<GameSession> globalHardTable;

    @FXML
    private TableColumn<GameSession, String> globalHardUsernameColumn;

    @FXML
    private TableColumn<GameSession, String> globalHardTitleColumn;

    @FXML
    private TableColumn<GameSession, Integer> globalHardScoreColumn;

    @FXML
    private TableColumn<GameSession, String> globalHardDateColumn;

    private ObservableList<GameSession> globalEasySessions = FXCollections.observableArrayList();

    private ObservableList<GameSession> globalMediumSessions = FXCollections.observableArrayList();

    private ObservableList<GameSession> globalHardSessions = FXCollections.observableArrayList();

    /**
     * Inizializza la classe controller e configura le impostazioni richieste per l'interfaccia utente,
     * come la configurazione delle colonne delle tabelle, il caricamento dei dati dal database,
     * la visualizzazione delle tabelle dei punteggi iniziali e l'applicazione dell'ordinamento a tutte le tabelle.
     *
     * @param url la posizione utilizzata per risolvere i percorsi relativi per l'oggetto root, o null se sconosciuta.
     * @param rb  il resource bundle che fornisce funzionalità di localizzazione per questo controller o null se non necessario.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Configura le colonne per tutte le tabelle della classifica personale (facile, medio, difficile)
        setupTableColumns(easyTitleColumn, easyScoreColumn, easyDateColumn);
        setupTableColumns(mediumTitleColumn, mediumScoreColumn, mediumDateColumn);
        setupTableColumns(hardTitleColumn, hardScoreColumn, hardDateColumn);

        // Configura le colonne per tutte le tabelle della classifica globale (facile, medio, difficile)
        setupGlobalTableColumns(globalEasyUsernameColumn, globalEasyTitleColumn, globalEasyScoreColumn, globalEasyDateColumn);
        setupGlobalTableColumns(globalMediumUsernameColumn,globalMediumTitleColumn, globalMediumScoreColumn, globalMediumDateColumn);
        setupGlobalTableColumns(globalHardUsernameColumn,globalHardTitleColumn, globalHardScoreColumn, globalHardDateColumn);

        // Carica i dati delle sessioni dal database
        loadPersonalData();
        loadGlobalData();

        // Mostra i dati nelle rispettive tabelle
        showEasyScore(null);
        showMediumScore(null);
        showHardScore(null);

        showGlobalEasyScore(null);
        showGlobalMediumScore(null);
        showGlobalHardScore(null);

        // Applica l'ordinamento iniziale a tutte le tabelle
        applySortToAllTables();
    }

    /**
     * Configura le factory dei valori delle celle per le colonne specificate della tabella per visualizzare correttamente
     * i dati delle sessioni di gioco, incluso il titolo del documento, il punteggio e la data.
     *
     * @param titleCol la colonna della tabella che mostrerà il titolo del documento
     *                 associato alla sessione di gioco. Il titolo è recuperato usando
     *                 l'ID del documento dalla sessione di gioco.
     * @param scoreCol la colonna della tabella che mostrerà il punteggio associato alla
     *                 sessione di gioco. Il punteggio è recuperato direttamente come valore intero.
     * @param dateCol  la colonna della tabella che mostrerà il timestamp della sessione di gioco,
     *                 riflettendo la data e l'ora in cui è avvenuta.
     */
    private void setupTableColumns(TableColumn<GameSession, String> titleCol,
                                   TableColumn<GameSession, Integer> scoreCol,
                                   TableColumn<GameSession, String> dateCol) {

        // Configura la colonna del titolo per mostrare il titolo del documento basato sull'ID
        titleCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(DocumentsManagement.getTitleFromId(cellData.getValue().getDocumentID())));

        // Configura la colonna del punteggio per mostrare il punteggio come intero
        scoreCol.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getScore()).asObject());

        // Configura la colonna della data per mostrare il timestamp come stringa
        dateCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTimestamp()));
    }

    private void setupGlobalTableColumns(TableColumn<GameSession, String> usernameCol,
                                    TableColumn<GameSession, String> titleCol,
                                    TableColumn<GameSession, Integer> scoreCol,
                                    TableColumn<GameSession, String> dateCol) {

        // Configura la colonna dell'username per mostrare il nome del giocatore di quella posizione in classifica
        usernameCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getUsername()));

        // Configura la colonna del titolo per mostrare il titolo del documento basato sull'ID
        titleCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(DocumentsManagement.getTitleFromId(cellData.getValue().getDocumentID())));

        // Configura la colonna del punteggio per mostrare il punteggio come intero
        scoreCol.setCellValueFactory(cellData ->
                new SimpleIntegerProperty(cellData.getValue().getScore()).asObject());

        // Configura la colonna della data per mostrare il timestamp come stringa
        dateCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getTimestamp()));
    }

    private void loadPersonalData() {
        List<GameSession> userSessions = management.GameSessionManagement.getSessionsByUsername(management.GameSessionManagement.getInstance().getUsername());

        easySessions.clear();
        mediumSessions.clear();
        hardSessions.clear();

        for (GameSession session : userSessions) {
            String diff = session.getDifficulty();
            if ("EASY".equals(diff)) {
                easySessions.add(session);
            } else if ("MEDIUM".equals(diff)) {
                mediumSessions.add(session);
            } else if ("HARD".equals(diff)) {
                hardSessions.add(session);
            }
        }
    }

    private void loadGlobalData() {
        List<GameSession> allSessions = management.GameSessionManagement.getAllSessions();

        globalEasySessions.clear();
        globalMediumSessions.clear();
        globalHardSessions.clear();

        for (GameSession session : allSessions) {
            String diff = session.getDifficulty();
            if ("EASY".equals(diff)) {
                globalEasySessions.add(session);
            } else if ("MEDIUM".equals(diff)) {
                globalMediumSessions.add(session);
            } else if ("HARD".equals(diff)) {
                globalHardSessions.add(session);
            }
        }
    }

    /**
     * Applica la modalità di ordinamento corrente a tutte le tabelle di difficoltà: facile, medio e difficile.
     * Cancella i criteri di ordinamento esistenti per ogni tabella e ri-applica l'ordinamento basato su
     * la modalità {@code currentSort} specificata. L'ordinamento può essere applicato
     * per punteggio o per data a seconda del valore di {@code currentSort}.
     *
     * Questo metodo delega la logica effettiva di ordinamento al metodo {@code applySort},
     * che è invocato separatamente per ogni tabella di difficoltà con le rispettive
     * colonne punteggio e data.
     */
    private void applySortToAllTables() {
        // Applica l'ordinamento a tutte e tre le tabelle di difficoltà
        applySort(easyTable, easyScoreColumn, easyDateColumn);
        applySort(mediumTable, mediumScoreColumn, mediumDateColumn);
        applySort(hardTable, hardScoreColumn, hardDateColumn);
        applySort(globalEasyTable, globalEasyScoreColumn, globalEasyDateColumn);
        applySort(globalMediumTable, globalMediumScoreColumn, globalMediumDateColumn);
        applySort(globalHardTable, globalHardScoreColumn, globalHardDateColumn);
    }

    /**
     * Applica l'ordinamento alla tabella specificata basandosi sulla modalità di ordinamento corrente.
     * Cancella i criteri di ordinamento esistenti e imposta un ordine di ordinamento usando la specifica
     * colonna punteggio o data a seconda del valore di {@code currentSort}.
     * Infine, attiva l'ordinamento sulla tabella.
     *
     * @param table    la vista tabella alla quale è applicato l'ordinamento. Mostra
     *                 i dati delle sessioni di gioco ed è ordinata secondo i criteri specificati.
     * @param scoreCol la colonna della tabella che rappresenta il punteggio delle sessioni di gioco.
     *                 Questa colonna è usata quando si ordina per punteggio.
     * @param dateCol  la colonna della tabella che rappresenta la data delle sessioni di gioco.
     *                 Questa colonna è usata quando si ordina per data.
     */
    private void applySort(TableView<GameSession> table,
                           TableColumn<GameSession, Integer> scoreCol,
                           TableColumn<GameSession, String> dateCol) {
        // Pulisce gli ordinamenti esistenti
        table.getSortOrder().clear();

        // Aggiunge la colonna di ordinamento appropriata in base alla modalità corrente
        table.getSortOrder().add(currentSort == SortMode.BY_SCORE ? scoreCol : dateCol);

        // Applica l'ordinamento
        table.sort();
    }

    /**
     * Gestisce l'azione di cambiare la modalità di ordinamento per visualizzare i dati delle sessioni di gioco.
     * Alterna tra l'ordinamento per punteggio e l'ordinamento per data basandosi sullo stato corrente.
     * Dopo aver aggiornato la modalità di ordinamento, il metodo applica l'ordinamento aggiornato a tutte
     * le tabelle relative alla difficoltà usando il metodo {@code applySortToAllTables}.
     *
     * @param event l'evento di azione attivato quando l'utente interagisce con il controllo di ordinamento,
     *              come il clic sul pulsante di ordinamento.
     */
    @FXML
    private void handleChangeSort(ActionEvent event) {
        // Alterna tra le modalità di ordinamento
        currentSort = (currentSort == SortMode.BY_SCORE) ? SortMode.BY_DATE : SortMode.BY_SCORE;
        
        // Applica il nuovo ordinamento a tutte le tabelle
        applySortToAllTables();
    }

    /**
     * Gestisce l'azione di navigazione di ritorno al menu principale.
     * Carica il file FXML UserManagementView, lo imposta come scena corrente,
     * e lo mostra nel stage principale.
     *
     * @param event l'evento di azione attivato quando viene cliccato il pulsante back-to-menu.
     */
    @FXML
    private void handleBackToMenu(ActionEvent event) {
        loadView(event, "/view/MainMenu.fxml");
    }

    /**
     * Mostra la tabella dei punteggi per il livello di difficoltà "Facile".
     * Popola la easyTable con i dati delle sessioni di gioco memorizzati in easySessions,
     * se la tabella non è null.
     *
     * @param event l'evento che attiva questa azione, tipicamente iniziato
     *              dall'utente che interagisce con l'interfaccia.
     */
    @FXML
    private void showEasyScore(Event event) {
        if (easyTable != null) {
            // Imposta i dati nella tabella facile
            easyTable.setItems(easySessions);
        }
    }

    /**
     * Mostra la tabella dei punteggi per il livello di difficoltà "Medio".
     * Popola la mediumTable con i dati delle sessioni di gioco memorizzati in mediumSessions,
     * se la tabella non è null.
     *
     * @param event l'evento che attiva questa azione, tipicamente iniziato
     *              dall'utente che interagisce con l'interfaccia.
     */
    @FXML
    private void showMediumScore(Event event) {
        if (mediumTable != null) {
            // Imposta i dati nella tabella medio
            mediumTable.setItems(mediumSessions);
        }
    }

    /**
     * Mostra la tabella dei punteggi per il livello di difficoltà "Difficile".
     * Popola la hardTable con i dati delle sessioni di gioco memorizzati in hardSessions,
     * se la tabella non è null.
     *
     * @param event l'evento che attiva questa azione, tipicamente iniziato
     *              dall'utente che interagisce con l'interfaccia.
     */
    @FXML
    private void showHardScore(Event event) {
        if (hardTable != null) {
            // Imposta i dati nella tabella difficile
            hardTable.setItems(hardSessions);
        }
    }

    /**
     * Mostra la tabella dei punteggi globali per il livello di difficoltà "Facile".
     * Popola la globalEasyTable con i dati delle sessioni di gioco memorizzati in globalEasySessions,
     * se la tabella non è null.
     *
     * @param event l'evento che attiva questa azione, tipicamente iniziato
     *              dall'utente che interagisce con l'interfaccia.
     */
    @FXML
    private void showGlobalEasyScore(Event event) {
        if (globalEasyTable != null) {
            // Imposta i dati nella tabella globale facile
            globalEasyTable.setItems(globalEasySessions);
        }
    }

    /**
     * Mostra la tabella dei punteggi globali per il livello di difficoltà "Medio".
     * Popola la globalMediumTable con i dati delle sessioni di gioco memorizzati in globalMediumSessions,
     * se la tabella non è null.
     *
     * @param event l'evento che attiva questa azione, tipicamente iniziato
     *              dall'utente che interagisce con l'interfaccia.
     */
    @FXML
    private void showGlobalMediumScore(Event event) {
        if (globalMediumTable != null) {
            // Imposta i dati nella tabella globale medio
            globalMediumTable.setItems(globalMediumSessions);
        }
    }

    /**
     * Mostra la tabella dei punteggi globali per il livello di difficoltà "Difficile".
     * Popola la globalHardTable con i dati delle sessioni di gioco memorizzati in globalHardSessions,
     * se la tabella non è null.
     *
     * @param event l'evento che attiva questa azione, tipicamente iniziato
     *              dall'utente che interagisce con l'interfaccia.
     */
    @FXML
    private void showGlobalHardScore(Event event) {
        if (globalHardTable != null) {
            // Imposta i dati nella tabella globale difficile
            globalHardTable.setItems(globalHardSessions);
        }
    }

    /**
     * Metodo helper per caricare una vista FXML e mostrarla nella finestra corrente
     * @param event L'evento che ha triggerato il cambio di vista
     * @param fxmlPath Il percorso del file FXML da caricare
     */
    private void loadView(ActionEvent event, String fxmlPath) {
        try {
            // Inizializza il loader con il percorso specificato
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

            // Carica la scena dal file FXML
            Scene scene = new Scene(loader.load());

            // Ottiene lo stage corrente a partire dal nodo che ha generato l'evento
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // Imposta la nuova scena nello stage corrente
            stage.setScene(scene);

            // Centra la finestra sullo schermo
            stage.centerOnScreen();

            // Visualizza la nuova scena
            stage.show();
        } catch (IOException e) {
            alert.Messages.showError("Errore", "Impossibile caricare la finestra");
        }
    }
}