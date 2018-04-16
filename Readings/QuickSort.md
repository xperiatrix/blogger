# A QuickSort in Java

&nbsp;&nbsp;&nbsp;&nbsp; 递归是计算机理念中的精髓之一，简洁。举个递归算法映射到生活中的例子吧：CEO只需要管理好直属部门总监，总监去管理好各个部门负责人，各个部门负责人管理好员工，这样自顶向下的递归管理方式。 与递归相反的是，与人类思考相似的自底向上的递推。今天在这里重点实现一下在工程中被广泛应用的排序方案： 递归式的快速排序。

+ 什么是快速排序  
&nbsp;&nbsp;&nbsp;&nbsp; 快速排序是一种排序算法，最早由托尼·霍尔提出，平均状态下，在解决排序问题中有比较良好的性能，该算法被广泛运用到工程中去。其核心思想是：少做无用功。  

+ 快速排序的实现细节  
&nbsp;&nbsp;&nbsp;&nbsp; 在一大堆无序的数字，随机挑选一个数字作为【枢值】，接下来将所有要排序的数字分为两部分，一部分大于等于【枢值】，一部分小于【枢值】，从上面已经分出的两堆数字中，分别再找出一个新的枢值（这两堆中，新的找出的枢值一个肯定大于等于旧的枢值， 一个小于旧的枢值），再次排序，这样原本的数字，已经分为4组了)， 采取同样的方案继续分组，直到分组结束，之前无序的数字就排好序了。


+ 快速排序的实现细节(Java)  
```java
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
        for (int i=0; i<n; ++i) {
            System.out.print(arr[i]+" ");
        }
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

```  

+ 快速排序中 关于枢值的优化   
&nbsp;&nbsp;&nbsp;&nbsp; 枢值选取的优化方法：
    * 固定值选择  
    当带排序数组内部有序或者局部有序时，这种情况最糟糕，算法复杂度会成为 O(n²)，显然，这并不是一中有效策略。

    * 随机值选择  
    随机选取当前待排序序列的任意记录作为枢轴。由于采取随机，所以时间性能要强于固定位置选取。  

    * 三值取中法  
    首元素、尾元素、中间位置元素，排序，取三个值中的<b>中位数</b>！


```java
package com.springboot.guide.springboot.guide;


public class QuickSort {

    private int pivotCompared(int array[]) {
        int high = array.length-1;
        int[] pivotArray = {array[0], array[(int)(high/2)], array[high]};
        Arrays.sort(pivotArray);
        return pivotArray[1];
    }


    private int partition(int array[], int low, int high) {
        int pivot = pivotCompared(array);
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
        for (int i=0; i<n; ++i) {
            System.out.print(arr[i]+" ");
        }
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

```  
