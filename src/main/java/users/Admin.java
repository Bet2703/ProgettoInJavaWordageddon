package users;

/**
 * Represents an Admin user, extending the base User class.
 * Inherits attributes and operations from the User class and adds
 * the ability to specify and manage the admin status of the user.
 *
 * @author Gruppo6
 */
public class Admin extends User{

    /**
     * Indicates whether the user has Admin privileges.
     * This field represents the admin status of the user,
     * allowing for differentiation between administrative
     * and regular users.
     */
    private boolean isAdmin;

    /**
     * Constructs a new Admin user with the specified username, password, and admin status.
     * This constructor initializes the admin user by calling the parent class constructor
     * with the provided username and password and sets the admin status.
     *
     * @param username the username of the*/
    public Admin(String username, String password, boolean isAdmin) {
        super(username, password);
        this.isAdmin = isAdmin;
    }

    /**
     * Checks whether the user has administrative privileges.
     *
     * @return true if the user is an admin, false otherwise
     */
    public boolean isIsAdmin() {
        return isAdmin;
    }

    /**
     * Sets the administrative status of the user.
     *
     * @param isAdmin the new administrative status to be set for the user.
     */
    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }
}
