/*
 * The words.txt file is from the standard unix dictionary, found in
 * /usr/share/dict/words on mac os systems
 */

/**
 * @Author Nicholas Phan
 * @Date: 12/9/2021
 */

/*
    /\_____/\
   /  o   o  \
  ( ==  ^  == )             Have a cat!
   )         (
  (           )
 ( (  )   (  ) )
(__(__)___(__)__)
 */

import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;

import java.util.ArrayList;

public class SpellChecker {
    SeparateChainingHashST<String, String> dictionary;
    public final int AMOUNTOFLETTERS = 26; //amount of letters to iterate through
    public final char[] alphabet = "abcdefghijklmnopqrstuvwxyz".toCharArray();


    private static void initDictionary(SeparateChainingHashST<String, String> dictionary, String fileName) {
        In input = new In(fileName);
        while (!input.isEmpty()) {
            String s = input.readLine();
            if (!dictionary.contains(s)) { //this ensures no repeating of words
                dictionary.put(s, s); //puts in the entries, for the Key it would contain the actual word, but for the values it'll say no mistakes found!
            }
        }
    }

    public SpellChecker(String fileName) {
        dictionary = new SeparateChainingHashST<String, String>();
        initDictionary(dictionary, fileName);
    }


    public boolean checkSpelling(String enteredWord) {
        return dictionary.contains(enteredWord);
    }

    /**
     * This does all 5 rules of swapping strings in one method
     * @param inputWord - word that we're checking if it's a spelling error
     * @return the list of suggested words that it may be
     */

    public ArrayList<String> possibleStrings(String inputWord) {

        //We're gonna go with strategy 2

        ArrayList<String> stringList = new ArrayList<String>();
        StringBuilder manipulatedWord = new StringBuilder(inputWord);

        //O(n)
        //Rule 1, Add a character to the beginning of the string
        for(int i = 0; i < AMOUNTOFLETTERS; i++){ //for loop that iterates through alphabet with char array
            manipulatedWord.insert(0, alphabet[i]);
            if(checkSpelling(manipulatedWord.toString())) { //includes this so our arraylist isn't flooded with words
                stringList.add(manipulatedWord.toString()); //adds it to our suggestions list
            }
            manipulatedWord.deleteCharAt(0); //resets to original word
        }

        //Rule 2, Add a character to the end of the string

        for(int i = 0; i < AMOUNTOFLETTERS; i++){ //for loop that iterates through alphabet with char array
            manipulatedWord.append(alphabet[i]);
            if(checkSpelling(manipulatedWord.toString())) {
                stringList.add(manipulatedWord.toString()); //adds it to our suggestions list
            }
            manipulatedWord.deleteCharAt(manipulatedWord.length() - 1); //resets to original word
        }

        //makes 2 elements total
        //Rule 3 & 4, removes one char from beginning or end
        for (int i = 0; i < 2; i++) { //might be a better way to write this
            if(i == 0) {
                char firstCharacter = manipulatedWord.charAt(0); //saves our first character
                manipulatedWord.deleteCharAt(0); //deletes first character
                if(checkSpelling(manipulatedWord.toString())) {
                    stringList.add(manipulatedWord.toString()); //adds deleted first character to our suggestions list
                }
                manipulatedWord.insert(0, firstCharacter); //resets our word
            }else{//don't have to worry about adding it back in since the for-loop ends
                manipulatedWord.deleteCharAt(manipulatedWord.length() - 1); //deletes last character
                if(checkSpelling(manipulatedWord.toString())) {
                    stringList.add(manipulatedWord.toString()); //adds word with deleted last character to suggestions list
                }
            }
        }

        //For what we know, we swap two adjacent characters of the total length minus 1
        //Rule 5, Swap two adjacent characters
        for (int i = 0; i < inputWord.length() - 1; i++){
            //essentially progresses through the word by swapping a character each iteration, one space at a time
            String swappedWord = inputWord.substring(0, i) +
                    inputWord.charAt(i + 1) +
                    inputWord.charAt(i) +
                    inputWord.substring(i + 2);
            if(checkSpelling(swappedWord)){
                stringList.add(swappedWord);
            }
        }

        return stringList;
    }

    public static void main(String[] args) {
        SpellChecker spellChecker = new SpellChecker("src/words");
        String enteredWord;
        System.out.println("Please enter a word, or press 'Ctrl + D' to end!");

        while (!StdIn.isEmpty()) {
            enteredWord = StdIn.readString();
            System.out.println("You have entered the word: " + enteredWord);
            System.out.println("Checking if the word is spelled correctly. . .");

            // If the word is correctly spelled, print "no mistakes found"
            if (spellChecker.checkSpelling(enteredWord)) {
                System.out.println("No Mistakes Found!");
            } else {

                System.out.println("The word you entered: " + enteredWord + " was spelled wrong");
                //another way of looking at it in a neat way
//                System.out.println("List of possible words that are correct: " + spellChecker.possibleStrings(enteredWord).toString());
                for(String s: spellChecker.possibleStrings(enteredWord)){ //the for-each loop way
                    System.out.println(spellChecker.dictionary.get(s));
                }

            }
            System.out.println("Please enter a word, or press 'Ctrl + D' to end!");
        }
        // Note: When running the code, hold down the Control key while pressing D to end the program.
    }
}
