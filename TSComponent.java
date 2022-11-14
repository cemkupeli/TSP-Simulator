/**
 * A component that draws the points and implements the algorithms to solve the Travelling Salesperson problem
 * @author Cem Küpeli
 * @version 1.0
 */

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.JComponent;
import java.util.ArrayList;
import java.util.Random;
import java.util.Collections;

public class TSComponent extends JComponent
{
    private int numCities;
    private double[][] distances;
    private ArrayList<Point2D.Double> cities;
    private int[] currentPath;
    private int[] optimalPath;
    private int optimalPathLength;
    private int currentPermutation;
    
    /**
     * The parameterized constructor that generates the inputted amount of
     * random Point2D.Double objects representing city coordinates
     * @param numCities the number of points that will be generated
     * @param frameWidth the width of the frame that the component will be added to
     * @param frameHeight the height of the frame that the component will be added to
     */
    public TSComponent(int initNumCities, int frameWidth, int frameHeight)
    {
        numCities = initNumCities;
        cities = new ArrayList<Point2D.Double>();
        
        currentPath = new int[numCities];
        optimalPath = new int[numCities];
        for (int i = 0; i < numCities; i++)
        {
            currentPath[i] = i;
            optimalPath[i] = i;
        }
        
        currentPermutation = 1;
        
        int randX = 0;
        int randY = 0;
        
        Random generator = new Random();
        
        for (int i = 0; i < initNumCities; i++)
        {
            randX = generator.nextInt(frameWidth - 120) + 120;
            randY = generator.nextInt(frameHeight - 120) + 120;
            
            cities.add(new Point2D.Double(randX, randY));
        }
        
        // Initializes the two-dimensional array representing the distances between the cities
        distances = new double[numCities][numCities];
        
        // Fills the distances array
        for (int i = 0; i < numCities; i++)
        {
            for (int j = 0; j < numCities; j++)
            {
                distances[i][j] = cities.get(i).distance(cities.get(j));
                // System.out.println(distances[i][j]);
            }
        }
    }
    
    // Runs the nearest-neighbor method and returns the runtime in milliseconds
    public long nearestNeighbor()
    {   
        resetPaths();
        
        long startTime = System.nanoTime();
        
        // A reference to the index current city
        int currentCityIndex;
        
        // A boolean array paralel to cities, representing whether the city at that index has been visited
        boolean[] visited = new boolean[numCities];
        
        // Picks a random index to determine the first city in the path
        int first = (int) (Math.random() * numCities);
        currentCityIndex = first;
        visited[currentCityIndex] = true;
        currentPath[0] = currentCityIndex;
        
        // Determines the rest of the path
        for (int n = 1; n < numCities; n++)
        {
            currentCityIndex = findNearest(currentCityIndex, visited);
            
            visited[currentCityIndex] = true;
            currentPath[n] = currentCityIndex;
        }
        
        // Copies the current path to the optimal path
        for (int i = 0; i < numCities; i++)
        {
            optimalPath[i] = currentPath[i];
        }
        
        // Determines the length of the optimal path
        int length = 0;
        for (int i = 0; i < numCities - 1; i++)
        {
            int current = optimalPath[i];
            int next = optimalPath[i + 1];
            length += distances[current][next];
        }
        optimalPathLength = length;
        
        long endTime = System.nanoTime();
        
        return endTime - startTime;
    }
    
    // Helper - Finds the closest unvisited city for the nearestNeighbor method
    private int findNearest(int currentCityIndex, boolean[] visited)
    {
        double minDistance = Double.MAX_VALUE;
        int nearest = -1;
        
        for (int i = 0; i < numCities; i++)
        {
            if (!visited[i])
            {
                double distance = distances[currentCityIndex][i];
                if (distance < minDistance)
                {
                    minDistance = distance;
                    nearest = i;
                }
            }
        }

        return nearest;
    }
    
