
/* Zhang Jingya  CS610 PP 2508 */

/* function: Implement Huffman Coding of a file. */

import java.io.*;
import java.util.*;

public class henc {
	HashMap<Integer, Integer> statistics = null;

	public henc() {
		// bytes readed from file
		// bytes very from 0 to 255(0~2^8); values in array is the frequency of each number
		statistics = new HashMap<>();
	}

	// read a file by byte
	protected void readFile(String filename) {
		File file = new File(filename);
		InputStream readin = null;
		try {
			// System.out.println("Reading file as binary...");
			readin = new FileInputStream(file);
			int buffer;
			while ((buffer = readin.read()) != -1) {
				// System.out.println(buffer);
				if (statistics.containsKey(buffer)) {
					int value = statistics.get(buffer);
					statistics.put(buffer, value + 1);
				} else {
					statistics.put(buffer, 1);
				}
				// buffer is the number of byte, value in array is this number's frequency
			}
			readin.close();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}

	public HashMap<Integer, Integer> getHashMap() {
		return statistics;
	}

	public static void main(String[] args) {

		henc encode = new henc();
		encode.readFile(args[0]); // read file byte by byte and put data in array

		BuildHuffmanTree hufftree;
		hufftree = new BuildHuffmanTree(encode.getHashMap()); // build huffman tree by array

		
/*		System.out.println(encode.getHashMap()); //show data
		System.out.println(encode.getHashMap().size()); //check array length and it should be 256
*/		 

		hufftree.generateCode();
		hufftree.huffmanTable();
		HashMap<Integer, String> huffmap = hufftree.getHuffmanTable();
		
/*		for (Entry<Integer, String> item : huffmap.entrySet()) {
		System.out.println(item.getKey() + " : " + item.getValue()); }
*/		 

		String path = "./name.huf";
		if (args.length == 1) {
			File compression = new File(path.replace("name", args[0].toString()));
			try {
				compression.createNewFile();

				String outfilename = compression.getName(); // get filename of .huf
				WriteFile write = new WriteFile();
				write.writeHuffCode(args[0], outfilename, huffmap);
				// write huffman code map and codes in file

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
