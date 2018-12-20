/*
 * FileManager.java
 *
 * Created on August 31, 2005, 2:12 PM
 */

package org.rummage.toolbox.io;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextArea;

import org.rummage.slide.DirectoryScanListener;
import org.rummage.slide.JDirectoryChooser;
import org.rummage.slide.JOptionPane;
import org.rummage.slide.JProgressIndicator;
import org.rummage.slide.event.DirectoryScanEvent;
import org.rummage.toolbox.util.ObjectUtils;
import org.rummage.toolbox.util.Properties;
import org.rummage.toolbox.util.Text;

/**
 *
 * @author not attributable
 */
public class FileManager {
    public static final int NO_VISUAL = 1;
    public static final int INDICATE_PROGRESS = 1;
    
    private static boolean rememberRecentDirectoryForExtension = false;
    private static File fileChoice = null;
    private static FileFilter fileFilterChoice = null;
    private static DirectoryScanListener directoryScannedDelegate = null;
    private static PropertyChangeListener propertyChangedDelegate = null;
    private static ActionListener actionDelegate = null;
    
    private FileManager() {
        
    }
    
    public static boolean isLink(File file) {
        Boolean isLink = Boolean.FALSE;
        
        // remember, Java doesn't care about unix links, and treats them like
        // good old fasioned files.  But Win32 is another story.
        if (file.getPath().endsWith(".lnk")) {
            try {
                Object shellFolderObject =
                    ObjectUtils.maybeInvokeMethod("sun.awt.shell.ShellFolder",
                    "getShellFolder", file);
                
                isLink = 
                    (Boolean) ObjectUtils.maybeInvokeMethod(shellFolderObject,
                    "isLink");
            }
            catch (Exception ex) {
                // This exception means that the file might be a link, but
                // there is no way to find out.  Not because of a file system 
                // problem, but because Sun moved or deleted its ShellFolder 
                // class.
                // TODO: It'd be nice to figure out a way to represent this
                // ambiguity.
            }
        }
            
        return isLink.booleanValue();
    }
    
    public File maybeResolveLink(File file) {
        File linkedTo = null;
        
        if (file != null && file.getPath().endsWith(".lnk")) {
            try {
                //sun.awt.shell.ShellFolder shellFolder =
                //    sun.awt.shell.ShellFolder.getShellFolder(file);
                Object shellFolderObject =
                    ObjectUtils.maybeInvokeMethod("sun.awt.shell.ShellFolder",
                    "getShellFolder", file);
                
                //linkedTo = (File) shellFolder.getLinkLocation();
                linkedTo = 
                    (File) ObjectUtils.maybeInvokeMethod(shellFolderObject,
                    "getLinkLocation");
            }
            catch (Exception ex) {
                String s = ex.getMessage();
                
                if (s != null) {
                
                // Get link path but strip off "Could Not Find File " the
                // first 20 characters
                String linkPath =
                    ex.getMessage().substring(20, ex.getMessage().length());
                
                linkedTo = new File(linkPath);
                
                }
            }
        }

        return linkedTo == null ? file : linkedTo;
    }
    
    public static String getExtension(File file) {
        String fileName = file == null ? "" : file.getName();
        
        return
            fileName == null || !fileName.contains(".") ?
            "" : fileName.substring(fileName.lastIndexOf(".")); 
    }
    
    public static URL getURLFromFile(File file) {
        // Convert the file object to a URL
        URL url = null;
        
        try {
            url = file.toURI().toURL();
        }
        catch (MalformedURLException e) {
            
        }
        
        return url;
    }
    
    public static File getFileFromURL(URL url) {
        // Convert the URL to a file object
        File file = new File(url.getFile());
        
        return file;
    }
    
    public static File getDirectory(String path) {
        return getDirectory(new File(path));
    }
    
    public static File getDirectory(File file) {
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }
        
