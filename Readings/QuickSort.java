package com.springboot.guide.springboot.guide;

public class QuickSort {
    private int partition(int array[], int low, int high) {
	int i = low;
	int povit = array[high];
	for (int j = low; j < high; j++) {
	    if (array[j] < povit) {
		int temp = array[i];
		array[i] = array[j];
		array[j] = temp;
		i++;
	    }
	}

	int tempValue = array[i];
	array[i] = array[high];
	array[high] = tempValue;
        System.out.println(i + "");
	return i;
    }

    public void recursionSort(int array[], int low, int high) {
        if (low >= high)    return;

        int keyPoint = partition(array, low, high);
        recursionSort(array, low, keyPoint-1);
        recursionSort(array, keyPoint+1, high);
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

