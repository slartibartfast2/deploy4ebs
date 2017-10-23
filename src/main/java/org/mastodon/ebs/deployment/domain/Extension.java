package org.mastodon.ebs.deployment.domain;

/**
 * Created by Erdem.Akdogan on 17.10.2017.
 */
public enum Extension {
    rdf,
    rtf,
    xml,
    xdo,
    wft,
    ldt,
    pll,
    fmb,
    jsp,
    xlf,
    java;

    public static String[] names() {
        Extension[] extensions = values();
        String[] extNames = new String[extensions.length];

        for (int i = 0; i < extensions.length; i++) {
            extNames[i] = extensions[i].name();
        }

        return extNames;
    }

    public static String[] names(Extension[] extensions) {
        String[] extNames = new String[0];
        if (extensions != null) {
            extNames = new String[extensions.length];

            for (int i = 0; i < extensions.length; i++) {
                extNames[i] = extensions[i].name();
            }
        }

        return extNames;
    }
}
