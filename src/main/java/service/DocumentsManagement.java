package service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The DocumentsManagement class provides methods for managing documents and their associated words.
 * It includes functionality for text filtering, database insertion, and document management.
 * The class supports operations such as loading, deleting, and retrieving document titles from the database.
 *
 * @author Gruppo6
 */
public class DocumentsManagement {

    /**
     * A collection of stop words in the Italian language.
     *
     * This variable is typically used in natural language processing (NLP)
     * tasks to filter out common words that are often not meaningful for
     * text analysis, such as "e," "ma," "anche," etc. These words are
     * frequently excluded to improve the performance of text processing
     * algorithms, such as search engines, sentiment analysis, and text
     * classification.
     *
     * The stop words contained in this variable are specific to the Italian
     * language and may not be relevant for usage with other languages.
     */
    private static List<String> stopWords_IT = new ArrayList<>(Arrays.asList("a", "abbastanza", "abbia", "abbiamo", "abbiano", "abbiate", "accidenti", "ad", "adesso", "affinché", "agl", "agli", "ahime", "ahimè", "ai", "al", "alcuna", "alcuni", "alcuno", "all", "alla", "alle", "allo", "allora", "altre", "altri", "altrimenti", "altro", "altrove", "altrui", "anche", "ancora", "anni", "anno", "ansa", "anticipo", "assai", "attesa", "attraverso", "avanti", "avemmo", "avendo", "avente", "aver", "avere", "averlo", "avesse", "avessero", "avessi", "avessimo", "aveste", "avesti", "avete", "aveva", "avevamo", "avevano", "avevate", "avevi", "avevo", "avrai", "avranno", "avrebbe", "avrebbero", "avrei", "avremmo", "avremo", "avreste", "avresti", "avrete", "avrà", "avrò", "avuta", "avute", "avuti", "avuto", "basta", "ben", "bene", "benissimo", "brava", "bravo", "buono", "c", "caso", "cento", "certa", "certe", "certi", "certo", "che", "chi", "chicchessia", "chiunque", "ci", "ciascuna", "ciascuno", "cima", "cinque", "cio", "cioe", "cioè", "circa", "citta", "città", "ciò", "co", "codesta", "codesti", "codesto", "cogli", "coi", "col", "colei", "coll", "coloro", "colui", "come", "cominci", "comprare", "comunque", "con", "concernente", "conclusione", "consecutivi", "consecutivo", "consiglio", "contro", "cortesia", "cos", "cosa", "cosi", "così", "cui", "d", "da", "dagl", "dagli", "dai", "dal", "dall", "dalla", "dalle", "dallo", "dappertutto", "davanti", "degl", "degli", "dei", "del", "dell", "della", "delle", "dello", "dentro", "detto", "deve", "devo", "di", "dice", "dietro", "dire", "dirimpetto", "diventa", "diventare", "diventato", "dopo", "doppio", "dov", "dove", "dovra", "dovrà", "dovunque", "due", "dunque", "durante", "e", "ebbe", "ebbero", "ebbi", "ecc", "ecco", "ed", "effettivamente", "egli", "ella", "entrambi", "eppure", "era", "erano", "eravamo", "eravate", "eri", "ero", "esempio", "esse", "essendo", "esser", "essere", "essi", "ex", "fa", "faccia", "facciamo", "facciano", "facciate", "faccio", "facemmo", "facendo", "facesse", "facessero", "facessi", "facessimo", "faceste", "facesti", "faceva", "facevamo", "facevano", "facevate", "facevi", "facevo", "fai", "fanno", "farai", "faranno", "fare", "farebbe", "farebbero", "farei", "faremmo", "faremo", "fareste", "faresti", "farete", "farà", "farò", "fatto", "favore", "fece", "fecero", "feci", "fin", "finalmente", "finche", "fine", "fino", "forse", "forza", "fosse", "fossero", "fossi", "fossimo", "foste", "fosti", "fra", "frattempo", "fu", "fui", "fummo", "fuori", "furono", "futuro", "generale", "gente", "gia", "giacche", "giorni", "giorno", "giu", "già", "gli", "gliela", "gliele", "glieli", "glielo", "gliene", "grande", "grazie", "gruppo", "ha", "haha", "hai", "hanno", "ho", "i", "ie", "ieri", "il", "improvviso", "in", "inc", "indietro", "infatti", "inoltre", "insieme", "intanto", "intorno", "invece", "io", "l", "la", "lasciato", "lato", "le", "lei", "li", "lo", "lontano", "loro", "lui", "lungo", "luogo", "là", "ma", "macche", "magari", "maggior", "mai", "male", "malgrado", "malissimo", "me", "medesimo", "mediante", "meglio", "meno", "mentre", "mesi", "mezzo", "mi", "mia", "mie", "miei", "mila", "miliardi", "milioni", "minimi", "mio", "modo", "molta", "molti", "moltissimo", "molto", "momento", "mondo", "ne", "negl", "negli", "nei", "nel", "nell", "nella", "nelle", "nello", "nemmeno", "neppure", "nessun", "nessuna", "nessuno", "niente", "no", "noi", "nome", "non", "nondimeno", "nonostante", "nonsia", "nostra", "nostre", "nostri", "nostro", "novanta", "nove", "nulla", "nuovi", "nuovo", "o", "od", "oggi", "ogni", "ognuna", "ognuno", "oltre", "oppure", "ora", "ore", "osi", "ossia", "ottanta", "otto", "paese", "parecchi", "parecchie", "parecchio", "parte", "partendo", "peccato", "peggio", "per", "perche", "perchè", "perché", "percio", "perciò", "perfino", "pero", "persino", "persone", "però", "piedi", "pieno", "piglia", "piu", "piuttosto", "più", "po", "pochissimo", "poco", "poi", "poiche", "possa", "possedere", "posteriore", "posto", "potrebbe", "preferibilmente", "presa", "press", "prima", "primo", "principalmente", "probabilmente", "promesso", "proprio", "puo", "pure", "purtroppo", "può", "qua", "qualche", "qualcosa", "qualcuna", "qualcuno", "quale", "quali", "qualunque", "quando", "quanta", "quante", "quanti", "quanto", "quantunque", "quarto", "quasi", "quattro", "quel", "quella", "quelle", "quelli", "quello", "quest", "questa", "queste", "questi", "questo", "qui", "quindi", "quinto", "realmente", "recente", "recentemente", "registrazione", "relativo", "riecco", "rispetto", "salvo", "sara", "sarai", "saranno", "sarebbe", "sarebbero", "sarei", "saremmo", "saremo", "sareste", "saresti", "sarete", "sarà", "sarò", "scola", "scopo", "scorso", "se", "secondo", "seguente", "seguito", "sei", "sembra", "sembrare", "sembrato", "sembrava", "sembri", "sempre", "senza", "sette", "si", "sia", "siamo", "siano", "siate", "siete", "sig", "solito", "solo", "soltanto", "sono", "sopra", "soprattutto", "sotto", "spesso", "sta", "stai", "stando", "stanno", "starai", "staranno", "starebbe", "starebbero", "starei", "staremmo", "staremo", "stareste", "staresti", "starete", "starà", "starò", "stata", "state", "stati", "stato", "stava", "stavamo", "stavano", "stavate", "stavi", "stavo", "stemmo", "stessa", "stesse", "stessero", "stessi", "stessimo", "stesso", "steste", "stesti", "stette", "stettero", "stetti", "stia", "stiamo", "stiano", "stiate", "sto", "su", "sua", "subito", "successivamente", "successivo", "sue", "sugl", "sugli", "sui", "sul", "sull", "sulla", "sulle", "sullo", "suo", "suoi", "tale", "tali", "talvolta", "tanto", "te", "tempo", "terzo", "th", "ti", "titolo", "tra", "tranne", "tre", "trenta", "triplo", "troppo", "trovato", "tu", "tua", "tue", "tuo", "tuoi", "tutta", "tuttavia", "tutte", "tutti", "tutto", "uguali", "ulteriore", "ultimo", "un", "una", "uno", "uomo", "va", "vai", "vale", "vari", "varia", "varie", "vario", "verso", "vi", "vicino", "visto", "vita", "voi", "volta", "volte", "vostra", "vostre", "vostri", "vostro", "è"));

