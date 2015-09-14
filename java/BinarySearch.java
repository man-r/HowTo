import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;


public class BinarySearch {
    public static void main(String[] args) throws IOException {
        int [] sorted = {1,2,3,4,5,6,7,8,9,10,11};
        int searchNumber = 1;

        int start = 0;
        int end = sorted.length - 1;
        

        for (int i = 0; i< (int) sorted.length/2; i++ ) {
            int searchIndex = (int) (start + end) /2;
            System.out.println("# " + searchIndex);
            System.out.println("start# " + start + "  -  end# " + end);
            if (sorted[searchIndex] == searchNumber) {
                System.out.println("found at " + searchIndex);
                break;
            } else if (searchNumber > sorted[searchIndex]) {
                start = searchIndex + 1;
            } else {
                end = searchIndex - 1;
            }
            
        }
    }

}