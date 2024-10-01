package cn.controller;

import cn.dto.EncodeDto;
import cn.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import static cn.utils.UnicodeUtil.decodeUnicode;
import static cn.utils.UnicodeUtil.encodeToUnicode;

@RestController
@Slf4j
@RequestMapping("/api/unicode")
public class UnicodeController {
    @PostMapping("/encode")
    @ResponseBody
    public Result encode (HttpServletRequest request, @RequestBody EncodeDto encodeDto){
        String res = encodeToUnicode(encodeDto.getData());
        return Result.success(res);
    }

    @PostMapping("/decode")
    @ResponseBody
    public Result decode (HttpServletRequest request, @RequestBody EncodeDto decodeDto) {
        String res = decodeUnicode(decodeDto.getData());
        return Result.success(res);
    }
}
