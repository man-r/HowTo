import java.nio.file.Files;
import java.nio.file.Paths;
import javax.crypto.*;

public class Aes {

    public static void main(String[] args) throws Exception {
    
        String text = "This is some String";

        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        //keyGen.init(128);

        SecretKey secKey = keyGen.generateKey();

        Cipher aesCipher = Cipher.getInstance("AES");


        byte[] byteText = text.getBytes();

        aesCipher.init(Cipher.ENCRYPT_MODE, secKey);
        byte[] byteCipherText = aesCipher.doFinal(byteText);
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0;i<byteCipherText.length ; i++) {
            sb.append(String.format("%02x", byteCipherText[i]));
        }

        System.out.println("Original:\n" + text);
        System.out.println("Cipher:\n" + sb.toString());
    }
}