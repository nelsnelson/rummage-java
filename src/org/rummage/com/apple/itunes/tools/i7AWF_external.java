/**
 * i7AWF.java
 * 
 * Created on September 20, 2006, 6:20 PM
 * Copyright (c) 2006 Nels Nelson <nnelson@cs.utsa.edu>
 * 
 * Licensed under the Creative Commons share-alike license.
 * 
 * The person or persons who have associated work with this document (the
 * "Dedicator" or "Certifier") hereby either (a) certifies that, to the best
 * of his knowledge, the work of authorship identified is in the public domain
 * of the country from which the work is published, or (b) hereby dedicates
 * whatever copyright the dedicators holds in the work of authorship
 * identified below (the "Work") to the public domain. A certifier, moreover,
 * dedicates any copyright interest he may have in the associated work, and
 * for these purposes, is described as a "dedicator" below.
 *
 * A certifier has taken reasonable steps to verify the copyright status of
 * this work. Certifier recognizes that his good faith efforts may not shield
 * him from liability if in fact the work certified is not in the public
 * domain.
 * 
 * Dedicator makes this dedication for the benefit of the public at large and
 * to the detriment of the Dedicator's heirs and successors. Dedicator intends
 * this dedication to be an overt act of relinquishment in perpetuity of all
 * present and future rights under copyright law, whether vested or
 * contingent, in the Work. Dedicator understands that such relinquishment of
 * all rights includes the relinquishment of all rights to enforce (by lawsuit
 * or otherwise) those copyrights in the Work.
 * 
 * Dedicator recognizes that, once placed in the public domain, the Work may
 * be freely reproduced, distributed, transmitted, used, modified, built upon,
 * or otherwise exploited by anyone for any purpose, commercial or
 * non-commercial, and in any way, including by methods that have not yet been
 * invented or conceived.
 *
 * For the original transcript of the Creative Commons Public Domain
 * Share-alike License, please visit: <a href="http://creativecommons.org/licenses/publicdomain/">http://creativecommons.org/licenses/publicdomain/</a>
 * 
 * 
 * Based on i7AWF.pm by Jesper Noehr originally posted at <a href="http://printf.dk/itunes_7_fetching_artwork_for.html">printf.dk:
 * iTunes 7: Fetching artwork for fun and profit</a>
 * 
 * and also on fetcher.cs (presumably by somebody named aKzenT who apparently
 * wished to remain anonymous and neglected to include a URL or E-mail address
 * with his Comments post at <a href="http://printf.dk/itunes_7_fetching_artwork_for.html#comment-14134">printf.dk: iTunes 7:
 * Fetching artwork for fun and profit</a>).
 *
 *
 * This program requires the Apache Jakarta Commons Codec,
 * <code>org.apache.commons.codec.binary.Base64</code> which can be downloaded
 * at <a href="http://jakarta.apache.org/site/downloads/downloads_commons-codec.cgi">http://jakarta.apache.org/site/downloads/downloads_commons-codec.cgi</a>
 * 
 *
 * Fetches (free) artwork from iTMS, iTunes 7 style.
 * Copyright (c) 2006 Jesper Noehr <jesper@noehr.org>
 * Version 0.1 - Licensed under the Creative Commons share-alike license.
 */

package org.rummage.com.apple.itunes.tools;


/**
 * <summary>
 * Usage:
 * i7AWF f = new i7AWF();
 * 
 * // Check for failure
 * if (!f.fetch(artist, album))
 *     return;
 * 
 * // Write result to file
 * f.writeToFile(artist + " - " + album + ".jpg");
 * 
 * // Alternatively:
 * 
 * // Get Image object
 * java.awt.Image img = f.getImage();
 * 
 * // Get IinputStream
 * java.io.InputStream stream = f.getStream();
 * </summary>
 * 
 * @author "Nels N. Nelson" <nnelson@cs.utsa.edu>
 */
public class i7AWF_external {
    // Define some constants. Notice the i7STORE, which points to the local
    // store. Change if you want to.
    public String i7STORE = "143457";
    
    // Spoof
    public String i7UserAgent = "iTunes/7.0 (Macintosh; U; PPC Mac OS X 10.4.7)";
    
    public String i7URL =
        "http://ax.phobos.apple.com.edgesuite.net/WebObjects/MZSearch.woa" +
        "/wa/coverArtMatch?an=%s&pn=%s";
    
    private String artist = null;
    private String album = null;
    private String url = null;
    private String eTag = null;
    private String contentMd5 = null;
    private String contentLength = null;
    private String contentType = null;
    private java.io.InputStream imageStream = null;
    private java.awt.image.BufferedImage image = null;

    
    /** Creates a new instance of i7AWF */
    public i7AWF_external() {
        // does nothing
    }
    
