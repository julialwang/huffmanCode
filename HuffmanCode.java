// Julia Wang
// CSE 143 A with Professor Schafer
// Section AG with Mino Nakura
// Assignment #8

// HuffmanCode creates a compressor to compress a text file. It can also provide any intermediate
// steps that a client wishes, such as creating a code file for the text or decompressing a
// precompressed message using the compression code to regenerate the original message. This class
// can only be used on .txt files.

import java.io.*;
import java.util.*;

public class HuffmanCode {
	
	// stores the codes of each character
	private HuffmanNode overallNode;

	// pre:  requires the number of times each ASCII character is used in the text file in an
	//		 integer array
	// post: creates and stores a code for each letter
	public HuffmanCode(int[] frequencies) {
	
		Queue<HuffmanNode> sorted = new PriorityQueue<>();
		
		for (int i = 0; i < frequencies.length; i++) {
			
			if (frequencies[i] != 0) {
				sorted.add(new HuffmanNode(i, frequencies[i]));
			}
		}
		
		while (sorted.size() > 1) {
		
			HuffmanNode first = sorted.remove();
			HuffmanNode second = sorted.remove();
			
			sorted.add(new HuffmanNode(first, second, first.frequency + second.frequency, -1));
		
		}
		
		overallNode = sorted.remove();
		
	}
	
	// pre:  requires a scanner for a file of ASCII characters and codes in standard format
	//		 (where the ASCII value for the character appears on the line before its corresponding
	//		 code)
	// post: stores the code for each letter
	public HuffmanCode(Scanner input) {
		
		overallNode = new HuffmanNode(-1, -1);
		
		while (input.hasNext()) {
			
			int asciiValue = Integer.parseInt(input.nextLine());
			String code = input.nextLine();

			overallNode = nodeCreator(overallNode, asciiValue, code, 0);
			
		}
		
	}
	
	// pre:  requires a HuffmanNode, the ASCII value of the letter, a string for the letter's code,
	//		 and the numeric location in the code
	// post: stores the code for each letter
	private HuffmanNode nodeCreator(HuffmanNode node, int letter, String code, int index) {
		
		if (index == code.length()) {
			
			return new HuffmanNode(letter, -1);
		
		}
		
		else if (code.charAt(index) == '0') {
				
				if (node.left == null) {
					node.left = new HuffmanNode(-1, -1);
				}
				
				node.left = nodeCreator(node.left, letter, code, index + 1);
				
		}
			
		else {
			
			if (node.right == null) {
				node.right = new HuffmanNode(-1, -1);
			}
			
			node.right = nodeCreator(node.right, letter, code, index + 1);
			
		}
		
		return node;
		
}
	
	// pre:  requires a PrintStream to print the current codes to a file
	// post: prints out the current huffman codes into a file in standard format
	//		 (where the ASCII value for the character appears on the line before its corresponding
	//		 code)
	public void save(PrintStream output) {
	
		save(output, overallNode, "");
		
	}
		
	// pre:  requires a PrintStream to print the current huffman codes to a file, a HuffmanNode,
	//		 and the code that is being decompressed
	// post: prints out the current huffman codes into a file in standard format
	//		 (where the ASCII value for the character appears on the line before its corresponding
	//		 code)
	private void save(PrintStream output, HuffmanNode node, String code) {
		
		if (node.letter != -1) {
			output.println(node.letter);
			output.println(code);
		}
		
		else {
			save(output, node.left, code + "0");
			save(output, node.right, code + "1");
		}
	}
	
	// pre:  requires a BitInputStream of a .code file and a PrintStream to print
	//		 the decompressed text to a file
	// post: prints out the decompressed text to a file
	public void translate(BitInputStream input, PrintStream output) {
		
		while (input.hasNextBit()) {
			
			translate(input, output, overallNode);
		
		}
	}
		
	// pre:	 requires a BitInputStream of a .code file and a PrintStream to print
	//		 the decompressed text to a file, a PrintStream to print
	//		 the decompressed letters to a file, and a HuffmanNode
	// post: prints out the decompressed text to a file
	private void translate(BitInputStream input, PrintStream output, HuffmanNode node) {
		
		if (node.letter != -1) {
			output.write(node.letter);
		}
		
		else {
				
			if (input.nextBit() == 0) {
				translate(input, output, node.left);
			}
			
			else {
				translate(input, output, node.right);
			}
		}

	}

	// HuffmanNode is a private class that stores a character, frequency of the character, 
	// and pathways to the next characters. The class can also compare the current character
	// to another HuffmanNode
	private static class HuffmanNode implements Comparable<HuffmanNode> {
	
		// stores the pathway to the next character
		public HuffmanNode right;
		
		// stores the pathway to the next character
		public HuffmanNode left;
		
		// stores the numeric ASCII value of the character
		public int letter;
		
		// stores the character's frequency
		public int frequency;
		
		// pre:  requires the ASCII value of the character and the frequency
		//		 of the character
		// post: stores the data of the character based on the input information
		//		 and sets the pathways to empty
    	public HuffmanNode(int letter, int frequency) {
    		
    		this(null, null, frequency, letter);
    	
    	}
    	
    	// pre:  requires the ASCII value of the character, the frequency
		//		 of the character, and the pathways to the next characters
		// post: stores the data of the character based on the input information
    	public HuffmanNode(HuffmanNode left, HuffmanNode right, int frequency, int letter) {
    		
    		this.letter = letter;
    		this.frequency = frequency;
    		this.left = left;
    		this.right = right;
    	
    	}
	
    	// pre:  requires a HuffmanNode to be compared to
    	// post: compares the frequency of this HuffmanNode to the other
    	//		 and returns a negative number if this frequency is lower than the other, 
    	//		 0 if they are the same, and a positive number if this frequency is higher
    	//		 than the other
    	public int compareTo(HuffmanNode node) {
    		
    		return frequency - node.frequency;
    		
    	}	
	}
}
