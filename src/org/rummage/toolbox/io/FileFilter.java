/*
 * FileFilter.java
 *
 * Created on October 5, 2005, 6:02 PM
 */

package org.rummage.toolbox.io;

import java.io.File;
import java.util.List;

import org.rummage.toolbox.util.Text;

/**
 *
 * @author not attributable
 */
public class FileFilter
    extends javax.swing.filechooser.FileFilter
{
    private static final String ALL = "*.*";
    private static final String ALL_FILES = "All Files";
    
    private List<String> extensionsList =
        Text.listify(new String[] {ALL});
    
    private String description = ALL_FILES;
    
    private javax.swing.filechooser.FileFilter fileFilter = null;
    
    public FileFilter() {}
    
    public FileFilter(String extension) {
        extensionsList.clear();
        extensionsList.add(extension);
    }
    
    public FileFilter(String description, String... extensions) {
        this.description = description;
        
        extensionsList.clear();
        extensionsList.addAll(Text.listify(extensions));
    }
    
    public FileFilter(javax.swing.filechooser.FileFilter fileFilter) {
        this.fileFilter = fileFilter;
    }

    public boolean accept(File file) {
        return
            isWrapper() ? fileFilter.accept(file) :
            isAll() ? true : file.isDirectory() ||
            extensionsList.contains(getExtension(file));
    }

    public String getDescription() {
        return
            isWrapper() ? fileFilter.getDescription() :
            extensionsList.size() == 0 ? "" :
            isAll() ? ALL_FILES : description + " (" +
            ((extensionsList.size() > 1 ? Text.join(extensionsList, ";") :
                extensionsList.get(0)) + ")");
    }
    
    public String toString() {
        return getDescription();
    }

    /**
     * Returns all file extensions for this <code>FileFilter</code> with
     * wildcards, i.e.: "*.txt".
     *
     * @returns List<String>
     */
    public List<String> getFileExtensions() {
        return Text.stripAll(extensionsList, "*");
    }

    /**
     * Returns the primary file extension for this <code>FileFilter</code>
     * without wildcards, i.e.: "txt".
     *
     * @returns String
     */
    public String getFileExtension() {
        return Text.strip(extensionsList.get(0), "*");
    }

    /**
     * Returns all file extensions for this <code>FileFilter</code> with
     * wildcards, i.e.: "*.txt".
     *
     * @returns List<String>
     */
    public List<String> getExtensions() {
        return extensionsList;
    }

    /**
     * Returns the primary file extension for this <code>FileFilter</code>
     * with wildcards, i.e.: "*.txt".
     *
     * @returns String
     */
    public String getExtension() {
        return extensionsList.get(0);
    }
    
    /**
     * Utility method to determine the file extension of a given file.
     */
    private static String getExtension(File file) {
        String fileName = file.getName().toLowerCase();
        
        if (fileName.contains(".")) {
            return "*" + fileName.substring(fileName.lastIndexOf("."));
        }
        else {
            return "*.*";
        }
    }
    
    private boolean isAll() {
        return extensionsList.size() > 0 && extensionsList.get(0).equals(ALL);
    }
    
    private boolean isWrapper() {
        return fileFilter != null;
    }
}
