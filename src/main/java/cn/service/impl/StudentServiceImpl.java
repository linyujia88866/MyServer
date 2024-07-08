package cn.service.impl;

import cn.dao.StudentDao;
import cn.dto.StudentDTO;
import cn.entity.StudentEntity;
import cn.hutool.core.convert.Convert;
import org.springframework.util.ObjectUtils;
import cn.hutool.core.util.StrUtil;

import cn.service.StudentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author: 1399543132@qq.com
 * @Date: 2024/01/21/21:14
 * @Description:
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Resource
    StudentDao studentDao;

    @Override
    public Boolean addStudent(StudentDTO studentDTO) {
        //判定主键id是否为空，空则直接false
        if(StrUtil.isNotEmpty(studentDTO.getId().toString())){
            //dto转换为entity后插入
            StudentEntity studentEntity = Convert.convert(StudentEntity.class,studentDTO);
            return studentDao.insert(studentEntity) != 0;
        }
        return false;
    }

    @Override
    public Boolean deleteStudentById(Long id) {
        return studentDao.deleteById(id) != 0;
    }

    @Override
    public Boolean updateStudent(StudentDTO studentDTO) {
        StudentEntity studentEntity = studentDao.selectById(studentDTO.getId());
        //不能查到对应entity，则插入
        if(ObjectUtils.isEmpty(studentEntity)){
            return this.addStudent(studentDTO);
        }
        return studentDao.updateById(Convert.convert(StudentEntity.class,studentDTO)) != 0;
    }

    @Override
    public StudentDTO findStudentById(Long id) {
        StudentEntity studentEntity = studentDao.selectById(id);
        return Convert.convert(StudentDTO.class,studentEntity);
    }
}

