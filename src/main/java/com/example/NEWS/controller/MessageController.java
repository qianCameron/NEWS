package com.example.NEWS.controller;

import com.example.NEWS.dao.MessageDAO;
import com.example.NEWS.model.HostHolder;
import com.example.NEWS.model.Message;
import com.example.NEWS.model.User;
import com.example.NEWS.model.ViewObject;
import com.example.NEWS.service.MessageService;
import com.example.NEWS.service.UserService;
import com.example.NEWS.util.ToutiaoUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(NewsController.class);
    @Autowired
    MessageDAO messageDAO;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;
    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("fromId") int fromId,
                             @RequestParam("toId") int toId,
                             @RequestParam("content") String content) {
        Message msg = new Message();
        msg.setContent(content);
        msg.setCreatedDate(new Date());
        msg.setToId(toId);
        msg.setFromId(fromId);
        msg.setConversationId(fromId < toId ? String.format("%d_%d", fromId, toId) :
                String.format("%d_%d", toId, fromId));
        messageService.addMessage(msg);
        return ToutiaoUtil.getJSONString(msg.getId());
    }
    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String conversationList(Model model) {
        try {
            int localUserId = hostHolder.getUser().getId();
            List<ViewObject> conversations = new ArrayList<>();
            List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
            for (Message msg : conversationList) {
                ViewObject vo = new ViewObject();
                vo.set("conversation", msg);
                int targetId = msg.getFromId() == localUserId ? msg.getToId() : msg.getFromId();
                User user = userService.getUser(targetId);
                vo.set("headUrl", user.getHeadUrl());
                vo.set("userName", user.getName());
                vo.set("targetId", targetId);
                vo.set("totalCount", msg.getId());
                vo.set("unreadCount", messageService.getUnreadCount(localUserId, msg.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations", conversations);
            return "letter";
        } catch (Exception e) {
            logger.error("获取站内信列表失败" + e.getMessage());
        }
        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String conversationDetail(Model model, @Param("conversationId") String conversationId){
     try {
         List<Message> conversationList = messageService.getConversationDetail(conversationId, 0, 10);
         List<ViewObject> messages = new ArrayList<>();
         for (Message msg : conversationList) {
             ViewObject vo = new ViewObject();
             vo.set("message", msg);
             User user = userService.getUser(msg.getFromId());
             if (user == null) {
                 continue;
             }
             vo.set("headUrl", user.getHeadUrl());
             vo.set("userId", user.getId());
             messages.add(vo);
         }
         model.addAttribute("messages", messages);
         return "letterDetail";
     }
     catch (Exception e) {
         logger.error("获取站内信列表失败" + e.getMessage());
     }
        return "letterDetail";
    }
}
