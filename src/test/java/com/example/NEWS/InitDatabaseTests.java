package com.example.NEWS;

import com.example.NEWS.dao.CommentDAO;
import com.example.NEWS.dao.LoginTicketDAO;
import com.example.NEWS.dao.NewsDAO;
import com.example.NEWS.dao.UserDao;
import com.example.NEWS.model.*;
import com.example.NEWS.service.CommentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringRunner.class)
@SpringBootTest
@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    UserDao userDao;
    @Autowired
    NewsDAO newsDAO;
    @Autowired
    LoginTicketDAO loginTicketDAO;
    @Autowired
    CommentService commentService;
    @Autowired
    CommentDAO commentDAO;
    @Test
    public void initData() {

        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDao.addUser(user);

            News news=new News();
            news.setCommentCount(i);
            Date date=new Date();
            date.setTime(date.getTime()+1000*3600*5*i);
            news.setCreatedDate(date);
            news.setLikeCount(i+1);
            news.setUserId(i+1);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            newsDAO.addNews(news);
            for(int j=0;j<3;j++){
                Comment comment=new Comment();
                comment.setUserId(i+1);
                comment.setEntityId(news.getId());
                comment.setEntityType(EntityType.ENTITY_NEWS);
                comment.setStatus(0);
                comment.setCreatedDate(new Date());
                comment.setContent("Comment "+j);
                commentDAO.addComment(comment);
            }
            user.setPassword("newpassword");

            userDao.updatePassword(user);
            LoginTicket loginTicket=new LoginTicket();
            loginTicket.setStatus(0);
            loginTicket.setUserId(user.getId()+10);
            loginTicket.setExpired(date);
            loginTicket.setTicket(String.format("TICKET%d",i+1));
            loginTicketDAO.addTicket(loginTicket);
            loginTicketDAO.updateStatus(loginTicket.getTicket(),1);
        }
        //userDao.deleteById(1);
           //Assert.assertEquals("newpassword",userDao.selectById(1).getPassword());
        //Assert.assertNull(userDao.selectById(1));
    }

}
