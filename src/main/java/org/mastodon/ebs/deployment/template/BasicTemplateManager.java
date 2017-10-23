package org.mastodon.ebs.deployment.template;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.log4j.Logger;
import org.mastodon.ebs.deployment.DeploymentFolderType;
import org.mastodon.ebs.deployment.domain.BasicFile;
import org.mastodon.ebs.deployment.domain.Extension;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * @author Erdem.Akdogan
 */
public abstract class BasicTemplateManager {
    protected static Configuration cfg;
    protected File mainFolder;
    protected DeploymentFolderType parentDirType;

    private List<String> ignoredFiles;
    private final static Logger logger = Logger.getLogger(BasicTemplateManager.class);

    public BasicTemplateManager(String templateDirectory, File mainFolder, boolean isLocal) throws IOException {
        /* Create and adjust the configuration singleton */
        cfg = new Configuration(Configuration.VERSION_2_3_25);
        if (isLocal) {
            cfg.setDirectoryForTemplateLoading(new File(templateDirectory));
        }
        cfg.setClassForTemplateLoading(this.getClass(), templateDirectory);
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);

        this.ignoredFiles = new ArrayList<>();
        this.mainFolder = mainFolder;
    }

    public List<String> getIgnoredFiles() {
        return ignoredFiles;
    }

    public abstract void create(File folder) throws IOException, TemplateException;

    protected final List<BasicFile> getFiles(File directory, String parentDirName, Extension... extensions) {
        String[] extensionArr = Extension.names(extensions);
        List<File> allFiles = (List<File>) FileUtils.listFiles(directory, FileFileFilter.FILE, TrueFileFilter.INSTANCE);

        List<BasicFile> basicFiles = new ArrayList<>();
        for (File file : allFiles) {
            if (extensionArr.length == 0 || FilenameUtils.isExtension(file.getName(), extensionArr)) {
                try {
                    String name = FilenameUtils.getBaseName(file.getAbsolutePath());
                    String extension = FilenameUtils.getExtension(file.getAbsolutePath());

                    String basePath = FilenameUtils.getPath(file.getAbsolutePath());
                    String partialPath = basePath.substring(basePath.indexOf(parentDirName));

                    basicFiles.add(new BasicFile(name, extension, partialPath));
                } catch (Exception e) {
                    addToIgnoredFiles(file);

                    logger.error("parentDirMame: " + parentDirName);
                    logger.error("mlFile.basePath: " + FilenameUtils.getPath(file.getAbsolutePath()));
                    logger.error("mlFile.name: " + FilenameUtils.getBaseName(file.getAbsolutePath()));
                    logger.error("mlBasicFile: " + file.toString());
                }
            } else {
                ignoredFiles.add(FilenameUtils.getPath(file.getAbsolutePath()));
            }
        }

        return basicFiles;
    }

    protected final void addToIgnoredFiles(File file) {
        try {
            ignoredFiles.add(file.getCanonicalPath());
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }

    protected final void createDeploymentFile(File outDir, String outFileName, String script) {
        try {
            File deployFile = new File(outDir.getParentFile().getParent() + IOUtils.DIR_SEPARATOR + outFileName);
            FileUtils.writeStringToFile(deployFile, script, Charset.forName("UTF-8"), false);
            logger.info(outFileName + " created.");

            File mainDeployFile = new File(outDir.getParentFile().getParent() + IOUtils.DIR_SEPARATOR + "deploy.sh");
            if (!mainDeployFile.exists()) {
                FileUtils.writeStringToFile(mainDeployFile, "#" + now() + "\n", Charset.forName("UTF8"), true);
            }
            FileUtils.writeStringToFile(mainDeployFile, "sh " + outFileName + " $1" + "\n", Charset.forName("UTF-8"), true);
        } catch (IOException e) {
            logger.error("Deployment Sh file cannot be created! Look below for details; ");
            logger.error(e.getMessage(), e);
        }
    }

    private String now() {
        Date currentTime = GregorianCalendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd G 'at' HH:mm:ss z");
        return sdf.format(currentTime);
    }

}
