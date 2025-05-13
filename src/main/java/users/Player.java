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
 *
 * @author Benedetta
 */
public class Player extends User{
    private String username;
    private String password;
    private Role role;
    private List<Integer> personalScore;
    
    public Player(String username, String password, Role role) {
        super(username, password);
        personalScore = new ArrayList<>();
        this.role=role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
    
    public List<Integer> getPersonalScore(){
        return this.personalScore;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + Objects.hashCode(this.username);
        return hash;
    }

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
        final Player other = (Player) obj;
        return Objects.equals(this.username, other.username);
    }
    
    
    @Override
    public String toString(){
        return "Username: "+ this.getUsername()+ ", Password: "+ this.getPassword()+ ", Ruolo: "+this.getRole();
    }
    
}
