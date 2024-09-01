package dev.hudsonprojects.simplechat.common.lib.io;

import java.io.InputStream;
import java.util.Scanner;

public interface IOUtils {
	
	static String readAllText(InputStream in) {
		try(Scanner scanner = new Scanner(in)){
			StringBuilder builder = new StringBuilder();
		
			while (scanner.hasNext()) {
				builder.append(scanner.next());
			}
		
			return builder.toString();
		}
		
	} 

}
