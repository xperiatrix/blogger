# A MergeSort in Java

&nbsp;&nbsp;&nbsp;&nbsp; 继续使用递归来实现另一种常见排序，归并排序。

+ 什么归并是排序  
&nbsp;&nbsp;&nbsp;&nbsp; 归并排序简单说，这种排序分为两部分，一部分是讲元素分割，再讲分割后的元素重新归并组合。其时间复杂度与快速排序均为O(n* logn)，但是归并排序除了递归调用间接使用了辅助空间栈，还需要额外的O(n)空间进行临时存储。从这一点看待，归并排序会略逊于快速排序，但<b>归并排序是一种稳定的排序算法，快速排序则不是</b>。 Divide and Conquer
	- 什么是稳定排序算法  
稳定排序算法：对于具有相同值的多个元素，其间的先后顺序保持不变。
		- 对于基本数据类型而言，一个排序算法是否稳定，影响很小。
		- <b>对于结构体数组，稳定排序就十分重要。 稳定排序可以保证下面例子中B始终在D前面；而非稳定排序，则无法保证。 </b>

```java
typedef struct __Student
{
    char name[16];
    int marks;
}Student;

// Array of students
name :  A      B     C     D
marks:  80     70    75    70
 
Stable sort in ascending order:
name :  B      D     C     A
marks:  70     70    75    80
 
Unstable sort in ascending order:
name :  D      B     C     A
marks:  70     70    75    80
```


+ 归并排序的实现细节  
&nbsp;&nbsp;&nbsp;&nbsp; 在一大堆无序的数字，分成两组A和B。在A组中继续分两组A1-X和A1-Y，在A1-X中继续分两组A2-X和A2-Y，采取同样的方案继续向下分组，直到该分组不能继续向下再分，进行递归排序。在B组中也采取相同的方案，直到该分组不能继续向下再分，进行递归排序。最后将各自排好序的有序序列合并，最初的数组就全部排好序了。


+ 归并排序的实现细节(Java)  

```java
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
		/*
		int arr[] = new int[100000];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random()*100000);
        }
		*/
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
```