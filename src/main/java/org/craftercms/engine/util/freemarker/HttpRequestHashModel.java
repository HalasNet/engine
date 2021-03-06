package org.craftercms.engine.util.freemarker;

import java.util.ArrayList;
import java.util.Enumeration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import freemarker.template.ObjectWrapper;
import freemarker.template.SimpleCollection;
import freemarker.template.TemplateCollectionModel;
import freemarker.template.TemplateHashModelEx;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

/**
 * Just like {@link freemarker.ext.servlet.HttpRequestHashModel}, but besides returning request attributes, it also
 * returns values for the following properties of the request:
 *
 * <ul>
 *     <li>scheme</li>
 *     <li>serverName</li>
 *     <li>serverPort</li>
 *     <li>contextPath</li>
 *     <li>servletPath</li>
 *     <li>requestURI</li>
 *     <li>queryString</li>
 * </ul>
 *
 * @author avasquez
 */
public class HttpRequestHashModel implements TemplateHashModelEx {

    public static final String KEY_SCHEME = "scheme";
    public static final String KEY_SERVER_NAME = "serverName";
    public static final String KEY_SERVER_PORT = "serverPort";
    public static final String KEY_CONTEXT_PATH = "contextPath";
    public static final String KEY_SERVLET_PATH = "servletPath";
    public static final String KEY_REQUEST_URI = "requestURI";
    public static final String KEY_QUERY_STRING = "queryString";

    private final HttpServletRequest request;
    private final HttpServletResponse response;
    private final ObjectWrapper wrapper;

    public HttpRequestHashModel(HttpServletRequest request, ObjectWrapper wrapper) {
        this(request, null, wrapper);
    }

    public HttpRequestHashModel(HttpServletRequest request, HttpServletResponse response, ObjectWrapper wrapper) {
        this.request = request;
        this.response = response;
        this.wrapper = wrapper;
    }

    public TemplateModel get(String key) throws TemplateModelException {
        switch (key) {
            case KEY_SCHEME:
                return wrapper.wrap(request.getScheme());
            case KEY_SERVER_NAME:
                return wrapper.wrap(request.getServerName());
            case KEY_SERVER_PORT:
                return wrapper.wrap(request.getServerPort());
            case KEY_CONTEXT_PATH:
                return wrapper.wrap(request.getContextPath());
            case KEY_SERVLET_PATH:
                return wrapper.wrap(request.getServletPath());
            case KEY_REQUEST_URI:
                return wrapper.wrap(request.getRequestURI());
            case KEY_QUERY_STRING:
                return wrapper.wrap(request.getQueryString());
            default:
                return wrapper.wrap(request.getAttribute(key));
        }
    }

    public boolean isEmpty()
    {
        return !request.getAttributeNames().hasMoreElements();
    }

    public int size() {
        int result = 0;

        for (Enumeration enumeration = request.getAttributeNames(); enumeration.hasMoreElements();) {
            enumeration.nextElement();
            ++result;
        }

        return result;
    }

    public TemplateCollectionModel keys() {
        ArrayList keys = new ArrayList();

        for (Enumeration enumeration = request.getAttributeNames(); enumeration.hasMoreElements();) {
            keys.add(enumeration.nextElement());
        }

        return new SimpleCollection(keys.iterator());
    }

    public TemplateCollectionModel values() {
        ArrayList values = new ArrayList();

        for (Enumeration enumeration = request.getAttributeNames(); enumeration.hasMoreElements();) {
            values.add(request.getAttribute((String)enumeration.nextElement()));
        }

        return new SimpleCollection(values.iterator(), wrapper);
    }

    public HttpServletRequest getRequest()
    {
        return request;
    }

    public HttpServletResponse getResponse()
    {
        return response;
    }

    public ObjectWrapper getObjectWrapper()
    {
        return wrapper;
    }

}
