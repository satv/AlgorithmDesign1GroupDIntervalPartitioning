
import java.io.File;
import java.io.FileNotFoundException;
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
