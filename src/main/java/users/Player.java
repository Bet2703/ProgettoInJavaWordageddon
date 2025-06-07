/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package users;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a player of the game, extending the base User class.
 * A Player has a role (Admin or Player), a username, password, and a list of personal scores.
 * 
 * @author Gruppo6
 */
public class Player extends User {
    
    private Role role;
    private List<Integer> personalScore;

    /**
     * Constructs a new Player with the specified username, password, and role.
     * 
     * @param username the username of the player
     * @param password the password of the player
     * @param role the role assigned to the player (Admin or Player)
     */
    public Player(String username, String password, Role role) {
        super(username, password);
        personalScore = new ArrayList<>();
        this.role = role;
    }

    /**
     * Returns the player's role.
     * 
     * @return the role (Admin or Player)
     */
    public Role getRole() {
        return role;
    }

    /**
     * Returns a list of player's integer scores.
     * 
     * @return a list of integer scores
     */
    public List<Integer> getPersonalScore() {
        return this.personalScore;
    }

    /**
     * Generates a hash code for the player based on the username.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(super.getUsername());
        return hash;
    }

    /**
     * Compares this player with another object for equality based on username.
     * 
     * @param obj the object to compare with
     * @return true if the usernames match, false otherwise
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
     * Returns a string representation of the player including username, password, and role.
     * 
     * @return a formatted string with player details
     */
    @Override
    public String toString() {
        return "Username: " + this.getUsername() + ", Password: " + this.getPassword() + ", Ruolo: " + this.getRole();
    }
}
