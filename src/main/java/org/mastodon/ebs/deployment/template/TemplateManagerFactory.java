package org.mastodon.ebs.deployment.template;

import org.apache.log4j.Logger;
import org.mastodon.ebs.deployment.DeploymentFolderType;
import org.mastodon.ebs.deployment.commons.PropertyUtils;
import org.mastodon.ebs.deployment.template.impl.*;

import java.io.File;
import java.io.IOException;

/**
 * @author Erdem.Akdogan
 */
public class TemplateManagerFactory {
    private static final String templateDirectory = "/templates";
    private static final Logger logger = Logger.getLogger(TemplateManagerFactory.class);

    public BasicTemplateManager createDeployTemplate(File folder) throws IOException, IllegalArgumentException {
        BasicTemplateManager templateManager = null;

        DeploymentFolderType fType = null;
        try {
            logger.trace("folder.getName(): " + folder.getName());
            fType = DeploymentFolderType.valueOf(folder.getName());
            logger.trace("DeploymentFolderType Before: " + fType.name());
        } catch (java.lang.IllegalArgumentException iae) {
            logger.error(iae.getMessage());

            String customTopName = PropertyUtils.getValue("deploy4ebs.properties", "custom-top-name");
            logger.trace("custom-top-name-prop: " + customTopName);
            if (folder.getName().equals(customTopName)) {
                fType = DeploymentFolderType.customTop;
            }
        }

        if (fType != null)
            logger.trace("DeploymentFolderType After: " + fType.name());

        try {
            switch (fType) {
                case ldt:
                    return new FndLoadTemplateManager(templateDirectory, folder, false);
                case fmb:
                    return new FormsTemplateManager(templateDirectory, folder, false);
                case resource:
                    return new PllTemplateManager(templateDirectory, folder, false);
                case wft:
                    return new WorkflowTemplateManager(templateDirectory, folder, false);
                case reports:
                    return new ReportsTemplateManager(templateDirectory, folder, false);
                case java:
                    return new JavaTemplateManager(templateDirectory, folder, false);
                case customTop:
                    templateManager = new CustomTopTemplateManager(templateDirectory, folder, false);
            }
        } catch (IOException ioe) {
            logger.error("could not find template directory.");
        }

        return templateManager;
    }
}
