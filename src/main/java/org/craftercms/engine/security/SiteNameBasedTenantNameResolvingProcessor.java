/*
 * Copyright (C) 2007-2013 Crafter Software Corporation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.craftercms.engine.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.craftercms.core.util.HttpServletUtils;
import org.craftercms.engine.servlet.filter.AbstractSiteContextResolvingFilter;
import org.craftercms.security.api.RequestContext;
import org.craftercms.security.api.RequestSecurityProcessor;
import org.craftercms.security.api.RequestSecurityProcessorChain;

/**
 * Security processor that resolves the tenant ID using the site name request attribute set by the {@link org.craftercms.engine.servlet.filter.AbstractSiteContextResolvingFilter}.
 *
 * @author Alfonso Vásquez
 */
public class SiteNameBasedTenantNameResolvingProcessor implements RequestSecurityProcessor {

    private static final Log logger = LogFactory.getLog(SiteNameBasedTenantNameResolvingProcessor.class);

    @Override
    public void processRequest(RequestContext context, RequestSecurityProcessorChain processorChain) throws Exception {
        String tenantName = (String) HttpServletUtils.getAttribute(AbstractSiteContextResolvingFilter.SITE_NAME_ATTRIBUTE,
                HttpServletUtils.SCOPE_REQUEST);

        if (logger.isDebugEnabled()) {
            logger.debug("Tenant name resolved for current request: " + tenantName);
        }

        context.setTenantName(tenantName);

        processorChain.processRequest(context);
    }

}
