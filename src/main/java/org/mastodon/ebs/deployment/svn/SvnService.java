package org.mastodon.ebs.deployment.svn;

import java.io.File;

/**
 * @author Erdem.Akdogan
 */
public interface SvnService {
    void writeFilesAndDirs(String outPackageName, String labelName);

    // void writeFiles(String outPackageName, String includeLabel, String excludeLabel);

    void logFiles(File outFile, String labelName);
}
