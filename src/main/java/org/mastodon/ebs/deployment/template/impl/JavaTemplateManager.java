package org.mastodon.ebs.deployment.template.impl;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.mastodon.ebs.deployment.DeploymentFolderType;
import org.mastodon.ebs.deployment.domain.BasicFile;
import org.mastodon.ebs.deployment.domain.Extension;
import org.mastodon.ebs.deployment.template.BasicTemplateManager;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ErdemA.
 */
public class JavaTemplateManager extends BasicTemplateManager {
    private static final Extension[] extensions = {Extension.xml, Extension.jsp, Extension.xlf, Extension.java};
    private static final String templateFileName = "java-deploy.ftlh";
    private final List<BasicFile> javaObjects;
    private static String shfileName = "deploy_java.sh";
    private final static Logger logger = Logger.getLogger(JavaTemplateManager.class);

    public JavaTemplateManager(String templateDirectory, File mainFolder, boolean isLocal) throws IOException {
        super(templateDirectory, mainFolder, isLocal);

        this.parentDirType = DeploymentFolderType.java;
        this.javaObjects = getFiles(mainFolder);
    }

    @Override
    public void create(File mainFolder) throws IOException, TemplateException {
        logger.debug("java tipindeki obje sayisi: " + javaObjects.size());

        /* Create a data-model */
        Map<String, Object> root = new HashMap<>();

        String oaHtmlExists = dirExists(new File(mainFolder.getParent() + IOUtils.DIR_SEPARATOR + "OA_HTML"));
        root.put("oaHtmlExists", oaHtmlExists);

        String oaMediaExists = dirExists(new File(mainFolder.getParent() + IOUtils.DIR_SEPARATOR + "OA_MEDIA"));
        root.put("oaMediaExists", oaMediaExists);

        root.put("javaObjects", javaObjects);

        /* Get the template (uses cache internally) */
        Template temp = cfg.getTemplate(templateFileName);

        /* Merge data-model with template */
        StringWriter out = new StringWriter();
        temp.process(root, out);

        if (StringUtils.isNotEmpty(out.toString())) {
            createDeploymentFile(mainFolder, shfileName, out.toString());
        }
    }

    protected final List<BasicFile> getFiles(File javaDirectory) {
        List<BasicFile> javaBasicFiles = super.getFiles(javaDirectory, parentDirType.name(), extensions);

        logger.trace("javaDirectory: " + javaDirectory.getPath());
        File oaHtmlDir = new File(javaDirectory.getAbsolutePath() + IOUtils.DIR_SEPARATOR + ".." + IOUtils.DIR_SEPARATOR + DeploymentFolderType.OA_HTML.name());
        if (oaHtmlDir.exists()) {
            logger.trace("oaHtmlDir: " + oaHtmlDir.getPath());
            javaBasicFiles.addAll(super.getFiles(oaHtmlDir, DeploymentFolderType.OA_HTML.name(), Extension.jsp));
        }

        logger.debug("javaBasicFiles size before file check: " + javaBasicFiles.size());

        int localizationObjects = 0;
        int personalizationObjects = 0;
        int webuiObjects = 0;
        int jspObjects = 0;

        for (int i = 0; i < javaBasicFiles.size(); i++) {
            BasicFile javaBasicFile = javaBasicFiles.get(i);
            if (StringUtils.contains(javaBasicFile.getExtension(), "xlf")) {
                if (StringUtils.contains(javaBasicFile.getPath(), "localization")) {
                    localizationObjects++;
                } else {
                    javaBasicFiles.remove(i);
                    super.addToIgnoredFiles(new File(javaBasicFile.getPath() + IOUtils.DIR_SEPARATOR + javaBasicFile.getName() + javaBasicFile.getExtension()));
                }
            } else if (StringUtils.contains(javaBasicFile.getExtension(), "xml")) {
                if (StringUtils.contains(javaBasicFile.getPath(), "personalization")) {
                    personalizationObjects++;
                } else if (StringUtils.contains(javaBasicFile.getPath(), "webui")) {
                    webuiObjects++;
                } else {
                    javaBasicFiles.remove(i);
                    super.addToIgnoredFiles(new File(javaBasicFile.getPath() + IOUtils.DIR_SEPARATOR + javaBasicFile.getName() + javaBasicFile.getExtension()));
                }
            } else if (StringUtils.contains(javaBasicFile.getExtension(), "jsp")) {
                if (StringUtils.contains(javaBasicFile.getPath(), "OA_HTML")) {
                    jspObjects++;
                } else {
                    javaBasicFiles.remove(i);
                    super.addToIgnoredFiles(new File(javaBasicFile.getPath() + IOUtils.DIR_SEPARATOR + javaBasicFile.getName() + javaBasicFile.getExtension()));
                }
            }
        }

        logger.debug("javaBasicFiles size after file check: " + javaBasicFiles.size());
        logger.debug("localization object size: " + localizationObjects);
        logger.debug("personalization object size: " + personalizationObjects);
        logger.debug("webui object size: " + webuiObjects);
        logger.debug("jsp object size: " + jspObjects);

        return javaBasicFiles;
    }

    private String dirExists(File dir) {
        logger.trace("dir: " + dir.getAbsolutePath() + " exists ==> " + dir.exists());
        return dir.exists() && FileUtils.listFiles(dir, null, true).size() > 0 ? "yes" : "no";
    }
}
