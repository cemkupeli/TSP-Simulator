/**
 * Analyzes the performance of the different algorithms
 */
public class TSPerformance
{
    public static void main(String[] args)
    {
        final int NUM_ITERATIONS = 100;
        
        // Analysis of the exhaustive search method
        System.out.println("------ EXHAUSTIVE SEARCH ------");
        for (int n = 4; n <= 10; n ++)
        {
            TSComponent cityComp = new TSComponent(n, 900, 700);
            
            double totalRuntime = 0;
            for (int i = 0; i < NUM_ITERATIONS; i++)
            {
                totalRuntime += cityComp.exhaustiveSearch();
                
                // Resets the graph with the same number of cities
                cityComp = new TSComponent(n, 900, 700);
            }
            double averageRuntime = totalRuntime / NUM_ITERATIONS;
            
            System.out.println("Average runtime for exhaustive search with " + n + " cities (ns): " + scientific(averageRuntime));
        }
        
        System.out.println("\n------ NEAREST-NEIGHBOR ------");
        // Analysis of the nearest-neighbor method
        for (int n = 4; n <= 10; n++)
        {
            TSComponent cityComp = new TSComponent(n, 900, 700);
            
            double totalRuntime = 0;
            double totalPercentIncrease = 0;
            for (int i = 0; i < NUM_ITERATIONS; i++)
            {
                // Determines the optimal length for the current graph using the exhaustive search method
                cityComp.exhaustiveSearch();
                final double OPTIMAL_LENGTH = cityComp.getOptimalPathLength();
                
                totalRuntime += cityComp.nearestNeighbor();
                totalPercentIncrease += 100 * (cityComp.getOptimalPathLength() - OPTIMAL_LENGTH) / OPTIMAL_LENGTH;
                
                // Resets the graph with the same number of cities
                cityComp = new TSComponent(n, 900, 700);
            }
            double averageRuntime = totalRuntime / NUM_ITERATIONS;
            double averagePercentIncrease = totalPercentIncrease / NUM_ITERATIONS;
            
            System.out.println("Average runtime for nearest-neighbor with " + n + " cities (ns): " + scientific(averageRuntime));
            System.out.println("Average percent increase for nearest-neighbor with " + n + " cities: " + String.format("%.2f", averagePercentIncrease));
        }
    }
    
    private static String scientific(double n)
    {
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