    /**
     * A list of commonly used English stop words.
     *
     * These words are typically filtered out during text processing as they do not
     * contribute significantly to the meaning of the text. The list includes
     * various pronouns, articles, prepositions, and auxiliary verbs.
     */
    private static List<String> stopWords_EN = new ArrayList<>(Arrays.asList("i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"));

    /**
     * Default constructor for the DocumentsManagement class.
     * Initializes an instance of the DocumentsManagement class.
     */
    public DocumentsManagement(){}

    /**
     * Filters and counts the occurrences of words from the input string.
     * The method removes punctuation, converts text to lowercase, excludes certain predefined stop words,
     * and returns a mapping of words to their respective frequencies.
     *
     * @param line The input string to be processed and filtered.
     * @return A map where the keys are words (after filtering) and the values are their respective frequencies as integers.
     */
    public static Map<String, Integer> textFiltering(String line) {

        Map<String, Integer> wordCount = new HashMap<>();
        String[] words = line.split("\\P{L}+");
        for(String word : words)
        {
            String cleaned = word.toLowerCase().replaceAll("[^a-zàèéìòùäöüßáêíóúñ]", "");

            if(!cleaned.isEmpty() && !stopWords_IT.contains(cleaned))
            {
                wordCount.put(cleaned, wordCount.getOrDefault(cleaned, 0) + 1);
            }
        }
        return wordCount;
    }

