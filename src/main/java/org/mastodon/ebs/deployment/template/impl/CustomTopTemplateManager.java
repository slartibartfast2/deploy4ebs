package org.mastodon.ebs.deployment.template.impl;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.mastodon.ebs.deployment.commons.PropertyUtils;
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
 * @author ErdemAkdogan.
 */
public class CustomTopTemplateManager extends BasicTemplateManager {
    private final Extension[] extensions = {};
    private final List<BasicFile> customTopObjects;
    private static final String templateFileName = "customTop-deploy.ftlh";
    private static final String shfileName = "deploy_customTop.sh";
    private final static Logger logger = Logger.getLogger(CustomTopTemplateManager.class);

    public CustomTopTemplateManager(String templateDirectory, File mainFolder, boolean isLocal) throws IOException {
        super(templateDirectory, mainFolder, isLocal);

        this.customTopObjects = getFiles(mainFolder);
    }

    @Override
    public void create(File mainFolder) throws IOException, TemplateException {
        logger.debug("customTop altindaki obje sayisi: " + customTopObjects.size());

        /* Create a data-model */
        Map<String, Object> root = new HashMap<>();

        root.put("customTopObjects", customTopObjects);

        /* Get the template (uses cache internally) */
        Template temp = cfg.getTemplate(templateFileName);

        /* Merge data-model with template */
        StringWriter out = new StringWriter();
        temp.process(root, out);

        if (StringUtils.isNotEmpty(out.toString())) {
            createDeploymentFile(mainFolder, shfileName, out.toString());
        }
    }

    protected final List<BasicFile> getFiles(File customTopDirectory) {
        String customTopName = PropertyUtils.getValue("deploy4ebs.properties", "custom-top-name");
        return super.getFiles(customTopDirectory, customTopName, extensions);
    }
}
