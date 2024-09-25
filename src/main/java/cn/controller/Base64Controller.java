package cn.controller;

import cn.dto.EncodeDto;
import cn.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static cn.utils.Base64Util.decodeToString;
import static cn.utils.Base64Util.encodeToString;

@RestController
@Slf4j
@RequestMapping("/api/base64")
public class Base64Controller {
    @PostMapping("/encode")
    @ResponseBody
    public Result encode (HttpServletRequest request, @RequestBody EncodeDto encodeDto){
        String articleId = encodeToString(encodeDto.getData());
        return Result.success(articleId);
    }

    @PostMapping("/decode")
    @ResponseBody
    public Result decode (HttpServletRequest request, @RequestBody EncodeDto decodeDto) {
        String articleId = decodeToString(decodeDto.getData());
        return Result.success(articleId);
    }
}