    /**
     * Inserts words and their frequencies into the database for a given document.
     * The method processes the provided text to extract and count words,
     * retrieves the corresponding document ID from the database, and inserts the word-frequency pairs.
     * If the document is not found in the database, the insertion process is aborted.
     *
     * @param fileName The name of the document whose words are to be inserted into the database.
     * @param line     The text line containing words to be processed and stored in the database.
     */
    public static void insertWordsIntoDatabase(String fileName, String line) {

        Map<String, Integer> words = textFiltering(line);

        String findIDQuery = "SELECT id FROM documents WHERE title = ?";

        String insertQuery = "INSERT INTO words (id_document, word, frequency) VALUES (?, ?, ?) ";

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
                int id=0;
                for (Map.Entry<String, Integer> entry : words.entrySet()) {
                    insertStmt.setInt(1, documentId);
                    insertStmt.setString(2, entry.getKey());
                    insertStmt.setInt(3, entry.getValue());
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }
            conn.commit();

            System.out.println("Parole inserite/aggiornate con successo.");
        } catch (SQLException e) {
            System.err.println("Errore database: " + e.getMessage());
        }
    }

    /**
     * Loads the content of a specified file into the database, associating it with a given difficulty level.
     * This method reads the file's content, processes it, and inserts it into the database with its metadata.
     *
     * @param fileName   The file to be loaded into the database. It must be a readable file.
     * @param difficulty The difficulty level to associate with the file's content. It must be one of the levels in Levels.Difficulty.
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
        System.out.println("File selezionato: " + fileName);

        String sql = "INSERT INTO documents (text, title, difficulty) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseManagement.getConnection();
             BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), StandardCharsets.UTF_8));
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line);
            }

            pstmt.setString(1, contentBuilder.toString());
            pstmt.setString(2, getBaseName(fileName.getName()));
            pstmt.setString(3, difficulty.name());

            pstmt.executeUpdate();

            System.out.println("Documento inserito/aggiornato con successo.");

            insertWordsIntoDatabase(getBaseName(fileName.getName()), contentBuilder.toString());

        } catch (FileNotFoundException e) {
            System.err.println("File non trovato: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Errore di I/O: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Errore SQL: " + e.getMessage());
        }
    }

    /**
     * Deletes a document and its associated words from the database based on the specified title.
     * This method performs the deletion in a transactional manner to ensure data consistency.
     * If the document is not found, it logs a corresponding error message.
     *
     * @param title The title of the document to be deleted from the database.
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
     * Extracts the base name of a file by removing the file extension if it exists.
     * If the provided filename does not contain an extension, the method returns the original filename.
     *
     * @param filename The full name of the file from which the base name should be extracted.
     *                 It must not be null and should represent a valid filename.
     * @return The base name of the file as a String, excluding its extension if one is present.
     */
    public static String getBaseName(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return filename; // Filename without extension
        } else {
            return filename.substring(0, index);
        }
    }

    /**
     * Retrieves the titles of all documents stored in the database.
     * The method executes an SQL query to fetch all titles from the "documents" table
     * and returns them as a list of strings.
     *
     * @return a list of document titles as strings, or an empty list if no titles are found
     *         or an error occurs during the database operation.
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
}