import java.awt.event.*;
import javax.swing.*;
import javax.swing.JTabbedPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.stream.IntStream;

/**
 * Menu class containing all methods in regards to GUI
 */
public class Menu extends JPanel implements ActionListener {
    JLabel savedLabel, addWordLabel, spellingLabel, predictLabel, wordLimitLabel, removeWordLabel, searchWordLabel,
            currentLanguageLabel, currentAddSettingLabel;
    JTextField predictTextField, wordLimitTextField, removeWordTextField, searchWordTextField;
    JTextArea predictTextArea, wordLimitTextArea, searchWordTextArea;
    JButton addButton, spellCheckButton, predictButton, wordLimitButton, removeWordButton, searchWordButton,
            saveDictButton, changeLanguageButton, addWordButton;
    JRadioButton addOnRadio, addOffRadio;
    JScrollPane scroll, predictScroll;
    ButtonGroup group;

    // Reference to the current two dictionaries
    Dictionary dictionary, dict_en, dict_it;
    // Reference to the current prediction object
    Prediction prediction;

    /**
     * Menu default constructor to set up tabbed pane containing all panels
     */
    public Menu() {
        super(new GridLayout(1, 1));

        JTabbedPane tabbedPane = new JTabbedPane();
        JComponent panel1 = makeManualPanel();
        tabbedPane.addTab("How To Use", panel1);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent panel2 = makePredictionPanel();
        tabbedPane.addTab("Word Prediction!", panel2);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        JComponent panel3 = changeSettingPanel();
        tabbedPane.addTab("Settings", panel3);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        JComponent panel4 = removeWordPanel();
        tabbedPane.addTab("Remove Words", panel4);
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

        JComponent panel5 = searchWordPanel();
        tabbedPane.addTab("Search For Word", panel5);
        tabbedPane.setMnemonicAt(4, KeyEvent.VK_5);

        add(tabbedPane);
        // allows scrolling tab usage
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

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
     * User panel containing instructions on using predictions
     */
    protected JComponent makeManualPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea text = new JTextArea();

        String instructions = " You can navigate through all the tabs above by clicking on them. \n\n In the word prediction screen, enter a sentence and once the predict button is clicked, a list of possible predictions will appear on the screen. \n You can also check for spelling mistakes, and words are added if there is no such entry in the dictionary and the setting to add words is enabled.\n You can then save the dictionary so that any added/removed words are saved for the next time the program is loaded. \n\n The setting screen allows you to choose the maximum number of words shown as predictions (default is 7 words), to change the language, and also to toggle the \n add word setting. \n\n You can also remove words, which upon entering a word, the program will display if the word has been successfully deleted or the word has not been found in the \n dictionary.\n\n Users can also search for a specific words, as well as displaying the whole dictionary when the search button is clicked with the text field empty.\n Note that the frequency count of the words used are shown in brackets after the words as shown below. ";
        text.append(instructions);
        text.setEditable(false);

        panel.add(text, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Panel for word predictions
     */
    protected JComponent makePredictionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        spellingLabel = new JLabel("-");
        spellingLabel.setBounds(660, 40, 250, 20);
        saveDictButton = new JButton("Save Current Dictionary");
        saveDictButton.setBounds(270, 70, 170, 20);
        savedLabel = new JLabel("-");
        savedLabel.setBounds(660, 70, 250, 20);
        predictLabel = new JLabel("Please enter text below: ");
        predictLabel.setBounds(10, 9, 150, 20);
        predictTextField = new JTextField();
        predictTextField.setBounds(10, 40, 615, 20);
        predictButton = new JButton("Predict!");
        predictButton.setBounds(10, 70, 120, 20);
        spellCheckButton = new JButton("Check spelling");
        spellCheckButton.setBounds(140, 70, 120, 20);
        predictTextArea = new JTextArea();
        predictTextArea.setEditable(false);

        predictScroll = new JScrollPane(predictTextArea);
        predictScroll.setBounds(10, 100, 1160, 420);
        predictScroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        predictButton.addActionListener(this);
        spellCheckButton.addActionListener(this);
        saveDictButton.addActionListener(this);

        panel.add(spellingLabel);
        panel.add(spellCheckButton);
        panel.add(saveDictButton);
        panel.add(predictLabel);
        panel.add(predictTextField);
        panel.add(predictButton);
        panel.add(predictScroll);
        panel.add(savedLabel);

        return panel;
    }

    /**
     * Panel for prediction settings
     */
    protected JComponent changeSettingPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(null);

        // Increase word limit range to 1-100
        Integer[] wordLimitOptions = IntStream.of(IntStream.rangeClosed(1, 100).toArray()).boxed()
                .toArray(Integer[]::new);
        JComboBox<Integer> wordLimitBox = new JComboBox<>(wordLimitOptions);
        wordLimitBox.setBounds(80, 50, 50, 20);
        wordLimitButton = new JButton("Set Word Limit");
        wordLimitButton.setBounds(140, 50, 120, 20);
        wordLimitLabel = new JLabel("Default Word Limit: 7");
        wordLimitLabel.setBounds(270, 50, 150, 20);

        changeLanguageButton = new JButton("Change language of dictionary");
        changeLanguageButton.setBounds(170, 110, 210, 20);
        String[] language = { "English", "Italian" };
        JComboBox<String> languageComboBox = new JComboBox<>(language);
        languageComboBox.setBounds(80, 110, 80, 20);
        currentLanguageLabel = new JLabel("Current Language: English");
        currentLanguageLabel.setBounds(390, 110, 200, 20);

        JLabel text = new JLabel("Toggle Add Word Setting: ");
        text.setBounds(80, 170, 150, 20);
        addOnRadio = new JRadioButton("On");
        addOffRadio = new JRadioButton("Off");
        addOnRadio.setBounds(80, 190, 200, 20);
        addOffRadio.setBounds(80, 210, 200, 20);
        addOffRadio.setSelected(true);
        group = new ButtonGroup();
        group.add(addOnRadio);
        group.add(addOffRadio);
        addOnRadio.addActionListener(this);
        addOffRadio.addActionListener(this);
        currentAddSettingLabel = new JLabel("Off");
        currentAddSettingLabel.setBounds(235, 170, 50, 20);

        panel.add(text);
        panel.add(wordLimitBox);
        panel.add(wordLimitButton);
        panel.add(wordLimitLabel);
        panel.add(languageComboBox);
        panel.add(changeLanguageButton);
        panel.add(currentLanguageLabel);
        panel.add(addOnRadio);
        panel.add(addOffRadio);
        panel.add(currentAddSettingLabel);

        wordLimitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedNumber = "Word Limit: " + wordLimitBox.getItemAt(wordLimitBox.getSelectedIndex());
                wordLimitLabel.setText(selectedNumber);
                prediction.setMaxCompletions(wordLimitBox.getItemAt(wordLimitBox.getSelectedIndex()));
            }
        });

        changeLanguageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedLanguage = "Current Language: "
                        + languageComboBox.getItemAt(languageComboBox.getSelectedIndex());
                currentLanguageLabel.setText(selectedLanguage);
                String language = languageComboBox.getItemAt(languageComboBox.getSelectedIndex());
                if (language.equals("English")) {
                    prediction.setLanguage(Dictionary.Language.ENGLISH);
                    dictionary = dict_en;
                } else {
                    prediction.setLanguage(Dictionary.Language.ITALIAN);
                    dictionary = dict_it;
                }

            }
        });

        return panel;
    }

    /**
     * Panel for removing words
     */
    protected JComponent removeWordPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JPanel topPanel = new JPanel();
        JLabel text = new JLabel("Enter word to remove: ");

        removeWordLabel = new JLabel("-", JLabel.CENTER);
        removeWordTextField = new JTextField();
        removeWordTextField.setColumns(50);
        removeWordButton = new JButton("Remove");

        topPanel.add(text);
        topPanel.add(removeWordTextField);
        topPanel.add(removeWordButton);
        removeWordButton.addActionListener(this);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(removeWordLabel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Panel for searching for a specific word
     */
    protected JComponent searchWordPanel() {
        JPanel topPanel = new JPanel();
        JPanel panel = new JPanel();
        JPanel textAreaPanel = new JPanel();

        JPanel panelHolderSouth = new JPanel();

        panel.setLayout(new BorderLayout());

        JLabel text = new JLabel("Enter word to search for in dictionary: ", JLabel.CENTER);

        searchWordTextField = new JTextField();
        searchWordTextField.setColumns(20);
        searchWordButton = new JButton("Search");
        searchWordButton.addActionListener(this);
        searchWordTextArea = new JTextArea();

        searchWordTextArea.setEditable(false);
        scroll = new JScrollPane(searchWordTextArea);
        scroll.setPreferredSize(new Dimension(600, 480));
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        topPanel.add(text);
        topPanel.add(searchWordTextField);
        topPanel.add(searchWordButton);

        textAreaPanel.add(scroll);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(textAreaPanel, BorderLayout.CENTER);
        panel.add(panelHolderSouth, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Method to set up the frame and initialise menu object
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Prediction");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 600);

        frame.add(new Menu(), BorderLayout.CENTER);
        frame.setVisible(true);
    }

    /**
     * Contains action listener events and functions
     * @param ActionEvent The event that has occurred
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        // Reset the information labels
        spellingLabel.setText("-");
        // addWordLabel.setText("-");
        savedLabel.setText("-");

        if (e.getSource() == predictButton) {
            predictText();
            predictTextArea.select(0, 0);

        } else if (e.getSource() == removeWordButton) {
            deleteUserEnteredWord();

        } else if (e.getSource() == searchWordButton) {
            displayText();
            searchWordTextArea.select(0, 0);

        } else if (e.getSource() == saveDictButton) {
            savedLabel.setText("-");
            if (prediction.getLanguage().equals(Dictionary.Language.ENGLISH)) {
                if (dict_en.saveToFile(Dictionary.Language.ENGLISH)) {
                    String text = "Saved!";
                    savedLabel.setText(text);
                } else {
                    String text = "Save unsuccessful";
                    savedLabel.setText(text);
                }
            } else {
                if (dict_it.saveToFile(Dictionary.Language.ITALIAN)) {
                    String text = "Saved!";
                    savedLabel.setText(text);
                } else {
                    String text = "Save unsuccessful";
                    savedLabel.setText(text);
                }
            }

        } else if (e.getSource() == spellCheckButton) {
            this.getSpellings();

        } else if (e.getSource() == addOnRadio) {
            getSelectedButton();

        } else if (e.getSource() == addOffRadio) {
            getSelectedButton();
        }
    }

    /**
     * This method will read in the option chosen by the user in the radio buttons and will change the field to on or off
     */
    public void getSelectedButton() {
        for (Enumeration<AbstractButton> buttons = group.getElements(); buttons.hasMoreElements();) {
            AbstractButton button = buttons.nextElement();
            if (button.isSelected()) {
                if (button.getText() == "On") {
                    currentAddSettingLabel.setText("On");
                    turnOnSetting();
                } else {
                    currentAddSettingLabel.setText("Off");
                    turnOffSetting();
                }
            }
        }
    }

    /**
     * This method will find all the predictions for a word entered by the user and will display them to the user.
     * It will also add the word if no completions can be found (if addWord setting is on).
     * And it will take note of the sentences entered by the user and will store them as a phrase so when it is typed again, it will appear as a phrase underneathe the potential completions.
     */
    public void predictText() {
        predictTextArea.setText("");
        String textToComplete = predictTextField.getText();

        prediction.resetCompletions();

        boolean empty = dictionary.wordEnteredIsNull(textToComplete);

        if (empty == true) {
            predictTextArea.setText("Word Entered Is Empty\n");
            return;
        }

        // Remove multiple spaces from the words in the phrase - FIX by BT 30/3/22
        // String[] sentence = textToComplete.split(" ");
        String[] sentence = textToComplete.split("\\s+");

        for (int i = 0; i < sentence.length; i++) {
            // Get the completion for the last word in the phrase
            if (sentence.length - 1 == i) {
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
                    predictTextArea.setText("No Completions Were Found In The Dictionary \n");
                    if (prediction.getAddWord() == true) {
                        boolean added = dictionary.addWord(sentence[i]);
                        if (added == true) {
                            predictTextArea.append("*" + sentence[i]
                                    + " Is Not Recognised But Has Been Added Due To AddWord Setting Being On \n");
                        }
                    }
                    dictionary.updateFrequency(sentence[i], 1);
                    getPhrase(foundTextNode, textToComplete);
                    return;
                }

                for (String word : prediction.getCompletions(foundTextNode, textToComplete)) {
                    predictTextArea.append(word + "\n");
                }
                getPhrase(foundTextNode, textToComplete);

            } else {
                // Add new words to the dictionary if the setting is on - update by BT 29/03/22
                if (prediction.getAddWord() == true) {
                    boolean added = dictionary.addWord(sentence[i]);
                    if (added == true) {
                        predictTextArea.append("*" + sentence[i]
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
        predictTextArea.append("\n\n Phrases:");
        if (foundTextNode != null) {
            foundTextNode.getPhrases().remove("");
            if (foundTextNode.getIsWord() == true && foundTextNode.getPhrases().isEmpty() == false) {
                for (String phrase : foundTextNode.getPhrases()) {
                    phrase.trim();
                    sentence.trim();
                    predictTextArea.append("\n" + sentence + " " + phrase);
                }
                return;
            }
            predictTextArea.append("\n There Are No Phrases To Continue From This Word:");
            return;
        }
        predictTextArea.append("\n There Are No Phrases To Continue From This Word:");
    }

    /**
     * Method to do the spell checking on the user-entered phrase
     * 
     * @author Becky Tyler (2461535)
     */
    public void getSpellings() {
        // Get a spell checker for the current dictionary and prediction object
        SpellChecker spellChecker = new SpellChecker(this.dictionary, this.prediction);

        // Create an ArrayList to store the output
        ArrayList<String> spellingOutput;

        // Reset the text display area
        predictTextArea.setText("");

        // Get the user's phrase to spell check
        String phraseToCheck = predictTextField.getText();

        // Spell check the user's phrase
        spellingOutput = spellChecker.checkSpelling(phraseToCheck);

        // Display the output in the text area
        for (String newline : spellingOutput) {
            predictTextArea.append(newline + "\n");
        }

        String text = "See text area for spelling suggestions.";
        spellingLabel.setText(text);
    }

    /**
     * This method will remove the word entered by the user in the text field
     */
    public void deleteUserEnteredWord() {
        String word = removeWordTextField.getText();

        // Make sure the word entered contains some letters
        if (dictionary.wordEnteredIsNull(word)) {
            removeWordLabel.setText("Word Entered Is Null");
        }

        // Try removing the node for the word
        else {
            if (dictionary.deleteNode(word, dictionary.getRoot()))
                removeWordLabel.setText(word + " has been removed from the dictionary.");
            else
                removeWordLabel.setText("Word '" + word + "' does not exist in the dictionary.");
            // dictionary.findNode(word, dictionary.getRoot());
        }
    }

    /**
     * Method to display the full dictionary trie from the root node
     * 
     * @author Becky Tyler (2461535)
     */
    public void displayText() {
        searchWordTextArea.setText("");

        String startWord = searchWordTextField.getText();

        if (startWord.equals("")) {
            this.displayDictionary(dictionary.getRoot(), "");
        } else // Display the part of the dictionary with words starting with startWord
        {
            // Find the starting node;
            WordNode startNode = dictionary.findNode(startWord);

            if (startNode != null) {
                searchWordTextArea.append("DISPLAYING THE DICTIONARY: Words beginning with " + startWord + "\n");

                // Display the dictionary from the start word
                this.displayDictionary(startNode, startWord);
            } else
                searchWordTextArea.append("Word Does Not Exist In the Dictionary");
        }
    }

    /**
     * This method will display the current version of the dictionary from a specific node in the tree and will show the frequency of those words too
     * @param currentNode is the node to start displaying from
     * @param nodeName is the word asociated with that starting node so each word displayed will be made up from it
     */
    public void displayDictionary(WordNode currentNode, String nodeName) {
        // Print the information stored in the current node;
        if (currentNode.getIsWord() == true) {
            int freq = currentNode.getFrequency();
            if (freq <= 0)
                searchWordTextArea.append(nodeName + "\n");
            else
                searchWordTextArea.append(nodeName + " (" + freq + ")\n");
        }

        // Use recursion to print the information stored in the next nodes
        if (!currentNode.getNextNodes().isEmpty()) {
            for (String letter : currentNode.getNextNodes().keySet()) {
                this.displayDictionary(currentNode.getNextNodes().get(letter), nodeName + letter);
            }
        }
    }

    /**
     * turns the addWord setting on
     */
    public void turnOnSetting() {
        prediction.setAddWord(true);
    }

    /**
     * turns the addWord setting off
     */
    public void turnOffSetting() {
        prediction.setAddWord(false);
    }

    /**
     * Is the main method to start the GUI and display it
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}
