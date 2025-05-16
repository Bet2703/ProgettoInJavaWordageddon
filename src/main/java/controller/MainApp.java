/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package controller;

import java.util.Scanner;
import service.Login;
import users.Player;

/**
 *
 * @author Gruppo6
 */
public class MainApp {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Inserisci username: ");
        String username = scanner.nextLine();

        System.out.print("Inserisci password: ");
        String password = scanner.nextLine();

        Player p = Login.login(username, password); // ora usa Login

        if (p != null) {
            System.out.println("✅ Login riuscito!");
            System.out.println(p); // usa il toString di Player
        } else {
            System.out.println("❌ Login fallito.");
        }

        scanner.close();
    }
    
}
