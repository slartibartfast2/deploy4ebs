package org.mastodon.ebs.deployment.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Created by Erdem.Akdogan on 16.10.2017.
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = false)
public class MultiLanguageFile extends BasicFile {
    private Language language;

    public MultiLanguageFile(String name, String extension, String path, Language language) {
        super(name, extension, path);
        this.language = language;
    }
}
