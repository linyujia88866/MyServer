package cn.controller;


import cn.utils.JWTUtils;
import cn.dto.ArticleDto;
import cn.entity.Article;
import cn.result.Result;
import cn.service.ArticleService;
import cn.utils.UuidUtil;
import cn.vo.ArticleVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.List;

import static cn.utils.requestUtils.getTokenFromRequest;

@RestController
@Slf4j
@RequestMapping("/api/article")
public class ArticleController {

    //注入userService
    @Autowired
    private ArticleService articleService;

    /**
     * 用户注册接口
     * @param articledto 包含用户名和密码的用户对象，通过RequestBody接收前端传来的JSON数据
     * @return 返回注册结果，如果用户名不存在，则返回注册成功结果；否则返回错误信息
     * 对参数没有进行验证，仅用于演示
     */
    @PostMapping("/save")
    @ResponseBody
    public Result save (HttpServletRequest request,@RequestBody ArticleDto articledto) {
        String articleId = UuidUtil.getUuid();
        String title = articledto.getTitle();

        long currentTimeMillis = System.currentTimeMillis();

        // 将当前时间戳转换为java.sql.Timestamp
        Timestamp timestamp = new Timestamp(currentTimeMillis);
        String content = articledto.getContent();
        log.info("保存文章{}成功！", articleId);
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        articleService.saveArticle(articleId, title, timestamp, content, username);
        return Result.success(articleId);
    }

    @PostMapping("/update/{articleId}")
    @ResponseBody
    public Result update (HttpServletRequest request,@RequestBody ArticleDto articledto, @PathVariable String articleId) {
        String title = articledto.getTitle();
        long currentTimeMillis = System.currentTimeMillis();
        // 将当前时间戳转换为java.sql.Timestamp
        Timestamp timestamp = new Timestamp(currentTimeMillis);
        String content = articledto.getContent();
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        int res = articleService.updateArticle(articleId, title, timestamp, content,username);
        log.info("保存文章{}成功！", articleId);
        return Result.success(res);
    }

    @GetMapping("/articles")
    @ResponseBody
    public Result getAllArticles(HttpServletRequest request){
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        List<ArticleVo> res =  articleService.getAllArticles(username);
        return Result.success(res);
    }


    @GetMapping("/get/{articleId}")
    @ResponseBody
    public Result getArticleById(HttpServletRequest request, @PathVariable("articleId") String articleId){
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        Article res =  articleService.findById(articleId, username);
        return Result.success(res);
    }
}
