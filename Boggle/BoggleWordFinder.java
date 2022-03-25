import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/*

    Boggle uses a 4x4 grid of characters, allowing graph based problem-solving.
    For one, for graph traversal we can use depth-first-search.
    Another thing to note, a data structure we can use is Hashmaps and Tries. From what I get we can create a hashmap
    of characters that can create potential words.
    The words text doc has literally from A - Z, so we either set up the trie based off of where what characters are available
    and then create our tree based off of it. - Nick

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
            if(withinRange(row+1, col+1, board)) { 
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
