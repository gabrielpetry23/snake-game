

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class App extends JFrame {
    public static void main (String[] args) {
        new App();
        
    }

    App () {
        SnakeGame game = new SnakeGame();
        add(game);
        setTitle("SnakeGame");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true);
        pack();
        setVisible(true);
        setLocationRelativeTo(null);
    }
}
