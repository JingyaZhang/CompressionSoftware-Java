
/* Zhang Jingya  CS610 PP 2508 */

/* Function: Build Huffman Tree and generate Huffman Code when compress files. */

import java.util.*;

public class BuildHuffmanTree {
	public HuffmanNode root; // root node
	HashMap<Integer, String> huffmantable = new HashMap<Integer, String>();

	public BuildHuffmanTree(HashMap<Integer, Integer> min) {
		HuffmanNode parent = null;
		MinHeap heap;

		heap = new MinHeap(min); // build a min-heap of array min[]

		// build the Huffman Tree
		for (int i = 0; i < min.size() - 1; i++) {
			HuffmanNode left = heap.extractMin(); // find the first smallest key as left child
			HuffmanNode right = heap.extractMin(); // find the second smallest key as right child

			// build a parent node, its weight is sum of weight of left and right child
			parent = new HuffmanNode(left.weight + right.weight, -1, "", left, right, null);
			left.parent = parent;
			right.parent = parent; // connect left and right child with their parent

			heap.insert(parent); // copy data of parent into min-heap
		}

		root = parent;

		heap.free();
	}

	// generate Huffman Code
	public void generateCode() {
		if (root != null) {
			generateCode(root);
		}
	}

	private void generateCode(HuffmanNode tree) {
		if (tree != null) {
			if (tree.left != null) {
				tree.left.code = tree.code + "0";
			}
			if (tree.right != null) {
				tree.right.code = tree.code + "1";
			}
			generateCode(tree.left);
			generateCode(tree.right);
		}
	}

	// save keys and their huffman code of leaf nodes into Hash map
	public void huffmanTable() {
		if (root != null)
			huffmanTable(root);
	}

	private void huffmanTable(HuffmanNode tree) {
		if (tree.key == -1) {
			huffmanTable(tree.left);
			huffmanTable(tree.right);
		} else if ((tree.left == null) && (tree.right == null)) {
			huffmantable.put(tree.key, tree.code);
		} else
			System.out.print("Errors occured when build the Huffman Table.");

	}

	public HashMap<Integer, String> getHuffmanTable() {
		return huffmantable;
	}

	// set Huffman Tree and its memory free
	public void free() {
		free(root);
		root = null;
	}

	private void free(HuffmanNode tree) {
		if (tree == null)
			return;
		if (tree.left != null)
			free(tree.left); // free left child
		if (tree.right != null)
			free(tree.right); // free right child
		tree = null; // free root node
	}

}
