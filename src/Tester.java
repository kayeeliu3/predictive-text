import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class runs tests on the Dictionary class and also
 * implements a text-based menu for the Predictive Text Application
 * @author Becky Tyler (2461535), Oliver Shearer (2455913)
 * @version 1.0 (17 March 2022)
 */
public class Tester
{
	// Create object references to the dictionaries
	// dictionary = the current dictionary in the selected language
	// dict_en = English dictionary, dict_it = Italian dictionary
	Dictionary dictionary, dict_en, dict_it;

	// Object reference to the prediction
	Prediction prediction;

	/**
	 * Default constructor
	 * @author Becky Tyler (2461535)
	 */
	public Tester()
	{
		// Create the new dictionaries in each language
		dict_en = new Dictionary(Dictionary.Language.ENGLISH);
		dict_it = new Dictionary(Dictionary.Language.ITALIAN);

		// Create a new prediction
		prediction = new Prediction();

		// Select the current dictionary
		if (prediction.getLanguage().equals(Dictionary.Language.ENGLISH))
			dictionary = dict_en;
		else
			dictionary = dict_it;
	}

	/**
	 * Main method to run tests from
	 */
	public static void main(String[] args)
	{
		Tester tester = new Tester();
		tester.processUserInput();

		// Run automated tests
//		tester.runAutomatedTests();

		// Test spell checker
//		tester.testSpellCheck();
	}

	/**
	 * Displays the menu to the user and asks them what option they would like to do and validates it and sends it to be processed
	 *
	 * @return userChoice which is an integer representing the option that the user chose
	 * @author Oliver Shearer (2455913), minor updates by Becky Tyler (2461535)
	 */
	private int displayMenu()
	{
		boolean validChoiceProvided = false;
		int userChoice = 0;

		while (!validChoiceProvided)
		{
			System.out.println("\n----------MENU----------");
			System.out.println("1. Enter A Word Or Phrase");
			System.out.println("2. Change Limit Of Word Suggestions (Currently: " +
					   prediction.getMaxCompletions() + ")");
			if (prediction.getAddWord())
				System.out.println("3. Add New Words To The Dictionary (Setting: ON)");
			else
				System.out.println("3. Add New Words To The Dictionary (Setting: OFF)");
			if (prediction.getLanguage().equals(Dictionary.Language.ENGLISH))
				System.out.println("4. Change Language to Italian");
			else
				System.out.println("4. Change Language to English");
			System.out.println("5. Remove Word From Dictionary");
			System.out.println("6. Display The Dictionary");
			System.out.println("7. Save Dictionary");
			System.out.println("8. Run Completions for Uncompleted Word File");
			System.out.println("0. Exit");
			userChoice = getInt("Enter Menu Option (0-8): ");

			if ((userChoice >= 0) && (userChoice <= 8))
			{
				validChoiceProvided = true;
			}
			else
			{
				System.out.println("\nSorry, that is not a valid choice.");
			}
        }

		return userChoice;
	}

	/**
	 * Will process the input entered by the user at the menu stage and will run a specific method depending on what number the user entered.
	 */
	private void processUserInput()
	{

		boolean exitChoiceSelected = false;

		while (!exitChoiceSelected)
		{
			int userChoice = 0;
			userChoice = displayMenu();

			switch(userChoice) {

			case 1:
				enterSentence();
				break;

			case 2:
				updateWordLimit();
				break;

			case 3:
				if(prediction.getAddWord() == false)
				{
					prediction.setAddWord(true);
					System.out.println("\nAdd Word Setting Has Been Turned ON");
				}
				else
				{
					prediction.setAddWord(false);
					System.out.println("\nAdd Word Setting Has Been Turned OFF");
				}
				break;

			case 4:
				// Updated by BT to change the dictionary
				if(prediction.getLanguage().equals(Dictionary.Language.ENGLISH))
				{
					prediction.setLanguage(Dictionary.Language.ITALIAN);
					dictionary = dict_it;
					System.out.println("\nLanguage Has Been Switched To Italian.");
				}
				else
				{
					prediction.setLanguage(Dictionary.Language.ENGLISH);
					dictionary = dict_en;
					System.out.println("\nLanguage Has Been Switched To English.");
				}
				break;

			case 5:
				deleteUserEnteredWord();
				break;

			case 6:
				displayTest();
				break;

			case 7:
				boolean saved = dictionary.saveToFile(prediction.getLanguage());
				if(saved == true){
					System.out.println("Save Was Successful");
				}
				break;

			case 8:
				dictionary.benchmark(prediction.getLanguage());
				break;

			case 0:
				System.out.println("\nGoodbye!\n");
				exitChoiceSelected = true;
				break;

			default:
				System.out.println("\nInvalid Input, Try Again");
				displayMenu();

			}
		}
	}

