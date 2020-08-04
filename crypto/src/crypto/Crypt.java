package crypto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/*********************************
 A class for encoding and decoding data using the keccak algorithm.

 @version 7/29/20
 *********************************/
public class Crypt
{

    // ----------ATTRIBUTES----------

    private int myI; //=i
    private int myJ; //=j
    private int myR; //=r
    private long myT; //=t
    private long myBC[]; //=bc, some array of size 5.

    //Long values for encoding, I think.
    private static final long[] keccakf_rndc =
            {
                    0x0000000000000001L, 0x0000000000008082L, 0x800000000000808aL,
                    0x8000000080008000L, 0x000000000000808bL, 0x0000000080000001L,
                    0x8000000080008081L, 0x8000000000008009L, 0x000000000000008aL,
                    0x0000000000000088L, 0x0000000080008009L, 0x000000008000000aL,
                    0x000000008000808bL, 0x800000000000008bL, 0x8000000000008089L,
                    0x8000000000008003L, 0x8000000000008002L, 0x8000000000000080L,
                    0x000000000000800aL, 0x800000008000000aL, 0x8000000080008081L,
                    0x8000000000008080L, 0x0000000080000001L, 0x8000000080008008L
            };

    //Int values for encoding
    private static final int keccakf_rotc[] =
            {
                    1,  3,  6,  10, 15, 21, 28, 36, 45, 55, 2,  14,
                    27, 41, 56, 8,  25, 43, 62, 18, 39, 61, 20, 44
            };

    //More int values for encoding
    private static final int keccakf_piln[] =
            {
                    10, 7,  11, 17, 18, 3, 5,  16, 8,  21, 24, 4,
                    15, 23, 19, 13, 12, 2, 20, 14, 22, 9,  6,  1
            }; //index 0 swapped with 10, 1 - 7, etc. combination of row and pi transformation.

    // ----------BEHAVIORS----------


    /*********************************
     KMAC DEFINITION: KMAC concatenates a padded version of the key K with the input X and an encoding of the
     requested output length L. The result is then passed to cSHAKE, along with the requested output
     length L, the name N ="KMAC" = 11010010 10110010 10000010 110000109
     , and the
     optional customization string S.

     @param theK "A key bit string of any length, including 0."
     @param theX "Main input bit string. It may be of any length, including 0."
     @param theL "An integer representing the requested output length (usually 512) in bits."
     @param theS "An optional customization bit string of any length, including 0. If no customization is desired,
     S is set to the empty String."

     @return ----------------------- to do
     *********************************/

    //Putting everything as bytes/byte arrays until we find out exactly what classes these should be.
    public KMACXOF256(byte[] theK, byte[] theX, int theL, byte theS) throws IOException 
    {
        byte[] newXP1 = byteConcatenation(bytepad(encode_string(theK), 136), theX);
        byte[] newXP2 = byteConcatenation(newXP1, right_encode(0));
        return cSHAKE256(newXP2, theL, "KMAC", theS);

    }

    /*********************************
     cSHAKE Definition:

     @param theX "The main input bit string of any length, including 0."
     @param theL "An integer representing the requested output length (usually 512) in bits."
     @param theN "A function-name bit string, used by NIST to define functions based on cShake. When no function
     other than cShake is desired, N is set to the empty string."
     @param theS "Customization bit string. The user selects this string to define a variant of the function. When
     no customization is desired, S is set to the empty string."

     @return ----------------------- to do
     *********************************/

    //Putting everything as bytes/byte arrays until we find out exactly what classes these should be.
    public cSHAKE256(byte[] theX, int theL, byte theN, byte theS) 
    {
    	//These checks are likely supposed to be checking empty BIT STRINGS, not java.
        if (theN = "" && theS = "") 
        {
            return SHAKE256(theX, theL); //doesn't exist.. yet.
        } 
        else 
        {
            byte[] part1 = bytepad(byteConcatenation(encode_string(theN), encode_string(theS)), 136);
            byte[] part2 = byteConcatenation(byteConcatenation(part1, theX), '00'); //Says supposed to be 2 zero bits.
            byte[] part3 = byteConcatenation(part2, theL);
            return KECCAK[512] ;
        }
    }

    /*********************************
     A method for encoding an integer to a byte array with the
     values encoded on the left.

     @param theX Integer to encode
     @return Integer encoded as array of bytes.
     *********************************/
    public byte[] left_encode(int theX) throws IOException
    {
        //Make sure n is big enough to hold the int. It defaults to 2^8, or 256.
        int n = 1;
        while (Math.pow(2, (8 * n)) < theX)
        {
            n++;
        }

        int x = theX;

        //Might need another array, for x1, x2,...xn to be 'base-256 encoding of x'

        //Make a byte array
        byte[] O = new byte[n + 1];

        //Encode the full array. (Encode takes ints though.)
        //Can clean up indexing here.
        for (int i = 1; i <= n; i++)
        {
            O[i] = enc8(x);
            x >>>= 8; //I think this is byte shifting.
        }

        O[0] = enc8(n);

        //-----------------------------------------------------

        //Alt. idea
        byte[] returnByte = new byte[1]; //size 1?
        for (byte X : O)
        {
            byte[] tempByte = new byte[1]; //size 1?
            tempByte[0] = X;
            returnByte = byteConcatenation(returnByte, tempByte);
        }

        return O;
        //return returnByte ?
        //Return O = O0 || O1 || … || On−1 || On.
    }

