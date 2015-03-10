import java.nio.file.Files;
import java.nio.file.Paths;
import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;

public class Aes {

    public static void main(String[] args) throws Exception {
    
        String text = "This is some String";

        //KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        //keyGen.init(128);
        byte raw[] = "11111111111111111111111111111111".getBytes();
        SecretKeySpec spec = new SecretKeySpec(raw, "AES");
        //SecretKey secKey = keyGen.generateKey();

        Cipher aesCipher = Cipher.getInstance("AES/CTR/NoPadding");


        byte[] byteText = text.getBytes();

        aesCipher.init(Cipher.ENCRYPT_MODE, spec);
        byte[] byteCipherText = aesCipher.doFinal(byteText);
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0;i<byteCipherText.length ; i++) {
            sb.append(String.format("%02x", byteCipherText[i]));
        }

        System.out.println("Original:\n" + text);
        System.out.println("Cipher:\n" + sb.toString());
    }
}