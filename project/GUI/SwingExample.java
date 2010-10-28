// Import the swing and AWT classes needed
import java.awt.EventQueue;
import java.awt.FlowLayout;
 
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
 
/**
 * Basic Swing example.
 */
public class SwingExample {
    public static void main(String[] args) {
 
        // Make sure all Swing/AWT instantiations and accesses are done on the
        // Event Dispatch Thread (EDT)
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Create a JFrame, which is a Window with "decorations", i.e.
                // title, border and close-button
                JFrame f = new JFrame("Swing Example Window");
 
                // Set a simple Layout Manager that arranges the contained
                // Components
                f.setLayout(new FlowLayout());
 
                // Add some Components
                f.add(new JLabel("Hello, world!"));
                f.add(new JButton("Press me!"));
 
                // "Pack" the window, making it "just big enough".
                f.pack();
 
                // Set the default close operation for the window, or else the
                // program won't exit when clicking close button
                //  (The default is HIDE_ON_CLOSE, which just makes the window
                //  invisible, and thus doesn't exit the app)
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
 
                // Set the visibility as true, thereby displaying it
                f.setVisible(true);
            }
        });
    }
}