/*
 * #%L
 * Wisdom-Framework
 * %%
 * Copyright (C) 2013 - 2014 Wisdom Framework
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package sample;

import javassist.util.proxy.Proxy;
import org.apache.felix.ipojo.annotations.Requires;
import org.wisdom.api.DefaultController;
import org.wisdom.api.annotations.*;
import org.wisdom.api.content.Json;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.security.Authenticated;
import org.wisdom.api.templates.Template;
import org.wisdom.monitor.service.MonitorExtension;
import org.wisdom.orientdb.object.OrientDbCrud;

import java.util.LinkedList;
import java.util.List;

/**
 * Your first Wisdom Controller.
 */
@Controller
public class NewsExtensionController extends DefaultController implements MonitorExtension{

    /**
     * Injects a template named 'welcome'.
     */
    @View("welcome")
    Template welcome;
    @View("news/manageView")
    Template manage;

    @Model(value = NewsArticle.class)
    private OrientDbCrud<NewsArticle, String> newsArticleCrud;

    Class klass = Proxy.class;

    @Requires
    Json json;


    /**
     * The action method returning the welcome page. It handles
     * HTTP GET request on the "/" URL.
     *
     * @return the welcome page
     */
    @Route(method = HttpMethod.GET, uri = "/")
    public Result welcome() {
        return ok(render(welcome, "welcome", "Welcome to Wisdom Framework!"));
    }

    @Authenticated("Monitor-Authenticator")
    @Route(method = HttpMethod.GET, uri = "/monitor/news/manage")
    public Result manage() {
        return ok(render(manage));
    }



    @Route(method = HttpMethod.GET, uri = "/news/list")
    public Result get() {

        List<NewsArticle> list = new LinkedList<NewsArticle>();
        for (NewsArticle article : newsArticleCrud.findAll()) {
            list.add(article);
        }
        return ok(list).json();
    }

//    @Authenticated("Monitor-Authenticator")
//    @Route(method = HttpMethod.POST, uri = "/registry/list/{id}")
//    public Result update(@Parameter("id") String id) {
//        String url = newsArticleCrud.findOne(id).getSelf();
//        return ok(parseUrl(url));
//    }

    /**
     * The action method that loads a json object via a url and adds it to the database. It handles
     * HTTP POST request on the "/upload" URL.
     *
     * @return json structure containing the new extension
     */
//    @Route(method = HttpMethod.POST, uri = "/registry/upload")
//    public Result addArticle(final @FormParameter("url") String u) {
//        return ok(parseUrl(u));
//    }


    @Override
    public String label() {
        return "News Manager";
    }

    @Override
    public String url() {
        return "/monitor/news/manage";
    }

    @Override
    public String category() {
        return "Documentation";
    }
}