package org.mastodon.ebs.deployment;

import org.junit.Test;

/**
 * Created by Erdem.Akdogan on 19.10.2017.
 */
public class LocalDeploymentTest {
    @Test
    public void create() throws Exception {
        String[] params = {"LOCAL", "D:\\dev\\IdeaProjects\\DeployPackageCreator-new\\test-output", "D:\\dev\\IdeaProjects\\DeployPackageCreator-new\\test-input\\erpDeployment"};
        DeploymentFactory.main(params);
    }

}