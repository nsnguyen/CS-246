import java.net.*;
import java.io.*;

public class GetWebpage {
	public static void main(String args[]) throws Exception {
		// String builder to build the string of content which will then stream to
		// terminal.
		StringBuilder content = new StringBuilder();
		String website = args[0];
		String line;
		
		URL url = new URL(website);
		URLConnection conn = url.openConnection();
		
		BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		
		while(true) {
			line = br.readLine();
			if(line != null) {
				content.append(line + "\n");
			}
			else {
				br.close();
				break;
			}
		}
		
		System.out.println(content);
	}
}
