package service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * La classe DocumentsManagement fornisce metodi per la gestione dei documenti e delle parole associate.
 * Include funzionalità per il filtraggio del testo, l'inserimento nel database e la gestione dei documenti.
 * La classe supporta operazioni come il caricamento, l'eliminazione e il recupero dei titoli dei documenti dal database.
 *
 * @author Gruppo6
 */
public class DocumentsManagement {

    /**
     * Una collezione di stop words (parole vuote) in lingua italiana.
     *
     * Questa variabile è tipicamente utilizzata nell'elaborazione del linguaggio naturale (NLP)
     * per filtrare parole comuni che spesso non sono significative per l'analisi del testo,
     * come "e", "ma", "anche", ecc. Queste parole vengono frequentemente escluse per migliorare
     * le prestazioni degli algoritmi di elaborazione del testo, come motori di ricerca,
     * analisi del sentiment e classificazione del testo.
     *
     * Le stop words contenute in questa variabile sono specifiche per la lingua italiana
     * e potrebbero non essere rilevanti per l'uso con altre lingue.
     */
    private static List<String> stopWords_IT = new ArrayList<>(Arrays.asList("a", "abbastanza", "abbia", "abbiamo", "abbiano", "abbiate", "accidenti", "ad", "adesso", "affinché", "agl", "agli", "ahime", "ahimè", "ai", "al", "alcuna", "alcuni", "alcuno", "all", "alla", "alle", "allo", "allora", "altre", "altri", "altrimenti", "altro", "altrove", "altrui", "anche", "ancora", "anni", "anno", "ansa", "anticipo", "assai", "attesa", "attraverso", "avanti", "avemmo", "avendo", "avente", "aver", "avere", "averlo", "avesse", "avessero", "avessi", "avessimo", "aveste", "avesti", "avete", "aveva", "avevamo", "avevano", "avevate", "avevi", "avevo", "avrai", "avranno", "avrebbe", "avrebbero", "avrei", "avremmo", "avremo", "avreste", "avresti", "avrete", "avrà", "avrò", "avuta", "avute", "avuti", "avuto", "basta", "ben", "bene", "benissimo", "brava", "bravo", "buono", "c", "caso", "cento", "certa", "certe", "certi", "certo", "che", "chi", "chicchessia", "chiunque", "ci", "ciascuna", "ciascuno", "cima", "cinque", "cio", "cioe", "cioè", "circa", "citta", "città", "ciò", "co", "codesta", "codesti", "codesto", "cogli", "coi", "col", "colei", "coll", "coloro", "colui", "come", "cominci", "comprare", "comunque", "con", "concernente", "conclusione", "consecutivi", "consecutivo", "consiglio", "contro", "cortesia", "cos", "cosa", "cosi", "così", "cui", "d", "da", "dagl", "dagli", "dai", "dal", "dall", "dalla", "dalle", "dallo", "dappertutto", "davanti", "degl", "degli", "dei", "del", "dell", "della", "delle", "dello", "dentro", "detto", "deve", "devo", "di", "dice", "dietro", "dire", "dirimpetto", "diventa", "diventare", "diventato", "dopo", "doppio", "dov", "dove", "dovra", "dovrà", "dovunque", "due", "dunque", "durante", "e", "ebbe", "ebbero", "ebbi", "ecc", "ecco", "ed", "effettivamente", "egli", "ella", "entrambi", "eppure", "era", "erano", "eravamo", "eravate", "eri", "ero", "esempio", "esse", "essendo", "esser", "essere", "essi", "ex", "fa", "faccia", "facciamo", "facciano", "facciate", "faccio", "facemmo", "facendo", "facesse", "facessero", "facessi", "facessimo", "faceste", "facesti", "faceva", "facevamo", "facevano", "facevate", "facevi", "facevo", "fai", "fanno", "farai", "faranno", "fare", "farebbe", "farebbero", "farei", "faremmo", "faremo", "fareste", "faresti", "farete", "farà", "farò", "fatto", "favore", "fece", "fecero", "feci", "fin", "finalmente", "finche", "fine", "fino", "forse", "forza", "fosse", "fossero", "fossi", "fossimo", "foste", "fosti", "fra", "frattempo", "fu", "fui", "fummo", "fuori", "furono", "futuro", "generale", "gente", "gia", "giacche", "giorni", "giorno", "giu", "già", "gli", "gliela", "gliele", "glieli", "glielo", "gliene", "grande", "grazie", "gruppo", "ha", "haha", "hai", "hanno", "ho", "i", "ie", "ieri", "il", "improvviso", "in", "inc", "indietro", "infatti", "inoltre", "insieme", "intanto", "intorno", "invece", "io", "l", "la", "lasciato", "lato", "le", "lei", "li", "lo", "lontano", "loro", "lui", "lungo", "luogo", "là", "ma", "macche", "magari", "maggior", "mai", "male", "malgrado", "malissimo", "me", "medesimo", "mediante", "meglio", "meno", "mentre", "mesi", "mezzo", "mi", "mia", "mie", "miei", "mila", "miliardi", "milioni", "minimi", "mio", "modo", "molta", "molti", "moltissimo", "molto", "momento", "mondo", "ne", "negl", "negli", "nei", "nel", "nell", "nella", "nelle", "nello", "nemmeno", "neppure", "nessun", "nessuna", "nessuno", "niente", "no", "noi", "nome", "non", "nondimeno", "nonostante", "nonsia", "nostra", "nostre", "nostri", "nostro", "novanta", "nove", "nulla", "nuovi", "nuovo", "o", "od", "oggi", "ogni", "ognuna", "ognuno", "oltre", "oppure", "ora", "ore", "osi", "ossia", "ottanta", "otto", "paese", "parecchi", "parecchie", "parecchio", "parte", "partendo", "peccato", "peggio", "per", "perche", "perchè", "perché", "percio", "perciò", "perfino", "pero", "persino", "persone", "però", "piedi", "pieno", "piglia", "piu", "piuttosto", "più", "po", "pochissimo", "poco", "poi", "poiche", "possa", "possedere", "posteriore", "posto", "potrebbe", "preferibilmente", "presa", "press", "prima", "primo", "principalmente", "probabilmente", "promesso", "proprio", "puo", "pure", "purtroppo", "può", "qua", "qualche", "qualcosa", "qualcuna", "qualcuno", "quale", "quali", "qualunque", "quando", "quanta", "quante", "quanti", "quanto", "quantunque", "quarto", "quasi", "quattro", "quel", "quella", "quelle", "quelli", "quello", "quest", "questa", "queste", "questi", "questo", "qui", "quindi", "quinto", "realmente", "recente", "recentemente", "registrazione", "relativo", "riecco", "rispetto", "salvo", "sara", "sarai", "saranno", "sarebbe", "sarebbero", "sarei", "saremmo", "saremo", "sareste", "saresti", "sarete", "sarà", "sarò", "scola", "scopo", "scorso", "se", "secondo", "seguente", "seguito", "sei", "sembra", "sembrare", "sembrato", "sembrava", "sembri", "sempre", "senza", "sette", "si", "sia", "siamo", "siano", "siate", "siete", "sig", "solito", "solo", "soltanto", "sono", "sopra", "soprattutto", "sotto", "spesso", "sta", "stai", "stando", "stanno", "starai", "staranno", "starebbe", "starebbero", "starei", "staremmo", "staremo", "stareste", "staresti", "starete", "starà", "starò", "stata", "state", "stati", "stato", "stava", "stavamo", "stavano", "stavate", "stavi", "stavo", "stemmo", "stessa", "stesse", "stessero", "stessi", "stessimo", "stesso", "steste", "stesti", "stette", "stettero", "stetti", "stia", "stiamo", "stiano", "stiate", "sto", "su", "sua", "subito", "successivamente", "successivo", "sue", "sugl", "sugli", "sui", "sul", "sull", "sulla", "sulle", "sullo", "suo", "suoi", "tale", "tali", "talvolta", "tanto", "te", "tempo", "terzo", "th", "ti", "titolo", "tra", "tranne", "tre", "trenta", "triplo", "troppo", "trovato", "tu", "tua", "tue", "tuo", "tuoi", "tutta", "tuttavia", "tutte", "tutti", "tutto", "uguali", "ulteriore", "ultimo", "un", "una", "uno", "uomo", "va", "vai", "vale", "vari", "varia", "varie", "vario", "verso", "vi", "vicino", "visto", "vita", "voi", "volta", "volte", "vostra", "vostre", "vostri", "vostro", "è"));

