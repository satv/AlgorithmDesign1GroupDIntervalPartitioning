
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author Rene Anda Nielsen <rann@itu.dk>
 * @author Aaron Gornott <agor@itu.dk>
 * @author Sarah
 */
public class IP {

    private static Scanner sc;
    private static int n; // number of intervals
    private static int k; // number of partitions
    private static Interval[] intervals;
    private static Interval interval;
    
    //private final static ArrayList<Integer> sortedIntervalIDs = new ArrayList();
        
    private static int d; // no of partitions
    private final static ArrayList<ArrayList<Integer>> partitionIntervals = new ArrayList();

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("ip-rand-1k.in");
        sc = new Scanner(file);
        int id = 0;
        int lineNr = 0;
        // read file
        while (sc.hasNextLine()) {

            String line = sc.nextLine().trim();
            if (line.length() == 0) {
                continue;
            }

            if (lineNr == 0) {
                n = Integer.parseInt(line);
                System.out.println("N is: " + n);
                intervals = new Interval[n];
                lineNr++;
            } else {
                String[] times = line.split(" ");
                int start = Integer.parseInt(times[0]);
                int end = Integer.parseInt(times[1]);
                interval = new Interval(id, start,end, "sort by start time");
                intervals[id] = interval;
                id++;
            }
        }
        
        // SORT lectures by start time so that s1 ≤ s2 ≤ ... ≤ sn.
        Arrays.sort(intervals);    
        // d <- 0
        d = 0;
        // FOR j = 1 TO n
        for(int j = 0; j < n; j++){
            // IF lecture j is compatible with some classroom
            //// int sortedJ = sortedIntervalIDs.get(j);
            int compatibleID = isCompatible(intervals[j], partitionIntervals);
            if(compatibleID != -1){
                // Schedule lecture j in any such classroom k. 
                partitionIntervals.get(compatibleID).add(j);
                intervals[j].partition = compatibleID;
            }
            // ELSE
            else{
                // Allocate a new classroom d + 1.                
                partitionIntervals.add(new ArrayList());
                // Schedule lecture j in classroom d + 1.
                partitionIntervals.get(d).add(j);
                intervals[j].partition = d;
                // d <- d + 1
                d++;
            }
        }
        // RETURN schedule.
        for(Interval i: intervals)
            i.comparator = "sort by id";
        Arrays.sort(intervals);
        
        System.out.println("\nOutput:\n" + n + "\n");
        for(Interval i: intervals){
            System.out.println(i.start + " " + i.end + " " + i.partition);
        }
        
        // === the real algo from the book ===
        // Sort the intervals by their start times, breaking ties arbitrarily
        // Let I1, I2, . . . , In denote the intervals in this order
        // For j = 1, 2, 3, . . . , n
        // For each interval Ii that precedes Ij in sorted order and overlaps it
        // Exclude the label of Ii from consideration for Ij
        // Endfor
        // If there is any label from {1, 2, . . . , d} that has not been excluded then
        // Assign a nonexcluded label to Ij
        // Else
        // Leave Ij unlabeled
        // Endif
        // Endfor
    }
    
    /**
 * 
 * @param interval
 * @param partitionIntervals
 * @return -1 = not fitting; any other value = index of partition
 */
    private static int isCompatible(Interval interval, ArrayList<ArrayList<Integer>> partitionIntervals){
        int fittingPartition = -1;        
        // search free space -- Big O(N); because max iteration over all intervals
        for(int i = 1; i <= d; i++){ // not start with d=0
            boolean  noOverlaping = true;
            for(int j = 0; j < partitionIntervals.get(i-1).size(); j++){
                int other = partitionIntervals.get(i-1).get(j);
                if(
                    (intervals[other].start <= interval.start
                        &&
                        intervals[other].end > interval.start)
                    ||                        
                    (intervals[other].start > interval.start
                        && 
                        intervals[other].start < interval.end)
                    ){ 
                    noOverlaping = false;
                    break;
                }
            }
            if(noOverlaping){
               fittingPartition = i-1; 
               break;
            }            
        }
        return fittingPartition;
    }

    private static class Interval implements Comparable{

        public int id;
        public int start;
        public int end;
        public int partition;        
        public String comparator;

        public Interval(int id, int start, int end, String comparator) {
            this.id = id;
            this.start = start;
            this.end = end;
            this.comparator = comparator;
        }

        @Override
        public int compareTo(Object o) {
            if(comparator.compareTo("sort by start time") == 0){
                if(this.start < ((Interval)o).start){ return -1; }
                if(this.start > ((Interval)o).start){ return 1; } 
                return 0;
            }
            if(comparator.compareTo("sort by id") == 0){
                if(this.id < ((Interval)o).id){ return -1; }
                if(this.id > ((Interval)o).id){ return 1; } 
                return 0;
            }
            throw new IllegalArgumentException("Your comparator is not valid!");
        }
    }
}