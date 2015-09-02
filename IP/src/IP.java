
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Rene Anda Nielsen <rann@itu.dk>
 * @author Aaron Gornott <agor@itu.dk>
 */
public class IP {

    private static Scanner sc;
    private static int n; // number of intervals
    private static int k; // number of partitions
    private static Interval[] intervals;
    private static Interval interval;
    
    private final static ArrayList<Integer> sortedIntervalIDs = new ArrayList();
        
    private static int d; // no of partitions
    private final static ArrayList<ArrayList<Integer>> partitionIntervals = new ArrayList();

    public static void main(String[] args) throws FileNotFoundException {
        IP ip = new IP();
        File file = new File("ip-rand-1k.in");
        sc = new Scanner(file);
        //sc = new Scanner(System.in);
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
                interval = new Interval(id, start,end );
                intervals[id] = interval;
                id++;
            }
        }
//        for (Interval interval : intervals) {
//            System.out.println(interval.start + " " + interval.end);
//        }
        
        // SORT lectures by start time so that s1 ≤ s2 ≤ ... ≤ sn.
        //////Arrays.sort(intervals);
        
//        System.out.println("Sorting order:\n");
//        for(Interval i: intervals){
//            System.out.println(i.start);
//        }
        
//####################################      
        // quicksort hint: use the interval.compareTo(Obj o) Method ;-)
        for(int i = 0; i < n; i++){ // this is O(n^n) !!!!!!! DANGER DANGER
            int smallestID = -1;
            int smallestVal = Integer.MAX_VALUE;
            for(int j = 0; j < n; j++){                
                if(intervals[j].start < smallestVal && !sortedIntervalIDs.contains(j)){
                    smallestVal = intervals[j].start;
                    smallestID = j;
                }                            
            }
            sortedIntervalIDs.add(smallestID);
        }
 //###################################       
        
        // d <- 0
        d = 0;
        // FOR j = 1 TO n
        for(int j = 0; j < n; j++){
            // IF lecture j is compatible with some classroom
            int sortedJ = sortedIntervalIDs.get(j);
            int compatibleID = isCompatible(intervals[sortedJ], partitionIntervals);
            if(compatibleID != -1){
                // Schedule lecture j in any such classroom k. 
                partitionIntervals.get(compatibleID).add(sortedJ);
                intervals[sortedJ].partition = compatibleID;
            }
            // ELSE
            else{
                // Allocate a new classroom d + 1.                
                partitionIntervals.add(new ArrayList());
                // Schedule lecture j in classroom d + 1.
                partitionIntervals.get(d).add(sortedJ);
                intervals[sortedJ].partition = d;
                // d <- d + 1
                d++;
            }
        }
        // RETURN schedule.
        
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

        int id;
        int start;
        int end;
        int partition;

        public Interval(int id, int start, int end) {
            this.id = id;
            this.start = start;
            this.end = end;
        }

        @Override
        public int compareTo(Object o) {
            if(this.start < ((Interval)o).start){ return -1; }
            if(this.start > ((Interval)o).start){ return 1; } 
            return 0;
        }
    }
}
