/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Gruppo6
 */
public class DocumentsManagement {

    private static List<String> stopWords_IT = new ArrayList<>(Arrays.asList("a", "abbastanza", "abbia", "abbiamo", "abbiano", "abbiate", "accidenti", "ad", "adesso", "affinché", "agl", "agli", "ahime", "ahimè", "ai", "al", "alcuna", "alcuni", "alcuno", "all", "alla", "alle", "allo", "allora", "altre", "altri", "altrimenti", "altro", "altrove", "altrui", "anche", "ancora", "anni", "anno", "ansa", "anticipo", "assai", "attesa", "attraverso", "avanti", "avemmo", "avendo", "avente", "aver", "avere", "averlo", "avesse", "avessero", "avessi", "avessimo", "aveste", "avesti", "avete", "aveva", "avevamo", "avevano", "avevate", "avevi", "avevo", "avrai", "avranno", "avrebbe", "avrebbero", "avrei", "avremmo", "avremo", "avreste", "avresti", "avrete", "avrà", "avrò", "avuta", "avute", "avuti", "avuto", "basta", "ben", "bene", "benissimo", "brava", "bravo", "buono", "c", "caso", "cento", "certa", "certe", "certi", "certo", "che", "chi", "chicchessia", "chiunque", "ci", "ciascuna", "ciascuno", "cima", "cinque", "cio", "cioe", "cioè", "circa", "citta", "città", "ciò", "co", "codesta", "codesti", "codesto", "cogli", "coi", "col", "colei", "coll", "coloro", "colui", "come", "cominci", "comprare", "comunque", "con", "concernente", "conclusione", "consecutivi", "consecutivo", "consiglio", "contro", "cortesia", "cos", "cosa", "cosi", "così", "cui", "d", "da", "dagl", "dagli", "dai", "dal", "dall", "dalla", "dalle", "dallo", "dappertutto", "davanti", "degl", "degli", "dei", "del", "dell", "della", "delle", "dello", "dentro", "detto", "deve", "devo", "di", "dice", "dietro", "dire", "dirimpetto", "diventa", "diventare", "diventato", "dopo", "doppio", "dov", "dove", "dovra", "dovrà", "dovunque", "due", "dunque", "durante", "e", "ebbe", "ebbero", "ebbi", "ecc", "ecco", "ed", "effettivamente", "egli", "ella", "entrambi", "eppure", "era", "erano", "eravamo", "eravate", "eri", "ero", "esempio", "esse", "essendo", "esser", "essere", "essi", "ex", "fa", "faccia", "facciamo", "facciano", "facciate", "faccio", "facemmo", "facendo", "facesse", "facessero", "facessi", "facessimo", "faceste", "facesti", "faceva", "facevamo", "facevano", "facevate", "facevi", "facevo", "fai", "fanno", "farai", "faranno", "fare", "farebbe", "farebbero", "farei", "faremmo", "faremo", "fareste", "faresti", "farete", "farà", "farò", "fatto", "favore", "fece", "fecero", "feci", "fin", "finalmente", "finche", "fine", "fino", "forse", "forza", "fosse", "fossero", "fossi", "fossimo", "foste", "fosti", "fra", "frattempo", "fu", "fui", "fummo", "fuori", "furono", "futuro", "generale", "gente", "gia", "giacche", "giorni", "giorno", "giu", "già", "gli", "gliela", "gliele", "glieli", "glielo", "gliene", "grande", "grazie", "gruppo", "ha", "haha", "hai", "hanno", "ho", "i", "ie", "ieri", "il", "improvviso", "in", "inc", "indietro", "infatti", "inoltre", "insieme", "intanto", "intorno", "invece", "io", "l", "la", "lasciato", "lato", "le", "lei", "li", "lo", "lontano", "loro", "lui", "lungo", "luogo", "là", "ma", "macche", "magari", "maggior", "mai", "male", "malgrado", "malissimo", "me", "medesimo", "mediante", "meglio", "meno", "mentre", "mesi", "mezzo", "mi", "mia", "mie", "miei", "mila", "miliardi", "milioni", "minimi", "mio", "modo", "molta", "molti", "moltissimo", "molto", "momento", "mondo", "ne", "negl", "negli", "nei", "nel", "nell", "nella", "nelle", "nello", "nemmeno", "neppure", "nessun", "nessuna", "nessuno", "niente", "no", "noi", "nome", "non", "nondimeno", "nonostante", "nonsia", "nostra", "nostre", "nostri", "nostro", "novanta", "nove", "nulla", "nuovi", "nuovo", "o", "od", "oggi", "ogni", "ognuna", "ognuno", "oltre", "oppure", "ora", "ore", "osi", "ossia", "ottanta", "otto", "paese", "parecchi", "parecchie", "parecchio", "parte", "partendo", "peccato", "peggio", "per", "perche", "perchè", "perché", "percio", "perciò", "perfino", "pero", "persino", "persone", "però", "piedi", "pieno", "piglia", "piu", "piuttosto", "più", "po", "pochissimo", "poco", "poi", "poiche", "possa", "possedere", "posteriore", "posto", "potrebbe", "preferibilmente", "presa", "press", "prima", "primo", "principalmente", "probabilmente", "promesso", "proprio", "puo", "pure", "purtroppo", "può", "qua", "qualche", "qualcosa", "qualcuna", "qualcuno", "quale", "quali", "qualunque", "quando", "quanta", "quante", "quanti", "quanto", "quantunque", "quarto", "quasi", "quattro", "quel", "quella", "quelle", "quelli", "quello", "quest", "questa", "queste", "questi", "questo", "qui", "quindi", "quinto", "realmente", "recente", "recentemente", "registrazione", "relativo", "riecco", "rispetto", "salvo", "sara", "sarai", "saranno", "sarebbe", "sarebbero", "sarei", "saremmo", "saremo", "sareste", "saresti", "sarete", "sarà", "sarò", "scola", "scopo", "scorso", "se", "secondo", "seguente", "seguito", "sei", "sembra", "sembrare", "sembrato", "sembrava", "sembri", "sempre", "senza", "sette", "si", "sia", "siamo", "siano", "siate", "siete", "sig", "solito", "solo", "soltanto", "sono", "sopra", "soprattutto", "sotto", "spesso", "sta", "stai", "stando", "stanno", "starai", "staranno", "starebbe", "starebbero", "starei", "staremmo", "staremo", "stareste", "staresti", "starete", "starà", "starò", "stata", "state", "stati", "stato", "stava", "stavamo", "stavano", "stavate", "stavi", "stavo", "stemmo", "stessa", "stesse", "stessero", "stessi", "stessimo", "stesso", "steste", "stesti", "stette", "stettero", "stetti", "stia", "stiamo", "stiano", "stiate", "sto", "su", "sua", "subito", "successivamente", "successivo", "sue", "sugl", "sugli", "sui", "sul", "sull", "sulla", "sulle", "sullo", "suo", "suoi", "tale", "tali", "talvolta", "tanto", "te", "tempo", "terzo", "th", "ti", "titolo", "tra", "tranne", "tre", "trenta", "triplo", "troppo", "trovato", "tu", "tua", "tue", "tuo", "tuoi", "tutta", "tuttavia", "tutte", "tutti", "tutto", "uguali", "ulteriore", "ultimo", "un", "una", "uno", "uomo", "va", "vai", "vale", "vari", "varia", "varie", "vario", "verso", "vi", "vicino", "visto", "vita", "voi", "volta", "volte", "vostra", "vostre", "vostri", "vostro", "è"));
    private static List<String> stopWords_EN = new ArrayList<>(Arrays.asList("i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"));

