import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner in = new Scanner(System.in);

        int m = in.nextInt(); // rows
        int n = in.nextInt(); // cols
        int r = in.nextInt(); // rotation

        int[][] matrix = new int[m][n];
        int[][] result = new int[m][n];

        // read in the matrix
        for (int i=0; i < m; i++) {
            for (int j=0; j < n; j++) {
                matrix[i][j] = in.nextInt();
            }
        }

        // prep work
        indexToRingIndex = new HashMap<String, RingIndex>(m*n);
        populateRings(m, n);

        // rotate
        for (int i=0; i < m; i++) {
            for (int j=0; j < n; j++) {
                int[] pos = newPos(m, n, r, i, j);
                result[pos[0]][pos[1]] = matrix[i][j];
            }
        }

        // print the result
        for (int i=0; i < m; i++) {
            for (int j=0; j < n; j++) {
                System.out.print(result[i][j] + " ");
            }
            System.out.println("");
        }
    }

    private static class Ring {
        public List<List<Integer>> indicies = new ArrayList<List<Integer>>();

        public String toString() {
            StringBuffer sb = new StringBuffer();
            for (List<Integer> l : indicies) {
                sb.append("[");
                sb.append(l.get(0));
                sb.append(",");
                sb.append(l.get(1));
                sb.append("], ");
            }
            return sb.toString();
        }
    }

    private static class RingIndex {
        public Ring ring;
        public int idx;
    }

    private static Map<String, RingIndex> indexToRingIndex;
    private static List<Ring> rings = new ArrayList<Ring>();

    private static String posStr(int i, int j) {
        return i + ":" + j;
    }

    private static void populateRings(int m, int n) {
        int numRings = Math.min(m, n) / 2;

        for (int k=0; k < numRings; k++) {
            Ring r = new Ring();

            // left side, down
            for (int i=k; i < m-k-1; i++) {
                String str = posStr(i, k);
                if (!indexToRingIndex.containsKey(str)) {
                    RingIndex ri = new RingIndex();
                    ri.ring = r;
                    ri.idx = r.indicies.size();
                    List<Integer> idxs = new ArrayList<Integer>();
                    idxs.add(i);
                    idxs.add(k);
                    r.indicies.add(idxs);
                    indexToRingIndex.put(str, ri);
                }
            }

            // bottom, right
            for (int j=k; j < n-k-1; j++) {
                String str = posStr(m-k-1, j);
                if (!indexToRingIndex.containsKey(str)) {
                    RingIndex ri = new RingIndex();
                    ri.ring = r;
                    ri.idx = r.indicies.size();
                    List<Integer> idxs = new ArrayList<Integer>();
                    idxs.add(m-k-1);
                    idxs.add(j);
                    r.indicies.add(idxs);
                    indexToRingIndex.put(str, ri);
                }
            }

            // right side, up
            for (int i=m-k-1; i > k; i--) {
                String str = posStr(i, n-k-1);
                if (!indexToRingIndex.containsKey(str)) {
                    RingIndex ri = new RingIndex();
                    ri.ring = r;
                    ri.idx = r.indicies.size();
                    List<Integer> idxs = new ArrayList<Integer>();
                    idxs.add(i);
                    idxs.add(n-k-1);
                    r.indicies.add(idxs);
                    indexToRingIndex.put(str, ri);
                }
            }

            // top, left
            for (int j=n-k-1; j > k; j--) {
                String str = posStr(k, j);
                if (!indexToRingIndex.containsKey(str)) {
                    RingIndex ri = new RingIndex();
                    ri.ring = r;
                    ri.idx = r.indicies.size();
                    List<Integer> idxs = new ArrayList<Integer>();
                    idxs.add(k);
                    idxs.add(j);
                    r.indicies.add(idxs);
                    indexToRingIndex.put(str, ri);
                }
            }

            rings.add(r);
        }
    }

    private static int[] newPos(int m, int n, int r, int i, int j) {
        int[] pos = new int[2];

        RingIndex ri = indexToRingIndex.get(posStr(i,j));
        Ring ring = ri.ring;
        int newPos = (r + ri.idx) % ring.indicies.size();
        List<Integer> li = ring.indicies.get(newPos);


        pos[0] = li.get(0);
        pos[1] = li.get(1);

        return pos;
    }
}
