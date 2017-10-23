package org.mastodon.ebs.deployment.domain;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Erdem.Akdogan on 11.10.2017.
 */
@Data
@ToString
public class BasicFile {
    private String name;
    private String extension;
    private String path;

    public BasicFile(String name, String extension, String path) {
        this.name = name;
        if (StringUtils.isNotEmpty(extension)) {
            this.extension = StringUtils.prependIfMissing(extension, ".");
        } else {
            this.extension = "";
        }
        this.path = path;
    }
}
