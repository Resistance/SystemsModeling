// Import the swing and AWT classes needed
import java.awt.*;
import javax.swing.*;

public class GUI {
    public static void main(String[] args) {
 
        // Make sure all Swing/AWT instantiations and accesses are done on the
        // Event Dispatch Thread (EDT)
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create a JFrame, which is a Window with "decorations", i.e.
                // title, border and close-button
                JFrame window = new JFrame("Mancala");
                window.setLayout(new BorderLayout());


                // Set up Elements ====
                // Panels
                JPanel field = new JPanel();
                field.setLayout(new BorderLayout());
                
                JPanel holes = new JPanel();
                holes.setLayout(new GridLayout(2,6));
                
                JPanel northern = new JPanel();
                northern.setLayout(new FlowLayout());
                
                JPanel southern = new JPanel();
                southern.setLayout(new FlowLayout());
                
                // Labels
                JLabel playerA = new JLabel("Mängija A");
                JLabel playerB = new JLabel("Mängija B");
                JLabel homeA = new JLabel("0");
                JLabel homeB = new JLabel("0");
                
                JLabel status = new JLabel("I will show status information");
                
                // Buttons
                JButton a1 = new JButton("4");
                JButton a2 = new JButton("4");
                JButton a3 = new JButton("4");
                JButton a4 = new JButton("4");
                JButton a5 = new JButton("4");
                JButton a6 = new JButton("4");
                
                JButton b1 = new JButton("4");
                JButton b2 = new JButton("4");
                JButton b3 = new JButton("4");
                JButton b4 = new JButton("4");
                JButton b5 = new JButton("4");
                JButton b6 = new JButton("4");
                
                JButton newGame = new JButton("Uus mäng");
                JButton history = new JButton("Ajalugu");
                
                
 
                // Add Components
                holes.add(b6);
                holes.add(a1);
                holes.add(b5);
                holes.add(a2);
                holes.add(b4);
                holes.add(a3);
                holes.add(b3);
                holes.add(a4);
                holes.add(b2);
                holes.add(a5);
                holes.add(b1);
                holes.add(a6);
                
                northern.add(playerB);
                northern.add(history);
                southern.add(newGame);
                southern.add(playerA);
                
                field.add(northern, BorderLayout.NORTH);
                field.add(southern, BorderLayout.SOUTH);
                field.add(homeB, BorderLayout.EAST);
                field.add(homeA, BorderLayout.WEST);
                field.add(holes, BorderLayout.CENTER);
                
                window.add(field, BorderLayout.CENTER);
                window.add(status, BorderLayout.SOUTH);
 
                // final settings
                window.pack();
                window.setResizable(false);
                window.setLocationRelativeTo(null);
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
                // Set the visibility as true, thereby displaying it
                window.setVisible(true);
            }
        });
    }
}