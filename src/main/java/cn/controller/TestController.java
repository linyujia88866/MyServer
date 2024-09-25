package cn.controller;


import cn.dto.StudentDTO;
import cn.service.StudentService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @Author: 1399543132@qq.com
 * @Date: 2024/01/21/21:37
 * @Description: 学生表控制类
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @Resource
    private StudentService studentService;

    @GetMapping("/aaa")
    public String aaa(@RequestParam(value = "id") String id){
        return "aaa" + id;
    }

    @GetMapping("/bbb")
    public String bbb(@RequestParam(value = "id") String id, @RequestParam(value = "jd") String jd){
        return "aaa" + id + "bbb" + jd;
    }


    @PostMapping("/add")
    public Boolean addStudent(@RequestBody StudentDTO studentDTO){
        return studentService.addStudent(studentDTO);
    }

    @PostMapping("/delete")
    public Boolean deleteStudent(@RequestParam(value = "id") Long id){
        return studentService.deleteStudentById(id);
    }

    @PostMapping("/update")
    public Boolean updateStudent(@RequestBody StudentDTO studentDTO){
        return studentService.updateStudent(studentDTO);
    }
    @GetMapping("/find")
    public StudentDTO findStudent(@RequestParam(value = "id") Long id){
        return studentService.findStudentById(id);
    }
    @GetMapping("/find/{id}")
    public StudentDTO findStudent2(@PathVariable("id") Long id){
        return studentService.findStudentById(id);
    }


}
