package org.mastodon.ebs.deployment;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.mastodon.ebs.deployment.commons.ZipUtils;
import org.mastodon.ebs.deployment.template.BasicTemplateManager;
import org.mastodon.ebs.deployment.template.TemplateManagerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by Erdem.Akdogan on 19.10.2017.
 */
public class LocalDeployment extends Deployment {
    private static File packageRootDir;
    private static final Logger logger = Logger.getLogger(LocalDeployment.class);

    public LocalDeployment(String sourcePath) {
        super(sourcePath);

        packageRootDir = new File(deployPackagePath);


        // Add shutdown hook to delete tmp directory when application shutdown
        LocalDeployment.ShutdownHook shutdownHook = new LocalDeployment.ShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    @Override
    public void create(String outputPath) {
        final File ignoredLogFile = new File(deployPackagePath + IOUtils.DIR_SEPARATOR + "ignoredFiles.log");
        final String outZipFilePath = outputPath + IOUtils.DIR_SEPARATOR + "erpDeployment.zip";

        try {

            TemplateManagerFactory factory = new TemplateManagerFactory();
            for (File subDir : ebsSubDir.listFiles(new DeploymentFilter())) {
                String subDirName = subDir.getName();
                try {
                    logger.info(subDirName + " icin deploy paketi olusturuluyor.");
                    BasicTemplateManager templateManager = factory.createDeployTemplate(subDir);
                    if (templateManager != null) {
                        templateManager.create(subDir);

                        ignoredFiles.addAll(templateManager.getIgnoredFiles());
                        logger.info(subDirName + " deploy paketi olusturuldu.");

                    } else {
                        logger.info(subDirName + " icin templateManager olusturulamadi.");
                    }
                } catch (Exception e) {
                    logger.error(subDirName + " paket olusturulurken hata alindi.", e);
                }
            }

            try {
                logger.debug("ignoredFiles list size: " + ignoredFiles.size());
                FileUtils.writeStringToFile(ignoredLogFile, "Ignored Files\n", Charset.forName("UTF-8"), true);
                FileUtils.writeLines(ignoredLogFile, ignoredFiles, "\n", true);

                logger.info("Gozardi edilmis dosyalar icin log dosyasi olusturuldu. " + ignoredLogFile.getCanonicalPath());
            } catch (IOException ioe) {
                logger.error("Gozardi edilmis dosyalar loglanirken hata alindi", ioe);
            }

            // ZipUtils appZip = new ZipUtils(deployPackagePath);
            // appZip.zipIt(outZipFilePath);
            ZipUtils.compressZipfile(deployPackagePath, outZipFilePath);
            logger.info(outZipFilePath + " zip file created.");

        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }
    }

    private static class ShutdownHook extends Thread {

        @Override
        public void run() {
            String[] extensions = {"sh", "log"};

            for (File deploymentFile : FileUtils.listFiles(packageRootDir, extensions, false)) {
                boolean success = FileUtils.deleteQuietly(deploymentFile);
                if (success) {
                    logger.trace(deploymentFile.getName() + " silindi. ");
                } else {
                    if (rootDir.exists()) {
                        logger.error(deploymentFile.getName() + " silinemedi.");
                    }
                }
            }


        }
    }
}