    /** Creates a new instance of i7AWF */
    public i7AWF_external(String artist, String album) {
        this.artist = artist;
        this.album = album;
        
        fetch(artist, album);
    }
    
    /**
     * Setters
     */
    
    public void setArtist(String artist) {
        this.artist = artist;
    }
    
    public void setAlbum(String album) {
        this.album = album;
    }

    public void writeToFile(String file) {
        if (imageStream == null) {
            return;
        }
        
        try {
            image = javax.imageio.ImageIO.read(imageStream);
            
            javax.imageio.ImageIO.write(image, "PNG",
                new java.io.File(file));
        }
        catch (java.io.IOException ex) {
            
        }
    }
    
    public java.awt.Image getImage() {
        if (image != null) {
            return image;
        }
        
        if (imageStream == null) {
            return null;
        }
        
        try {
            image = javax.imageio.ImageIO.read(imageStream);
            
            return (java.awt.Image) image;
        }
        catch (java.io.IOException ex) {
            return null;
        }
    }
    
    public java.io.InputStream getStream() {
        if (imageStream == null) {
            return null;
        }
        
        return imageStream;
    }
    
    public String getETag() {
        return eTag;
    }
    
    public String getContentMd5() {
        return contentMd5;
    }
    
    public String getContentLength() {
        return contentLength;
    }
    
    public String getContentType() {
        return contentType;
    }
    
    // Actual request. Creates new UA if not used before, and creates a new
    // request object, per call.
    // Caches result, and passes on to some nifty XML parsers. Notice content
    // is GZIP'ed.
    public boolean fetch(String artist, String album) {
        try {
            String userAgent = i7UserAgent;
            String url = String.format(i7URL, artist, album);
            
            System.out.println("URL to fetch: " + url);
            
            java.net.URLConnection client =
                new java.net.URL(url).openConnection();
            
            client.setDoInput(true);
            client.setDoOutput(true);
            client.setUseCaches(false);
            client.setRequestProperty("User-Agent", userAgent);
            client.setRequestProperty("X-Apple-Tz", "7200");
            client.setRequestProperty("X-Apple-Store-Front", i7STORE);
            client.setRequestProperty("Accept-Language","en-us, en;q=0.50");
            client.setRequestProperty("X-Apple-Validation", computeSeed(url, userAgent));
            client.setRequestProperty("Accept-Encoding", "gzip, x-aes-cbc");
            
            client.connect();
            
            java.io.InputStream in = client.getInputStream();
            
            String coverUrl = getURL(new java.util.zip.GZIPInputStream(in));
            
            //DEBUGGING
            System.out.println("Image URL: " + coverUrl);
            
            if (coverUrl == null) {
                return false;
            }
            
            java.net.URLConnection imageSource =
                new java.net.URL(coverUrl).openConnection();
            
            imageSource.setDoInput(true);
            imageSource.setDoOutput(false);
            imageSource.setUseCaches(false);
            
            imageSource.connect();
            
            imageStream = imageSource.getInputStream();
            
            eTag = imageSource.getHeaderField("ETag");
            contentMd5 = imageSource.getHeaderField("Content-MD5");
            contentLength = imageSource.getHeaderField("Content-Length");
            contentType = imageSource.getHeaderField("Content-Type");
            
            return true;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            
            return false;
        }
    }
    
