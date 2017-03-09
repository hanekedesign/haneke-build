package com.hanekedesign.build;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DemoFtpUploader {

    private FTPClient ftp;
    public DemoFtpUploader(String address, String user, String password) throws IOException {
        ftp = new FTPClient();

            ftp.connect(address);

            if(!FTPReply.isPositiveCompletion(ftp.getReplyCode())) {
                ftp.disconnect();
                System.err.println("FTP server refused connection.");
                return;
            }

            boolean loggedIn = ftp.login(user, password);
            if(!loggedIn){
                ftp.logout();
                System.err.println("FTP auth failed.");
                return;
            }

            ftp.setFileType(FTP.BINARY_FILE_TYPE);


    }

    public boolean uploadFile(String destination, File file) throws IOException {
        String destinationDirectory = new File(destination).getParent();
        createDirectoryTree(destinationDirectory);

        boolean storedFile = ftp.storeFile(file.getName(), new FileInputStream(file));
        if(!storedFile){
            System.err.println("failed to store the file");
        }
        return storedFile;
    }

    private void createDirectoryTree(String dirTree ) throws IOException {

        boolean dirExists = true;

        //tokenize the string and attempt to change into each directory level.  If you cannot, then start creating.
        String[] directories = dirTree.split("/");
        for (String dir : directories ) {
            if (!dir.isEmpty() ) {
                if (dirExists) {
                    dirExists = ftp.changeWorkingDirectory(dir);
                }
                if (!dirExists) {
                    if (!ftp.makeDirectory(dir)) {
                        throw new IOException("Unable to create remote directory '" + dir + "'.  error='" + ftp.getReplyString()+"'");
                    }
                    if (!ftp.changeWorkingDirectory(dir)) {
                        throw new IOException("Unable to change into newly created remote directory '" + dir + "'.  error='" + ftp.getReplyString()+"'");
                    }
                }
            }
        }
    }
}