    /**
     * Una lista di stop words comunemente utilizzate in inglese.
     *
     * Queste parole sono tipicamente filtrate durante l'elaborazione del testo poiché non contribuiscono
     * significativamente al significato del testo. La lista include vari pronomi, articoli,
     * preposizioni e verbi ausiliari.
     */
    private static List<String> stopWords_EN = new ArrayList<>(Arrays.asList("i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"));

    /**
     * Costruttore di default per la classe DocumentsManagement.
     * Inizializza un'istanza della classe DocumentsManagement.
     */
    public DocumentsManagement(){}

    /**
     * Filtra e conta le occorrenze delle parole dalla stringa di input.
     * Il metodo rimuove la punteggiatura, converte il testo in minuscolo, esclude alcune stop word predefinite,
     * e restituisce una mappatura delle parole alle rispettive frequenze.
     *
     * @param line La stringa di input da elaborare e filtrare.
     * @return Una mappa dove le chiavi sono parole (dopo il filtraggio) e i valori sono le rispettive frequenze come interi.
     */
    public static Map<String, Integer> textFiltering(String line) {
        return Arrays.stream(line.split("\\P{L}+"))
                .map(word -> word.toLowerCase().replaceAll("[^a-zàèéìòùäöüßáêíóúñ]", ""))
                .filter(cleaned -> !cleaned.isEmpty() && !stopWords_IT.contains(cleaned))
                .collect(Collectors.groupingBy(w -> w, Collectors.summingInt(w -> 1)));
    }

