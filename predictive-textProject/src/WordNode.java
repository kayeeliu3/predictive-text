import java.util.HashMap;
import java.util.Map;

/**
 * This class stores the data for a node in the dictionary Trie
 * 
 * @author Becky Tyler (2461535)
 * @version 1.0 (16 March 2022)
 */

public class WordNode
{
	
		// Define class variables/fields
		private String  letters;	// Letters in the word prefix
		private boolean isWord;		// Are the characters in the letters field a proper word? 
		private int     frequency;	// Number of times word has been used (if isWord is true)
		
		// Define a map to hold the references to the child nodes, indexed on the letters
		Map<String, WordNode> nextNodes;
		
		/**
		 * Default constructor
		 */  
		public WordNode()
		{
			// Initialise field values
			letters    = "";
			isWord     = false;
			frequency  = 0;
			
			// Initialise the hash map for the next nodes
			nextNodes  = new HashMap<String, WordNode>();
		}
		
		/**
		 * Alternative constructor to set fields to given values.
		 * @param letters The word prefix letters stored in the node
		 * @param isWord  Do the letters make a proper word
		 * @param frequency  The number of times this word has been used (if isWord is true)
		 */
		public WordNode(String letters, boolean isWord, int frequency)
		{
			// Set data value to value provided       
			this.letters = letters;
			this.frequency = frequency;
			this.isWord = isWord;
			
			// Initialise the hash map for the next nodes
			nextNodes  = new HashMap<String, WordNode>();
		}

		
		/**
		 * Method to get the letters in the node
		 * @return letters The letters in the node
		 */
		public String getLetters()
		{
			return this.letters;
		}

		/**
		 * Method to get the value of the 'isWord' field 
		 * @return isWord Whether the letters are a valid word or not
		 */
		public boolean getIsWord()
		{
			return this.isWord;
		}
		
		/**
		 * Method to get the frequency of the word (if isWord is true)
		 * @return frequency The number of times the user has used this word
		 */
		public int getFrequency()
		{
			return this.frequency;
		}
		
		/**
		 * Method to get the map of child nodes
		 * @return nextNodes The map containing the references to the child nodes
		 */
		public Map<String, WordNode> getNextNodes()
		{
			return this.nextNodes;
		}
		
		/**
		 * Method to set the letters for the node
		 * @param letters  The new letters for the node
		 */
		public void setLetters(String letters)
		{
			this.letters = letters;
		}

		/**
		 * Method to set the 'isWord' field (whether or not the letters make a proper word)
		 * @param isWord The new value for isWord
		 */
		public void setIsWord(boolean isWord)
		{
			this.isWord = isWord;
		}
		
		/**
		 * Method to set the frequency of the word (if isWord is true)
		 * @param frequency  The new frequency of the word
		 */
		public void setFrequency(int frequency)
		{
			this.frequency = frequency;
		}
		
		/**
		 * Method to set the map of references to the child nodes
		 * @param nextNodes The map of references to the child nodes
		 */
		public void setNextNodes(Map<String, WordNode> nextNodes)
		{
			this.nextNodes = nextNodes;
		}
		
		/**
		 * Method to get the information about the node ready for printing
		 * @return String containing the letters, if these are a word, and the frequency
		 */
		public String printInfo()
		{
			String printInfo = "";
			printInfo = "Letters: " + this.letters + 
					    ", IsWord: " + this.isWord + 
					    ", Frequency: " + this.frequency;	
			return printInfo;
		}
	}

