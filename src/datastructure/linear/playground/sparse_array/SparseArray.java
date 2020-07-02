package datastructure.linear.playground.sparse_array;

import java.util.Arrays;

public class SparseArray {

    public static void main(String[] args) {
        int[][] array = new int[11][11];
        array[1][2] = 1;
        array[2][3] = 2;
        System.out.println(Arrays.deepToString(array));

        int counter = 0;
        for (int[] column : array) {
            for (int cell : column) {
                if(cell != 0) {
                    counter++;
                }
            }
        }

        int[][] sparse = new int[counter + 1][3];
        int[] head = sparse[0];
        head[0] = 11;
        head[1] = 11;
        head[2] = counter;

        counter = 1;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if(array[i][j] == 0) {
                    continue;
                }
                sparse[counter][0] = i;
                sparse[counter][1] = j;
                sparse[counter][2] = array[i][j];
                counter++;
            }
        }

        System.out.println(Arrays.deepToString(sparse));
    }


}
