package qsort

import ()

func quickSort(values []int, left, right int) {
	if left >= right {
		return
	}

	i, j := left, right
	key := values[left]

	for i < j {
		for i < j && key <= values[j] {
			j--
		}
		values[i] = values[j]

		for i < j && key >= values[i] {
			i++
		}
		values[j] = values[i]
	}

	values[i] = key
	quickSort(values, left, i-1)
	quickSort(values, i+1, right)

}

func QuickSort(values []int) {
	quickSort(values, 0, len(values)-1)
}