    /**
     * Inserisce le parole e le loro frequenze nel database per un dato documento.
     * Il metodo elabora il testo fornito per estrarre e contare le parole,
     * recupera l'ID del documento corrispondente dal database e inserisce le coppie parola-frequenza.
     * Se il documento non viene trovato nel database, il processo di inserimento viene interrotto.
     *
     * @param fileName Il nome del documento le cui parole devono essere inserite nel database.
     * @param line     La riga di testo contenente le parole da elaborare e memorizzare nel database.
     */
    public static void insertWordsIntoDatabase(String fileName, String line) {
        Map<String, Integer> words = textFiltering(line);

        String findIDQuery = "SELECT id FROM documents WHERE title = ?";
        String insertQuery = "INSERT INTO words (id_document, word, frequency) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManagement.getConnection()) {
            int documentId = -1;

            try (PreparedStatement selectStmt = conn.prepareStatement(findIDQuery)) {
                selectStmt.setString(1, fileName);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    documentId = rs.getInt("id");
                } else {
                    System.err.println("Documento non trovato: " + fileName);
                    return;
                }
            }

            conn.setAutoCommit(false);

            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                int finalDocumentId = documentId;
                words.entrySet().stream().forEach(entry -> {
                    try {
                        insertStmt.setInt(1, finalDocumentId);
                        insertStmt.setString(2, entry.getKey());
                        insertStmt.setInt(3, entry.getValue());
                        insertStmt.addBatch();
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
                insertStmt.executeBatch();
            }

            conn.commit();

        } catch (SQLException e) {
            System.err.println("Errore database: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println("Errore nell'inserimento batch: " + e.getCause().getMessage());
        }
    }

    /**
     * Carica il contenuto di un file specificato nel database, associandolo a un determinato livello di difficoltà.
     * Questo metodo legge il contenuto del file, lo elabora e lo inserisce nel database con i relativi metadati.
     *
     * @param fileName   Il file da caricare nel database. Deve essere un file leggibile.
     * @param difficulty Il livello di difficoltà da associare al contenuto del file. Deve essere uno dei livelli in Levels.Difficulty.
     */
    public static void loadToDB(File fileName, Levels.Difficulty difficulty) {
        if (fileName == null) {
            System.err.println("Il file non è specificato (null).");
            return;
        }
        if (!fileName.exists() || !fileName.canRead()) {
            System.err.println("Il file non esiste o non è leggibile: " + fileName.getAbsolutePath());
            return;
        }
        System.out.println("File selezionato: " + fileName.getAbsolutePath());

        String insertDocSQL = "INSERT INTO documents (title, difficulty, content) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManagement.getConnection();
             PreparedStatement ps = conn.prepareStatement(insertDocSQL)) {

            StringBuilder contentBuilder = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    contentBuilder.append(line).append("\n");
                }
            }