	/**
	* Method to automatically run the tests as defined in the test plan
	* @author Becky Tyler (2461535)
	*/
	public void runAutomatedTests()
	{
		// Test adding words to the dictionary trie
		System.out.println("Testing the add word method");
		System.out.println("===========================");
		this.runAddTests();

		// Test deleting a word from the dictionary entered by the user
		System.out.println("Testing the delete method");
		System.out.println("=========================");
//		tester.deleteUserEnteredWord();
		this.runDeleteTest();

		// Test finding a word
		System.out.println("Testing the find method");
		System.out.println("=======================");
		this.findTest("ten");

		// Test displaying the dictionary
		System.out.println("Testing the display method");
		System.out.println("==========================");
		this.displayTest();

		// Test the prediction to get some completions
//		tester.predictTestv1("t");
//		tester.predictTestv1("i");
	}

	/**
	 * Method to run tests to add words to the dictionary trie
	 * @author Becky Tyler (2461535)
	 */
	public void runAddTests()
	{
		// Get the root of the dictionary trie
		WordNode root = dictionary.getRoot();

		// Try printing an empty dictionary
		System.out.println("\nPRINTING AN EMPTY DICTIONARY TRIE:");
		dictionary.printDictionary(root, "");

		// Add a word to the dictionary and print the trie
		System.out.println("\nADDING WORD 'to' TO THE DICTIONARY TRIE:");
		dictionary.addWord("to");
		dictionary.printDictionary(root, "");

		// Add some more words - these are the words from the Wikipedia trie example
		System.out.println("\nADDING MORE WORDS TO THE DICTIONARY TRIE:");
		dictionary.addWord("tea");
		dictionary.addWord("ted");
		dictionary.addWord("ten");
		dictionary.addWord("A");
		dictionary.addWord("I");
		dictionary.addWord("in");
		dictionary.addWord("inn");
		dictionary.printDictionary(root, "");

		// Test deleting a node and adding it back in again
		System.out.println("\nDELETING THE WORD 'in' FROM THE DICTIONARY TRIE:");
		WordNode inNode = dictionary.findNode("in");
		if (dictionary.deleteNode("in", root))
			System.out.println("'in' has been removed from the dictionary.");
		else
			System.out.println("Word 'in' does not exist in the dictionary.");
		dictionary.printDictionary(inNode, "in");
		System.out.println("\nADDING THE WORD 'in' BACK INTO THE DICTIONARY TRIE:");
		dictionary.addWord("in");
		dictionary.printDictionary(inNode, "in");

		// Test updating the frequency for a word
		System.out.println("\nINCREASING THE FREQUENCY OF 'in' BY 4:");
		dictionary.updateFrequency("in", 4);
		dictionary.printDictionary(inNode, "in");
		System.out.println("\nINCREASING THE FREQUENCY OF 'in' BY ANOTHER 3:");
		dictionary.updateFrequency("in", 3);
		dictionary.printDictionary(inNode, "in");
	}

	/**
	 * Method to test deleting a word
	 * @author Becky Tyler (2461535)
	*/
	public void runDeleteTest()
	{
		System.out.println("Remove word 'ten' from the Dictionary");
		if (dictionary.deleteNode("ten", dictionary.getRoot()))
			System.out.println("The word 'ten' has been removed from the dictionary.");
		else
			System.out.println("Word 'ten' does not exist in the dictionary.");
		dictionary.printDictionary(dictionary.getRoot(), "");
	}

