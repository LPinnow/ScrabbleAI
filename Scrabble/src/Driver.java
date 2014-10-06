/*
 * Driver.java
 * Author: Leah Pinnow
 * Purpose: Outputs the highest point word for the first turn of a game of Scrabble.
 * 			Creates a list of words from a rack of Scrabble tiles.
 * 			Then determines which word is worth the most points.
 * 			Then outputs where on the Scrabble board the word should be played, assuming the
 * 			upper left hand corner square has a grid location of (0,0) with the middle
 * 			located at (7,7).
*/

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


public class Driver {
	static Map<String, ArrayList<String>> wordList = new LinkedHashMap<String,ArrayList<String>>();
	static ArrayList<String> matchedWords = new ArrayList<String>();
	static int minLength = 2;
	
	public static void main(String[] args)  {
		long startTime, totalTime, timeOfAllTrials = 0;
		boolean multipleTests = false;
		int fileIndex = 0;
		ArrayList<String> rackFileTiles = new ArrayList<String>();
		BufferedReader in = null;
		
		//initialize dictionary of valid words
		Dictionary dict = new Dictionary();
		wordList = dict.words;
		
		//Ask user if they want to do multiple trials
		System.out.print("Use a file to do test trials? Enter yes or no: ");
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		String s;
		try {
			s = input.readLine();
			if (s.equalsIgnoreCase("yes")){
				multipleTests = true;
				try{
					in = new BufferedReader(new FileReader("rack.txt"));
					String wordValue = in.readLine();
					
					while(wordValue != null){						
						rackFileTiles.add(wordValue);
						wordValue = in.readLine();
					}
				}
				catch(FileNotFoundException e){
					e.printStackTrace();
				}
				catch(IOException e){
					e.printStackTrace();
				}
			}
				
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		//Loops through rack file input or do only one trial with random tiles
		do{
			ArrayList<String> validWordsFound = new ArrayList<String>();
			minLength = 0;
			
			//initialize rack of letters
			String tiles = null;			
			if(multipleTests == true){
				tiles = rackFileTiles.get(fileIndex);
				fileIndex += 1;
				
				if (rackFileTiles.size() == fileIndex)
					multipleTests = false;
			}
			Rack rack = new Rack(tiles);
			System.out.println("Rack: " + rack);
					
			//checks for wildcards
			int numOfWildcards = 0;
			String rackLetters = rack.toString();
			for (char c : rackLetters.toCharArray()){
				if (c == '_')
					numOfWildcards++;			
			}
			if(numOfWildcards > 0)
				rackLetters = rackLetters.replaceAll("_", "");
						
			System.out.println("Number of Wildcards: " + numOfWildcards);		
					
			//Put valid words into a list
			startTime = System.currentTimeMillis();
			validWordsFound = searchDictionary("", rackLetters, numOfWildcards);
			
			if (!validWordsFound.isEmpty()){				
				//Prints valid words found
				//for (int i = 0; i < validWordsFound.size(); i++){
				//	System.out.println(validWordsFound.get(i).toString());
				//}
				System.out.println("Valid Words Found: " + validWordsFound.size());
				
				//associate words with point values for initial word placement
				String bestWord = findHighestValueWord(matchedWords, numOfWildcards, rackLetters);
				
				//Print out highest value word
				System.out.println("Place word: " + bestWord);
			}
			else
				System.out.println("No Valid Words Found\nTurn Skipped");
			
			//Time elapsed during turn
			totalTime = System.currentTimeMillis() - startTime;
			timeOfAllTrials += totalTime;
			System.out.println("This turn took: " + totalTime + " ms\n");
			
			matchedWords.clear();
		} while (multipleTests == true);
		
		if(fileIndex == 0)
			fileIndex = 1;
		System.out.println("Average time of all trials: " + timeOfAllTrials/fileIndex + " ms");
	}
	
	/**
	 * Compares words in a list to determine which word would give the highest
	 * point value on the initial turn of a scrabble game
	 * @param validWords
	 * @param numOfWildcards
	 * @param rackLetters
	 * @return
	 */
	private static String findHighestValueWord(ArrayList<String> validWords,
			int numOfWildcards, String rackLetters) {
		String highestPointWord = null;
		int highestPoints = 0;
		int highestPointLetterIndex = 0;
				
		//array to hold point total for each corresponding alphabet letter with first position as a wild card
		int[] points = {0, 1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
		String alphabet = "_ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		
			for (String s : validWords){
				int tempPointHolder = 0;		
				int bestLetter = 0;				
				int wildcards = numOfWildcards;
				int indexOfWildcard = -1;
				int secondIndexOfWildcard = -1;
				String tempRackLetters = rackLetters;
								
				//Add up the points for the word. Wildcard letters are worth zero points
				for (char c : s.toCharArray()){	
					
					if (!tempRackLetters.contains(Character.toString(c)) && wildcards == 2){
						secondIndexOfWildcard = s.indexOf(c);
						wildcards -= 1;
					}
					else if (!tempRackLetters.contains(Character.toString(c)) && wildcards == 1){
						indexOfWildcard = s.indexOf(c);
						wildcards -= 1;
					}
					else if (tempRackLetters.contains(Character.toString(c)) && wildcards > 0){
						tempRackLetters = tempRackLetters.replace(Character.toString(c), "");
						tempPointHolder += points[alphabet.indexOf(c)];	
					}
					else
						tempPointHolder += points[alphabet.indexOf(c)];
				}
								
				//Determines the highest point letter that can be placed on the double letter square
				if (s.length() == 5){					
					if (points[alphabet.indexOf(s.charAt(0))] > points[alphabet.indexOf(s.charAt(s.length() - 1))]
							&& secondIndexOfWildcard != 0 && indexOfWildcard != 0)
						bestLetter = 0;
					else
						bestLetter = s.length() - 1;
					
					if (bestLetter != indexOfWildcard && bestLetter != secondIndexOfWildcard)
						tempPointHolder = tempPointHolder + points[alphabet.indexOf(s.charAt(bestLetter))]; //double letter tile
					tempPointHolder = tempPointHolder * 2; //initial turn uses a double word space
				}
				else if (s.length() == 6){
					int temp = 0;
					for (int i = 0; i < 6; i++){
						if (temp < points[alphabet.indexOf(s.charAt(i))] && i != 2 && i != 3
								&& i != secondIndexOfWildcard && i != indexOfWildcard){
							bestLetter = i;
							temp = points[alphabet.indexOf(s.charAt(i))];
						}							
					}
					
					tempPointHolder = tempPointHolder + points[alphabet.indexOf(s.charAt(bestLetter))]; //double letter tile
					tempPointHolder = tempPointHolder * 2; //initial turn uses a double word space
				}
				else if (s.length() == 7){
					int temp = 0;
					for (int i = 0; i < 7; i++){
						if (temp < points[alphabet.indexOf(s.charAt(i))] && i != 3
								&& i != secondIndexOfWildcard && i != indexOfWildcard){
							bestLetter = i;
							temp = points[alphabet.indexOf(s.charAt(i))];
						}
					}
					
					tempPointHolder = tempPointHolder + points[alphabet.indexOf(s.charAt(bestLetter))]; //double letter tile
					tempPointHolder = tempPointHolder * 2; //initial turn uses a double word space
					tempPointHolder += 50; // 50 Bonus points for using all seven tiles
				}
				else
					tempPointHolder = tempPointHolder * 2; //initial turn uses a double word space
				
				//stores highest point word
				if (tempPointHolder > highestPoints){
					highestPointLetterIndex = bestLetter;
					highestPoints = tempPointHolder;
					highestPointWord = s;
				}
			}
		
		//find initial board placement for word
		if (!validWords.isEmpty()){
			String placement = placeWord(highestPointWord, highestPointLetterIndex);
			return highestPointWord.concat(" worth " + highestPoints + " points.\nTile Placement: " + placement);
		}
		else
			return null;
	}
	
	/**
	 * Return a string displaying which board square to place the tiles on
	 * Initial words are placed horizontally
	 * @param highestPointWord
	 * @param highestPointLetterIndex
	 * @return
	 */
	private static String placeWord(String highestPointWord, int highestPointLetterIndex) {
		String tilePlacement = "";
		int startTile = 0;
		
		if(highestPointWord.length() < 5){
			startTile = 6;
			for (int i = 0; i < highestPointWord.length(); i++){
				tilePlacement = tilePlacement.concat(highestPointWord.charAt(i) + ": (" + (startTile + i) + ",7)  ");
			}
		}
		else {
			if(highestPointLetterIndex < 4)
				startTile = 3 - highestPointLetterIndex;
			else
				startTile = 11 - highestPointLetterIndex;
				
			for (int i = 0; i < highestPointWord.length(); i++){
				tilePlacement = tilePlacement.concat(highestPointWord.charAt(i) + ": (" + (startTile + i) + ",7)  ");
			}
		}
		return tilePlacement;
	}

	/**
	 * Builds a list of valid words from the letters on the rack
	 * @param prefix
	 * @param letters
	 * @param wildcards
	 * @return
	 */
	private static ArrayList<String> searchDictionary(String prefix, String letters, int wildcards) {
		int n = letters.length();
		
		//initial word can't be shorter than two letters
		if (n < minLength)
			return matchedWords;
		//checks for a "bingo" which is using all 7 rack letters
		else if (wordList.containsKey(letters) && n == 7){
			for (int i = 0; i < wordList.get(letters).size(); i++){
				if (!matchedWords.contains(wordList.get(letters).get(i)))
					matchedWords.add(wordList.get(letters).get(i));
			}
			minLength = 6; // No words less than 6 letters will be found after a word of 7 letters is found
		}		
		else if (wordList.containsKey(letters) && wildcards == 0){
			for (int i = 0; i < wordList.get(letters).size(); i++){
				if (!matchedWords.contains(wordList.get(letters).get(i)))
					matchedWords.add(wordList.get(letters).get(i));
			}	
		}
		else{
			
			for (int i = 0; i < n; i++){
				if(wildcards > 0){
					for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++){
						//add wildcard to rack and alphabetially sort the rack
						if (n < 7){
							letters = letters + alphabet;
							//sorts the rack with the wildcard added in
							char[] sortedLetters = letters.toCharArray();
							Arrays.sort(sortedLetters);
							StringBuilder sb = new StringBuilder(sortedLetters.length);
							for (char c : sortedLetters){
								sb.append(c);
							}
							letters = sb.toString();							
						}
						
						n= letters.length();
						wildcards -= 1;
						searchDictionary(prefix, letters, wildcards);
						letters = letters.replaceFirst(Character.toString(alphabet), ""); //remove wildcard
						n = letters.length();
						wildcards += 1;
					}					
				}
				else{
					searchDictionary(prefix + letters.charAt(i), letters.substring(0, i) + letters.substring(i+1, n), wildcards);	
				}
			}
		}
				
		return matchedWords;
	}

}
