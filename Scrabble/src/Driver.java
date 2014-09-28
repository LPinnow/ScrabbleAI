import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;


public class Driver {
	static Map<String, ArrayList<String>> wordList = new LinkedHashMap<String,ArrayList<String>>();
	static ArrayList<String> matchedWords = new ArrayList<String>();
	public static void main(String[] args)  {
		long startTime, totalTime;
		ArrayList<String> validWordsFound;
		
		//initialize dictionary of valid words
		Dictionary dict = new Dictionary();
		wordList = dict.words;
		
		//initialize rack of letters
		Rack rack = new Rack(null);
		System.out.println("Rack: " + rack);
		
		/* Prints hashmap keys plus values
		for (Entry<String, ArrayList<String>> entry : wordList.entrySet()) {
	        String key = entry.getKey().toString();;
	        ArrayList<String> value = entry.getValue();
	        System.out.println("key: " + key + "\t\t value: " + value );
	    }
	    */
		
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
		
		if (validWordsFound != null){
			
			//Prints valid words found
			//for (int i = 0; i < validWordsFound.size(); i++){
			//	System.out.println(validWordsFound.get(i).toString());
			//}
			System.out.println("Valid Words Found: " + validWordsFound.size());
			
			//associate words with point values for initial word placement
			String bestWord = findHighestValueWord(matchedWords, numOfWildcards, rackLetters);
			
			//Print out highest value word
			System.out.println("Place word: " + bestWord);
			
			//Time elapsed during turn
			totalTime = System.currentTimeMillis() - startTime;
			System.out.println("This turn took: " + totalTime + " ms");
		}
		else
			System.out.println("No Valid Words Found");
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
					//System.out.println(s + " worth: " + highestPoints);
				}
			}
		
		return highestPointWord.concat(" worth " + highestPoints + " points.");
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
		if (n < 2)
			return matchedWords;
		//checks for a "bingo"
		else if (wordList.containsKey(letters) && n == 7){
				for (int i = 0; i < wordList.get(letters).size(); i++){
					if (!matchedWords.contains(wordList.get(letters).get(i)))
						matchedWords.add(wordList.get(letters).get(i));
				}									
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
						letters = letters.replaceFirst(Character.toString(alphabet), "");
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
