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
package org.craftercms.engine.scripting;

import org.craftercms.engine.exception.ScriptRenderingException;
import org.craftercms.engine.exception.UnrecognizableMimeTypeException;
import org.craftercms.engine.exception.UnrecognizableMimeTypeException;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Locale;

/**
 * Resolves the view to use for a certain script base URL, HTTP method, format and status code.
 *
 * @author Alfonso Vásquez
 */
public interface ScriptViewResolver {

    ScriptView resolveView(String viewName, String method, List<MediaType> acceptableMimeTypes, Status status, Locale locale)
            throws UnrecognizableMimeTypeException, ScriptRenderingException;

}
