package org.mastodon.ebs.deployment.template.impl;

import freemarker.template.Template;
import freemarker.template.TemplateException;
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
 * @author ErdemAkdogan.
 */
public class PllTemplateManager extends BasicTemplateManager {
    private static final Extension extension = Extension.pll;
    private static final String templateFileName = "pll-deploy.ftlh";
    private final List<BasicFile> pllObjects;
    private static final String shfileName = "deploy_pll.sh";
    private final static Logger logger = Logger.getLogger(PllTemplateManager.class);

    public PllTemplateManager(String templateDirectory, File mainFolder, boolean isLocal) throws IOException {
        super(templateDirectory, mainFolder, isLocal);

        this.parentDirType = DeploymentFolderType.resource;
        this.pllObjects = getFiles(mainFolder);
    }

    @Override
    public void create(File mainFolder) throws IOException, TemplateException {
        logger.debug("pll tipindeki obje sayisi: " + pllObjects.size());

        /* Create a data-model */
        Map<String, Object> root = new HashMap<>();

        root.put("pllObjects", pllObjects);

        /* Get the template (uses cache internally) */
        Template temp = cfg.getTemplate(templateFileName);

        /* Merge data-model with template */
        StringWriter out = new StringWriter();
        temp.process(root, out);

        if (StringUtils.isNotEmpty(out.toString())) {
            createDeploymentFile(mainFolder, shfileName, out.toString());
        }
    }

    protected final List<BasicFile> getFiles(File pllDirectory) {
        return super.getFiles(pllDirectory, parentDirType.name(), extension);
    }
}
