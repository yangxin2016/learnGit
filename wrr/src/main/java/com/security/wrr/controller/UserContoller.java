package com.security.wrr.controller;

import com.security.wrr.model.*;
import com.security.wrr.repository.CpabeRepository;
import com.security.wrr.repository.MyDownloadRepository;
import com.security.wrr.repository.UserRepository;
import com.security.wrr.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Controller
@RequestMapping(value="/user")
public class UserContoller {
    @Autowired
    UserRepository userRepository;
    @Autowired
    CpabeRepository cpabeRepository;
    @Autowired
    MyDownloadRepository myDownloadRepository;

    private Integer pageSize=15;

    @RequestMapping("/checkLogin")
    @ResponseBody
    public Object checkLogin(HttpServletRequest request, ModelMap model){
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");

        List<User> users = userRepository.findByUsernameAndPassword(userName,password);
        if(users==null||users.size()==0){
            return "error";
        }else {
            return users.get(0);
        }
    }

    /**
     * 获取我的上传列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getMyUploadList")
    @ResponseBody
    public Object getMyUploadList(HttpServletRequest request, HttpServletResponse response){
        String userName = request.getParameter("userName");
        String page = request.getParameter("page");
        PageRequest pageable = new PageRequest(Integer.parseInt(page)-1,pageSize,DESC,"uploadTime");

        Page<FileInfo> pages= cpabeRepository.findByUser(userName,pageable);
        MyUploadList myUploadList = new MyUploadList();
        myUploadList.setMyUploadFileList(pages.getContent());
        myUploadList.setTotalCount(pages.getTotalElements());
        myUploadList.setTotalPage(pages.getTotalPages());
        return myUploadList;
    }

    /**
     * 获取我的下载列表
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/getMyDownloadList")
    @ResponseBody
    public Object getMyDownloadList(HttpServletRequest request, HttpServletResponse response){
        String userName = request.getParameter("userName");
        String page = request.getParameter("page");
        PageRequest pageable = new PageRequest(Integer.parseInt(page)-1,pageSize,DESC,"downloadTime");

        Page<MyDownload> pages= myDownloadRepository.findByUserName(userName,pageable);
        MyDownloadList downloadList = new MyDownloadList();
        downloadList.setMyDownloadFileList(pages.getContent());
        downloadList.setTotalCount(pages.getTotalElements());
        downloadList.setTotalPage(pages.getTotalPages());
        return downloadList;
    }

    //手动添加一些用户信息
    @RequestMapping("/testSave3")
    @ResponseBody
    public String testSave3(HttpServletRequest request, ModelMap model){
        String[] types={"professor","lecturer","assistant"};

        for(int i=1;i<10;i++){
            User user = new User();
            user.setUsername("yangxin"+i);
            user.setPassword(MD5Util.getMd5CodeByStr("123456"));
            user.setPosition(types[(int)Math.floor(Math.random()*3)]);
            user.setDegree("doctor");
            user.setCreteTime(new Date());
           /*
           *save方法和insert方法都可以向数据库插入数据，区别是如果当前对象已经存在数据库中，则save()方法可以更新该对象，而insert()方法则忽略此次操作。
           * 如果当前对象在数据库中不存在则均可以向数据库插入对象
            */
            userRepository.save(user);
        }
        return "ok";
    }
}
