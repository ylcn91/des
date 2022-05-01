import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class encryptionOrDecryption {

    public static String key, plaintext,encOrDec;
    public static int numOfBlocks;
    public static int[] array, encryptedArray, funcRet;
    public static int[] arrayBlock = new int[64];
    public static int[] encryptedBlock = new int[64];
    public static int[] arrayBlockTransposed = new int[64];
    public static int[] binaryKey64 = new int[64];
    public static int[] binaryKey56 = new int[56];
    public static int[] tempKey56 = new int[56];
    public static int[] tempKey48 = new int[48];
    public static int[] tempKey28 = new int[28];
    public static int[] e = new int[48];
    public static int[] Li_1 = new int[32];
    public static int[] Ri_1 = new int[32];
    public static int[] Li = new int[32];
    public static int[] Ri = new int[32];



    public static String init(String Key, String Plaintext,String EncOrDec) throws UnsupportedEncodingException {
        int rem;
        key = Key;
        plaintext = Plaintext;
        encOrDec = EncOrDec;
        numOfBlocks = (int) Math.ceil((1.0 * plaintext.length()) / 8.0);
        rem = plaintext.length() % 8;
        if (rem != 0) {
            for (int i = 0; i < 8 - rem; i++) {
                plaintext += "~";
            }
        }
        array = new int[numOfBlocks * 64];
        encryptedArray = new int[numOfBlocks * 64];
        encryptOrDecrypt();
        return printCipheredText();
        //print(encryptedArray);
        //keyAtLevelI(2);
    }

    public static String charToBin(char c) {
        int ascii = (int) c;
        String bin = Integer.toBinaryString(ascii);
        int len = bin.length();
        if (len != 8) {
            for (int i = 0; i < 8 - len; i++) {
                bin = "0" + bin;
            }
        }
        return bin;
    }

    public static void plainKeyToBinKey() {
        int len = key.length();
        String bin;
        for (int i = 0; i < len; i++) {
            bin = charToBin(key.charAt(i));
            for (int j = 0; j < 8; j++) {
                binaryKey64[i * 8 + j] = Character.getNumericValue(bin.charAt(j));
            }
        }
        //print(binaryKey64);
    }

    public static void plaintextToBinArray(String text) {
        int len = text.length();
        String bin;
        for (int i = 0; i < len; i++) {
            bin = charToBin(text.charAt(i));
            for (int j = 0; j < 8; j++) {
                array[i * 8 + j] = Character.getNumericValue(bin.charAt(j));
            }
        }
        //print(array);
    }

    public static void print(int[] array) {
        int len = array.length;

        for (int i = 0; i < len; i++) {
            System.out.print(array[i]);
        }
        System.out.println();
    }

    public static void permuteKey1() {
        for (int i = 0; i < 56; i++) {
            binaryKey56[i] = binaryKey64[data.CP_1[i] - 1];
        }
        //print(binaryKey56);
    }

    public static int[] func(int[] R, int[] key) {
        int[] ret = new int[48];
        int[] temp = new int[32];
        for (int i = 0; i < 48; i++) {
            e[i] = R[data.E[i] - 1];
        }
        for (int i = 0; i < 48; i++) {
            ret[i] = e[i] ^ key[i];
        }
        for (int i = 0; i < 32; i++) {
            temp[i] = ret[data.PI_2[i] - 1];
        }
        for (int i = 0; i < 32; i++) {
            ret[i] = temp[data.P[i] - 1];
        }
        return ret;
    }

    public static void keyAtLevelI(int level) {
        int leftRotate = data.SHIFT[level];
        int j = 0;
        int temp[] = new int[leftRotate];

        for (int i = 0; i < 56; i++) {
            tempKey56[i] = binaryKey56[i];
        }
        for (int i = 0; i < 28; i++) {
            tempKey28[i] = tempKey56[i];
        }
        for (int i = 0; i < leftRotate; i++) {
            temp[i] = tempKey28[i];
        }
        for (int i = 0; i < 28 - leftRotate; i++) {
            tempKey28[i] = tempKey28[i + leftRotate];
        }
        for (int i = 28 - leftRotate; i < 28; i++) {
            tempKey28[i] = temp[j];
            j++;
        }
        for (int i = 0; i < 28; i++) {
            tempKey56[i] = tempKey28[i];
        }

        j = 0;

        for (int i = 0; i < 28; i++) {
            tempKey28[i] = tempKey56[i + 28];
        }
        for (int i = 0; i < leftRotate; i++) {
            temp[i] = tempKey28[i];
        }
        for (int i = 0; i < 28 - leftRotate; i++) {
            tempKey28[i] = tempKey28[i + leftRotate];
        }
        for (int i = 28 - leftRotate; i < 28; i++) {
            tempKey28[i] = temp[j];
            j++;
        }
        for (int i = 0; i < 28; i++) {
            tempKey56[i + 28] = tempKey28[i];
        }

        //print(tempKey56);
        for (int i = 0; i < 48; i++) {
            tempKey48[i] = tempKey56[data.CP_2[i] - 1];
        }
        //print(tempKey48);
    }

    public static void finalSwap() {
        int temp;
        for (int i = 0; i < 32; i++) {
            temp = arrayBlockTransposed[i];
            arrayBlockTransposed[i] = arrayBlockTransposed[i + 32];
            arrayBlockTransposed[i + 32] = temp;
        }
    }

    public static String printCipheredText() throws UnsupportedEncodingException {
        int ascii = 0;
        char c;
        String cipherText=new String();
        int len = numOfBlocks * 64;
        int numOfChars = len / 8;
        for (int i = 0; i < numOfChars; i++) {
            ascii = 0;
            for (int j = 0; j < 8; j++) {
                ascii += Math.pow(2, 7 - j) * encryptedArray[i*8 + j];
            }
            if(encOrDec.equalsIgnoreCase("dec")&&ascii==126) continue;
            if (ascii > 256)
                cipherText+="\\u"+Integer.toHexString(ascii);
            c= (char) ascii ;
            cipherText+=c;
            //System.out.print(c);
        }
        //System.out.println();
        /*int len = numOfBlocks * 64;
        String bin = new String();
        String cipherText;
        for (int i = 0; i < len; i++) {
            bin += encryptedArray[i];
        }
        cipherText = binToHex(bin);
        PrintStream out = new PrintStream(System.out, true, "UTF-8");
        out.println(cipherText);*/
        return cipherText;
        //System.out.println(cipherText);
    }

    private static String binToUTF(String bin) {

        // Convert back to String
        byte[] ciphertextBytes = new byte[bin.length() / 8];
        String ciphertext = null;
        for (int j = 0; j < ciphertextBytes.length; j++) {
            String temp = bin.substring(0, 8);
            byte b = (byte) Integer.parseInt(temp, 2);
            ciphertextBytes[j] = b;
            bin = bin.substring(8);
        }

        try {
            ciphertext = new String(ciphertextBytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return ciphertext.trim();
    }

    private static String binToHex(String bin) {

        BigInteger b = new BigInteger(bin, 2);
        String ciphertext = b.toString(16);

        return ciphertext;
    }

    public static void encryptOrDecrypt() {
        plaintextToBinArray(plaintext);
        plainKeyToBinKey();
        permuteKey1();

        for (int i = 0; i < numOfBlocks; i++) {
            encryptOrDecryptBlock(i * 64);
        }
    }

    public static void encryptOrDecryptBlock(int index) {

        for (int i = 0; i < 64; i++) {
            arrayBlock[i] = array[i + index];
        }
        for (int i = 0; i < 64; i++) {
            arrayBlockTransposed[i] = arrayBlock[data.PI[i] - 1];
        }

        for (int i = 0; i < 16; i++) {
            if(encOrDec.equalsIgnoreCase("enc"))
                keyAtLevelI(i);
            else
                keyAtLevelI(15-i);
            for (int j = 0; j < 32; j++) {
                Li[j] = arrayBlockTransposed[j + 32];
                Ri_1[j] = arrayBlockTransposed[j + 32];
                Li_1[j] = arrayBlockTransposed[j];
            }
            funcRet = func(Ri_1, tempKey48);
            for (int j = 0; j < 32; j++) {
                Ri[j] = Li_1[j] ^ funcRet[j];
            }
            for (int j = 0; j < 32; j++) {
                arrayBlockTransposed[j] = Li[j];
                arrayBlockTransposed[j + 32] = Ri[j];
            }
        }
        finalSwap();

        for (int i = 0; i < 64; i++) {
            encryptedBlock[i] = arrayBlockTransposed[data.PI_1[i] - 1];
            encryptedArray[index + i] = encryptedBlock[i];
        }
    }

}

