package org.mastodon.ebs.deployment;

/**
 * @author Erdem.Akdogan
 */
public enum DeploymentFolderType {

    java(true),
    reports(true),
    fmb(true),
    ldt(true),
    wft(true),
    resource(true),
    customTop(true),
    OA_HTML(false);

    public boolean deployable;

    DeploymentFolderType(boolean deployable) {
        this.deployable = deployable;
    }
}
