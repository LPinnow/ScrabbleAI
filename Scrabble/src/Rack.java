import java.util.ArrayList;
import java.util.Arrays;


public class Rack {
	String letters;
	ArrayList<String> letterBag = new ArrayList<String>();
	
	/**
	 * Constructor
	 * Initializes the rack of letters
	 * @param rackLetters
	 */
	public Rack (String rackLetters){
		if(rackLetters == null || rackLetters == "")
			letters = pickLetterTiles();	
		else
			letters = rackLetters;
		letters = sortRack(letters);
	}
	
	/**
	 * Picks 7 tiles from the letter bag
	 * @return
	 */
	private String pickLetterTiles() {
		String randomLetters = "";
		ArrayList<Integer> pickedTiles = new ArrayList<Integer>();
		
		initializeLetterBag();
		
		while (randomLetters.length() < 7){
			int randomNum = RandomNumGen.randomNumber(0, 99);
			if (!pickedTiles.contains(randomNum)){
				randomLetters = randomLetters.concat(letterBag.get(randomNum));
				pickedTiles.add(randomNum);
			}
		}
		return randomLetters;		
	}
	
	/**
	 * Alphabetically sorts the rack of letters
	 * @param rackLetters
	 * @return
	 */
	private String sortRack(String rackLetters){
		char[] word = new char[rackLetters.length()];
		for(int i = 0; i< word.length; i++){
			word[i] = rackLetters.charAt(i);
		}
		Arrays.sort(word);
		StringBuilder sb = new StringBuilder(word.length);
		for (char c : word){
			sb.append(c);
		}
		letters = sb.toString();
		
		return letters;
	}
	
	/**
	 * Creates 100 tiles to choose from initially
	 * Letter frequencies adhere to standard Scrabble rules
	 */
	private void initializeLetterBag() {
		for (int i = 0; i < 9; i++){
			letterBag.add("A");				// 8 tiles of A
		}
		letterBag.add("B");					// 2 tiles of B
		letterBag.add("B");		 
		letterBag.add("C");					// 2 tiles of C
		letterBag.add("C");
		for (int i = 0; i < 4; i++){
			letterBag.add("D");				// 4 tiles of D
		}
		for (int i = 0; i < 12; i++){
			letterBag.add("E");				// 12 tiles of E
		}
		letterBag.add("F");					// 2 tiles of F
		letterBag.add("F");
		for (int i = 0; i < 3; i++){
			letterBag.add("G");				// 3 tiles of G
		}
		letterBag.add("H");					// 2 tiles of H
		letterBag.add("H");
		for (int i = 0; i < 9; i++){
			letterBag.add("I");				// 9 tiles of I
		}
		letterBag.add("J");					// 1 tiles of J
		letterBag.add("K");					// 1 tiles of K
		for (int i = 0; i < 4; i++){
			letterBag.add("L");				// 4 tiles of L
		}
		letterBag.add("M");					// 2 tiles of M
		letterBag.add("M");
		for (int i = 0; i < 6; i++){
			letterBag.add("N");				// 6 tiles of N
		}
		for (int i = 0; i < 8; i++){
			letterBag.add("O");				// 8 tiles of O
		}
		letterBag.add("P");					// 2 tiles of P
		letterBag.add("P");
		letterBag.add("Q");					// 1 tile of Q
		for (int i = 0; i < 6; i++){
			letterBag.add("R");				// 6 tiles of R
		}
		for (int i = 0; i < 4; i++){
			letterBag.add("S");				// 4 tiles of S
		}
		for (int i = 0; i < 6; i++){
			letterBag.add("T");				// 6 tiles of T
		}
		for (int i = 0; i < 4; i++){
			letterBag.add("U");				// 4 tiles of U
		}
		letterBag.add("V");					// 2 tiles of V
		letterBag.add("V");
		letterBag.add("W");					// 2 tiles of W
		letterBag.add("W");
		letterBag.add("X");					// 1 tile of X
		letterBag.add("Y");					// 2 tiles of Y
		letterBag.add("Y");
		letterBag.add("Z");					// 1 tile of Z
		letterBag.add("_");					// 2 wild cards
		letterBag.add("_");
	}
	
	public String toString(){
		return letters;
	}
	
}
