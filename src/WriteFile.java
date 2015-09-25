
/* Zhang Jingya  CS610 PP 2508 */

/* function: Write Huffman Hashmap and compressed content into file. */

import java.util.*;
import java.util.Map.Entry;
import java.io.*;

public class WriteFile {

	// wirte map of huffman code in .huf file
	public void writeHuffCode(String readfilename, String writefilename,
			HashMap<Integer, String> map) {
		String temp; // huffman code
		int length; // bits number of huffman code
		int maplength = 0; // number of bytes of hush map

		try {
			BufferedOutputStream out = new BufferedOutputStream(
					new FileOutputStream(writefilename));
			for (Entry<Integer, String> item : map.entrySet()) {
				int valuelength = 0;
				int itemlength = 0;
				valuelength = item.getValue().length();
				itemlength = 2 + (int) Math.ceil(valuelength / 8.0);
				maplength += itemlength;
			}
			out.write(maplength >> 8);
			out.write(maplength); // record the byte numbers of whole hash map

			for (Entry<Integer, String> item : map.entrySet()) {
				out.write(item.getKey());
				temp = item.getValue();
				length = temp.length();
				out.write(length);

				// change String type huffman code to int type
				int codeTonumber = 0;
				for (int i = length; i > 0; i--) {
					int a = temp.charAt(length - i) - '0'; // change str '0' or '1' to int number 0 or 1
					codeTonumber += (a << (i - 1));
				}

				// write huffman code into file
				int bytenumber = (int) Math.ceil(length / 8.0);
				for (int i = bytenumber; i > 0; i--) {
					out.write(codeTonumber >> ((i-1) * 8));
				}
			}
			out.close();
			writeCompressedContent(readfilename, writefilename, map);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// write compressed content, that is huffman codes, in .huf file
	private void writeCompressedContent(String readfilename, String writefilename, HashMap<Integer, String> map) {
		StringBuilder huffcode = new StringBuilder();
		int count = 0; // total bit numbers of whole codes

		try {
			FileOutputStream out = new FileOutputStream(writefilename, true);
			InputStream readin = null;

			readin = new FileInputStream(readfilename);
			int buffer;
			// get all huffman codes of all content in order
			while ((buffer = readin.read()) != -1) {
				String temp = map.get(buffer); // temp = huffman code of this variable "buffer"
				huffcode.append(temp); // add this huff code into huffcode
				int length = temp.length();
				count += length; // total bits of huffman codes
			}
			
			// write number of total bit of codes in file
			for (int i = 0; i < 4; i++) {
				out.write(count >> ((3 - i) * 8));
			}

			// cut huffman codes and write them in file
			int mod = count % 8;
			int head = 0;
			int nextlength = 8;
			for (int i = count; i > mod; i -= 8) {

				String aStr = huffcode.substring(head, head + nextlength);
				// separate whole codes into every 8

				// change String type huffman code to int type
				int codeTonumber = 0;
				for (int j = 8; j > 0; j--) {
					int aNum = aStr.charAt(8 - j) - '0'; 
					// change str '0' or '1' to int number 0 or 1
					
					codeTonumber += (aNum << (j - 1));
				}
				
				head += nextlength;
				
				// write huffman code into file
				out.write(codeTonumber);
			}

			// if codes left is less than 8 bits, write them in file by add "0" in rightmost (8-mod) bits
			String aStr = huffcode.substring(head, head + mod);
			int codeTonumber = 0;
			for (int i = mod; i > 0; i--) {
				int aNum = aStr.charAt(mod - i) - '0';
				codeTonumber += (aNum << (i + (8 - mod) - 1));
			}
			out.write(codeTonumber);

			readin.close();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	// read compressed .huf file and recover original file in the same time
	public boolean writeOriginalFile(String hufffilename, String originfilename) {
		HashMap<String, Integer> decodemap = new HashMap<>();
		BufferedInputStream readhuff = null;
		BufferedOutputStream writeorigin = null;
		
		try {
			readhuff = new BufferedInputStream(new FileInputStream(hufffilename));
			writeorigin = new BufferedOutputStream(new FileOutputStream(originfilename));
			
			// recover hashmap
			int maplength = (readhuff.read() << 8) + readhuff.read(); // number of bytes of hashmap
			int readedbyte = 0; // bytes already readed
			int shortest = 255; // the shortest length in all huff codes
			while (readedbyte<maplength) {
				int key = readhuff.read(); // key of hashmap recovered
				readedbyte++;
				
				int bitcount = readhuff.read(); // get the number of bits of this huff code
				readedbyte++;
				int byteocupied = (int) Math.ceil(bitcount/8.0); // bytes number of this code
				int codeNum = 0;
				for (int i = byteocupied; i > 0; i--) {
					int aNum = readhuff.read();
					codeNum += (aNum << (8*(i-1))); // recover the huff code in order
					readedbyte++;
				}
				String codeStrabsent = Integer.toBinaryString(codeNum); //change huff code into String type
				// this method will discard "0"s in the rightmost
				
				int lengthnow = codeStrabsent.length();
				// length without '0's leftmost if it's start with '0's originally
				
				StringBuilder codeStr = new StringBuilder();
				for (int i = 0; i < (bitcount - lengthnow); i++) {
					codeStr.append("0");
				}
				codeStr.append(codeStrabsent); //recover the complete huff code
				int codelength = codeStr.length(); //length of this code
				if (codelength < shortest) {
					shortest = codelength; //get the shortest bits in all huff codes
				}
				//System.out.println(codeStr);
				
				decodemap.put(codeStr.toString(), key);
			}
			
			// recover original content
			int contentbits = 0; // total bits of whole compressed content
			int scannedbyte = 0;
			
			for (int i = 0; i < 4; i++) {
				contentbits += (readhuff.read() << (3 - i) * 8); // total bits of content
			}
			
			int contentbyte = (int) Math.ceil(contentbits / 8.0);
			StringBuilder contenttemp = new StringBuilder();
			StringBuilder onebyte = null;
			while (scannedbyte < contentbyte) {
				onebyte = new StringBuilder();
				int bNum = readhuff.read();
				scannedbyte++;
				if (bNum != -1) {
					String bStr = Integer.toBinaryString(bNum);
					int lengthnow = bStr.length();
					//length without '0's leftmost if it's start with '0's originally
					for (int i = 0; i < (8 - lengthnow); i++) {
						onebyte.append("0"); // add "0" rightmost to complete the code
					}
					onebyte.append(bStr); // get complete byte
					
					contenttemp.append(onebyte.toString()); // get whole content
				} else {
					break;
				}
			}		
					
			String content = "";
			if ((contentbits % 8)  == 0) {
				content = contenttemp.toString();
			} else {
				content = contenttemp.substring(0, contentbits);
			}
			
			int start = 0; // start pointer of huff code while decoding
			int end = shortest; // end pointer of huff code while decoding
			int completebits = 0;
			while (completebits < contentbits) {
				String hufftemp = content.substring(start, end);
				if (decodemap.containsKey(hufftemp)) {
					writeorigin.write(decodemap.get(hufftemp)); //write origin content to file
					completebits += (end - start);
					start = end;
					end = start + shortest;
				} else {
					end += 1;
				}
			}
			readhuff.close();
			writeorigin.close();
			return true;
		} catch (IOException e) {
			return false;
			//e.printStackTrace();
		}
	}
	
}
