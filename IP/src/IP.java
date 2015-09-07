import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * @author Rene Anda Nielsen <rann@itu.dk>
 * @author Aaron Gornott <agor@itu.dk>
 * @author Sarah
 */
public class IP {

    private static Scanner sc;
    private static int n; // number of intervals
    private static Interval[] intervals;
    private static int d; // number of partitions
    private static final Comparator<Integer> shortestIntervalEndComparator = new Comparator<Integer>(){
        @Override
        public int compare(Integer o1, Integer o2) {
            Integer i1 = intervals[partitionIntervals.get(o1).get(partitionIntervals.get(o1).size()-1)].end;
            Integer i2 = intervals[partitionIntervals.get(o2).get(partitionIntervals.get(o2).size()-1)].end;
            return i1.compareTo(i2);
        }
    };
    private static final PriorityQueue<Integer> shortestIntervalEnd = new PriorityQueue(shortestIntervalEndComparator);
    private static final ArrayList<ArrayList<Integer>> partitionIntervals = new ArrayList();

    public static void main(String[] args) throws FileNotFoundException {
        File file = new File("ip-rand-1M.in");
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
                Interval interval = new Interval(id, start,end, "sort by start time");
                intervals[id] = interval;
                id++;
            }
        }
        
        // SORT lectures by start time so that s1 ≤ s2 ≤ ... ≤ sn.
        Arrays.sort(intervals);   
        // d <- 0
        // Add first element
        partitionIntervals.add(new ArrayList());
        partitionIntervals.get(0).add(0);
        intervals[0].partition = 0;
        shortestIntervalEnd.add(0);
        d = 1;
        // FOR j = 1 TO n
        for(int j = 1; j < n; j++){
            // IF lecture j is compatible with some classroom
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
            //// taking care of k
            if(shortestIntervalEnd.peek() == compatibleID)
                shortestIntervalEnd.add(shortestIntervalEnd.poll()); // recalculate
            else
                shortestIntervalEnd.add( d - 1 );
        }
        // RETURN schedule.
        for(Interval i: intervals)
            i.comparator = "sort by id";
        Arrays.sort(intervals);
        
        System.out.println("\nOutput:\n" + d + "\n");
        for(Interval i: intervals)
            System.out.println(i.start + " " + i.end + " " + i.partition);
    }
    
    /**
    * @param interval
    * @param partitionIntervals
    * @return -1 = not fitting; any other value = index of partition
    */
    private static int isCompatible(Interval interval, ArrayList<ArrayList<Integer>> partitionIntervals){
        int k = shortestIntervalEnd.peek();        
        if(intervals[partitionIntervals.get(k).get(partitionIntervals.get(k).size()-1)].end <= interval.start)
            return k;
        else
            return -1;
    }

    private static class Interval implements Comparable{

        public int id;
        public int start;
        public int end;
        public int partition;        
        public String comparator; // value: "sort by start time" or "sort by id"

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