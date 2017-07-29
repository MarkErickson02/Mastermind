/* Mark Erickson
 * CS 331 Project 2
 */

import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;


public class Mastermind {

	private ArrayList<int[]> options;

	/* 
	 * Mastermind()
	 * No-arg constructor for Mastermind class.
	 */
	public Mastermind(){
		options = new ArrayList<int[]>();
	}

	/*
	 * setOptions
	 * Mutator method for the arraylist that holds all current options.
	 */
	public void setOptions(ArrayList<int[]> op){
		options = op;
	}

	/*
	 * getOptions
	 * returns an arraylist that holds all possible guesses.
	 */
	public ArrayList<int[]> getOptions(){
		return options;
	}

	/*
	 *  filter
	 *  removes arrays from an arraylist that holds all current guesses that cannot be part of the answer given an answer.
	 *  returns an arraylist with fewer options.
	 */
	public ArrayList<int[]> filter(ArrayList<int[]> options, int[] guess, String answer){
		int size = options.size();
		char[] res = answer.toCharArray();
		int numW = 0;
		int numB = 0;
		for (int i=0;i<res.length;i++){
			if (res[i] == 'B'){
				numB++;
			}
			if (res[i] == 'W'){
				numW++;
			}
		}

		if (numB == 0 && numW ==0){
			for (int i=0;i<size;i++){
				String similar = response(options.get(i),guess);
				int similarW = 0;
				int similarB = 0;
				char[] sim = similar.toCharArray();
				for (int j=0;j<sim.length;j++){
					if (sim[j] == 'B'){
						similarB++;
					}
					if (sim[j] == 'W'){
						similarW++;
					}
				}
				if (similarB > 0 || similarW > 0){
					options.remove(i);
					size--;
					i--;
				}
			}

		}
		else{
			for (int i=0;i<size;i++){
				String similar = response(options.get(i),guess);
				int similarW = 0;
				int similarB = 0;
				char[] sim = similar.toCharArray();
				for (int j=0;j<sim.length;j++){
					if (sim[j] == 'B'){
						similarB++;
					}
					if (sim[j] == 'W'){
						similarW++;
					}
				}
				if (similarB != numB){
					options.remove(i);
					size--;
					i--;
				}
			}
		}
		return options;
	}

	/*
	 * response
	 * Returns a string that contains the white and black pegs given a guess and answer
	 */
	public String response(int[] guess, int[] answer){
		String response = "";
		int[] alreadySeen = {0,0,0,0};
		int counter = 0;
		boolean seen = false;
		for (int i=0;i<4;i++){
			if (guess[i] == answer[i]){
				response  += 'B';
				alreadySeen[counter] = guess[i];
				counter++;
			}
		}
		for (int i=0;i<4;i++){
			for (int j=0;j<4;j++){
				if (guess[i] == answer[j] && guess[i] != answer[i]){
					for (int k=0;k<4;k++){
						if (alreadySeen[k] == guess[i]){
							seen = true;
						}
					}
					if (seen == false){
						response += 'W';
						alreadySeen[counter] = guess[i];
						counter++;
					}

				}
				seen = false;
			}
		}
		return response;
	}

	/*
	 * getGeuss
	 * Returns a random guess from the list of possible choices.
	 */
	public int[] getGuess(ArrayList<int[]> options){
		Random rng = new Random();
		int index = rng.nextInt(options.size());
		int[] guess = options.get(index);
		options.remove(index);
		setOptions(options);
		return guess;

	}

	/*
	 * getAnswer
	 * Returns one random guess from all possibilities to be used as the answer.
	 */
	public int[] getAnswer(ArrayList<int[]> options){
		Random rng = new Random();
		int index = rng.nextInt(options.size());
		return options.get(index);
	}

	/*
	 * createList
	 * Returns an arraylist that contains all 6^4 possibilities for a Mastermind game with 6 colors and 4 slots.
	 */
	public ArrayList<int[]> createList(){
		ArrayList<int[]> options = new ArrayList<int[]>();
		for (int i=1;i<=6;i++){
			for (int j=1;j<=6;j++){
				for (int k=1;k<=6;k++){
					for (int l=1;l<=6;l++){
						int[] possibility = {i,j,k,l};
						options.add(possibility);
					}
				}
			}
		}
		return options;
	}
	public static void main(String[] args) {
		Mastermind game = new Mastermind();
		Scanner keyboard = new Scanner(System.in);
		System.out.print("How would you like to play? Auto mode(1) Computer Guesses(2): ");
		int input = keyboard.nextInt();
		if (input == 1){
			ArrayList<int[]> option = game.createList();
			System.out.print("The answer is: ");
			int[] answer = game.getAnswer(option);
			for (int i=0;i<4;i++){
				System.out.print(answer[i] + " ");
			}
			System.out.println("");
			int guesses = 0;
			while (guesses <= 12 && option.size() > 0){
				int[] guess = game.getGuess(option);
				option = game.getOptions();
				System.out.print("My guess is: ");
				for (int i=0;i<4;i++){
					System.out.print(guess[i] + " ");
				}
				System.out.println("");
				String r = game.response(guess, answer);
				System.out.println("Response: " + r);
				if (r.equalsIgnoreCase("BBBB")){
					System.out.println("You Win! It took " + guesses + " moves for you to win.");
					System.exit(0);
				}
				else{
					option = game.filter(option, guess, r);
				}
				guesses++;
			}

			if (guesses > 12){
				System.out.println("You lose.");
			}
			if (option.size() <=0){
				System.out.println("Something went wrong.");
			}
		}
		else if (input == 2){
			ArrayList<int[]> option = game.createList();
			System.out.println("Think of an answer.");
			keyboard.nextLine();
			int guesses = 0;
			while (guesses <= 12 && option.size() > 0){
				int[] guess = game.getGuess(option);
				option = game.getOptions();
				System.out.print("My guess is: ");
				for (int i=0;i<4;i++){
					System.out.print(guess[i] + " ");
				}
				System.out.println("");
				System.out.print("Response: ");
				String answer = keyboard.nextLine();
				if (answer.equalsIgnoreCase("BBBB")){
					System.out.println("You lose! It took " + guesses + " moves for you to lose.");
					System.exit(0);
				}
				else{
					option = game.filter(option, guess, answer);
				}
				guesses++;
			}

			if (guesses > 12){
				System.out.println("You win!");
			}
			if (option.size() <=0){
				System.out.println("Something went wrong.");
			}
		}
		/*
		else if(input == 3){
			ArrayList<int[]> option = game.createList();
			int[] answer = game.getAnswer(option);
			int guesses = 0;
			while (guesses <= 12){
				keyboard.nextLine();
				System.out.print("Your guess: ");
				String guessS= keyboard.nextLine();
				String[] g = guessS.split(" ");
				int[] guess = new int[4];
				for (int i=0;i<guessS.length();i++){
					if (!g.equals(" ")){
						guess[i] = Integer.parseInt(g[i]);
					}
				}
				String r = game.response(guess, answer);
				System.out.println("Response: " + r);
				if (r.equalsIgnoreCase("BBBB")){
					System.out.println("You Win! It took " + guesses + " moves for you to win.");
					System.exit(0);
				}
				guesses++;
			}

			if (guesses > 12){
				System.out.println("You lose.");
			}
		}
		*/
	}
}
