package com.example.NEWS.controller;

import com.example.NEWS.model.User;
import com.example.NEWS.service.ToutiaoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

//@Controller
public class IndexController {
    @Autowired
    private ToutiaoService toutiaoService;
   //private LogAspect logAspect;
    private static final Logger logger= LoggerFactory.getLogger(IndexController.class);
    @RequestMapping(path={"/","/hello"})
    @ResponseBody
    public String index(HttpSession session){
       logger.info("visit hello");

     return "Hello Cameron"+session.getAttribute("msg")+"<br>"+toutiaoService.say();

    }
    @RequestMapping(path={"/profile/{groupId}/{userId}"})
    @ResponseBody
    public String profile(@PathVariable("groupId") String groupId,
                          @PathVariable("userId") int userId,
                          @RequestParam(value="type",defaultValue = "1") int type,
                          @RequestParam(value="key",defaultValue = "Cameron") String key ){
        return String.format("GID{%s},UID{%d},val{%d},key{%s}",groupId,userId,type,key);
    }
    @RequestMapping(path={"/vm"})
    public String news(Model model){
        model.addAttribute("value1","vvl");
        ArrayList<String> list=new ArrayList<>();
        list.add("RED");
        list.add("GREEN");
        list.add("BLUE");
        Map<String,String> map=new HashMap<>();
        for(int i=0;i<4;i++){
            map.put(String.valueOf(i),String.valueOf(i*i));
        }
        model.addAttribute("list",list);
        model.addAttribute("map",map);
        model.addAttribute("users",new User("cameron"));
      return "news";
    }
    @RequestMapping(path={"/request"})
    @ResponseBody
    public String request(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpSession session){
        StringBuilder sb=new StringBuilder();
        Enumeration<String> headerNames =request.getHeaderNames();
        while(headerNames.hasMoreElements()){
            String name=headerNames.nextElement();
            sb.append(name+":"+request.getHeader(name)+"<br>");

        }
        for(Cookie cookie: request.getCookies()){
            sb.append("Cookie:");
            sb.append(cookie.getName());
            sb.append(":");
            sb.append(cookie.getValue());
            sb.append("<br>");
        }
        sb.append("getmathod"+request.getMethod());
        sb.append("getpathinfo"+request.getPathInfo());
        sb.append("getqueuestring"+request.getQueryString());
        sb.append("getrequesturi"+request.getRequestURI());
        return sb.toString();
    }
    @RequestMapping(path={"/response"})
    @ResponseBody
    public String response(@CookieValue(value="qianmenglong",defaultValue = "1") String qianmenglong,
                           @RequestParam(value="key",defaultValue = "key") String key,
                           @RequestParam(value="value",defaultValue = "value") String value ,
                           HttpServletResponse response){
        response.addCookie(new Cookie(key,value));
        response.addHeader(key,value);
        return "nowcoderid from cookie:"+qianmenglong;

    }
    @RequestMapping("/redirect/{code}")
    public String redirect(@PathVariable("code") int code,HttpSession seesion){
        //RedirectView red=new RedirectView("/",true);
       // if(code==301){
      //      red.setStatusCode(HttpStatus.MOVED_PERMANENTLY);
      //  }
        seesion.setAttribute("msg","jumpfromredirect");
        return "redirect:/";
    }
}
