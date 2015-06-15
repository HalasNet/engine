package org.craftercms.engine.test.utils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.craftercms.core.exception.PathNotFoundException;
import org.craftercms.core.service.CachingOptions;
import org.craftercms.core.service.Content;
import org.craftercms.core.service.ContentStoreService;
import org.craftercms.core.service.Context;
import org.craftercms.core.service.Item;
import org.craftercms.core.service.impl.CachedContent;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

/**
 * Test utils for mocking a {@link ContentStoreService}.
 *
 * @author Alfonso Vásquez
 */
public class ContentStoreServiceMockUtils {

    public static ContentStoreService setUpGetContentFromClassPath(ContentStoreService mock) {
        Answer<Content> getContentAnswer = new Answer<Content>() {

            @Override
            public Content answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String url;

                if (args.length == 2) {
                    url = (String) args[1];
                } else {
                    url = (String) args[2];
                }

                Content content = getContentFromClassPath(url);
                if (content == null) {
                    throw new PathNotFoundException();
                }

                return content;
            }

        };

        Answer<Boolean> existsAnswer = new Answer<Boolean>() {

            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                String url = (String) args[1];

                return new ClassPathResource(url).exists();
            }

        };

        when(mock.getContent(any(Context.class), anyString())).then(getContentAnswer);
        when(mock.getContent(any(Context.class), any(CachingOptions.class), anyString())).then(getContentAnswer);
        when(mock.exists(any(Context.class), anyString())).then(existsAnswer);
        when(mock.findChildren(any(Context.class), anyString())).then(new Answer<List<Item>>() {

            @Override
            public List<Item> answer(InvocationOnMock invocation) throws Throwable {
                String folderUrl = (String)invocation.getArguments()[1];
                Resource folderRes = new ClassPathResource(folderUrl);
                File folder = folderRes.getFile();
                String[] childNames = folder.list();
                List<Item> children = new ArrayList<>(childNames.length);

                for (String childName : childNames) {
                    Item child = new Item();
                    child.setUrl(folderUrl + "/" + childName);

                    children.add(child);
                }

                return children;
            }

        });

        return mock;
    }

    public static Content getContentFromClassPath(String url) throws IOException {
        ClassPathResource resource = new ClassPathResource(url);
        if (resource.exists()) {
            byte[] data = IOUtils.toByteArray(resource.getInputStream());

            return new CachedContent(data, resource.lastModified());
        } else {
            return null;
        }
    }

}
