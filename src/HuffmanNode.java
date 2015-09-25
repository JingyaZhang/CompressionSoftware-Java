
/* Zhang Jingya  CS610 PP 2508 */

/* Function: Structure of Node in Huffman Tree. */

public class HuffmanNode implements Comparable, Cloneable {
	protected int weight; // frequency of appearance
	protected int key; // readed number of this byte
	protected String code = ""; // to save the huffman code
	protected HuffmanNode left; // left child
	protected HuffmanNode right; // right child
	protected HuffmanNode parent; // parent node

	protected HuffmanNode(int weight, int key, String code,
			HuffmanNode left, HuffmanNode right, HuffmanNode parent) {
		this.weight = weight;
		this.key = key;
		this.code = code;
		this.left = left;
		this.right = right;
		this.parent = parent;
	}

	@Override
	public Object clone() {
		Object obj = null;
		try {
			obj = (HuffmanNode) super.clone();
			// clone() will recognize the object you want to clone in Object
		} catch (CloneNotSupportedException e) {
			System.out.println(e.toString());
		}
		return obj;
	}

	@Override
	public int compareTo(Object obj) {
		return this.weight - ((HuffmanNode) obj).weight;
	}
}
