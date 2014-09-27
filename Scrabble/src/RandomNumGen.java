import java.util.Random;

/*********************************************************
 * Generates a random number
 *********************************************************/
public class RandomNumGen 
{
	static int rNum;
	
	public static int randomNumber(int lowestNum, int highestNum)
	{
	    Random generator = new Random();
	    rNum = generator.nextInt(highestNum - lowestNum + 1) + lowestNum;
		
		return rNum;
	}
}
