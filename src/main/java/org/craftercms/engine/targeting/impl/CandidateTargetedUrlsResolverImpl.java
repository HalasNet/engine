package org.craftercms.engine.targeting.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.craftercms.core.util.UrlUtils;
import org.craftercms.engine.targeting.CandidateTargetIdsResolver;
import org.craftercms.engine.targeting.CandidateTargetedUrlsResolver;
import org.craftercms.engine.targeting.TargetIdResolver;
import org.craftercms.engine.targeting.TargetedUrlComponents;
import org.craftercms.engine.targeting.TargetedUrlStrategy;
import org.craftercms.engine.util.TargetingUtils;
import org.springframework.beans.factory.annotation.Required;

/**
 * Created by alfonsovasquez on 14/8/15.
 */
public class CandidateTargetedUrlsResolverImpl implements CandidateTargetedUrlsResolver {

    protected TargetIdResolver targetIdResolver;
    protected TargetedUrlStrategy targetedUrlStrategy;
    protected CandidateTargetIdsResolver candidateTargetIdsResolver;

    @Required
    public void setTargetIdResolver(TargetIdResolver targetIdResolver) {
        this.targetIdResolver = targetIdResolver;
    }

    @Required
    public void setTargetedUrlStrategy(TargetedUrlStrategy targetedUrlStrategy) {
        this.targetedUrlStrategy = targetedUrlStrategy;
    }

    @Required
    public void setCandidateTargetIdsResolver(CandidateTargetIdsResolver candidateTargetIdsResolver) {
        this.candidateTargetIdsResolver = candidateTargetIdsResolver;
    }

    @Override
    public List<String> getUrls(String targetedUrl) {
        List<String> candidateUrls = new ArrayList<>();
        String rootFolder = TargetingUtils.getMatchingRootFolder(targetedUrl);

        if (StringUtils.isNotEmpty(rootFolder)) {
            String relativeTargetedUrl = StringUtils.substringAfter(targetedUrl, rootFolder);
            TargetedUrlComponents urlComp = targetedUrlStrategy.parseTargetedUrl(relativeTargetedUrl);

            if (urlComp != null) {
                String prefix = UrlUtils.appendUrl(rootFolder, urlComp.getPrefix());
                String suffix = urlComp.getSuffix();
                String targetId = urlComp.getTargetId();
                String fallbackTargetId = targetIdResolver.getFallbackTargetId();
                List<String> candidateTargetIds = candidateTargetIdsResolver.getTargetIds(targetId, fallbackTargetId);

                if (CollectionUtils.isNotEmpty(candidateTargetIds)) {
                    for (String candidateTargetId : candidateTargetIds) {
                        candidateUrls.add(targetedUrlStrategy.buildTargetedUrl(prefix, candidateTargetId, suffix));
                    }
                } else {
                    candidateUrls.add(targetedUrl);
                }
            } else {
                candidateUrls.add(targetedUrl);
            }
        } else {
            candidateUrls.add(targetedUrl);
        }

        return candidateUrls;
    }

}
