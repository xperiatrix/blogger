package com.springboot.guide.springboot.guide;

public class QuickSort {
    private int partition(int array[], int low, int high) {
        int pivot = array[high];
        int startIndex = low-1;

        for (int sortIndex = low; sortIndex < high; sortIndex++) {
            if (array[sortIndex] <= pivot) {
                startIndex++;

                int temp = array[startIndex];
                array[startIndex] = array[sortIndex];
                array[sortIndex] = temp;
            }
        }

        int temp = array[startIndex+1];
        array[startIndex+1] = array[high];
        array[high] = temp;

        return startIndex+1;
    }

    public void recursionSort(int array[], int low, int high) {
        if (low > high || low == high) {
            return;
        }

        int pivotIndex = partition(array, low, high);

        recursionSort(array, low, pivotIndex-1);
        recursionSort(array, pivotIndex+1, high);
    }

    public static void printArray(int arr[]) {
        int n = arr.length;
        for (int i=0; i<n; ++i)
            System.out.print(arr[i]+" ");
        System.out.println();
    }

    public static void main(String args[]) {
        int arr[] = {1011, 9877, 118, 94, 11, 5, 0, 98};
        System.out.println("array before sorted");
        printArray(arr);

        int n = arr.length;
        QuickSort qs = new QuickSort();
        qs.recursionSort(arr, 0, n-1);

        System.out.println("array sorted");
        printArray(arr);
    }
}

