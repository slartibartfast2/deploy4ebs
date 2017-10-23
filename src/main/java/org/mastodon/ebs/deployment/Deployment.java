package org.mastodon.ebs.deployment;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.mastodon.ebs.deployment.commons.PropertyUtils;
import org.mastodon.ebs.deployment.svn.SvnService;
import org.mastodon.ebs.deployment.svn.SvnServiceFactory;
import org.mastodon.ebs.deployment.svn.SvnType;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Erdem.Akdogan
 */
public abstract class Deployment {
    protected String labelName;
    protected String packageName;
    protected final String deployPackagePath;
    // private final String rootPath;
    protected final File ebsSubDir;
    protected SvnService svnService;
    protected static File rootDir;
    private static final String deploy4ebsPropFileName = "deploy4ebs.properties";

    protected List<String> ignoredFiles = new ArrayList<>();
    private final static Logger logger = Logger.getLogger(Deployment.class);

    public Deployment(final SvnType svnType, final String labelName) {
        this.labelName = labelName;
        this.packageName = "packages_" + labelName;

        this.deployPackagePath = ".." + IOUtils.DIR_SEPARATOR + packageName + IOUtils.DIR_SEPARATOR + "erpDeployment";

        final String ebsSubDirPath = deployPackagePath + IOUtils.DIR_SEPARATOR + "EBS";
        ebsSubDir = new File(ebsSubDirPath);

        if (!ebsSubDir.exists()) {
            throw new RuntimeException(ebsSubDir.getName() + " dizini bulunamadi.");
        }

        this.svnService = new SvnServiceFactory().getService(svnType);

        String rootPath = ".." + IOUtils.DIR_SEPARATOR + packageName;
        rootDir = new File(rootPath);

        try {
            if (rootDir.exists()) {
                FileUtils.deleteQuietly(rootDir);
                logger.info("TEMP dizin silindi." + rootDir.getCanonicalPath());
            }

            FileUtils.forceMkdir(ebsSubDir);
            logger.info("Deploy paketi icin dizin olusturuldu. " + this.ebsSubDir.getCanonicalPath());
        } catch (IOException ioe) {
            throw new RuntimeException("Deploy paket icin dizin olusturulamadi");
        }

        if (!PropertyUtils.exists(deploy4ebsPropFileName)) {
            throw new RuntimeException(deploy4ebsPropFileName + " konfigurasyon dosyasi bulunamadi.");
        }
    }

    public Deployment(final String sourcePath) {
        this.deployPackagePath = sourcePath;

        final String ebsSubDirPath = deployPackagePath + IOUtils.DIR_SEPARATOR + "EBS";
        ebsSubDir = new File(ebsSubDirPath);

        if (!ebsSubDir.exists()) {
            throw new RuntimeException(ebsSubDir.getName() + " dizini bulunamadi.");
        }

        if (!PropertyUtils.exists(deploy4ebsPropFileName)) {
            throw new RuntimeException(deploy4ebsPropFileName + " konfigurasyon dosyasi bulunamadi.");
        }

    }

    public abstract void create(String outputPath);

}
