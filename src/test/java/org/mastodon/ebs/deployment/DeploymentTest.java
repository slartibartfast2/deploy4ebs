package org.mastodon.ebs.deployment;

import org.apache.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Erdem.Akdogan on 11.10.2017.
 */
public class DeploymentTest {
    private static Logger logger = Logger.getLogger(DeploymentTest.class);
    private static File f1 = new File("EBS/ldt/concurrent/TR/AJE_340_DETAY.ldt");
    private static File f2 = new File("EBS/ldt/menu/TR/AJE_340_DETAY.ldt");
    private static File f3 = new File("EBS/ldt/message/TR/AJE_340_DETAY.ldt");
    private static File f4 = new File("EBS/ldt/message/US/AJE_340_DETAY.ldt");
    private static File f5 = new File("EBS/ldt/responsibility/TR/AJE_340_DETAY.ldt");
    private static File f6 = new File("EBS/ldt/function/TR/AJE_340_DETAY.ldt");
    private static File f7 = new File("EBS/ldt/profile/TR/AJE_340_DETAY_PROF.ldt");
    private static File f8 = new File("EBS/ldt/lookup/US/AJE_340_DETAY_LOOK.ldt");
    private static File f9 = new File("/xxtg/oracle/apps/ap/oie/audit/server/XxtgAuditReportLinesVOImpl.java");
    private static File f10 = new File("/xxtg/oracle/apps/ap/oie/audit/server/XxtgAuditReportLinesVORowImpl.java");
    private static File f11 = new File("EBS/CUSTOM_TOP/bin/XXBR_GNSQLGIR_CB_DATA.sql");

    private static List<File> files = new ArrayList<>();
    private static Set<String> javaDirs = new HashSet<>();

    private static File outFile = new File("D:/outFile.txt");

    /*public static void main(String[] args) throws Exception {

        String baseName = FilenameUtils.getName(f11.getAbsolutePath());
        String basePath = FilenameUtils.getPath(f11.getAbsolutePath());
        String partialPath = basePath.substring(basePath.indexOf("CUSTOM_TOP") + 9);

        logger.info("baseName: " + baseName);
        logger.info("partialPath: " + partialPath);
        logger.info("basePath: " + basePath);

        javaDirs.add(FilenameUtils.getPath(f9.getAbsolutePath()));
        javaDirs.add(FilenameUtils.getPath(f9.getAbsolutePath()));
        javaDirs.add(FilenameUtils.getPath(f10.getAbsolutePath()));

        for (String javaDir : javaDirs) {
            logger.info("javaDir: " + javaDir);
        }


        files.add(f1);
        files.add(f2);
        files.add(f3);
        files.add(f4);
        files.add(f5);
        files.add(f6);
        files.add(f7);
        files.add(f8);

        logger.info("before sort");
        for(File file : files ) {
            logger.info(file.getCanonicalPath());
        }

        Collections.sort(files, new LdtComparator());

        logger.info("after sort");
        for(File file : files ) {
            logger.info(file.getCanonicalPath());
        }
    }*/

}