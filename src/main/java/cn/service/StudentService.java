package cn.service;


import cn.dto.StudentDTO;

/**
 * @Author: 1399543132@qq.com
 * @Date: 2024/01/21/21:06
 * @Description: 学生表相关Service接口
 */
public interface StudentService {
    //增
    Boolean addStudent(StudentDTO studentDTO);
    //删
    Boolean deleteStudentById(Long id);
    //改
    Boolean updateStudent(StudentDTO studentDTO);
    //查
    StudentDTO findStudentById(Long id);


}
