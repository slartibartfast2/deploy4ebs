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
import org.mastodon.ebs.deployment.domain.LdtFile;
import org.mastodon.ebs.deployment.domain.comparator.LdtFileComparator;
import org.mastodon.ebs.deployment.template.BasicTemplateManager;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.regex.Pattern;

/**
 * @author Erdem.Akdogan
 */
public class FndLoadTemplateManager extends BasicTemplateManager {
    private static final Extension extension = Extension.ldt;
    private static final String templateFileName = "fndLoad-deploy.ftlh";
    private final List<LdtFile> fndLoadObjects;
    private static final String shfileName = "deploy_ldt.sh";
    private final static Logger logger = Logger.getLogger(FndLoadTemplateManager.class);

    public FndLoadTemplateManager(String templateDirectory, File mainFolder, boolean isLocal) throws IOException {
        super(templateDirectory, mainFolder, isLocal);

        super.parentDirType = DeploymentFolderType.ldt;
        this.fndLoadObjects = getFiles(mainFolder);
        Collections.sort(this.fndLoadObjects, new LdtFileComparator());
    }

    @Override
    public void create(File mainFolder) throws IOException, TemplateException {
        logger.debug("ldt tipindeki obje sayisi: " + fndLoadObjects.size());

        /* Create a data-model */
        Map<String, Object> root = new HashMap<>();

        root.put("fndLoadObjects", fndLoadObjects);

        /* Get the template (uses ca
        che internally) */
        Template temp = cfg.getTemplate(templateFileName);

        /* Merge data-model with template */
        StringWriter out = new StringWriter();
        temp.process(root, out);

        if (StringUtils.isNotEmpty(out.toString())) {
            createDeploymentFile(mainFolder, shfileName, out.toString());
        }

    }

    protected final List<LdtFile> getFiles(File fndDirectory) {
        List<BasicFile> fndLoadFiles = super.getFiles(fndDirectory, parentDirType.name(), extension);

        List<LdtFile> fndLoadBasicFiles = new ArrayList<>();
        for (BasicFile fndLoadBasicFile : fndLoadFiles) {
            String[] dirAttributes = fndLoadBasicFile.getPath().split(Pattern.quote(Character.toString(IOUtils.DIR_SEPARATOR)));

            logger.trace("ldt lanugage: " + dirAttributes[2]);
            LdtFile ldtFile = new LdtFile(
                    fndLoadBasicFile.getName(), fndLoadBasicFile.getExtension(), fndLoadBasicFile.getPath(), Language.valueOf(dirAttributes[2]));

            logger.trace("ldt type: " + dirAttributes[1]);
            ldtFile.setType(dirAttributes[1]);

            String fndTypeProps = PropertyUtils.getValue("fnd-load.properties", ldtFile.getType());
            String[] fndAttributes = fndTypeProps.split("#");

            ldtFile.setApplTopName(fndAttributes[0]);
            ldtFile.setLctFile(fndAttributes[1]);

            fndLoadBasicFiles.add(ldtFile);
        }

        return fndLoadBasicFiles;
    }
}
