package org.mastodon.ebs.deployment.template.impl;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.mastodon.ebs.deployment.DeploymentFolderType;
import org.mastodon.ebs.deployment.commons.PropertyUtils;
import org.mastodon.ebs.deployment.domain.BasicFile;
import org.mastodon.ebs.deployment.domain.Extension;
import org.mastodon.ebs.deployment.domain.Language;
import org.mastodon.ebs.deployment.domain.MultiLanguageFile;
import org.mastodon.ebs.deployment.template.BasicTemplateManager;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author Erdem.Akdogan
 */
public class ReportsTemplateManager extends BasicTemplateManager {
    private static final Extension[] extensions = {Extension.rdf, Extension.rtf, Extension.xdo};
    private static final String templateFileName = "reports-deploy.ftlh";
    private final List<MultiLanguageFile> reportObjects;
    private static final String shfileName = "deploy_reports.sh";
    private final static Logger logger = Logger.getLogger(ReportsTemplateManager.class);

    public ReportsTemplateManager(String templateDirectory, File mainFolder, boolean isLocal) throws IOException {
        super(templateDirectory, mainFolder, isLocal);

        this.parentDirType = DeploymentFolderType.reports;
        this.reportObjects = getFiles(mainFolder);
    }

    @Override
    public void create(File mainFolder) throws IOException, TemplateException {
        logger.debug("reports tipindeki obje sayisi: " + reportObjects.size());

        /* Create a data-model */
        Map<String, Object> root = new HashMap<>();

        Map<String, String> nlsLangProps = PropertyUtils.getValues("nls-lang.properties");
        root.put("nlsLangProps", nlsLangProps);

        String customTopName = PropertyUtils.getValue("deploy4ebs.properties", "custom-top-name");
        root.put("customTop", customTopName);
        root.put("territory", "00");
        String appShortName = PropertyUtils.getValue("deploy4ebs.properties", "app-short-name");
        root.put("appShortName", appShortName);

        root.put("reportObjects", reportObjects);

        /* Get the template (uses cache internally) */
        Template temp = cfg.getTemplate(templateFileName);

        /* Merge data-model with template */
        StringWriter out = new StringWriter();
        temp.process(root, out);

        if (StringUtils.isNotEmpty(out.toString())) {
            createDeploymentFile(mainFolder, shfileName, out.toString());
        }
    }

    protected final List<MultiLanguageFile> getFiles(File reportsDirectory) {
        List<BasicFile> basicReportFiles = super.getFiles(reportsDirectory, parentDirType.name(), extensions);

        List<MultiLanguageFile> mlReportFiles = new ArrayList<>();
        for (BasicFile basicReportFile : basicReportFiles) {
            try {
                logger.trace("Report Basic File: " + basicReportFile.toString());

                String[] dirAttributes = basicReportFile.getPath().split(Pattern.quote(Character.toString(IOUtils.DIR_SEPARATOR)));
                logger.trace("languageStr: " + dirAttributes[2]);

                mlReportFiles.add(new MultiLanguageFile(
                        basicReportFile.getName(), basicReportFile.getExtension(), basicReportFile.getPath(), Language.valueOf(dirAttributes[2])));
            } catch (Exception e) {
                super.addToIgnoredFiles(new File(basicReportFile.getPath() + IOUtils.DIR_SEPARATOR + basicReportFile.getName() + basicReportFile.getExtension()));

                logger.error("basicReportFile.basePath: " + basicReportFile.getPath());
                logger.error("basicReportFile.name: " + basicReportFile.getName());
                logger.error("reportBasicFile: " + basicReportFile.toString());
            }
        }

        return mlReportFiles;
    }
}
