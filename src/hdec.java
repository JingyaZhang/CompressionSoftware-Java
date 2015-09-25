
/* Zhang Jingya  CS610 PP 2508 */

/* function: Implement Huffman Decoding of a file. */

import java.io.*;

public class hdec {
	
	public static void main(String[] args) {
		String path = "./name";
		if (args.length == 1) {
			String newname = args[0].substring(0, args[0].length() - 4);
			File recovery = new File(path.replace("name", newname));
			try {
				recovery.createNewFile();
				
				String originfile = recovery.getName();
				WriteFile recover = new WriteFile();
				if (recover.writeOriginalFile(args[0], originfile)) {
					System.out.println("Success!!");
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
