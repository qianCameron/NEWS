package com.example.NEWS.service;

import com.example.NEWS.dao.LoginTicketDAO;
import com.example.NEWS.dao.UserDao;
import com.example.NEWS.model.LoginTicket;
import com.example.NEWS.model.User;
import com.example.NEWS.util.ToutiaoUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
@Autowired
    private UserDao userDao;
    @Autowired
    private LoginTicketDAO loginTicketDAO;
    public Map<String,Object> register(String username, String password){
        Map<String,Object> map=new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }
        User user=userDao.selectByName(username);
        if(user!=null){
            map.put("msgname","用户已被注册");
            return map;
        }
        user= new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dm.png", new Random().nextInt(1000)));
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userDao.addUser(user);
        //登陆
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public Map<String,Object> login(String username, String password){
        //logger.error(username);
        Map<String,Object> map=new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpwd","密码不能为空");
            return map;
        }
        User user=userDao.selectByName(username);
        //logger.error("user id"+user.getId());
        if(user==null){
            map.put("msgname","用户不存在");
            return map;
        }
        if(!ToutiaoUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msgpwd","密码不正确");
            return map;
        }
       //ticket
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }
    private String addLoginTicket(int userId){
        LoginTicket ticket=new LoginTicket();
        ticket.setUserId(userId);
        Date date=new Date();
        date.setTime(date.getTime()+1000*3600*24);
        ticket.setExpired(date);
        ticket.setStatus(0);
        ticket.setTicket(UUID.randomUUID().toString().replaceAll("-",""));
        loginTicketDAO.addTicket(ticket);
        return ticket.getTicket();
    }
    public void logout(String ticket){
        loginTicketDAO.updateStatus(ticket,1);
    }
    public User getUser(int id){
        return userDao.selectById(id);
    }

}