	/**
	 * Method to test deleting a word entered by the user
	 * @author Oliver Shearer (2455913), updated by Becky Tyler (2461535)
	 */
	public void deleteUserEnteredWord()
	{
		// Ask the user for the word to delete
		String word = this.getString("Enter word to delete: ");

		// Make sure the word entered contains some letters
		if(dictionary.wordEnteredIsNull(word))
		{
			System.out.println("\n");
		}

		// Try removing the node for the word
		else
		{
			if (dictionary.deleteNode(word, dictionary.getRoot()))
				System.out.println(word + " has been removed from the dictionary.");
			else
				System.out.println("Word '" + word + "' does not exist in the dictionary.");
			// dictionary.findNode(word, dictionary.getRoot());
		}
	}

	/**
	 * Method to display the full dictionary trie from the root node
	 * @author Becky Tyler (2461535)
	 */
	public void displayTest()
	{
		String startWord = this.getString("Enter letter(s) to display the dictionary from, or press ENTER for full dictionary: ").trim();

		if (startWord.equals(""))
		{
			System.out.println("DISPLAYING THE FULL DICTIONARY:");
			System.out.println("===============================");
			dictionary.displayDictionary(dictionary.getRoot(), "");
			System.out.println("===============================");
			System.out.println();
		}
		else  // Display the part of the dictionary with words starting with startWord
		{
			// Find the starting node;
			WordNode startNode = dictionary.findNode(startWord);

			if (startNode != null)
			{
				System.out.println("DISPLAYING THE DICTIONARY: Words beginning with " + startWord);
				System.out.println("==========================");
				// Display the dictionary from the start word
				dictionary.displayDictionary(startNode, startWord);
				System.out.println("==========================\n");
			}
			else
				System.out.println("Word Does Not Exist In the Dictionary");
		}
	}

	/**
	 * Method to test the findNode method to search for a word and its node
	 * @param wordToFind The word to find
	 * @author Becky Tyler (2461535)
	 */
	public void findTest(String wordToFind)
	{
		// Search for a word
		System.out.println("SEARCHING FOR THE WORD: " + wordToFind);
		System.out.println("=======================");
		WordNode foundNode = dictionary.findNode(wordToFind);
		if (foundNode == null)
			System.out.println("Node '" + wordToFind + "' not found.");
		else
			System.out.println("Node '" + wordToFind + "' found!\n" + foundNode.printInfo());
		System.out.println();
	}

	/**
	 * Method to test the prediction method
	 * @param textToComplete The text to predict/complete
	 * @author Becky Tyler (2461535)
	 */
	public void predictTestv1(String textToComplete)
	{
		System.out.println("\nPREDICTING THE TEXT: " + textToComplete);
		System.out.println("====================");
		Map<String, Integer> completions =
				prediction.predictTextv1(dictionary, textToComplete);
		if (completions != null)
		{
			System.out.println("Possible completions for " + textToComplete + ":");
			for (String text : completions.keySet())
			{
				System.out.println(text + " (" + completions.get(text) + ")");
			}
			System.out.println();
		}
	}

	/**
	 * Method to obtain a word or phrase from the user and get the completions
	 * @author Oliver Shearer (2455913), updated by Becky Tyler (2461535)
	 */
	public void enterSentence()
	{
		// Clear the completions list - FIX by BT 28/03/22;
		prediction.resetCompletions();

		String textToComplete = this.getString("Enter a word or phrase: ");

		boolean empty = dictionary.wordEnteredIsNull(textToComplete);

		if(empty == true)
		{
			return;
		}

		// Remove multiple spaces from the words in the phrase - FIX by BT 30/3/22
		// String[] sentence = textToComplete.split(" ");
		String[] sentence = textToComplete.split("\\s+");

		for(int i = 0 ; i < sentence.length; i++)
		{
			// Get the completion for the last word in the phrase
			if(sentence.length - 1 == i)
			{
				WordNode foundTextNode = dictionary.findNode(sentence[i]);

				if (foundTextNode != null && foundTextNode.getIsWord() == true) {
                    
                    dictionary.updateFrequency(sentence[i], 1);
                    for (int v = 0; v < sentence.length; v++) {
                        String phrase = "";
                        for (int p = v + 1; p < sentence.length; p++) {
                            phrase = phrase + sentence[p] + " ";
                        }
                        WordNode word = dictionary.findNode(sentence[v]);
                        if (word.getIsWord() == true && word.getPhrases().contains(phrase) == false) {
                            word.getPhrases().add(phrase);
                        }
                    }
                }

				// Check the partial word was found - FIX by BT 30/3/22
				if (foundTextNode == null || foundTextNode.getNextNodes().isEmpty() == true) {
                    System.out.println("No Completions Were Found In The Dictionary \n");
                    if (prediction.getAddWord() == true) {
                        boolean added = dictionary.addWord(sentence[i]);
                        if (added == true) {
                            System.out.println("*" + sentence[i]
                                    + " Is Not Recognised But Has Been Added Due To AddWord Setting Being On \n");
                        }
                    }
                    dictionary.updateFrequency(sentence[i], 1);
                    getPhrase(foundTextNode, textToComplete);
                    return;
                }

				for (String word : prediction.getCompletions(foundTextNode, textToComplete)) {
					System.out.println(word);
				}
				getPhrase(foundTextNode, textToComplete);

			} else {
                // Add new words to the dictionary if the setting is on - update by BT 29/03/22
                if (prediction.getAddWord() == true) {
                    boolean added = dictionary.addWord(sentence[i]);
                    if (added == true) {
                        System.out.println("*" + sentence[i]
                                + " Is Not Recognised But Has Been Added Due To AddWord Setting Being On \n");
                    }
                }

                // Increase the frequency of the times this word has been used
                dictionary.updateFrequency(sentence[i], 1);
            }
		}

	}

