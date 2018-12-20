/*
 * ReaderUtilities.java
 *
 * Created on September 10, 2006, 6:09 PM
 */

package org.rummage.toolbox.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;

/**
 *
 * @author nnelson
 */
public class ReaderUtilities {
 	/**
     * Reads a single line from the given reader, including EOL
 	 * (\r\n); truncates the line at maxLength characters.
     * 
 	 * @param reader        the reader
 	 * @param maxLength     maximum length of the line
 	 * @return              the line
 	 * @exception EOFException if an EOF is read
 	 * @exception IOException if any other IOException occurs
 	 */
 	public static String readLine(Reader reader, int maxLength)
 		throws IOException
    {
 		StringBuffer buffer = new StringBuffer();
 		short prev;
 		short curr = 0;
        
 		do {
 			prev = curr;
 			curr = (short) reader.read();
            
 			if (curr == -1) {
 				throw new EOFException();
 			}
            
 			if (buffer.length() <= maxLength) {
 				buffer.append((char) curr);
 			}
 		}
        while (!((prev == '\r') && (curr == '\n')));
        
 		return (buffer.toString());
 	}
}
