package org.mastodon.ebs.deployment.template;

import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.mastodon.ebs.deployment.domain.BasicFile;
import org.mastodon.ebs.deployment.domain.Extension;
import org.mastodon.ebs.deployment.domain.Language;
import org.mastodon.ebs.deployment.domain.MultiLanguageFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by Erdem.Akdogan on 16.10.2017.
 */
public abstract class MultiLanguageTemplateManager extends BasicTemplateManager {
    private final static Logger logger = Logger.getLogger(MultiLanguageTemplateManager.class);

    public MultiLanguageTemplateManager(String templateDirectory, File mainFolder, boolean isLocal) throws IOException {
        super(templateDirectory, mainFolder, isLocal);
    }

    public abstract void create(File folder) throws IOException, TemplateException;

    protected final List<MultiLanguageFile> getMultiLanguageFiles(File directory, String parentDirName, Extension... extensions) {
        List<BasicFile> basicFiles = super.getFiles(directory, parentDirName, extensions);

        List<MultiLanguageFile> mlFiles = new ArrayList<>();
        for (BasicFile basicFile : basicFiles) {
            try {
                String[] dirAttributes = basicFile.getPath().split(Pattern.quote(Character.toString(IOUtils.DIR_SEPARATOR)));
                logger.trace("languageStr: " + dirAttributes[1]);

                mlFiles.add(new MultiLanguageFile(basicFile.getName(), basicFile.getExtension(), basicFile.getPath(), Language.valueOf(dirAttributes[1])));
            } catch (Exception e) {
                super.addToIgnoredFiles(new File(basicFile.getPath() + IOUtils.DIR_SEPARATOR + basicFile.getName() + basicFile.getExtension()));

                logger.error("basicReportFile.basePath: " + basicFile.getPath());
                logger.error("basicReportFile.name: " + basicFile.getName());
                logger.error("reportBasicFile: " + basicFile.toString());
            }
        }

        return mlFiles;
    }
}
