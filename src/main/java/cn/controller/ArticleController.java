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
import java.util.Objects;

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
        boolean publish = articledto.getPublish();

        long currentTimeMillis = System.currentTimeMillis();

        // 将当前时间戳转换为java.sql.Timestamp
        Timestamp timestamp = new Timestamp(currentTimeMillis);
        String content = articledto.getContent();
        log.info("保存文章{}成功！", articleId);
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        articleService.saveArticle(articleId, title, timestamp, content, username, publish);
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
        log.info("更新文章{}成功！", articleId);
        return Result.success(res);
    }

    @PostMapping("/addOneRead/{articleId}")
    @ResponseBody
    public Result addOneRead (@PathVariable String articleId) {
        int res = articleService.addOneRead(articleId);
        log.info("文章{}阅读数+1！", articleId);
        return Result.success(res);
    }

    @PostMapping("/addOneLike/{articleId}")
    @ResponseBody
    public Result addOneLike (@PathVariable String articleId) {
        int res = articleService.addOneLike(articleId);
        log.info("文章{}点赞数+1！", articleId);
        return Result.success(res);
    }

    @PostMapping("/publish/{articleId}")
    @ResponseBody
    public Result publish (HttpServletRequest request,@PathVariable String articleId) {
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        int res = articleService.publishArticle(articleId, username);
        log.info("发布文章{}成功！", articleId);
        return Result.success(res);
    }

    @GetMapping("/articles")
    @ResponseBody
    public Result getAllArticles(HttpServletRequest request){
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        List<ArticleVo> res =  articleService.getAllArticles(username);
        return Result.success(res);
    }
    @GetMapping("/pubArticles")
    @ResponseBody
    public Result getPublishArticles(HttpServletRequest request){
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        List<ArticleVo> res =  articleService.getAllArticles(username, 1);
        log.info(res.toString());
        return Result.success(res);
    }

    @GetMapping("/AllPubArticles")
    @ResponseBody
    public Result getAllPubArticles(){
        List<ArticleVo> res =  articleService.getAllPubArticles();
        return Result.success(res);
    }

    @GetMapping("/priArticles")
    @ResponseBody
    public Result getPrivateArticles(HttpServletRequest request){
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        List<ArticleVo> res =  articleService.getAllArticles(username, 0);
        return Result.success(res);
    }


    @GetMapping("/get/{articleId}")
    @ResponseBody
    public Result getArticleById(HttpServletRequest request, @PathVariable("articleId") String articleId){
        String username = JWTUtils.parseJWT(getTokenFromRequest(request));
        Article res =  articleService.findById(articleId);
//        if(!Objects.equals(res.getUsername(), username)){
//            log.info("文章作者和请求文章内容的用户不一致，所以阅读数量+1！");
//            articleService.addOneRead(articleId);
//        }
        articleService.addOneRead(articleId);
        return Result.success(res);
    }
}
