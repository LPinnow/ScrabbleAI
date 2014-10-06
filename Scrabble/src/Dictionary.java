import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;


public class Dictionary {
	public HashMap<String, ArrayList<String>> words = new LinkedHashMap<String, ArrayList<String>>();
	private int numOfWords = 0;
	
	/**
	 * Constructor
	 * Reads a file containing a dictionary of words
	 */
	public Dictionary (){
		words = readFile();
	}
	
	/**
	 * Puts dictionary words into a hash map
	 * Keys are the words with their letters sort alphabetically
	 * Separate chaining is used to store collisions
	 * @return
	 */
	public HashMap<String, ArrayList<String>> readFile(){
		try{
			BufferedReader in = new BufferedReader(new FileReader("SOWPODS_complete.txt"));
					
			String wordKey;
			String wordValue = "";
					
			while(wordValue != null){
				ArrayList<String> value = new ArrayList<String>();
				wordValue = in.readLine();
								
				//puts the word into an array and sorts it alphabetically
				//and then puts the sorted letters into a string
				if (wordValue != null && wordValue.length() > 0){
					char[] word = new char[wordValue.length()];
					for(int i = 0; i< word.length; i++){
						word[i] = wordValue.charAt(i);
					}
					Arrays.sort(word);
					StringBuilder sb = new StringBuilder(word.length);
					for (char c : word){
						sb.append(c);
					}
					wordKey = sb.toString();
					
					// Check for collisions
					if (words.containsKey(wordKey)){
						words.get(wordKey).add(wordValue);
						setNumOfWords(getNumOfWords() + 1);
					}
					else{
						value.add(wordValue);
						words.put(wordKey, value);
						setNumOfWords(getNumOfWords() + 1);
					}
				}
			}
		}
		catch(FileNotFoundException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		return words;
	}
	
	public int getNumOfWords() {
		return numOfWords;
	}

	public void setNumOfWords(int numOfWords) {
		this.numOfWords = numOfWords;
	}

	public String toString(){
		return words.toString();
	}
}
