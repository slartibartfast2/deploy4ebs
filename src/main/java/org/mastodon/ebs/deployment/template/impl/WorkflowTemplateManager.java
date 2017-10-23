package org.mastodon.ebs.deployment.template.impl;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.mastodon.ebs.deployment.DeploymentFolderType;
import org.mastodon.ebs.deployment.commons.PropertyUtils;
import org.mastodon.ebs.deployment.domain.Extension;
import org.mastodon.ebs.deployment.domain.MultiLanguageFile;
import org.mastodon.ebs.deployment.template.MultiLanguageTemplateManager;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Erdem.Akdogan
 */
public class WorkflowTemplateManager extends MultiLanguageTemplateManager {
    private static final Extension extension = Extension.wft;
    private static final String templateFileName = "workflow-deploy.ftlh";
    private final List<MultiLanguageFile> wfObjects;
    private static final String shfileName = "deploy_wf.sh";
    private final static Logger logger = Logger.getLogger(WorkflowTemplateManager.class);

    public WorkflowTemplateManager(String templateDirectory, File mainFolder, boolean isLocal) throws IOException {
        super(templateDirectory, mainFolder, isLocal);

        this.parentDirType = DeploymentFolderType.wft;
        this.wfObjects = getFiles(mainFolder);
    }

    @Override
    public void create(File mainFolder) throws IOException, TemplateException {
        logger.debug("workflow tipindeki obje sayisi: " + wfObjects.size());

        /* Create a data-model */
        Map<String, Object> root = new HashMap<>();

        Map<String, String> nlsLangProps = PropertyUtils.getValues("nls-lang.properties");
        root.put("nlsLangProps", nlsLangProps);

        root.put("wfObjects", wfObjects);

        /* Get the template (uses cache internally) */
        Template temp = cfg.getTemplate(templateFileName);

        /* Merge data-model with template */
        StringWriter out = new StringWriter();
        temp.process(root, out);

        if (StringUtils.isNotEmpty(out.toString())) {
            createDeploymentFile(mainFolder, shfileName, out.toString());
        }

    }

    protected final List<MultiLanguageFile> getFiles(File wfDirectory) {
        return super.getMultiLanguageFiles(wfDirectory, parentDirType.name(), extension);
    }
}