        return file;
    }
    
    public static List<String> getFileData(File file)
        throws FileNotFoundException, IOException
    {
        if (file == null) {
            return null;
        }
        
        ArrayList<String> data = new ArrayList<String>();
        
        FileReader fileReader = new FileReader(file);
        
        BufferedReader in = new BufferedReader(fileReader);

        String line = null;
        
        while ((line = in.readLine()) != null) {
            data.add(line);
        }

        in.close();
        
        return data;
    }
    
    public static String getFileText(File file)
        throws FileNotFoundException, IOException
    {
        if (file == null) {
            return null;
        }
        
        String text = "";
        
        FileReader fileReader = new FileReader(file);
        
        BufferedReader in = new BufferedReader(fileReader);
        
        String line = null;
        
        while ((line = in.readLine()) != null) {
            text = text + line;
        }
        
        in.close();
        
        return text;
    }

    public static void loadFileText(File file, JTextArea textArea)
        throws FileNotFoundException, IOException
    {
        loadFileText(file, textArea, NO_VISUAL);
    }
    
    private static JProgressIndicator progress = null;

    public static void loadFileText(File file, JTextArea textArea,
        int notificationMode)
        throws IOException
    {
        if (file == null) {
            return;
        }
        
        String text = "";
        
        BufferedReader in = getReader(file);
        
        String line = null;
        
        textArea.setText(text);
        
        if (notificationMode == INDICATE_PROGRESS) {
            // To do: figure out if this will always work.
            int size = new Long(file.length()).intValue();
            
            progress = new JProgressIndicator(size, "Loading file...");
        }
        
        while ((line = in.readLine()) != null) {
            if (notificationMode == INDICATE_PROGRESS) {
                 // THIS IS <bold>WAY</bold> TOO SLOW!!  WHY?!
                progress.update(line.length());
                
                progress.setStatus(((int) (progress.
                    getPercentComplete()*100.0d)) + "% finished.");
            }
            
            textArea.append(line + "\n");
        }
        
        progress.dispose();
        
        textArea.setCaretPosition(0);
        textArea.setSelectionStart(0);
        textArea.setSelectionEnd(0);
        
        in.close();
    }
    
    public static BufferedReader getReader(File file) {
        if (file == null) {
            return null;
        }
        
        try {
            FileReader fileReader = new FileReader(file);
            
            return new BufferedReader(fileReader);
        }
        catch (FileNotFoundException ex) {
            //ex.printStackTrace();
        }
        
        return null;
    }

    /**
     * Reads file contents, returning it as a String, using System default line separator.
     */
    public static String stringFromFile(File file) throws IOException {
        return stringFromFile(file, System.getProperty("line.separator"));
    }

    /**
     * Reads file contents, returning it as a String, joining lines with provided
     * separator.
     */
    public static String stringFromFile(File file, String joinWith) throws IOException {
        StringBuffer buf = new StringBuffer();
        BufferedReader in = new BufferedReader(new FileReader(file));

        try {
            String line = null;
            while ((line = in.readLine()) != null) {
                buf.append(line).append(joinWith);
            }
        }
        finally {
            in.close();
        }
        return buf.toString();
    }

    /**
     * Copies file contents from source to destination. Makes up for the lack of file
     * copying utilities in Java
     */
    public static boolean copy(File source, File destination) {
        return StreamUtilities.pipe(source, destination);
    }

    /**
     * Save URL contents to a file.
     */
    public static boolean copy(URL source, File destination) {
        return StreamUtilities.pipe(source, destination);
    }

    /**
     * Deletes a file or directory, allowing recursive directory deletion. This is an
     * improved version of File.delete() method.
     */
    public static boolean delete(String filePath, boolean recursive) {
        File file = new File(filePath);
        if (!file.exists()) {
            return true;
        }

        if (!recursive || !file.isDirectory())
            return file.delete();

        String[] list = file.list();
        for (int i = 0; i < list.length; i++) {
            if (!delete(filePath + File.separator + list[i], true))
                return false;
        }

        return file.delete();
    }
    
    public static boolean acceptsFileAsType(File file, FileFilter fileFilter) {
        return
            file != null && file.exists() &&
            fileFilter != null && fileFilter.accept(file);
    }

    public static String openFileAndReadIt() {
        try {
            return getFileText(openFile());
        }
        catch (FileNotFoundException ex) {
            
        }
        catch (IOException ex) {
            
        }
        
        return "";
    }
    
    public static File openFile() {
        return openFile(null, (File) null, (FileFilter[]) null);
    }
    
    public static File openFile(Component parent) {
        return openFile(parent, (File) null, (FileFilter[]) null);
    }
    
    public static File openFile(Component parent, File initialDirectory) {
        return openFile(parent, initialDirectory, (FileFilter[]) null);
    }
    
    public static File openFile(Component parent, FileFilter... fileFilters) {
        return openFile(parent, null, fileFilters);
    }

    public static File openFile(Component parent, File initialDirectory,
        FileFilter... fileFilters)
    {
        return openFile(parent, initialDirectory, null, fileFilters);
    }

    public static File openFile(Component parent, File initialDirectory,
        File initialFile, FileFilter... fileFilters)
    {
        JFileChooser fileChooser = new JFileChooser();
        
        if (initialDirectory == null && fileFilters != null &&
            fileFilters.length == 1)
        {
            initialDirectory = getRecentDirectory(fileFilters[0]);
        }
        
        fileChooser.setCurrentDirectory(initialDirectory);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setDialogTitle("Open");
        fileChooser.setApproveButtonText("Open");
        fileChooser.setApproveButtonMnemonic('O');
        fileChooser.setDragEnabled(true);
        
        fileChooser.setSelectedFile(initialFile);
        
        if (fileFilters != null) for (FileFilter fileFilter : fileFilters) {
            if (fileFilter != null) {
                fileChooser.addChoosableFileFilter(fileFilter);
            }
        }
        
        fileChooser.addDirectoryScanListener(getDirectoryScanDelegate());
        fileChooser.addPropertyChangeListener(getPropertyChangeDelegate());
        
        int selectedOption = fileChooser.showOpenDialog(parent);
        fileChoice = fileChooser.getSelectedFile();

        if (selectedOption != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        
        if (fileChoice == null) {
            return null;
        }
        
        javax.swing.filechooser.FileFilter ff = fileChooser.getFileFilter();
        fileFilterChoice =
            (ff instanceof FileFilter) ? (FileFilter) ff : new FileFilter(ff);
        
        if (fileFilterChoice instanceof FileFilter) {
            setRecentDirectory(fileChooser.getCurrentDirectory(),
                (FileFilter) fileFilterChoice);
        }

        return fileChoice;
    }
    
    public static File[] openFiles() {
        return openFiles(null, null, null);
    }
    
    public static File[] openFiles(Component parent) {
        return openFiles(parent, null, null);
    }
    
    public static File[] openFiles(Component parent, File initialDirectory) {
        return openFiles(parent, initialDirectory, null);
    }
    
    public static File[] openFiles(Component parent, FileFilter fileFilter) {
        return openFiles(parent, null, fileFilter);
    }

    public static File[] openFiles(Component parent, File initialDirectory,
        FileFilter fileFilter)
    {
        return openFiles(parent, initialDirectory, null, fileFilter);
    }

    public static File[] openFiles(Component parent, File initialDirectory,
        File initialFile, FileFilter fileFilter)
    {
        JFileChooser fileChooser = new JFileChooser();
        
        if (initialDirectory == null) {
            initialDirectory = getRecentDirectory(fileFilter);
        }
        
        fileChooser.setCurrentDirectory(initialDirectory);
        fileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        fileChooser.setDialogTitle("Open");
        fileChooser.setApproveButtonText("Open");
        fileChooser.setApproveButtonMnemonic('O');
        fileChooser.setDragEnabled(true);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        fileChooser.setMultiSelectionEnabled(true);
        
        fileChooser.setSelectedFile(initialFile);
        
        if (fileFilter != null) {
            fileChooser.setFileFilter(fileFilter);
        }
        
        int selectedOption = fileChooser.showOpenDialog(parent);
        File[] fileChoices = fileChooser.getSelectedFiles();

        if (selectedOption != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        
        if (fileChoices == null || fileChoices.length == 0) {
            return null;
        }
        
        javax.swing.filechooser.FileFilter ff = fileChooser.getFileFilter();
        fileFilterChoice =
            (ff instanceof FileFilter) ? (FileFilter) ff : new FileFilter(ff);
        
        if (fileFilterChoice instanceof FileFilter) {
            setRecentDirectory(fileChooser.getCurrentDirectory(),
                (FileFilter) fileFilterChoice);
        }

        return fileChoices;
    }
    
    public static void setRecentDirectory(File currentDirectory) {
        setRecentDirectory(currentDirectory, null);
    }
    
    public static void setRecentDirectory(File currentDirectory,
        FileFilter fileFilter)
    {
        if (rememberRecentDirectoryForExtension && fileFilter != null) {
            Properties.setProperty(Properties.RECENT_DIRECTORY +
                "(" + fileFilter + ")", currentDirectory);
        }
        else {
            Properties.setProperty(Properties.RECENT_DIRECTORY,
                currentDirectory);
        }
    }

    public static File getRecentFile() {
        return fileChoice;
    }

    public static FileFilter getRecentFileFilter() {
        return fileFilterChoice;
    }
    
    public static File getRecentDirectory() {
        return getRecentDirectory(null);
    }
    
    public static File getRecentDirectory(FileFilter fileFilter) {
        File recentDirectory = null;
        
        if (rememberRecentDirectoryForExtension && fileFilter != null) {
            recentDirectory =
                Properties.getFile(Properties.RECENT_DIRECTORY + "(" +
                fileFilter + ")");
        }

        if (recentDirectory == null) {
            recentDirectory = Properties.getFile(Properties.RECENT_DIRECTORY);
        }
        
        return recentDirectory;
    }
    
    public static void setRememberRecentDirectoryForExtension(boolean aflag) {
        rememberRecentDirectoryForExtension = aflag;
    }
    
    public static File openDirectory() {
        return openDirectory(null, (File) null);
    }
    
    public static File openDirectory(Component parent) {
        return openDirectory(parent, (File) null);
    }

    public static File openDirectory(Component parent, File initialDirectory) {
        JDirectoryChooser directoryChooser = new JDirectoryChooser(parent);
        
        int selectedOption = directoryChooser.getSelectedOption();
        
        fileChoice = directoryChooser.getCurrentDirectory();
        
        if (selectedOption != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        
        if (fileChoice == null) {
            return null;
        }
        
        setRecentDirectory(fileChoice);
        
        return fileChoice;
    }
    
    public static void exportFileData(File file, List<String> data)
        throws FileNotFoundException, IOException
    {
        if ( file == null || data == null) {
            return;
        }
        
        FileWriter fileWriter = new FileWriter(file);
        
        BufferedWriter out = new BufferedWriter(fileWriter);
        
        for (String line : data) {
            out.write(line);
            out.newLine();
        }

        out.close();
    }
    
    public static File saveAsFile() {
        return saveAsFile(null, null, null, (FileFilter[])null);
    }
    
    public static File saveAsFile(File file) {
        return saveAsFile(null, new File(""), file, (FileFilter[]) null);
    }
    
    public static File saveAsFile(FileFilter... fileFilters) {
        return saveAsFile(null, new File(""), null, fileFilters);
    }

    public static File saveAsFile(String fileName, FileFilter... fileFilters) {
        return
            saveAsFile(null, new File(""), new File(fileName), fileFilters);
    }
    
    public static File saveAsFile(File file, FileFilter... fileFilters) {
        return saveAsFile(null, new File(""), file, fileFilters);
    }
    
    public static File saveAsFile(File initialDirectory, File file) {
        return saveAsFile(null, initialDirectory, file, (FileFilter[]) null);
    }
    
    public static File saveAsFile(File initialDirectory, File file,
        FileFilter... fileFilters)
    {
        return saveAsFile(null, initialDirectory, file, fileFilters);
    }
    
    public static File saveAsFile(Component parent) {
        return saveAsFile(parent, new File(""), null, (FileFilter[]) null);
    }
    
    public static File saveAsFile(Component parent, File file) {
        return saveAsFile(parent, new File(""), file, (FileFilter[]) null);
    }
    
    public static File saveAsFile(Component parent, File file,
        FileFilter... fileFilters)
    {
        return saveAsFile(parent, new File(""), file, fileFilters);
    }
    
    public static File saveAsFile(Component parent, File initialDirectory,
        File file)
    {
        return saveAsFile(parent, initialDirectory, file, (FileFilter[]) null);
    }

    public static File saveAsFile(final Component parent,
        File initialDirectory, File file, FileFilter... fileFilters)
    {
        File fileChoice = null;
        FileFilter bestFileFilter = null;
        
        JFileChooser fileChooser = new JFileChooser();
        
        if (fileFilters == null || fileFilters.length == 0) {
            bestFileFilter =
                new FileFilter(fileChooser.getAcceptAllFileFilter());
        }
        else {
            bestFileFilter = fileFilters[0];
        }
        
        if (initialDirectory == null) {
            initialDirectory = getRecentDirectory(bestFileFilter);
        }
        
        fileChooser.setCurrentDirectory(initialDirectory);
        fileChooser.setSelectedFile(file == null ? bestFileFilter == null ?
            null : new File(bestFileFilter.getExtension()) : file);
        
        fileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        fileChooser.setDialogTitle("Save As");
        fileChooser.setApproveButtonText("Save");
        fileChooser.setApproveButtonMnemonic('S');
        fileChooser.setDragEnabled(true);
        
        if (fileFilters != null) for (FileFilter fileFilter : fileFilters) {
            if (fileFilter != null) {
                fileChooser.setFileFilter(fileFilter);
            }
        }
        
        fileChooser.addActionListener(getActionDelegate());
        //fileChooser.addDirectoryScanListener(getDirectoryScanDelegate());
        fileChooser.addPropertyChangeListener(getPropertyChangeDelegate());

        if (fileChooser.showSaveDialog(null) != JFileChooser.APPROVE_OPTION ||
            (fileChoice = fileChooser.getSelectedFile()) == null) {
            return null;
        }
        
        javax.swing.filechooser.FileFilter ff = fileChooser.getFileFilter();
        fileFilterChoice =
            (ff instanceof FileFilter) ? (FileFilter) ff : new FileFilter(ff);
        
        if (fileFilterChoice instanceof FileFilter) {
            setRecentDirectory(fileChooser.getCurrentDirectory(),
                (FileFilter) fileFilterChoice);
        }
        
        // append a file extension to the file name, unless it already has one
        fileChoice = ensureFileHasExtension(fileChoice, fileFilterChoice);

        return fileChoice;
    }

    private File ensureFileHasExtension() {
        return ensureFileHasExtension(fileChoice, fileFilterChoice);
    }

    private static File ensureFileHasExtension(File fileChoice,
        FileFilter fileFilterChoice)
    {
        String fileExtension = fileFilterChoice.getFileExtension();
        String fileName = fileChoice.getAbsolutePath();
        
        fileName =
            fileName.endsWith(fileExtension) ? fileName : fileName +
            fileExtension;
        
        return new File(fileName);
    }
    
    public static boolean userConfirmsFileReplacement(File file) {
        return userConfirmsFileReplacement(null, file);
    }
    
    public static boolean userConfirmsFileReplacement(Component parent,
        File file)
    {
        JOptionPane optionPane = new JOptionPane();

        String message =
            file.getAbsolutePath() + " already exists.\n" +
            "Do you want to replace it?";

        return
            optionPane.showConfirmDialog(null, message, "Save As",
            JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }

    public static boolean assureFileIsSafeToUnload(Component parent,
        File file, FileFilter fileFilter, List<String> fileData)
    {
        String message = null;
        String title = null;
        
        if (fileData == null || fileData.size() == 0 && file == null) {
            return true;
        }
        
        if (parent instanceof javax.swing.JFrame) {
            title = ((javax.swing.JFrame) parent).getTitle();
        }

        if (file == null) {
            message =
                "The text in the Untitled file has changed.\n" +
                "\n" +
                "Do you want to save changes?";
        }
        else {
            message =
                "The text in the " + file.getAbsolutePath() + " file has " +
                "changed.\n" +
                "\n" +
                "Do you want to save changes?";
        }

        int selectedOption =
            JOptionPane.showOptionDialog(parent, message, title,
            JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE,
            null, null, null, true);

        if (selectedOption == JOptionPane.CANCEL_OPTION) {
            return false;
        }
        else if (selectedOption == JOptionPane.NO_OPTION) {
            return true;
        }
        else if (selectedOption == JOptionPane.YES_OPTION) {
            try {
                if (file == null) {
                    file = saveAsFile(parent, file, fileFilter);
                }
                
                if (file == null) {
                    return false;
                }
                
                exportFileData(file, fileData);
                
                return true;
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        return false;
    }

    public static String getBestMIMEType(String[] MIMETypes) {
        String bestMIMEType =
            Text.getBestMatch(MIMETypes, Text.stripAll(getRecentFileFilter().
            getFileExtensions(), "."));
            
        return bestMIMEType;
    }

    public static List getRecursiveFileListing(File file, FileFilter filter) {
        List fileListing = new ArrayList();
        
        File[] files = file.listFiles();
        
        if (files != null) for (File f : files) {
            if (f.isDirectory()) {
                fileListing.addAll(getRecursiveFileListing(f, filter));
            }
            else if (filter.accept(f)) {
                fileListing.add(f);
            }
        }
        
        return fileListing;
    }
    
    public static DirectoryScanListener getDirectoryScanDelegate() {
        if (directoryScannedDelegate == null) {
            directoryScannedDelegate = new DirectoryScanListener() {
                public void directoryScan(DirectoryScanEvent e) {
                    
                }
            };
        }
        
        return directoryScannedDelegate;
    }
    
    public static PropertyChangeListener getPropertyChangeDelegate() {
        if (propertyChangedDelegate == null) {
            propertyChangedDelegate = new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent e) {
                    if (e.getPropertyName().equals(JFileChooser.
                        FILE_FILTER_CHANGED_PROPERTY))
                    {
                        JFileChooser source = (JFileChooser) e.getSource();
                        
                        de.bug.ging(source.getSelectedFile());
                        
                        source.setSelectedFile(source.getSelectedFile());
                    }
                    else if (e.getPropertyName().equals(JFileChooser.
                        FILE_SYSTEM_VIEW_CHANGED_PROPERTY))
                    {
                        
                    }
                    else if (e.getPropertyName().equals(JFileChooser.
                        FILE_VIEW_CHANGED_PROPERTY))
                    {
                        
                    }
                }
            };
        }
        
        return propertyChangedDelegate;
    }
    
    public static ActionListener getActionDelegate() {
        if (actionDelegate == null) {
            actionDelegate = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    
                }
            };
        }
        
        return actionDelegate;
    }
}

class JFileChooser
    extends javax.swing.JFileChooser
{
    List<DirectoryScanListener> directoryScanListeners = null;
    
    /**
     * Called by the UI when the user hits the Approve button
     * (labeled "Open" or "Save", by default). This can also be
     * called by the programmer.
     * This method causes an action event to fire
     * with the command string equal to
     * <code>APPROVE_SELECTION</code>.
     *
     * @see #APPROVE_SELECTION
     */
    public void approveSelection() {
        // Open or Save was clicked
        File fileChoice = getSelectedFile();
        
        // If this is a save dialog, and a file at the given path already
        // exists, then ask the user to confirm its replacement.
        //
        // Caution: just because the approve button was clicked
        // does not mean that the getSelectedFile() method in the
        // JFileChooser class will not return a null value.  Its
        // result <i>must</i> be checked for null to avoid possible
        // NullPointerException errors.
        if (fileChoice != null && fileChoice.exists() &&
            getDialogType() == SAVE_DIALOG &&
            !FileManager.userConfirmsFileReplacement(fileChoice))
        {
            return;
        }
        
        super.approveSelection();
    }

    /**
     * Sets the current file filter. The file filter is used by the
     * file chooser to filter out files from the user's view.
     */
    public void setFileFilter(FileFilter filter) {
        File selectedFile = getSelectedFile();
        File currentDirectory =
            selectedFile == null ? null : selectedFile.getParentFile();
        
        super.setFileFilter(filter);
        
        setSelectedFile(selectedFile);
    }

    /**
     * Tells the UI to rescan its files list from the current directory.
     */
    public void rescanCurrentDirectory() {
        File selectedFile = getSelectedFile();
        File currentDirectory =
            selectedFile == null ? null : selectedFile.getParentFile();
        
        super.rescanCurrentDirectory();
        
        setSelectedFile(selectedFile);
        
        fireDirectoryScanEvent(currentDirectory, selectedFile);
    }
    
    protected void fireDirectoryScanEvent(File currentDirectory,
        File selectedFile)
    {
        List<DirectoryScanListener> listeners = getDirectoryScanListeners();
        
        for (DirectoryScanListener l : listeners) {
            l.directoryScan(new DirectoryScanEvent(this, currentDirectory,
                selectedFile));
        }
    }
    
    public void addDirectoryScanListener(DirectoryScanListener listener) {
        List<DirectoryScanListener> listeners = getDirectoryScanListeners();
        
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }
    
    public List<DirectoryScanListener> getDirectoryScanListeners() {
        if (directoryScanListeners == null) {
            directoryScanListeners = new ArrayList();
        }
        
        return directoryScanListeners;
    }
}
