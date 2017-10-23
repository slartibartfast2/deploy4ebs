package org.mastodon.ebs.deployment.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by Erdem.Akdogan on 13.10.2017.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class LdtFile extends MultiLanguageFile {
    private String type;
    private String lctFile;
    private String applTopName;

    public LdtFile(String name, String extension, String path, Language language) {
        super(name, extension, path, language);
    }
}
