package com.example.NEWS.controller;
import com.example.NEWS.service.UserService;
import com.example.NEWS.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Controller
public class LoginController {
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

@Autowired
    UserService userService;
    @RequestMapping(path={"/reg/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String res(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember",defaultValue="0") int remember,
                      HttpServletResponse response){
       try {
           Map<String, Object> map = userService.register(username, password);
           if (map.containsKey("ticket")) {
               Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
               cookie.setPath("/");
               response.addCookie(cookie);

               if(remember>0){
                   cookie.setMaxAge(3600*24*5);
               }
               return ToutiaoUtil.getJSONString(0, "注册成功");
           }
           else{
               return ToutiaoUtil.getJSONString(1, map);
           }
       }
       catch(Exception e){
             logger.error("注册异常");
             return ToutiaoUtil.getJSONString(1,"注册异常");
       }


    }
    @RequestMapping(path={"/login/"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String login(Model model, @RequestParam("username") String username,
                      @RequestParam("password") String password,
                      @RequestParam(value="rember",defaultValue="0") int remember,
                        HttpServletResponse response){
        try {
            Map<String, Object> map = userService.login(username, password);
            if (map.containsKey("ticket")) {
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                cookie.setPath("/");
                response.addCookie(cookie);

                if(remember>0){
                    cookie.setMaxAge(3600*24*5);
                }
                return ToutiaoUtil.getJSONString(0, "登陆成功");
            }
            else{
                return ToutiaoUtil.getJSONString(1, map);
            }
        }
        catch(Exception e){
            logger.error("登陆异常");
            return ToutiaoUtil.getJSONString(1,"登陆异常");
        }


    }
    @RequestMapping(path={"/logout/"}, method = {RequestMethod.GET, RequestMethod.POST})

    public String logout(@CookieValue("ticket") String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }
}