    // Runs the exhaustive search method and returns the runtime in milliseconds
    public long exhaustiveSearch()
    {
        optimalPathLength = Integer.MAX_VALUE;
        currentPermutation = 1;
        resetPaths();
        
        long startTime = System.nanoTime();
        exhaustiveSearchHelper(currentPath, numCities, numCities);
        long endTime = System.nanoTime();
        
        return endTime - startTime;
    }
    
    // Returns the optimal path length
    public double getOptimalPathLength()
    {
        return optimalPathLength;
    }
    
    // Helper - Heap's algorithm source: https://www.geeksforgeeks.org/heaps-algorithm-for-generating-permutations/
    private void exhaustiveSearchHelper(int[] a, int size, int n)
    {
        // If the size is one, checks the current permutation
        if (size == 1)
        {
            // Calculates the distance of the current path
            int currentPathLength = 0;
            for (int i = 0; i < numCities - 1; i++)
            {
                currentPathLength += distances[currentPath[i]][currentPath[i + 1]];
            }
            
            // Prints the current permutation, increments it, and prints the current path
            // System.out.println("Number of permutations: " + currentPermutation);
            currentPermutation++;
            // printCurrentPath();
            
            // If the current path length is shorter than the optimal, updates the optimal path and its length
            if (currentPathLength < optimalPathLength)
            {
                for (int i = 0; i < numCities; i++)
                {
                    optimalPath[i] = currentPath[i];
                }
                optimalPathLength = currentPathLength;
            }
        }
            
 
        for (int i = 0; i < size; i++) {
            exhaustiveSearchHelper(a, size - 1, n);
 
            // If size is odd, swaps the first element and the last element
            if (size % 2 == 1) {
                int temp = a[0];
                a[0] = a[size - 1];
                a[size - 1] = temp;
            }
            // If size is even, swaps the current element and the last element
            else {
                int temp = a[i];
                a[i] = a[size - 1];
                a[size - 1] = temp;
            }
        }
    }
    
    /** 
     * Prints the coordinates of the generated cities
     */
    public void displayCityCoordinates()
    {
        System.out.println("City coordinates:");
        for (int i = 0; i < numCities; i++)
        {
            System.out.println(i + ": " + cities.get(i));
        }
    }
    
    /** 
     * Prints the current path
     */
    public void printCurrentPath()
    {
        System.out.print("Current path: ");
        for (int i = 0; i < numCities; i++)
        {
            System.out.print(currentPath[i] + " ");
        }
        System.out.println();
    }
    
    /** 
     * Returns the optimal path as a String
     */
    public String optimalPathString()
    {
        String optimalPathStr = "";
        
        for (int i = 0; i < numCities; i++)
        {
            optimalPathStr += optimalPath[i] + " ";
        }
        
        return optimalPathStr;
    }
    
    // Resets the current and optimal paths
    public void resetPaths()
    {
        for (int i = 0; i < numCities; i++)
        {
            currentPath[i] = i;
            optimalPath[i] = i;
        }
    }
    
    /** 
     * Paints the Travelling Salesmen component that displays the cities
     */
    public void paintComponent(Graphics g)
    {
        // Recovers Graphics2D
        Graphics2D g2 = (Graphics2D) g;
        
        // Draws circles to represent each point
        int currentCity = 0;
        for (Point2D.Double city : cities)
        {
            Ellipse2D.Double point = new Ellipse2D.Double(city.getX() - 3, city.getY() - 3, 7, 7);
            g2.fill(point);
            
            // Draws the number of the current city
            g2.drawString(Integer.toString(currentCity), (float) city.getX() + 7, (float) city.getY() - 7);
            
            currentCity++;
        }
        
        // Creates an edge between every vertex
        /* for (int i = 0; i < cities.size(); i++)
        {
            for (int j = i; j < cities.size(); j++)
            {
                Line2D.Double line = new Line2D.Double(cities.get(i), cities.get(j));
                g2.draw(line);
            }
        } */
    }
}
