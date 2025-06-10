package users;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a Player within the system, extending the User class.
 * The Player class includes additional attributes and behaviors specific
 * to a player, such as their role and personal scores.
 *
 * @author Gruppo6
 */
public class Player extends User {
    
    private Role role;
    private List<Integer> personalScore;

    /**
     * Constructs a Player object with the specified username, password, and role.
     * Extends the User class and initializes the player's specific attributes, including
     * their role and an empty list to store their personal scores.
     *
     * @param username the username of the player
     * @param password the password of the player
     * @param role the role of the player, specified as a {@link Role} enum
     */
    public Player(String username, String password, Role role) {
        super(username, password);
        personalScore = new ArrayList<>();
        this.role = role;
    }

    /**
     * Retrieves the role assigned to this player.
     *
     * @return the role of the player as a {@link Role} enum
     */
    public Role getRole() {
        return role;
    }

    /**
     * Retrieves the personal score list for the player.
     *
     * @return a list of integers representing the player's personal scores
     */
    public List<Integer> getPersonalScore() {
        return this.personalScore;
    }

    /**
     * Computes the hash code for the Player instance based on the username.
     * This method overrides the hashCode method from the User class
     * to include the specific implementation for Player objects.
     *
     * @return the hash code value for this Player object
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(super.getUsername());
        return hash;
    }

    /**
     * Compares this Player instance to another object for equality.
     * The equality is defined based on the username of the Player.
     *
     * @param obj the object to compare with this Player instance
     * @return true if the specified object is equal to this Player, false otherwise
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
     * Returns a string representation of the Player object.
     * The string includes the username, password, and role of the player.
     *
     * @return a string containing the player's username, password, and role
     */
    @Override
    public String toString() {
        return "Username: " + this.getUsername() + ", Password: " + this.getPassword() + ", Ruolo: " + this.getRole();
    }
}
