package BlockTest;

import java.util.Arrays;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        List<Integer> arr = Arrays.asList(1, 3, 5, 7, 9, 11, 13, 15);

        System.out.println(binarySearch(arr,16));
    }

    public static int binarySearch(List<Integer> arr, int key) {
        int low = 0;
        int high = arr.size() - 1;
        while (low <= high) {
            int mid = low + (high - low) / 2;
            int midVal = arr.get(mid);
            if (midVal < key) {
                low = mid + 1;
            } else if (midVal > key) {
                high = mid - 1;
            } else {
                return mid;
            }
        }
        return low; // 返回-1表示没找到
    }
}
