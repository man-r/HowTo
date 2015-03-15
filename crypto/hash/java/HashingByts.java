import java.security.MessageDigest;

public class HashingByts {

	public HashingByts () {

	}

	public static void main(String[] args)  throws Exception {
		String text = "This is some String";
		byte[] textByts = text.getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(textByts);
		byte[] digest = md.digest();
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0;i<digest.length ; i++) {
			sb.append(String.format("%02x", digest[i]));
		}

		StringBuilder sb2 = new StringBuilder();
		for (int i = 0;i<textByts.length ; i++) {
			sb2.append(String.format("%02x", textByts[i]));
		}
		
		System.out.println("Original:\n" + text);
		System.out.println("text:\n" + sb2.toString());
		System.out.println("Hash:\n" + sb.toString());
	}	
}