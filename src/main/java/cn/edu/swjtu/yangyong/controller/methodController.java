package cn.edu.swjtu.yangyong.controller;

import cn.edu.swjtu.yangyong.common.R;
import cn.edu.swjtu.yangyong.entity.Methods;
import cn.edu.swjtu.yangyong.service.MethodsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Yangyong
 * 2023-05-29 15:58
 */
@RestController
@RequestMapping("/methods")
@Slf4j
public class methodController {

    @Autowired
    private MethodsService methodsService;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        Page pageInfo = new Page(page,pageSize);

        LambdaQueryWrapper <Methods> queryWrapper = new LambdaQueryWrapper<>();

        // 添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Methods::getName,name);

        methodsService.page(pageInfo,queryWrapper);

        log.info("分页查询：{}",pageInfo);
        return R.success(pageInfo);
    }
}
