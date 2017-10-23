package org.mastodon.ebs.deployment.svn;

/**
 * Created by Erdem.Akdogan on 10.10.2017.
 */
public class SvnServiceFactory {
    public SvnService getService(SvnType type) {
        switch (type) {
            case GITHUB:
                return new GithubServiceImpl();
            case CLEARCASE:
                return new ClearCaseServiceImpl();
        }

        throw new RuntimeException("Unexpected type for svn service factory, " + type.name() + "." +
                "Should be one of " + SvnType.values() + ".");
    }
}
