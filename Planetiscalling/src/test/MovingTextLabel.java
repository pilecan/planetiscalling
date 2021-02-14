package test;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;
public class MovingTextLabel extends JFrame implements ActionListener {
   private JLabel label;
   public MovingTextLabel() {
      setTitle("MovingTextLabel");
      label= new JLabel(" Planet is Calling             ");
      label.setFont(new Font("Arial", 0, 25));
      add(label, BorderLayout.CENTER);
      Timer t = new Timer(100, this); // set a timer
      t.start();
      setSize(400, 300);
      setVisible(true);
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   }
   public void actionPerformed(ActionEvent e) {
      String oldText = label.getText();
      String newText= oldText.substring(1)+ oldText.substring(0,1);
      label.setText(newText);
   }
   public static void main (String[] args) {
      new MovingTextLabel();
   }

}