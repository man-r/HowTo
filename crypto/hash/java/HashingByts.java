import java.security.MessageDigest;

public class HashingByts {

	public HashingByts () {

	}

	public static void main(String[] args)  throws Exception {
		String text = "This is some String";
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.update(text.getBytes("UTF-8"));
		byte[] digest = md.digest();
		
		StringBuilder sb = new StringBuilder();
		for (int i = 0;i<digest.length ; i++) {
			sb.append(String.format("%02x", digest[i]));
		}
		
		System.out.println("Original:\n" + text);
		System.out.println("Hash:\n" + sb.toString());
	}	
}