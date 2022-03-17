import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/* This is where you'll write your code to use a canonical graph-traversal algorithm to
 * solve a problem that at first may not seem like it's a graph problem at all.
 *
 * A note: This really is a fun one. If it gets to feel frustrating instead of fun, or if you feel
 * like you're  completely stuck, step back a bit and ask some questions.
 *
 * Spend a lot of time sketching out a plan, and figuring out which data structures might be
 * best for the various tasks, before you write any code at all.
 *
 * If you're not sure about how to approach this as a graph problem, feel free to ask questions.
 * (I won't give you all the answer, but...)
 * -Ben
 *
 */



/*

    Boggle uses a 4x4 grid of characters, allowing graph based problem-solving.
    For one, for graph traversal we can use depth-first-search.
    Another thing to note, a data structure we can use is Hashmaps and Tries. From what I get we can create a hashmap
    of characters that can create potential words.
    The words text doc has literally from A - Z, so we either set up the trie based off of where what characters are available
    and then create our tree based off of it. - Nick

       *******************************A Ben Hint**********************************

At first, this problem might not look like a graph theory problem.
However, it is possible to think of it in terms of graph theory.
Let's treat each letter on the Boggle board as a vertex.
This vertex is adjacent to the vertices that are adjacent to it on the board, including diagonally.
However, there is only a valid link between a letter and the letter next to it if all the letters on the path to that
new vertex form a valid prefix for a word or words in the word list.
For example, "aar" would be a valid prefix, because it appears at the start of many words that appear in the list,
including "aardvark" and "aardwolf,  "aaz" would not be a valid prefix,
because there are no words starting with those characters in our word list.

***********************************************************************************************************************

From ben's sample output, it's organized from A - Z for the words, so a Trie would definitely seem to be the case
with the use of sedgwicks code since it's recommended we don't make our own data structure (and I'm uncertain of Tries)

Debugging Issues/Concerns

In the sample_output_optimized_normal file, theres a word Acate found, but in the rules of boggle it says you're unable to use the same
letter cube more than once. ***IT WAS A MISTAKE SO IT DON'T MATTER IF WE USE THIS***

 */
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.TrieST;

public class BoggleWordFinder {

    // some useful constants
    public static final String WORD_LIST = "src/words";
    public static final int ROWS = 5;
    public static final int COLUMNS = 5;
    public static final int SEED = 137;

    public ArrayList<String> wordList = new ArrayList<String>();

    /**
     * @return Trie data structure that houses Dictionary
     */
    public TrieST<String> initTrie() {
        TrieST<String> trieSet = new TrieST<String>();
        In input = new In(WORD_LIST); //uses WORD_LIST to use the word file
        while (!input.isEmpty()) {
            String word = input.readLine();
            //if it has a capital letter, do not add it in.
            if (!trieSet.contains(word) && !Character.isUpperCase(word.charAt(0)) ) {
                trieSet.put(word, word); //no need to really put a value
            }
        }
        return trieSet;
    }

    /**
     *
     * @param word - word to check
     * @param dictionary - Trie that contains all words
     * @return - true or false
     */
    public boolean isWord(String word, TrieST<String> dictionary) {
        return dictionary.contains(word);
    }

    /**
     * Checks to see if our row and Columns are within the range, whilest checking if it's visited or not
     *
     * @param row - current Row
     * @param col - current Column
     * @return - True or false
     */
    public boolean withinRange(int row, int col, BoggleBoard board) {
        return (row >= 0 && row < board.getRows() && col >= 0 && col < board.getColumns() && !board.isVisited(row, col));
    }

    public void DFS(int row, int col, BoggleBoard board, String word, TrieST<String> dictionary) { //recursive DFS ORIGINAL FUNCTION THAT WORKS
        if (isWord(word, dictionary) && !wordList.contains(word)){ //if the word is valid in the dictionary and the arraylist doesn't contain the word
            wordList.add(word); //add the word to arraylist of FoundWords
        }

        if (withinRange(row, col, board)) {
            board.setVisited(row, col);
            if(withinRange(row+1, col+1, board)) { //im not sure why it runs best like this, but it works?
                if (withinRange(row - 1, col - 1, board)) {
                    DFS(row - 1, col - 1, board, word + board.getCharAt(row - 1, col - 1), dictionary); //checks top left of position
                }

                if (withinRange(row - 1, col, board)) {
                    DFS(row - 1, col, board, word + board.getCharAt(row - 1, col), dictionary); // checks top position
                }
                if (withinRange(row - 1, col + 1, board)) {
                    DFS(row - 1, col + 1, board, word + board.getCharAt(row - 1, col + 1), dictionary); //checks top right of position
                }
                if (withinRange(row, col + 1, board)) {
                    DFS(row, col + 1, board, word + board.getCharAt(row, col + 1), dictionary); //checks right of position
                }
                // and it's withinRange, which makes sure it doesn't repeat characters
                if (withinRange(row + 1, col + 1, board)) {
                    DFS(row + 1, col + 1, board, word + board.getCharAt(row + 1, col + 1), dictionary); //checks bottom right of position
                }

                if (withinRange(row + 1, col, board)) {
                    DFS(row + 1, col, board, word + board.getCharAt(row + 1, col), dictionary); //checks bottom of position
                }

                if (withinRange(row + 1, col - 1, board)) {
                    DFS(row + 1, col - 1, board, word + board.getCharAt(row + 1, col - 1), dictionary); //checks bottom left of position
                }
                if (withinRange(row, col - 1, board)) {
                    DFS(row, col - 1, board, word + board.getCharAt(row, col - 1), dictionary); //checks left position
                }

            }
            board.setUnvisited(row,col);
        }
    }

    /**
     * @param givenBoard - Board we're searching for Boggle
     * @return Arraylist of words found within board
     */


    public void searchWords(BoggleBoard givenBoard) {

        TrieST<String> dictionary = initTrie();
        String word = ""; //initializes an empty word that we'll use to add characters to create words to
        for (int row = 0; row < ROWS; row++) { //
            for (int column = 0; column < COLUMNS; column++) {
                DFS(row, column, givenBoard, word, dictionary);
            }
        }
        Collections.sort(wordList);
        System.out.println(wordList);
        System.out.println(wordList.size());

    }

    public static void main(String[] args) {
        BoggleBoard board = new BoggleBoard(ROWS, COLUMNS, SEED);
        BoggleWordFinder finder = new BoggleWordFinder();

        System.out.println(board);
        finder.searchWords(board);
    }

}
