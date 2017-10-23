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
public class FormsTemplateManager extends MultiLanguageTemplateManager {
    private static final Extension extension = Extension.fmb;
    private static final String templateFileName = "forms-deploy.ftlh";
    private final List<MultiLanguageFile> formObjects;
    private static final String shfileName = "deploy_forms.sh";
    private final static Logger logger = Logger.getLogger(FormsTemplateManager.class);

    public FormsTemplateManager(String templateDirectory, File mainFolder, boolean isLocal) throws IOException {
        super(templateDirectory, mainFolder, isLocal);

        this.parentDirType = DeploymentFolderType.fmb;
        this.formObjects = getFiles(mainFolder);
    }

    @Override
    public void create(File mainFolder) throws IOException, TemplateException {
        logger.debug("form tipindeki obje sayisi: " + formObjects.size());

        /* Create a data-model */
        Map<String, Object> root = new HashMap<>();

        String customTopName = PropertyUtils.getValue("deploy4ebs.properties", "custom-top-name");
        root.put("customTop", customTopName);

        root.put("formObjects", formObjects);

        /* Get the template (uses cache internally) */
        Template temp = cfg.getTemplate(templateFileName);

        /* Merge data-model with template */
        StringWriter out = new StringWriter();
        temp.process(root, out);

        if (StringUtils.isNotEmpty(out.toString())) {
            createDeploymentFile(mainFolder, shfileName, out.toString());
        }
    }

    protected final List<MultiLanguageFile> getFiles(File formsDirectory) {
        return super.getMultiLanguageFiles(formsDirectory, parentDirType.name(), extension);
    }
}
