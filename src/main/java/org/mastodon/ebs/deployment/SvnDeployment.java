package org.mastodon.ebs.deployment;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.mastodon.ebs.deployment.commons.ZipUtils;
import org.mastodon.ebs.deployment.svn.SvnType;
import org.mastodon.ebs.deployment.template.BasicTemplateManager;
import org.mastodon.ebs.deployment.template.TemplateManagerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Created by Erdem.Akdogan on 19.10.2017.
 */
public class SvnDeployment extends Deployment {

    private static final Logger logger = Logger.getLogger(SvnDeployment.class);

    SvnDeployment(SvnType svnType, String labelName) {
        super(svnType, labelName);

        // Add shutdown hook to delete tmp directory when application shutdown
        SvnDeployment.ShutdownHook shutdownHook = new SvnDeployment.ShutdownHook();
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }

    public void create(String outputPath) {
        final File versionLogFile = new File(deployPackagePath + IOUtils.DIR_SEPARATOR + labelName + "_listFileVersions.log");
        final File ignoredLogFile = new File(deployPackagePath + IOUtils.DIR_SEPARATOR + "ignoredFiles.log");
        final String outZipFilePath = outputPath + IOUtils.DIR_SEPARATOR + "erpDeployment.zip";

        try {

            svnService.writeFilesAndDirs(packageName, labelName);

            /*logger.info("Versiyon dosyasi olusturuluyor.");
            svnService.logFiles(versionLogFile, labelName);
            logger.info("Etiketlenmis dosyalar icin versiyon dosyasi olusturuldu. " + versionLogFile.getCanonicalPath());*/

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
            boolean success = FileUtils.deleteQuietly(rootDir);
            if (success) {
                logger.info("TEMP dizin silindi. " + rootDir.getAbsolutePath());
            } else {
                if (rootDir.exists()) {
                    logger.error("TEMP dizin silinemedi." + rootDir.getAbsolutePath());
                }
            }
        }
    }

}
