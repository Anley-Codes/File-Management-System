
package filemanager.Forms;

import java.io.File;
import java.io.FilenameFilter;


public class FileSearch implements FilenameFilter{
    
    private String initials;

    public FileSearch(String initls) {
        
        this.initials = initls;
    }
    
    

    @Override
    public boolean accept(File dir, String name) {
         return name.startsWith(this.initials);
    }
    
}
