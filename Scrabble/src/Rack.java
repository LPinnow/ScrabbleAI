import java.util.Arrays;


public class Rack {
	String letters;
	
	public Rack (String rackLetters){
		letters = pickLetterTiles();
		letters = sortRack(letters);
	}

	private String pickLetterTiles() {
		
		return "ZYGOTQP";
	}
	
	public String sortRack(String rackLetters){
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
	
	public String toString(){
		return letters;
	}
	
}