	/**
     * This method will loop through the phrases and will display them to the user if there is any and will display that there arent any if the array is empty.
     * @param foundTextNode which is the node that the word is stored in
     * @param sentence which is the sentence that the user typed in and is stored so it can be used to display it with the phrases added on
     */
    public void getPhrase(WordNode foundTextNode, String sentence) {
        System.out.println("\n\n Phrases:");
        if (foundTextNode != null) {
            foundTextNode.getPhrases().remove("");
            if (foundTextNode.getIsWord() == true && foundTextNode.getPhrases().isEmpty() == false) {
                for (String phrase : foundTextNode.getPhrases()) {
                    phrase.trim();
                    sentence.trim();
                    System.out.println("\n" + sentence + " " + phrase);
                }
                return;
            }
            System.out.println("\n There Are No Phrases To Continue From This Word:");
            return;
        }
        System.out.println("\n There Are No Phrases To Continue From This Word:");
    }


	/**
	 * This method will change the word suggestion limit to whatever the user enters as long as it is between 1 and 100 inclusive
	 */
	public void updateWordLimit()
	{
		int limit = getInt("Enter The New Limit Of Word Suggestions: ");

		if(limit < 1 || limit > 100)
		{
			System.out.println("Number Entered Is Out Of Range");
		}
		else
		{
			prediction.setMaxCompletions(limit);
		}
	}


	/**
	 * This method will parse a user prompt and expects a integer in return so the number entered will be taken in and sent back to where this method was called
	 * @param userPrompt is the question to be asked to the user
	 * @return an int that the user enters
	 */
	public int getInt(String userPrompt)
	{
		Scanner s = new Scanner(System.in);

		System.out.print("\n" + userPrompt);
		while (!s.hasNextInt())
		{
			s.next();
			System.out.print(userPrompt);
		}

		int num = s.nextInt();
		return num;
	}

	/**
	 * Method to read in a string from the user
	 * @param userPrompt Message to prompt the user for input
	 * @return The String entered by the user
	 * @author Becky Tyler (2461535)
	 */
	public String getString(String userPrompt)
	{
		Scanner s = new Scanner(System.in);
		System.out.print("\n" + userPrompt);
		String userInput = s.nextLine();

		// Check that the input doesn't contain special characters
		Pattern p = Pattern.compile("[^a-z ]", Pattern.CASE_INSENSITIVE);
		Matcher match = p.matcher(userInput);
		while (match.find())
		{
			System.out.println("Sorry, numbers and special characters are not allowed.");
			System.out.print("\n" + userPrompt);
			userInput = s.nextLine();
			match = p.matcher(userInput);
		}

		return userInput.trim();
	}

	/**
	 * Method to test the spell checker
	 * @author Becky Tyler (2461535)
	 */
	public void testSpellCheck()
	{
		SpellChecker spellcheck = new SpellChecker(dictionary, prediction);
		String phraseToCheck = getString("Enter a phrase: ");
		ArrayList<String> output = spellcheck.checkSpelling(phraseToCheck);
		for (String newline : output)
		{
			System.out.println(newline);
		}
	}

}
