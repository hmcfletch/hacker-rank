import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

public class Solution {

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner in = new Scanner(System.in);

        int num = in.nextInt();

        Heap maxHeap = new Heap(true);
        Heap minHeap = new Heap(false);

        for (int i=0; i < num; i++) {
            int next = in.nextInt();
            // insert into correct heap

            // small than maxHeap root
            if (maxHeap.size() > 0 && maxHeap.peek().intValue() > next) {
                maxHeap.insert(next);
            // bigger than minHeap root
            } else if (minHeap.size() > 0 && minHeap.peek().intValue() < next) {
                minHeap.insert(next);
            // otherwise just add to maxHeap
            } else {
                maxHeap.insert(next);
            }

            // balance heaps

            // too many elements in maxHeap
            if (maxHeap.size() > minHeap.size() + 1) {
                // pop maxHeap root and add to minHeap
                int maxHeapRoot = maxHeap.pop().intValue();
                minHeap.insert(maxHeapRoot);
            // too many elements in minHeap
            } else if (minHeap.size() > maxHeap.size() + 1) {
                // pop minHeap root and add to maxHeap
                int minHeapRoot = minHeap.pop().intValue();
                maxHeap.insert(minHeapRoot);
            }

            // print median
            double median;
            if (maxHeap.size() > minHeap.size()) {
                median = maxHeap.peek();
            } else if (maxHeap.size() < minHeap.size()) {
                median = minHeap.peek();
            } else {
                int max = maxHeap.peek();
                int min = minHeap.peek();
                median = (max + min) / 2.0;
            }

            String str = String.format("%.1f", median);
            System.out.println(str);
        }
    }

    private static class Heap {

        private static int NO_PARENT = -1;

        private List<Integer> data = new ArrayList<Integer>();

        private boolean isMaxHeap;

        public Heap(boolean isMaxHeap) {
            this.isMaxHeap = isMaxHeap;
        }

        public void insert(int val) {
            data.add(new Integer(val));
            upHeap(data.size() - 1);
        }

        public Integer peek() {
            if (data.size() > 0) {
                return data.get(0);
            } else {
                return null;
            }
        }

        public Integer pop() {
            if (data.size() > 0) {
                Integer i = data.get(0);
                Integer last = data.remove(data.size() - 1);
                data.set(0, last);
                downHeap(0);
                return i;
            } else {
                return null;
            }
        }

        public int size() {
            return data.size();
        }

        public String toString() {
            StringBuffer sb = new StringBuffer();

            for (Integer i : data) {
                sb.append(i);
                sb.append(" ");
            }

            return sb.toString();
        }

        // helpers

        private void upHeap(int i) {
            int parentIndex = getParentIndex(i);
            boolean isCorrectOrder;

            if (parentIndex == NO_PARENT) {
                isCorrectOrder = true;
            } else if (isMaxHeap) {
                isCorrectOrder = data.get(parentIndex) > data.get(i);
            } else {
                isCorrectOrder = data.get(parentIndex) < data.get(i);
            }

            if (!isCorrectOrder) {
                swap(i, parentIndex);
                upHeap(parentIndex);
            }
        }

        private void downHeap(int i) {
            int[] childrenIdx = getChildIndex(i);
            List<Integer> children = getChildren(i);

            boolean isCorrectOrder = true;

            for (int j=0; j < 2; j++) {
                if (children.get(j) == null) {
                    // nop, no child this direction
                } else if (isMaxHeap) {
                    isCorrectOrder &= data.get(i) > children.get(j);
                } else {
                    isCorrectOrder &= data.get(i) < children.get(j);
                }
            }

            if (!isCorrectOrder) {
                int newDownHeapIdx;
                if (isMaxHeap) {
                    if (children.get(1) == null || children.get(0) > children.get(1)) {
                        swap(i, childrenIdx[0]);
                        newDownHeapIdx = childrenIdx[0];
                    } else {
                        swap(i, childrenIdx[1]);
                        newDownHeapIdx = childrenIdx[1];
                    }
                } else {
                    if (children.get(1) == null || children.get(0) < children.get(1)) {
                        swap(i, childrenIdx[0]);
                        newDownHeapIdx = childrenIdx[0];
                    } else {
                        swap(i, childrenIdx[1]);
                        newDownHeapIdx = childrenIdx[1];
                    }
                }
                downHeap(newDownHeapIdx);
            }
        }

        private void swap(int i, int j) {
            Integer temp = data.get(i);
            data.set(i, data.get(j));
            data.set(j, temp);
        }

        private Integer getParent(int i) {
            if (i == 0) {
                return null;
            }
            return data.get(getParentIndex(i));
        }

        private List<Integer> getChildren(int i) {
            int[] idx = getChildIndex(i);
            List<Integer> children = new ArrayList(2);

            for (int j=0; j < 2; j++) {
                if (idx[j] >= size()) {
                    children.add(null);
                } else {
                    children.add(data.get(idx[j]));
                }
            }

            return children;
        }

        private static int[] getChildIndex(int i) {
            int[] idx = new int[2];
            idx[0] = 2*i + 1;
            idx[1] = 2*i + 2;
            return idx;
        }

        private static int getParentIndex(int i) {
            if (i == 0) {
                return NO_PARENT;
            }
            return (int) Math.floor((i - 1) / 2);
        }

    }
}
