
package org.mastodon.ebs.deployment.domain;

import org.apache.commons.io.FilenameUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

/**
 * Created by Erdem.Akdogan on 12.10.2017.
 */
public class BasicFileTest {

    @Test
    public void basic() throws IOException {
        File file = new File("/data01/erp/deployment/packages_ERP_Build_2014.04/erpDeployment/EBS/XXTG_TOP/bin/GET_TCMB_FILE");
        String name = FilenameUtils.getName(file.getAbsolutePath());


        String basePath = FilenameUtils.getPath(file.getAbsolutePath());
        System.out.println("basePath: " + basePath);
        String partialPath = basePath.substring(basePath.indexOf("XXTG_TOP"));
        System.out.println("partialPath: " + partialPath);

        System.out.println("file absolute path: " + file.getAbsolutePath());
        System.out.println("file name: " + name);
    }

}