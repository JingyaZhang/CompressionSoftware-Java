
/* Zhang Jingya  CS610 PP 2508 */

/* Function: Algorithms of build the Min-Heap. */

import java.util.*;
import java.util.Map.Entry;

public class MinHeap {
	private List<HuffmanNode> minheap; // list to store min heap

	protected MinHeap(HashMap<Integer, Integer> freqmap) {
		minheap = new ArrayList<HuffmanNode>();

		// initialize the list
		for (Entry<Integer, Integer> item : freqmap.entrySet()) {
			HuffmanNode node = new HuffmanNode(item.getValue(), item.getKey(), "", null, null, null);
			minheap.add(node);
		}

		// after traverse from (size/2-1) to 0, we get the Min-Heap
		for (int i = freqmap.size() / 2 - 1; i >= 0; i--) {
			downHeap(i, freqmap.size() - 1);
		}
	}

	// Down-Heap algorithm int start and end is the beginning and last position of the node needs to be downheap
	protected void downHeap(int start, int end) {
		int current = start; // position of current node
		int left = 2 * current + 1; // position of current node's left child

		HuffmanNode temp = minheap.get(current);
		// huffman node in the current position of arraylist

		while (left <= end) // downheap recurrence
		{
			// if left<end and (weight of huffman node in "left" position)>(weight of huffman node in "right" position) of minheap
			if (left < end && (minheap.get(left).compareTo(minheap.get(left + 1)) > 0))
				left++; // choose the smallest child

			int compare = temp.compareTo(minheap.get(left));
			// compare=(weight of current huffman node)-(weight of left child) of minheap
			
			if (compare <= 0)
				break; // if current node is smaller than its left child, downheap is complete
			else {
				minheap.set(current, minheap.get(left)); // replace the current node with its left child
				current = left; // set left position as new current position in recurrence
				left = left * 2 + 1; // set new left child position in recurrence
			}
		}

		minheap.set(current, temp);
		// put the start huffman node into the position where no child's weight smaller than it
	}

	// insert node into heap
	protected void insert(HuffmanNode node) {
		int size = minheap.size();
		minheap.add(node); // add node in the tail
		upHeap(size); // upheap inserted node to the appropriate position
	}

	// Upheap algorithm. Help when a node is inserted and need re-sort again
	protected void upHeap(int start) {
		int current = start; // current node's position
		int parent = (current - 1) / 2; // parent node's position
		HuffmanNode temp = minheap.get(current); // huffman node of current position

		while (current > 0) // upheap recurrence
		{
			int compare = minheap.get(parent).compareTo(temp); 
			// compare=(weight of parent node)-(weight of current node)
			
			if (compare <= 0)
				break; // if parent node is smaller than current node, upheap is complete
			else {
				minheap.set(current, minheap.get(parent)); 
				// replace huffman node of current position with its parent node
				
				current = parent; // renew the current position
				parent = (parent - 1) / 2; // renew the parent position
			}
		}

		minheap.set(current, temp);
		// put the bottom node into where there parent is smaller than it
	}

	// extract min huffman node and re-sort to build the minheap
	protected HuffmanNode extractMin() {
		int size = minheap.size();

		if (size == 0)
			return null;

		HuffmanNode node = (HuffmanNode) minheap.get(0).clone();
		// copy the smallest huffman node to "node"

		minheap.set(0, minheap.get(size - 1)); // replace the min node in top position with the last node
		minheap.remove(size - 1); // delete the last node

		if (minheap.size() > 1)
			downHeap(0, minheap.size() - 1);

		return node;
	}

	// set the minheap and its memory free
	protected void free() {
		minheap.clear();
		minheap = null;
	}

}
