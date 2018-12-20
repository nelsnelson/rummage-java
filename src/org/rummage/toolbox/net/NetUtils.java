/*
 * NetUtils.java
 *
 * Created on August 2, 2006, 2:03 PM
 */

package org.rummage.toolbox.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.rummage.toolbox.io.BufferedReaderFactory;
import org.rummage.toolbox.util.Text;

/**
 *
 * @author nelsnelson
 */
public class NetUtils {
    /** Creates a new instance of NetUtilities */
    private NetUtils() {
        //stub
    }

    public static URLConnection getConnection(String url) {
        URLConnection connection = null;
        
        try {
            connection = new URL(url).openConnection();
            connection.connect();
        }
        catch (MalformedURLException ex) {
            
        }
        catch (IOException ex) {
            
        }
        
        return connection;
    }
    
    public static InetAddress parseInternetProtocolAddress(String s)
        throws InetAddressParsingException 
    {
        InetAddress address = null;
        
        Text.strip(s, "/"); // This doesn't work.  To do: Figure out why not.
        
        if (s.contains("/")) {
            s = s.substring(1); // Why can't I strip the '/' character above?
        }
        
        byte[] addr = new byte[4];
        
        String[] tuples = Text.explode(s, ".");
        
        for (int i = 0; i < tuples.length; i++) {
            byte b = 0x00;
            
            try {
                b = (byte) Integer.parseInt(tuples[i]);
            }
            catch (NumberFormatException ex) {
                throw new InetAddressParsingException(s);
            }
            
            addr[i] = (byte) (b & 0xFF);
        }
        
        try {
            address = InetAddress.getByAddress(addr);
        }
        catch (UnknownHostException ex) {
            
        }
        
        return address;
    }
    
    public static InetAddress getRemoteAddress(URL server)
        throws InetAddressParsingException 
    {
        InetAddress address = null;
        
        try {
            BufferedReader reader =
                BufferedReaderFactory.getInstance().getReader(server);
            
            // Callback.  Only read one line.  This will block.
            //
            // Oh, and BTW, <code>reader</code> here could be null, but there
            // is no check because the resulting exception will be caught.
            String data = reader.readLine();
            
            address = NetUtils.parseInternetProtocolAddress(data);
        }
        catch (InetAddressParsingException ex) {
            throw ex;
        }
        catch (Exception ex) {
            
        }
        
        return address;
    }
    
    public static InetAddress getRemoteAddress(String serverPath)
        throws InetAddressParsingException 
    {
        InetAddress address = null;
        
        try {
            BufferedReader reader =
                BufferedReaderFactory.getInstance().getReader(serverPath);
            
            // Callback.  Only read one line.  This will block.
            //
            // Oh, and BTW, <code>reader</code> here could be null, but there
            // is no check because the resulting exception will be caught.
            String data = reader.readLine();
            
            address = NetUtils.parseInternetProtocolAddress(data);
        }
        catch (InetAddressParsingException ex) {
            throw ex;
        }
        catch (Exception ex) {
            
        }
        
        return address;
    }
    
    public static InetAddress getLocalHost() throws UnknownHostException {
        try {
            return (InetAddress.getLocalHost());
        }
        catch( Throwable e ) {
            // sometimes get this when changing host name
            // return first non-loopback one
            try{
                Enumeration nis = NetworkInterface.getNetworkInterfaces();

                while (nis.hasMoreElements()) {
                    NetworkInterface ni = (NetworkInterface)nis.nextElement();
                    
                    Enumeration addresses = ni.getInetAddresses();
                    while (addresses.hasMoreElements()) {
                        InetAddress address = 
                            (InetAddress) addresses.nextElement();
                        
                        if (address.isLoopbackAddress() || 
                            address instanceof Inet6Address) 
                        {
                            continue;
                        }

                        return (address);
                    }
                }
            }
            catch (Throwable f) {

            }

            return (InetAddress.getByName("127.0.0.1"));
        }
    }
    
    public static int compare(InetAddress a0, InetAddress a1) {
        return a0.toString().compareTo(a1.toString());
    }
}
