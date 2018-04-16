package com.springboot.guide.springboot.guide;

public class MergeSort {

    // merge two arrays:
    // firstArray: from l to mid        ->  array[l...mid]
    // secondArray: from mid+1 to r     ->  array[mid+1...r]
    private void merge(int arr[], int l, int mid, int r) {
        // Create two arrays for dividing origin array into halves
        int leftArrayItemCount = mid - l + 1;
        int leftArray[] = new int[leftArrayItemCount];
        int rightArrayItemCount = r - mid;
        int rightArray[] = new int[rightArrayItemCount];

        for (int i=0; i<leftArrayItemCount; i++) {
            leftArray[i] = arr[l+i];
        }
        for (int j=0; j<rightArrayItemCount; j++) {
            rightArray[j] = arr[mid+1 + j];
        }

        // Initial indexes of first and second subarrays
        int i = 0;
        int j = 0;

        // Initial index of merged subarry array
        int k = l;
        while (i < leftArrayItemCount && j < rightArrayItemCount) {
            if (leftArray[i] <= rightArray[j]) {
                arr[k] = leftArray[i];
                i++;
            } else {
                arr[k] = rightArray[j];
                j++;
            }
            k++;
        }

        /* Copy remaining elements of leftArray[] if any */
        while (i < leftArrayItemCount) {
            arr[k] = leftArray[i];
            i++;
            k++;
        }

        /* Copy remaining elements of leftArray[] if any */
        while (j < rightArrayItemCount) {
            arr[k] = rightArray[j];
            j++;
            k++;
        }
    }


    public void recursionMerge(int array[], int low, int high) {
        if (low > high || low == high) {
            return;
        }

        int mid = (low + high) / 2;

        recursionMerge(array, low, mid);
        recursionMerge(array, mid+1, high);

        merge(array, low, mid, high);
    }


    public static void printArray(int arr[]) {
        int n = arr.length;
        for (int i=0; i<n; ++i) {
            System.out.print(arr[i] + " ");
        }
        System.out.println();
    }

    public static void main(String args[]) {
        int arr[] = {1011, 9877, 118, 94, 11, 5, 0, 98, 15, 11};
        System.out.println("array sorted before:");
        printArray(arr);

        int n = arr.length;
        MergeSort ms = new MergeSort();
        ms.recursionMerge(arr, 0, n-1);

        System.out.println("array sorted");
        printArray(arr);
    }
}

