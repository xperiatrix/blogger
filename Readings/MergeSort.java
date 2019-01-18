package com.springboot.guide.springboot.guide;

public class MergeSort {

    public void recursionMerge(int array[], int low, int high) {
        if (low >= high)    return;

        int mid = low + (high-low)/2;

        recursionMerge(array, low, mid);
        recursionMerge(array, mid+1, high);

        merge(array, low, mid, high);
    }

    public void merge(int[] array, int low, int mid, int high) {
	int i = low;
	int j = mid + 1;
	int k = 0;
	int[] temp = new int[high - low + 1];
	
	while(i <= mid && j <= high) {
	    if (array[i] <= array[j]) {
		temp[k++] = array[i++];
	    } else {
		temp[k++] = array[j++];
	    }
	}

	int start = i;
	int end = mid;
	if (j <= high) {
	    start = j;
	    end = high;
	}

	while (start <= end) {
	    temp[k++] = array[start++];
	}

	if (int z = 0; z <= high - low; z++) {
	    array[low + z] = temp[z];
        }
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