    /*********************************
     A method for encoding an integer to a byte array with the
     values encoded on the right.

     @param theX Integer to encode
     @return Integer encoded as array of bytes.
     *********************************/
    public byte[] right_encode(int theX) throws IOException
    {
        int n = 1;

        while (Math.pow(2, (8 * n)) < theX)
        {
            n++;
        }

        int x = theX;

        //Might need another array, for x1, x2,...xn to be 'base-256 encoding of x'
        byte[] O = new byte[n + 2];

        for (int i = 1; i <= n; i++)
        {
            O[i] = enc8(x);
            x >>>= 8; //Gets next byte, if any.
        }
        O[n + 1] = enc8(n);

        //Alt. idea
        byte[] returnByte = new byte[1]; //size 1?
        for (byte X : O)
        {
            byte[] tempByte = new byte[1]; //size 1?
            tempByte[0] = X;
            returnByte = byteConcatenation(returnByte, tempByte);
        }

        return O;
        //return returnByte ?
        // Return O = O1 || O2 || … || On || On+1.
    }

    /*********************************
     For an integer i ranging from 0 to 255, enc8(i) is the byte encoding of i,
     with bit 0 being the low-order bit of the byte.

     @param theI Integer to encode
     @return Integer encoded as a byte
     *********************************/
    public byte enc8(int theI)
    {
        if (theI > 255 || theI < 0)
        {
            theI = theI % 256;
            //Seems good.
            if (theI < 0)
            {
                System.out.println("enc8 passed value less than 0, likely an error.");
            }
        }
        byte b = (byte) theI;
        theI = b & 0xFF;
        return (byte) theI;
    }

    /*********************************
     Encodes byte array as character string; old idea.

     @param theS Byte array to encode
     @return Integer encoded as a byte
     *********************************/
    public byte[] encode_string(byte[] theS) throws IOException
    {
        return byteConcatenation(left_encode(theS.length), theS);
    }

    /*********************************
     For strings X and Y, X || Y is the concatenation of X and Y.
     For example, 11001 || 010 = 11001010.

     @param theFirstByte Byte array to concatenate
     @param theSecondByte Byte array to concatenate
     @return Concatenated Byte arrays
     *********************************/
    public byte[] byteConcatenation(byte[] theFirstByte, byte[] theSecondByte) throws IOException
    {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        outputStream.write(theFirstByte);
        outputStream.write(theSecondByte);

        return outputStream.toByteArray();
    }

    /********************************
     Method to ???


     @param theFirstByte
     @param theW
     @return
     *********************************/
    public byte[] bytepad(byte[] theX, int theW) throws IOException
    {
        if (theW > 0)
        {
            byte[] Z = byteConcatenation(left_encode(theW), theX);

            while ((Z.length % 8) != 0)
            {
                byte[] zeroBit = new byte[0]; //However you make a byte that just has one zero in it, do it here.
                Z = byteConcatenation(Z, zeroBit);
            }

            while (((Z.length / 8) % theW) != 0)
            {
                byte[] eightZeroBit = new byte[000000000]; //However you make a byte that is 8 zeroes, do it here.
                Z = byteConcatenation(Z, eightZeroBit);
            }

            return Z;
        }

        return null; //Probably shouldn't receive this, if we do then likely error.
    }

    /********************************
     Method to change Big-Endian encoding to Little-Endian.

     @param numero Long value to re-encode.
     @return Byte array containing re-encoded Long value.
     *********************************/
    private byte[] littleEndian(long numero)
    {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putInt((int) numero);
        return bb.array();
    }

    /********************************
     * Compute a square root of v mod p with a specified
     * least significant bit, if such a root exists.
     *
     * @param v the radicand.
     * @param p the modulus (must satisfy p mod 4 = 3).
     * @param lsb desired least significant bit (true: 1, false: 0).
     * @return a square root r of v mod p with r mod 2 = 1 iff lsb = true
     * if such a root exists, otherwise null.
     *********************************/
    public static BigInteger sqrt(BigInteger v, BigInteger p, boolean lsb)
    {
        assert (p.testBit(0) && p.testBit(1)); // p = 3 (mod 4)

        if (v.signum() == 0)
        {
            return BigInteger.ZERO;
        }

        BigInteger r = v.modPow(p.shiftRight(2).add(BigInteger.ONE), p);

        if (r.testBit(0) != lsb)
        {
            r = p.subtract(r); // correct the lsb
        }

        return (r.multiply(r).subtract(v).mod(p).signum() == 0) ? r : null;
    }

}


/***************
 Notes on this:

 - Left encode, right encode and encode8 are necessary methods.
 - Left and right encode could be simplified a little for readability. They do similar things.
 - The byte concatenation method seems a little overcomplicated for just jamming two arrays together.

 To do:

 - Either understand the backcode enough to be comfortable manipulating or set up a controlled environment
 that you do understand.
 - Test existing methods for accuracy (jUnit?).
 - Add file reading support methods.
 - See what byte and BigInteger classes can do for us that we already do.

 ***************/