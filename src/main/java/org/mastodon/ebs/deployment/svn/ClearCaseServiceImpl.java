package org.mastodon.ebs.deployment.svn;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;


/**
 * @author Erdem.Akdogan
 */
public class ClearCaseServiceImpl implements SvnService {

    private final static Logger logger = Logger.getLogger(ClearCaseServiceImpl.class);

    public void writeFilesAndDirs(String outPackageName, String labelName) {
        ProcessBuilder pBuilder;
        Process process;

        logger.info(labelName + " olarak etiketlenmis dosyalar CC'den alinacak.");

        try {
            logger.info("CC dizinleri olusturuluyor.");

            // -type d processed builder icn 
            pBuilder = new ProcessBuilder("cleartool", "find", "/EBS", "-type", "d", "-version", "version(/main/LATEST) ", "-exec",
                    "mkdir -p ../" + outPackageName + "/erpDeployment$CLEARCASE_PN");

            pBuilder.directory(new File("."));
            process = pBuilder.start();
            process.waitFor();

            logger.info("CC dizinleri olusturuldu.");
            logger.info("Dosyalar kopyalaniyor.");

            // && !lbtype(" + excludeLabel + ")
            pBuilder = new ProcessBuilder("cleartool", "find", "/EBS", "-type", "f", "-version", "lbtype(" + labelName + ") && !version(/main/0) ", "-exec",
                    "cp -rf $CLEARCASE_XPN ../" + outPackageName + "/erpDeployment/$CLEARCASE_PN");
            pBuilder.directory(new File("."));
            process = pBuilder.start();
            process.waitFor();

            logger.info("Dosyalar CC den basariyla alindi.");
        } catch (Exception e) {
            logger.error("Dosyalar CC den alinirken hata ile karsilasildi", e);
        }
    }
    
    /*public void writeFiles(String outPackageName, String includeLabel, String excludeLabel) {
        ProcessBuilder pBuilder;
        Process process;
        
        try {
            logger.info("Dosyalar kopyalaniyor.");
            pBuilder = new ProcessBuilder("cleartool", "find", "/EBS", "-type", "f", "-version", "lbtype(" + includeLabel + ") && !version(/main/0) && !lbtype(" + excludeLabel + ") ", "-exec",
                    "cp -rf $CLEARCASE_XPN ../" + outPackageName + "/erpDeployment/$CLEARCASE_PN");
            pBuilder.directory(new File("."));
            process = pBuilder.start();            
            process.waitFor();
            logger.info("Dosyalar CC den basariyla alindi.");
        } catch (Exception e) {
            logger.error("Dosyalar CC den alinirken hata ile karsilasildi", e);
        }        
    }*/

    public void logFiles(File outFile, String labelName) {
        ProcessBuilder pBuilder;
        Process process;

        try {
            FileUtils.writeStringToFile(outFile, "File List: \n", Charset.forName("UTF-8"), true);

            // && !lbtype(" + excludeLabel + ")
            pBuilder = new ProcessBuilder("cleartool", "find", "/EBS", "-type", "f", "-version", "lbtype(" + labelName + ") && !version(/main/0) ", "-print");

            pBuilder.directory(new File("."));
            process = pBuilder.start();

            List<String> vLines = org.apache.commons.io.IOUtils.readLines(process.getInputStream(), Charset.forName("UTF-8"));
            logger.debug("CC'den alinan versiyonlanmis dosya sayisi: " + vLines.size());
            FileUtils.writeLines(outFile, vLines, "\n", true);
        } catch (Exception ex) {
            logger.error("CC versiyon loglama dosyasi olusturulurken hata alindi.", ex);
        }
    }
}
