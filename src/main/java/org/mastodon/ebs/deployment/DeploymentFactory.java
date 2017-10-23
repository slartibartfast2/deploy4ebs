package org.mastodon.ebs.deployment;

import org.apache.log4j.Logger;
import org.mastodon.ebs.deployment.svn.SvnType;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @author Erdem.Akdogan
 */
public class DeploymentFactory {

    private final static Logger logger = Logger.getLogger(DeploymentFactory.class);

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();

        if (args.length < 3) {
            throw new RuntimeException("Eksik parametre var! Düzgün kullanım 'java " + DeploymentFactory.class.getSimpleName() + " svnTipi etiketAdi ciktiDizin' seklinde.");
        }

        SvnType svnType;
        try {
            svnType = SvnType.valueOf(args[0]);
        } catch (Exception e) {
            throw new RuntimeException("Gecersiz svnTipi degeri. Gecerli degerler; " + SvnType.values().toString());
        }

        Deployment deployment;
        if (svnType.equals(SvnType.LOCAL)) {
            String outputPath = args[1];
            String sourcePath = args[2];

            deployment = new LocalDeployment(sourcePath);
            deployment.create(outputPath);

        } else {
            String labelName = args[1];
            String outputPath = args[2];

            deployment = new SvnDeployment(svnType, labelName);
            deployment.create(outputPath);
        }

        long endTime = System.currentTimeMillis();
        NumberFormat formatter = new DecimalFormat("#0.00000");
        logger.info("Execution time is " + formatter.format((endTime - startTime) / 1000d) + " seconds");
    }
}
