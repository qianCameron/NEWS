package com.example.NEWS.controller;

import com.example.NEWS.model.HostHolder;
import com.example.NEWS.model.News;
import com.example.NEWS.model.ViewObject;
import com.example.NEWS.service.NewsService;
import com.example.NEWS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
@Autowired
    NewsService newsService;
@Autowired
    UserService userService;
@Autowired
    HostHolder hostHolder;
private List<ViewObject> getNews(int userId,int offset,int Limit){
    List<News> newsList = newsService.getLatestNews(0, 0, 11);

    List<ViewObject> vos = new ArrayList<>();
    for (News news : newsList) {
        ViewObject vo = new ViewObject();
        vo.set("news", news);
        vo.set("user", userService.getUser(news.getUserId()));
        vos.add(vo);
    }
    return vos;
}


    @RequestMapping(path={"/","/hello"}, method = {RequestMethod.GET, RequestMethod.POST})
    //@ResponseBody
    public String index(Model model, @RequestParam(value="pop", defaultValue = "0") int pop){


          model.addAttribute("vos",getNews(0,0,11));
          model.addAttribute("pop",pop);
        return "home";
    }

    @RequestMapping(path = {"/user/{userId}/"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String userIndex(Model model, @PathVariable("userId") int userId) {
        model.addAttribute("vos", getNews(userId, 0, 10));
        return "home";
    }


}