    public String getURL() {
        if (url == null) {
            try {
                url = getURL(imageStream);
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        
        return url;
    }
    
    /**
     * Returns the URL (if found), or undef on error. Payload calls this, but
     * you can call it manually on the cached result.
     */
    public String getURL(java.io.InputStream in) throws
        javax.xml.parsers.ParserConfigurationException,
        org.xml.sax.SAXException,
        javax.xml.transform.TransformerConfigurationException,
        javax.xml.transform.TransformerException,
        java.io.IOException
    {
        
        java.io.BufferedReader reader =
            new java.io.BufferedReader(new java.io.InputStreamReader(in));
        
        org.xml.sax.InputSource source =
            new org.xml.sax.InputSource(in);
        
        final javax.xml.parsers.SAXParserFactory saxFactory =
            javax.xml.parsers.SAXParserFactory.newInstance();
        
        saxFactory.setNamespaceAware(true);
        
        javax.xml.parsers.SAXParser parser = saxFactory.newSAXParser();
        org.xml.sax.XMLReader xmlReader = parser.getXMLReader();
        
        javax.xml.transform.TransformerFactory factory =
            javax.xml.transform.TransformerFactory.newInstance();
        
        javax.xml.transform.Transformer transformer = factory.newTransformer();
        
        javax.xml.transform.dom.DOMResult result =
            new javax.xml.transform.dom.DOMResult();
        
        transformer.transform(new javax.xml.transform.sax.
            SAXSource(xmlReader, source), result);
        
        org.w3c.dom.Document doc = (org.w3c.dom.Document) result.getNode();
        
        in.close();
        
        System.out.println(doc.getDocumentElement().getTextContent());
        
        // This is not ideal. This mess of w3c XML API chained expressions
        // only retrieves the "dict" node. (Heh. I said, "'dict' node".)
        //
        // Also note that the doc contains much more information, including
        // iTunes stock file labels, and so on. This info could easily be made
        // accessible via some getter methods, and the file parameter of the
        // <code>writeToFile</code> method could be done away with, in this
        // way ensuring that the resulting image file is "correctly" named.
        // Or, at least, as correctly as possible.
        org.w3c.dom.Node dictNode =
            doc.getDocumentElement().getFirstChild().getNextSibling().
            getFirstChild().getNextSibling().getFirstChild().getNextSibling();
        
        org.w3c.dom.NodeList children =
            dictNode.getChildNodes();
        
        String key = "";
        String value = null;
        
        java.util.LinkedHashMap<String, String> dictData =
            new java.util.LinkedHashMap<String, String>();
        
        for (int i = 0, n = children.getLength(); i < n; i++) {
            org.w3c.dom.Node childNode = children.item(i);
            
            if (childNode.getNodeName() == "key") {
                key = childNode.getTextContent();
            }
            
            if (childNode.getNodeName() == "string" ||
                childNode.getNodeName() == "integer")
            {
                value = childNode.getTextContent();
                
                dictData.put(key, value);
            }
        }
        
        //Check result
        if (dictData.get("status") == null ||
            !dictData.get("status").equals("0"))
        {
            return null;
        }
        
        url = dictData.get("cover-art-url");
        
        return url;
    }
    
    /**
     * Some magic. Generates a seed we use for X-Apple-Validation. Adapted
     * from LWP::UserAgent::iTMS_Client.
     */
    private String computeSeed(String url, String userAgent) {
        try {
            java.net.URL uri = new java.net.URL(url);
            
            String random =
                ((int) (Math.random() * 0x10000)) + "" +
                ((int) (Math.random() * 0x10000));
            
            String staticData =
                new String(org.apache.commons.codec.binary.Base64.
                decodeBase64("ROkjAaKid4EUF5kGtTNn3Q==".getBytes()));
            
            String urlEnd = uri.getPath() + "?" + uri.getQuery();
            String digest  = md5(urlEnd + userAgent + staticData + random);
            
            return random + '-' + digest.toUpperCase();
        }
        catch (Exception ex) {
            return null;
        }
    }
    
    private String md5(String p) {
        byte[] data = p.getBytes();
        
        try {
            java.security.MessageDigest messageDigest =
                java.security.MessageDigest.getInstance("MD5");
            
            data = messageDigest.digest(data);
        }
        catch (java.security.NoSuchAlgorithmException ex) {
            return null;
        }
        
        String result = "";
        
        for (byte b : data) {
            result += Byte.toString(b);
        }
        
        return result;
    }
}

/**
 * Just a quick little demo app.
 */
class i7AWFDemo_external {
    /** Creates a new instance of i7AWFDemo */
    public i7AWFDemo_external(Object... options) {
        String artist = (String) options[0];
        String album = (String) options[1];
        
        i7AWF_external f = new i7AWF_external();
        
        // Check for failure
        if (!f.fetch(artist, album))
            return;
        
        // Write result to file
        f.writeToFile(artist + " - " + album + ".jpg");
        
        // Alternatively:
        
        // Get Image object
        java.awt.Image img = f.getImage();
        
        // Get ImageStream
        java.io.InputStream stream = f.getStream();
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.util.List<String> options = new java.util.ArrayList<String>();
        
        // Note: command line argument parameters must be escaped using URL
        // encoding characters and then decoded here.
        //
        // For instance: "Dire%20Straits Sultans%20of%20Swing"
        for (String s : args) {
            try {
                options.add(java.net.URLDecoder.decode(s, "UTF-8"));
            }
            catch (java.io.UnsupportedEncodingException ex) {
                ex.printStackTrace();
            }
        }
        
        if (options.size() == 2 &&
            options.get(0) != null &&
            options.get(1) != null)
        {
            new i7AWFDemo_external(options.toArray());
        }
        
        // Or, just do something like this:
        
        new i7AWF_external("Portishead", "Portishead").writeToFile("Portishead - Portishead.jpg");
        new i7AWF_external("Miles Davis", "Birth of the Cool").writeToFile("Miles Davis - Birth of the Cool.jpg");
    }
}
