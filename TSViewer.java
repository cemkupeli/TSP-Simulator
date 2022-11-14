/**
 * Creates a frame that displays a component with a map of cities on it
 * @author Cem Küpeli
 * @version 1.0
 */

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import java.awt.event.*;

public class TSViewer
{
    static TSComponent cityComp;
    
    public static void main(String[] args)
    {
        final int FRAME_WIDTH = 1000;
        final int FRAME_HEIGHT = 800;
        final int NUM_CITIES = 6;
        
        // Sets up the frame
        JFrame frame = new JFrame("Travelling Salesperson");
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel p = new JPanel();
        p.setLayout(null);
        p.setBounds (0, 0, FRAME_WIDTH, 100);
        frame.add(p);
        
        // Creates a button to run the exhaustive search and adds it to the JPanel within the frame
        JButton runExhaustive = new JButton("Run exhaustive search");
        runExhaustive.setBounds(250, 20, 170, 60);
        p.add(runExhaustive);
        
        // Creates a button to run the nearest-neighbor algorithm and adds it to the JPanel within the frame
        JButton runNearestNeighbor = new JButton("Run nearest-neighbor");
        runNearestNeighbor.setBounds(450, 20, 170, 60);
        p.add(runNearestNeighbor);
           
        // Creates a slider to allow the user to adjust the number of cities
        JSlider citiesSlider = new JSlider(3, 15, NUM_CITIES);
        citiesSlider.createStandardLabels(1);
        citiesSlider.setMinorTickSpacing(1);
        citiesSlider.setSnapToTicks(true);
        citiesSlider.setPaintTicks(true);
        citiesSlider.setPaintLabels(true);
        citiesSlider.setBounds(20, 30, 200, 50);
        p.add(citiesSlider);
        
        // Creates a button to confirm changing the number of cities
        JButton changeCities = new JButton("Set");
        changeCities.setBounds(95, 70, 50, 30);
        p.add(changeCities);
        
        JLabel citiesLabel = new JLabel("Cities: " + NUM_CITIES);
        citiesLabel.setBounds(90, 0, 100, 50);
        p.add(citiesLabel);
        
        JLabel algorithmLabel = new JLabel();
        algorithmLabel.setBounds(FRAME_WIDTH - 300, 10, 250, 15);
        p.add(algorithmLabel);
        
        JLabel lengthLabel = new JLabel();
        lengthLabel.setBounds(FRAME_WIDTH - 300, 30, 250, 15);
        p.add(lengthLabel);
        
        JLabel pathLabel = new JLabel();
        pathLabel.setBounds(FRAME_WIDTH - 300, 50, 250, 15);
        p.add(pathLabel);
        
        JLabel runtimeLabel = new JLabel();
        runtimeLabel.setBounds(FRAME_WIDTH - 300, 70, 250, 15);
        p.add(runtimeLabel);
        
        // Creates the city component and adds it to the frame
        cityComp = new TSComponent(NUM_CITIES, FRAME_WIDTH - 100, FRAME_HEIGHT - 100);
        frame.add(cityComp);

        // Adds an action listener to the runExhaustive button that will run the exhaustive search method and print the results when clicked
        runExhaustive.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Determines the optimal path using exhaustive search and updates the GUI
                algorithmLabel.setText("Using exhaustive search:");
                runtimeLabel.setText("Runtime (ns): " + scientific(cityComp.exhaustiveSearch()));
                lengthLabel.setText("Length of path: " + Double.toString(cityComp.getOptimalPathLength())); 
                pathLabel.setText("Optimal path: " + cityComp.optimalPathString());
            }
        });
        
        // Adds an action listener to the runNearestNeighbor button that will run the nearest-neighbor algorithm and print the results when clicked
        runNearestNeighbor.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                // Determines the optimal path using the nearest-neighbor method and updates the GUI
                algorithmLabel.setText("Using nearest-neighbor:");
                runtimeLabel.setText("Runtime (ns): " + scientific(cityComp.nearestNeighbor()));
                lengthLabel.setText("Length of path: " + Double.toString(cityComp.getOptimalPathLength())); 
                pathLabel.setText("Optimal path: " + cityComp.optimalPathString());
            }
        });
        
        // Adds an action listener to the changeCities button that will create a new cityComp with the number of cities indicated on the slider
        changeCities.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                System.out.println("Creating new city component");
                
                // Changes the citiesLabel to reflect the new number of cities
                citiesLabel.setText("Cities: " + citiesSlider.getValue());
                
                // Removes the current city component
                frame.remove(cityComp);
                
                // Resets the labels
                algorithmLabel.setText("");
                runtimeLabel.setText("");
                lengthLabel.setText(""); 
                pathLabel.setText("");
                
                // Creates a new city component and adds it to the frame
                cityComp = new TSComponent(citiesSlider.getValue(), FRAME_WIDTH - 100, FRAME_HEIGHT - 100);
                frame.add(cityComp);
                
                // Refreshes the frame after adding the new city component
                frame.invalidate();
                frame.validate();
                frame.repaint();
            }
        });
        
        frame.setVisible(true);
    }
    
    private static String scientific(long num)
    {
        double n = (double) num;
        int power = 0;
        
        if (n < 1)
        {
            while (n < 1)
            {
                n *= 10;
                power--;
            }
        }
        else
        {
            while (n >= 10)
            {
                n /= 10;
                power++;
            }
        }
        
        return String.format("%.2f", n) + "E" + power;
    }
}
