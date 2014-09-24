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
		
		/*
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
			if (c == '_'){
				numOfWildcards++;
			}
		}
		if(numOfWildcards > 0){
			rackLetters = rackLetters.replaceAll("_", "");
			
			System.out.println("Rack with wildcards removed: " + rackLetters);
		}
		System.out.println("Number of Wildcards: " + numOfWildcards);		
				
		//Put valid words into a list
		startTime = System.currentTimeMillis();
		validWordsFound = searchDictionary("", rackLetters, numOfWildcards);
		
		if (validWordsFound != null){
			
			//Compare scores of valid words
			for (int i = 0; i < validWordsFound.size(); i++){
				System.out.println(validWordsFound.get(i).toString());
			}
			
			//Print out highest value word
			System.out.println("Place word: ");
			System.out.println("Total words found: " + matchedWords.size());
			
			//Time elapsed during turn
			totalTime = System.currentTimeMillis() - startTime;
			System.out.println("Elapsed Time: " + totalTime);
		}
		else
			System.out.println("No Valid Words Found");
	}

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
						if (n < 7)
							letters = letters + alphabet;
						n= letters.length();
						
						//sorts the rack with the wildcard added in
						char[] sortedLetters = letters.toCharArray();
						Arrays.sort(sortedLetters);
						StringBuilder sb = new StringBuilder(sortedLetters.length);
						for (char c : sortedLetters){
							sb.append(c);
						}
						letters = sb.toString();
						//System.out.println("Rack with wildcard: " + letters);
												
						wildcards -= 1;
						searchDictionary(prefix, letters, wildcards);
						letters = letters.replaceFirst(Character.toString(alphabet), "");
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
