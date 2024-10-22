package cn.controller;

import cn.dao.CategoryDao;
import cn.dao.RecordDao;
import cn.dto.CategoryDto;

import cn.dto.EditCategoryDto;
import cn.dto.RecordDto;
import cn.entity.Category;
import cn.entity.Record;
import cn.entityConvert.CategoryMapper;
import cn.result.Result;
import cn.utils.JWTUtils;
import cn.vo.CategoryVo;
import cn.vo.RecordVo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static cn.utils.requestUtils.getTokenFromRequest;

@RestController
@Slf4j
@RequestMapping("/api/ledger")
public class LedgerController {
    @Autowired
    private CategoryDao categoryDao;
    @Autowired
    private RecordDao recordDao;

    @Resource
    private CategoryMapper categoryMapper;
    @PostMapping("/category/add")
    @ResponseBody
    public Result save (HttpServletRequest request, @RequestBody CategoryDto categoryDto) {
        Category category = new Category();
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        category.setUsername(username);
        category.setName(categoryDto.getName());
        try {
            categoryDao.insert(category);
        }catch (DuplicateKeyException e){
            log.error(e.getMessage());
            return Result.error(80001, "分类已存在");
        }

        return Result.success(1);
    }

    @PostMapping("/category/edit")
    @ResponseBody
    public Result edit (HttpServletRequest request, @RequestBody EditCategoryDto editCategoryDto) {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", editCategoryDto.getOldName()).eq("username", username);
        Category category = categoryDao.selectOne(queryWrapper);
        if(category != null) {
            category.setName(editCategoryDto.getNewName());
            try {
                categoryDao.updateById(category);
            }catch (DuplicateKeyException e){
                log.error(e.getMessage());
                return Result.error(80001, "分类已存在");
            }
        } else {
            return Result.error(80002, "分类不存在");
        }
        return Result.success(1);
    }

    @PostMapping("/category/delete")
    @ResponseBody
    public Result delete (HttpServletRequest request, @RequestBody CategoryDto categoryDto) {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", categoryDto.getName()).eq("username", username);
        return Result.success(categoryDao.delete(queryWrapper));
    }

    @PostMapping("/record/add")
    @ResponseBody
    public Result saveRecord (HttpServletRequest request, @RequestBody RecordDto recordDto) {
        Record record = new Record();
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username).eq("name", recordDto.getCname());
        Category category = categoryDao.selectOne(queryWrapper);
        if(category == null) {
            return Result.error(80002, "分类不存在");
        }
        record.setCid(category.getId());
        record.setDate(Date.valueOf(LocalDate.now()));
        record.setComment(recordDto.getComment());
        record.setSpend(recordDto.getSpend());
        recordDao.insert(record);
        return Result.success();
    }

    @GetMapping("/category/all")
    @ResponseBody
    public Result<List<CategoryVo>> get_all (HttpServletRequest request) {
        String token = getTokenFromRequest(request);
        String username = JWTUtils.parseJWT(token);
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        List<Category> res = categoryDao.selectList(queryWrapper);
        List<CategoryVo> voList = res.stream().map(this::getCount).collect(Collectors.toList());
//        return Result.success(categoryMapper.toVoList(res));
        return Result.success(voList);
    }

    @GetMapping("/record/all")
    @ResponseBody
    public Result<List<RecordVo>> get_all_record (HttpServletRequest request) {
        String token = getTokenFromRequest(request);
//        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("username", JWTUtils.parseJWT(token));
//        List<Category> res = categoryDao.selectList(queryWrapper);
//        List<RecordVo> recordVos = new ArrayList<>();
//        for(Category category: res){
//            List<RecordVo> recordVoList = selectRecordList(category);
//            records.addAll(recordVoList);
//        }
        List<RecordVo> records = recordDao.getAllRecord(JWTUtils.parseJWT(token));
//        log.info(records.toString());
//        List<RecordVo> recordVos = records.stream().map(this::convertToRecordVo).collect(Collectors.toList());
        return Result.success(records);
    }

    private List<RecordVo> selectRecordList(Category category) {
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cid", category.getId());
        List<Record> recordList= recordDao.selectList(queryWrapper);
        return recordList.stream().map(this::convertToRecordVo)
                .collect(Collectors.toList());
    }

    private RecordVo convertToRecordVo(Record record) {
        RecordVo recordVo = new RecordVo();
        Category category = categoryDao.selectById(record.getCid());
        recordVo.setName(category.getName());
        recordVo.setDate(record.getDate());
        recordVo.setSpend(record.getSpend());
        recordVo.setComment(record.getComment());
        return recordVo;
    }

    private CategoryVo getCount(Category c) {
        CategoryVo res = new CategoryVo();
        res.setName(c.getName());
        QueryWrapper<Record> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("cid", c.getId());
        long count  =recordDao.selectCount(queryWrapper);
        res.setCount(count);
        return res;
    }
}
