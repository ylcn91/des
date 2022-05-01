import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class desMain {

    public static Scanner scn =new Scanner(System.in);
    public static String key,plaintext,cipherText;

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.print("Key: ");
        key = scn.nextLine();
        System.out.print("Plain text: ");
        plaintext=scn.nextLine();

        cipherText = encryptionOrDecryption.init(key, plaintext,"enc");
        System.out.print("Ciphered: "+cipherText+"\n");

        plaintext=encryptionOrDecryption.init(key, cipherText,"dec");
        System.out.print("Deciphered: "+plaintext+"\n");
    }
}
