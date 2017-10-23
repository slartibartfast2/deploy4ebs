package org.mastodon.ebs.deployment.domain.comparator;


import org.apache.log4j.Logger;
import org.mastodon.ebs.deployment.commons.PropertyUtils;
import org.mastodon.ebs.deployment.domain.LdtFile;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Erdem.Akdogan
 */
public class LdtFileComparator implements Comparator<LdtFile> {
    private final static Logger logger = Logger.getLogger(LdtFileComparator.class);

    public int compare(LdtFile f1, LdtFile f2) {
        Map<String, Integer> ldtPriority = new HashMap<>();

        Map<String, String> ldtPriorityProps = PropertyUtils.getValues("ldt-priority.properties");
        for (String key : ldtPriorityProps.keySet()) {
            ldtPriority.put(key, Integer.parseInt(ldtPriorityProps.get(key)));
        }

        int cResult = 0;
        try {
            cResult = ldtPriority.get(f1.getType()).compareTo(ldtPriority.get(f2.getType()));
        } catch (Exception e) {
            logger.debug("type for " + f1.getPath() + " = " + f1.getType() + " and priority = " + ldtPriority.get(f1.getType()));
            logger.debug("type for " + f2.getPath() + " = " + f2.getType() + " and priority = " + ldtPriority.get(f2.getType()));

            logger.error(e.getMessage(), e);
        }

        return cResult;
    }
}