    //Costruttore
    public DocumentsManagement(){}

    //Filtra parole rimuovendo le StopWords
    public static Map<String, Integer> textFiltering(String line) {

        Map<String, Integer> wordCount = new HashMap<>();
        String[] words = line.split("\\s+");
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

    public static void insertWordsIntoDatabase(String fileName, String line) {

        Map<String, Integer> words = textFiltering(line);

        String URL="jdbc:sqlite:./wordageddon.db";

        String findIDQuery = "SELECT id FROM documents WHERE title = ?";

        String insertQuery = "INSERT INTO words (id_document, word, frequency) VALUES (?, ?, ?) ";

        try (Connection con = DriverManager.getConnection(URL)) {

            // Recuperiamo id tramite il titolo del testo
            int documentId = -1;
            try (PreparedStatement selectStmt = con.prepareStatement(findIDQuery)) {
                selectStmt.setString(1, fileName);
                ResultSet rs = selectStmt.executeQuery();
                if (rs.next()) {
                    documentId = rs.getInt("id");
                } else {
                    System.err.println("Documento non trovato: " + fileName);
                    return;
                }
            }

            // Inseriamo le parole nel database
            con.setAutoCommit(false); // Avvia la transazione
            try (PreparedStatement insertStmt = con.prepareStatement(insertQuery)) {
                for (Map.Entry<String, Integer> entry : words.entrySet()) {
                    insertStmt.setInt(1, documentId);
                    insertStmt.setString(2, entry.getKey());
                    insertStmt.setInt(3, entry.getValue());
                    insertStmt.addBatch();
                }
                insertStmt.executeBatch();
            }
            con.commit(); // Termina la transazione

            System.out.println("Parole inserite/aggiornate con successo.");
        } catch (SQLException e) {
            System.err.println("Errore database: " + e.getMessage());
        }
    }

    public static void loadToDB(File fileName, Levels.Difficulty difficulty) {

        String URL="jdbc:sqlite:./wordageddon.db";

        //Controllo sull'esistenza del file
        if (fileName == null) {
            System.err.println("Il file non è specificato (null).");
            return;
        }
        if (!fileName.exists() || !fileName.canRead()) {
            System.err.println("Il file non esiste o non è leggibile: " + fileName.getAbsolutePath());
            return;
        }
        System.out.println("File selezionato: " + fileName);

        //Query per inserimento nel database
        String sql = "INSERT INTO documents (text, title, difficulty) VALUES (?, ?, ?)";

        try(Connection con = DriverManager.getConnection(URL);
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            PreparedStatement pstmt = con.prepareStatement(sql)) {

            //Lettura del File
            StringBuilder contentBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {

                contentBuilder.append(line);

            }

            //Inviamo la Query al DB
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

    //Metodo per ricavare il titolo dal nome del file
    public static String getBaseName(String filename) {
        int index = filename.lastIndexOf('.');
        if (index == -1) {
            return filename; // Filename without extension
        } else {
            return filename.substring(0, index);
        }
    }

}