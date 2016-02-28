package kz.kaznu.web.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import kz.kaznu.lucene.model.Autocomplete;
import kz.kaznu.web.bean.IndexesBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * User: Sanzhar Aubakirov
 * Date: 2/28/16
 */
@RestController
@RequestMapping("/api")
public class TutorialRestController {
    public Gson gson = new GsonBuilder().create();

    @Autowired
    private IndexesBean indexesBean;

    @RequestMapping(value = "/autocomplete/{query}", method = RequestMethod.GET)
    public void message(@PathVariable("query") String query, HttpServletRequest request,
                        HttpServletResponse response) throws Exception {
        System.out.println("autocomplete_query = " + query);
        final List<Autocomplete> autocomplete = indexesBean.autocomplete().withTermQuery(query);
        final String json = gson.toJson(autocomplete);
        response.getWriter().write(json);
    }
}