            String content = contentBuilder.toString();


            ps.setString(1, fileName.getName());
            ps.setString(2, difficulty.toString());
            ps.setString(3, content);

            ps.executeUpdate();

            insertWordsIntoDatabase(getBaseName(fileName.getName()), contentBuilder.toString());

        } catch (SQLException | IOException e) {
            System.err.println("Errore caricamento documento: " + e.getMessage());
        }
    }

    /**
     * Elimina un documento e le parole associate dal database in base al titolo specificato.
     * Questo metodo esegue l'eliminazione in modo transazionale per garantire la consistenza dei dati.
     * Se il documento non viene trovato, registra un messaggio di errore corrispondente.
     *
     * @param title Il titolo del documento da eliminare dal database.
     */
    public static void deleteFromDB(String title) {
        String selectIdSql = "SELECT id FROM documents WHERE title = ?";
        String deleteWordsSql = "DELETE FROM words WHERE id_document = ?";
        String deleteDocumentSql = "DELETE FROM documents WHERE id = ?";

        try (Connection conn = DatabaseManagement.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement selectStmt = conn.prepareStatement(selectIdSql)) {
                selectStmt.setString(1, title);
                ResultSet rs = selectStmt.executeQuery();

                if (rs.next()) {
                    int documentId = rs.getInt("id");

                    try (PreparedStatement deleteWordsStmt = conn.prepareStatement(deleteWordsSql);
                         PreparedStatement deleteDocStmt = conn.prepareStatement(deleteDocumentSql)) {

                        deleteWordsStmt.setInt(1, documentId);
                        deleteWordsStmt.executeUpdate();

                        deleteDocStmt.setInt(1, documentId);
                        deleteDocStmt.executeUpdate();

                        conn.commit();

                        System.out.println("Documento e Parole cancellate con successo dal DB.");
                    }
                } else {
                    System.err.println("Documento non trovato: " + title);
                }
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Estrae il nome base di un file rimuovendo l'estensione del file se esiste.
     * Se il nome del file fornito non contiene un'estensione, il metodo restituisce il nome del file originale.
     *
     * @param filename Il nome completo del file da cui estrarre il nome base.
     *                 Non deve essere null e deve rappresentare un nome file valido.
     * @return Il nome base del file come String, escludendo la sua estensione se presente.
     */
    public static String getBaseName(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return filename; // Filename senza estensione
        } else {
            return filename.substring(0, index);
        }
    }

    /**
     * Recupera i titoli di tutti i documenti memorizzati nel database.
     * Il metodo esegue una query SQL per recuperare tutti i titoli dalla tabella "documents"
     * e li restituisce come una lista di stringhe.
     *
     * @return una lista di titoli di documenti come stringhe, o una lista vuota se non vengono trovati titoli
     *         o si verifica un errore durante l'operazione sul database.
     */
    public static List<String> getAllDocumentTitles() {
        List<String> titles = new ArrayList<>();
        String query = "SELECT title FROM documents";

        try (Connection conn = DatabaseManagement.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                titles.add(rs.getString("title"));
            }

        } catch (SQLException e) {
            System.err.println("Errore SQL: " + e.getMessage());
        }

        return titles;
    }

    /**
     * Recupera il titolo di un singolo documento dal database in base all'ID del documento fornito.
     * Esegue una query per recuperare il titolo per l'ID dato e restituisce il titolo se trovato.
     * Se il documento non viene trovato o si verifica un errore, vengono registrati opportuni messaggi di errore.
     *
     * @param id_document L'identificatore univoco del documento di cui recuperare il titolo.
     *                    Non deve essere null e deve rappresentare un ID valido presente nel database.
     * @return Il titolo del documento come String se il documento viene trovato,
     *         o null se il documento non viene trovato o si verifica un errore durante l'operazione sul database.
     */
    public static String getTitleFromId(int id_document) {

        String query = "SELECT title FROM documents WHERE id = ?";

        try(Connection conn = DatabaseManagement.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query);){

            pstmt.setInt(1, id_document);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                return rs.getString("title");
            }else{
                System.err.println("Documento non trovato: " + id_document);
            }

        }
        catch (SQLException e) {
            System.err.println("Errore SQL nell'ottenimento del singolo titolo: " + e.getMessage());
            return null;
        }
        return null;
    }

}