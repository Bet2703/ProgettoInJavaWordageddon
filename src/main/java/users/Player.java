/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package users;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Benedetta
 */
public class Player extends User{
    
    private List<Integer> personalScore;
    
    public Player(String username, String password) {
        super(username, password);
        personalScore = new ArrayList<>();
    }
    
    
    
}
