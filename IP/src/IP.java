
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Rene Anda Nielsen <rann@itu.dk>
 */
public class IP {

    private static Scanner sc;
    private static int n; // number of intervals
    private static int k; // number of partitions
    private static Interval[] intervals;
    private static Interval interval;
    
    private static Queue<Integer> sortedIntervalIDs = new LinkedList();
        
    private static int d; // no of partitions
    private static ArrayList<ArrayList<Integer>> partitionIntervals = new ArrayList();

    public static void main(String[] args) throws FileNotFoundException {
        IP ip = new IP();
        File file = new File("ip-1.in");
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
                interval = new Interval(start,end );
                intervals[id] = interval;
                id++;
            }
        }
        for (Interval interval : intervals) {
            System.out.println(interval.start + " " + interval.end);
        }
        
        
        // SORT lectures by start time so that s1 ≤ s2 ≤ ... ≤ sn.
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
        // d <- 0
        d = 0;
        // FOR j = 1 TO n
        for(int j = 1; j <= 0; j++){
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
        }
        // RETURN schedule.
        
        
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
        for(int i = 0; i < d; i++){
            boolean  noOverlaping = true;
            for(int j = 0; j < partitionIntervals.get(i).size(); j++){
                int other = partitionIntervals.get(i).get(j);
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
               fittingPartition = i; 
               break;
            }            
        }
        return fittingPartition;
    }

    private static class Interval {

        int start;
        int end;
        int partition;

        public Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
}
