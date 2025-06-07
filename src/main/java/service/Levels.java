package service;

import java.util.List;

/**
 * Provides methods to manage different levels of difficulty available in a game.
 * Each level can define specific attributes such as the number of questions, time limits,
 * scoring rules, and other customized properties.
 *
 * @author Gruppo6
 */
public class Levels {

    /**
     * Enum of difficulties available in the application.
     * This enum is used to categorize different levels of game challenge
     * and is typically associated with properties such as number of questions,
     * time limits, and scoring mechanisms.
     *
     * The available difficulty levels are:
     * - EASY: Represents a basic or beginner level.
     * - MEDIUM: Represents an intermediate challenge level.
     * - HARD: Represents an advanced or expert level.
     */
    public enum Difficulty {
        EASY, MEDIUM, HARD
    }

    /**
     * Returns the number of questions for a specified difficulty level.
     *
     * @param difficulty the difficulty level for which the number of questions is determined
     * @return the number of questions associated with the specified difficulty level
     */
    public static int getNumberOfQuestions(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 5;
            case MEDIUM:
                return 10;
            case HARD:
                return 15;
            default:
                return 0;
        }
    }

    /**
     * Determines the time limit (in seconds) per question for a specified difficulty level.
     *
     * @param difficulty the difficulty level for which the time limit is determined
     * @return the time limit in seconds associated with the specified difficulty level,
     *         or 0 if the difficulty is not recognized
     */
    public static int getTimeLimitPerQuestion(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 40;  // 30 secondi per domanda
            case MEDIUM:
                return 30;
            case HARD:
                return 15;
            default:
                return 0;
        }
    }

    /**
     * Determines the maximum score allocated for a question based on the specified difficulty level.
     *
     * @param difficulty the difficulty level of the question, represented by the {@code Difficulty} enum.
     *                   Possible values include {@code EASY}, {@code MEDIUM}, and {@code HARD}.
     * @return the maximum score for a question corresponding to the provided difficulty level.
     *         Returns {@code 0} if the difficulty level is not recognized or is null.
     */
    public static int getMaxScorePerQuestion(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 10;
            case MEDIUM:
                return 20;
            case HARD:
                return 30;
            default:
                return 0;
        }
    }

    /**
     * Retrieves a list of question types associated with a specific difficulty level.
     *
     * @param difficulty the difficulty level for which the question types are requested.
     *                    This must be a value from the {@code Difficulty} enum, such as {@code EASY}, {@code MEDIUM}, or {@code HARD}.
     * @return a {@code List} of {@code String} representing the question types for the provided difficulty level.
     *         Returns an empty list if there are no question types available for the specified difficulty level or if the difficulty level is invalid.
     */
    public static List<String> getQuestionTypesByDifficulty(Difficulty difficulty) {
        return null;
    }

    /**
     * Validates if the provided difficulty string corresponds to a valid
     * {@code Difficulty} enum value.
     *
     * @param difficulty the difficulty level as a string. This should represent
     *                   one of the valid enum values in {@code Difficulty}, such as
     *                   "EASY", "MEDIUM", or "HARD". The comparison is case-insensitive.
     * @return {@code true} if the input string matches a valid {@code Difficulty} enum value,
     *         {@code false} otherwise, including cases where the input is null.
     */
    public static boolean isValidDifficulty(String difficulty) {
        try {
            Difficulty.valueOf(difficulty.toUpperCase());
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}
