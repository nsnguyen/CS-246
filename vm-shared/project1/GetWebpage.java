import java.net.*;
import java.io.*;

public class GetWebpage {
	public static void main(String args[]) throws Exception {
		String website = args[0];
		String line;

		URL url = new URL(website);
		URLConnection conn = url.openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

		while (true) {
			line = br.readLine();
			if (line != null) {
				System.out.println(line);
			} else {
				br.close();
				break;
			}
		}
	}
}
