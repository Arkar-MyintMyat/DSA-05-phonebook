package oy.tol.tra;

import java.util.Comparator;
import java.util.function.Predicate;

public class Algorithms {

   private static final int INSERTION_SORT_THRESHOLD = 16;

   private Algorithms() {
   }

   public static <T> void swap(T[] array, int first, int second) {
      T temp = array[first];
      array[first] = array[second];
      array[second] = temp;
   }

   public static <T extends Comparable<T>> void sort(T[] array) {
      if (array == null) {
         return;
      }
      for (int i = 1; i < array.length; i++) {
         T current = array[i];
         int j = i - 1;
         while (j >= 0 && array[j].compareTo(current) > 0) {
            array[j + 1] = array[j];
            j--;
         }
         array[j + 1] = current;
      }
   }

   public static <T> void reverse(T[] array) {
      if (array == null) {
         return;
      }
      int i = 0;
      int j = array.length - 1;
      while (i < j) {
         swap(array, i++, j--);
      }
   }

   public static <T extends Comparable<T>> void fastSort(T[] array) {
      if (array == null || array.length < 2) {
         return;
      }
      quickSort(array, 0, array.length - 1);
   }

   public static <T extends Comparable<T>> void fastSort(T[] array, int count) {
      if (array == null || count < 2) {
         return;
      }
      quickSort(array, 0, Math.min(count, array.length) - 1);
   }

   private static <T extends Comparable<T>> void quickSort(T[] array, int low, int high) {
      while (high - low > INSERTION_SORT_THRESHOLD) {
         int pivotIndex = partition(array, low, high);
         if (pivotIndex - low < high - pivotIndex) {
            quickSort(array, low, pivotIndex);
            low = pivotIndex + 1;
         } else {
            quickSort(array, pivotIndex + 1, high);
            high = pivotIndex;
         }
      }
      insertionSort(array, low, high);
   }

   private static <T extends Comparable<T>> int partition(T[] array, int low, int high) {
      int middle = low + (high - low) / 2;
      if (array[middle].compareTo(array[low]) < 0) {
         swap(array, middle, low);
      }
      if (array[high].compareTo(array[low]) < 0) {
         swap(array, high, low);
      }
      if (array[high].compareTo(array[middle]) < 0) {
         swap(array, high, middle);
      }
      T pivot = array[middle];
      int i = low - 1;
      int j = high + 1;
      while (true) {
         do {
            i++;
         } while (array[i].compareTo(pivot) < 0);
         do {
            j--;
         } while (array[j].compareTo(pivot) > 0);
         if (i >= j) {
            return j;
         }
         swap(array, i, j);
      }
   }

   private static <T extends Comparable<T>> void insertionSort(T[] array, int low, int high) {
      for (int i = low + 1; i <= high; i++) {
         T current = array[i];
         int j = i - 1;
         while (j >= low && array[j].compareTo(current) > 0) {
            array[j + 1] = array[j];
            j--;
         }
         array[j + 1] = current;
      }
   }

   public static <T> void sortWithComparator(T[] array, Comparator<? super T> comparator) {
      if (array == null || comparator == null || array.length < 2) {
         return;
      }
      quickSort(array, 0, array.length - 1, comparator);
   }

   private static <T> void quickSort(T[] array, int low, int high, Comparator<? super T> comparator) {
      while (high - low > INSERTION_SORT_THRESHOLD) {
         int pivotIndex = partition(array, low, high, comparator);
         if (pivotIndex - low < high - pivotIndex) {
            quickSort(array, low, pivotIndex, comparator);
            low = pivotIndex + 1;
         } else {
            quickSort(array, pivotIndex + 1, high, comparator);
            high = pivotIndex;
         }
      }
      for (int i = low + 1; i <= high; i++) {
         T current = array[i];
         int j = i - 1;
         while (j >= low && comparator.compare(array[j], current) > 0) {
            array[j + 1] = array[j];
            j--;
         }
         array[j + 1] = current;
      }
   }

   private static <T> int partition(T[] array, int low, int high, Comparator<? super T> comparator) {
      int middle = low + (high - low) / 2;
      if (comparator.compare(array[middle], array[low]) < 0) {
         swap(array, middle, low);
      }
      if (comparator.compare(array[high], array[low]) < 0) {
         swap(array, high, low);
      }
      if (comparator.compare(array[high], array[middle]) < 0) {
         swap(array, high, middle);
      }
      T pivot = array[middle];
      int i = low - 1;
      int j = high + 1;
      while (true) {
         do {
            i++;
         } while (comparator.compare(array[i], pivot) < 0);
         do {
            j--;
         } while (comparator.compare(array[j], pivot) > 0);
         if (i >= j) {
            return j;
         }
         swap(array, i, j);
      }
   }

   public static <T> int partitionByRule(T[] array, int count, Predicate<T> rule) {
      // Find first element rules applies to.
      // Index of that element will be in variable index.
      int index = 0;
      for (; index < count; index++) {
         if (rule.test(array[index])) {
            break;
         }
      }
      // If went to the end, nothing was selected so quit here.
      if (index >= count) {
         return count;
      }
      // Then start finding not selected elements starting from next from index.
      // If the element is not selected, swap it with the selected one.
      int nextIndex = index + 1;
      // Until end of array reached.
      while (nextIndex != count) {
         if (!rule.test(array[nextIndex])) {
            swap(array, index, nextIndex);
            // If swapping was done, add to index since now it has non-selected element.
            index++;
         }
         nextIndex++;
      }
      return index;
   }
}
