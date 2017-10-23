package org.mastodon.ebs.deployment.commons;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Erdem.Akdogan
 */
public class ZipUtils {

    public static void compressZipfile(String sourceDir, String outputFile) throws IOException {
        ZipOutputStream zipFile = new ZipOutputStream(new FileOutputStream(outputFile));
        Path srcPath = Paths.get(sourceDir);
        compressDirectoryToZipfile(srcPath.getParent().toString(), srcPath.getFileName().toString(), zipFile);
        IOUtils.closeQuietly(zipFile);
    }

    private static void compressDirectoryToZipfile(String rootDir, String sourceDir, ZipOutputStream out) throws IOException {
        String dir = Paths.get(rootDir, sourceDir).toString();
        for (File file : new File(dir).listFiles()) {
            if (file.isDirectory()) {
                compressDirectoryToZipfile(rootDir, Paths.get(sourceDir, file.getName()).toString(), out);
            } else {
                ZipEntry entry = new ZipEntry(Paths.get(sourceDir, file.getName()).toString());
                out.putNextEntry(entry);

                FileInputStream in = new FileInputStream(Paths.get(rootDir, sourceDir, file.getName()).toString());
                IOUtils.copy(in, out);
                IOUtils.closeQuietly(in);
            }
        }
    }
}
