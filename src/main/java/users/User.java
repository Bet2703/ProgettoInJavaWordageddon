package users;

import java.util.Objects;

/**
 * Represents an abstract user within the system.
 * This class provides a common foundation for all types of users,
 * defining core attributes like username and password as well as
 * shared functionalities such as password validation, equality checks,
 * and hash code generation.
 *
 * The class is intended to be extended by specific user types,
 * which can add their own additional behaviors and attributes.
 *
 * @author Gruppo6
 */
public abstract class User {
    private final String username;
    private final String password;

    /**
     * Constructs a User with the specified username and password.
     * This constructor initializes the core attributes of the User.
     *
     * @param username the username of the user
     * @param password the password of the user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Retrieves the username of the user.
     *
     * @return the username associated with this user
     */
    public String getUsername() {
        return username;
    }

    /**
     * Retrieves the password associated with the user.
     *
     * @return the password of the user as a string
     */
    public String getPassword() {
        return password;
    }

    /**
     * Validates the provided password against the user's stored password.
     *
     * @param input the password input to be checked
     * @return true if the provided password matches the stored password, false otherwise
     */
    public boolean checkPassword(String input) {
        return password.equals(input);
    }

    /**
     * Generates a hash code for the User instance based on the username.
     * This method ensures that the hash code is consistent with the
     * equals method, where Users with the same username will have the same
     * hash code.
     *
     * @return the hash code value for this User object
     */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + Objects.hashCode(this.username);
        return hash;
    }

    /**
     * Compares this User object to the specified object for equality.
     * The equality is defined based on the username of the User.
     *
     * @param obj the reference object with which to compare
     * @return true if the specified object is equal to this User, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (!Objects.equals(this.username, other.username)) {
            return false;
        }
        return true;
    }    
}
