package org.mastodon.ebs.deployment;

import org.apache.commons.lang3.StringUtils;
import org.mastodon.ebs.deployment.commons.PropertyUtils;

import java.io.File;
import java.io.FileFilter;

/**
 * @author Erdem.Akdogan
 */
public class DeploymentFilter implements FileFilter {
    public boolean accept(File file) {
        String customTopName = PropertyUtils.getValue("deploy4ebs.properties", "custom-top-name");

        if (StringUtils.equals(file.getName(), customTopName))
            return true;

        for (DeploymentFolderType dFolderType : DeploymentFolderType.values()) {
            if (StringUtils.equalsIgnoreCase(dFolderType.name(), file.getName()) && dFolderType.deployable)
                return true;
        }

        return false;
    }
}
