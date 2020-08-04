package crypto;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import crypto.Crypt;

class CryptTest 
{
	
	private Crypt myCrypt; 
	
	/*
	 * 
	 * Methods:
	 *  - KMACXOF256 
	 *  - cSHAKE256
	 *  - left_encode
	 *  - right_encode
	 *  - enc8
	 *  - encodeString
	 *  - byteConcatenation
	 *  - bytepad
	 *  - sqrt    
	 */
	
	//Simple setup method
	@BeforeEach
	void setup()
	{
		myCrypt = new Crypt(); 
	}
	
	//Test for the KMACXof256 function
	@Test
	void kmacxTest()
	{
		
	}
	
	//Test for the cSHAKE256 function
	@Test
	void cShakeTest()
	{
		
	}
	
	//Test for the Left Encode function. 
	@Test
	void leftEncodeTest()
	{
		
	}
	
	//Test for the Right Encode function. 
	@Test
	void rightEncodeTest()
	{
		
	}
	
	//Test for the Encode8 function. 
	@Test
	void encode8Test()
	{
		
	}
	
	//Test for the EncodeString function. 
	@Test
	void encodeStringTest()
	{
		
	}
	
	//Test for the Byte Concatenation function. 
	@Test
	void byteConcatenationTest()
	{
		
	}
	
	//Test for the BytePad function. 
	@Test
	void bytePadTest()
	{
		
	}
	
	//Test for the modular square root function. 
	@Test
	void sqrtModularTest()
	{
		
	}

}
