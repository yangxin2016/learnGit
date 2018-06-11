package com.security.wrr.controller;

import com.mongodb.util.JSON;
import com.security.wrr.model.Course;
import com.security.wrr.model.Student;
import com.security.wrr.repository.ColRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentController {

    @Autowired
    ColRepository colRepository;


    @RequestMapping("/index")
    public String index(){


        return "index";
    }


    /**
     * 按照name字段模糊查询
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/getStudentByName.do")
    public String getStudentByName(HttpServletRequest request, ModelMap model){
        String name="5";
        List<Student> students = colRepository.findByNameLike(name);
        model.addAttribute("students",students);
        return "students";

    }

    /**
     * 插入mongodb中
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/save.do")
    @ResponseBody
    public String save(HttpServletRequest request, ModelMap model){
       for(int i=1;i<30;i++){
           Student student = new Student();
           student.setName("test"+i);
           student.setAge(i+20);
           if(i%3==0){
               student.setSex("m");
           }else{
               student.setSex("w");
           }
           student.setClassNo("20170"+i);
           Course course = new Course();
           course.setChinese(i+60);
           course.setEnglish(i+55);
           course.setMath(i+70);
           student.setCourse(course);
           /*
           *save方法和insert方法都可以向数据库插入数据，区别是如果当前对象已经存在数据库中，则save()方法可以更新该对象，而insert()方法则忽略此次操作。
           * 如果当前对象在数据库中不存在则均可以向数据库插入对象
            */
            colRepository.save(student);
       }
        return "ok";
    }

    /**
     * 全部查询
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/findAll.do")
    public String findAll(HttpServletRequest request, ModelMap model){

        List<Student> students = colRepository.findAll();
        model.addAttribute("students",students);
        return "students";

    }

    /**
     * 分页查询
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/findByPage.do",method = RequestMethod.GET)
    public String findByPage(HttpServletRequest request, ModelMap model){
        int page = Integer.parseInt(request.getParameter("page"));
        String sort = request.getParameter("sort");//排序字段名
        int pageSize=10;
        PageRequest pageable = new PageRequest(page-1,pageSize, Sort.Direction.DESC,sort);
        Page<Student> pages = colRepository.findAll(pageable);
        model.addAttribute("totalCount",pages.getTotalElements());
        model.addAttribute("totalPages",pages.getTotalPages());
        List<Student> students = pages.getContent();
        model.addAttribute("students",students);
        return "students";
    }

    /**
     * 按照年龄区间筛选，并排序，同时分页显示
     * @param request
     * @param model
     * @return
     */
    @RequestMapping(value = "/findByAgeAndPage.do",method = RequestMethod.GET)
    public String findByAgeAndPage(HttpServletRequest request, ModelMap model){
        int from = Integer.parseInt(request.getParameter("from"));//开始值（不包含）
        int to = Integer.parseInt(request.getParameter("to"));//结束值（不包含）
        int page = Integer.parseInt(request.getParameter("page"));//分页
        String sort = request.getParameter("sort");//排序字段名
        int pageSize=10;
        PageRequest pageable = new PageRequest(page-1,pageSize, Sort.Direction.DESC,sort);
        Page<Student> pages = colRepository.findByAgeBetween(from,to,pageable);
        model.addAttribute("totalCount",pages.getTotalElements());
        model.addAttribute("totalPages",pages.getTotalPages());
        List<Student> students = pages.getContent();
        model.addAttribute("students",students);
        return "students";
    }

    /**
     * 根据id更新某字段
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateById.do",method = RequestMethod.GET)
    @ResponseBody
    public String update(HttpServletRequest request){
         String id= request.getParameter("id");
         Student student = colRepository.findOne(id);
         student.setAge(212);
         colRepository.save(student);
         return "success";
    }

    /**
     * 根据id删除文档
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteById.do",method = RequestMethod.GET)
    @ResponseBody
    public String deleteById(HttpServletRequest request){
        String id= request.getParameter("id");
        if(id!=null){
            colRepository.delete(id);
        }else {
            //批量删除
            List<Student> students = colRepository.findAll();
            colRepository.delete(students);
        }
        return "success";
    }
}
