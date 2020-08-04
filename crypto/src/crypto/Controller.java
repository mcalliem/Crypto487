package crypto;

import java.io.File;
import java.io.IOException;
import javax.swing.JFileChooser;
import java.util.Scanner; 

public class Controller 
{

	/*
	 * Main method to run the encryption / decryption app loop.
	 */
    public static void main(final String[] theArgs) throws IOException 
    {
    	printHeader(); 
    	
    	String u = "";
    	String text = "";
    	
    	Scanner keyboard = new Scanner(System.in);
    	File f = null;
    	
    	boolean isFile = false;
    	
    	/*Choice loop (expand later for decryption, hashes, exit, etc.).
    	 * 
    	 * Probably add options to encrypt file, decrypt file, encrypt text, decrypt text, help, exit.
    	 */
    	while (true)
    	{
    		System.out.println("Would you like to encrypt text (t) or a text file (tf)?");
    		System.out.println("Type (t/tf) or 'c' to cancel: ");
    		u = keyboard.nextLine();
    		
    		//Encrypt some text.
    		if (u.equals("t"))
    		{
    			System.out.println("Enter the text to encrypt: ");
    			text = keyboard.nextLine();
    			break;
    		}
    		
    		//Encrypt a text file.
    		if (u.equals("tf"))
    		{
    			System.out.println("Enter the path of the file to encrypt: ");
    			text = keyboard.nextLine();
    			isFile = true;
    			break;
    		}
    		
    		if (u.equals("c"))
    		{
    			break;
    		}
    		
    		System.out.println("Invalid input. Please try again.");
    	}
    	
    	if (isFile)
    	{
    		f = new File(text); //Text will be file path
    		System.out.println("It's a file!"); //handle file
    	}
    	else
    	{
    		System.out.println("It's a text input!"); //handle text
    	}
    	
        testStuff();
        
        System.out.println("------------------------------------------");
    }
    
    /*
     * A helper method to print out the header. 
     */
    public static void printHeader()
    {
    	System.out.println("------------------------------------------");
    	System.out.println("Welcome to the Cryptography 487 encryption/decryption app.");
    	System.out.println("Summer Quarter 2020");
    	System.out.println("\nAuthors: Nicolas Steinacker-Olsztyn, Evan McAllister, Chase Alder");
    	System.out.println("------------------------------------------\n");
    }
    
    /*
     * A method to test stuff related to the app.
     */
    public static void testStuff() throws IOException
    {
    	//Make an instance of the project file
        Crypt bob2 = new Crypt();

        //Run left and right encode methods
        System.out.println(bob2.right_encode(0) + " TestRight");
        System.out.println(bob2.left_encode(0) + " TestLeft");
        
        //Not sure here
        byte b1 = bob2.right_encode(0)[2];
        String s1 = String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
        System.out.println(s1);
    }
}
