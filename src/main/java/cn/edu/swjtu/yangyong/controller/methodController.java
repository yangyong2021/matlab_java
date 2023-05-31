package cn.edu.swjtu.yangyong.controller;

import cn.edu.swjtu.yangyong.common.R;
import cn.edu.swjtu.yangyong.entity.Methods;
import cn.edu.swjtu.yangyong.service.MethodsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mathworks.engine.MatlabEngine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.Queue;

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
    public R<Page> page(int page, int pageSize, String name) {
        Page pageInfo = new Page(page, pageSize);

        LambdaQueryWrapper<Methods> queryWrapper = new LambdaQueryWrapper<>();

        // 添加过滤条件
        queryWrapper.like(StringUtils.isNotEmpty(name), Methods::getName, name);

        methodsService.page(pageInfo, queryWrapper);

        log.info("分页查询：{}", pageInfo);
        return R.success(pageInfo);
    }

    // 0: 未寻优；1：已寻优 ；2：排队中； 3：寻优中
    private Queue<String[]> queue = new LinkedList<>();
    private Integer status = 0; // 保存请求状态，初始为0 表示未执行

    @PostMapping("/execute")
    public R<Integer> executeMethod(@RequestParam("method") String method,
                                    @RequestParam("dataset") String dataset) {
        if (status != 0) {
            status = 2;
            update(method, dataset, status);
            queue.offer(new String[]{"queued", method, dataset});
            // 第一次请求正在执行，将第二次请求状态设置为2表示排队中
            return R.success(status);
        } else {
            // 第一次请求未执行，开始执行并将状态设置为 3 寻优中
            status = 3;
            // 执行 executive 方法（耗时较长）
            log.info("第一次执行的方法：running 方法= {}, 数据集={}", method, dataset);
            update(method, dataset, status);
            executiveMethod(method, dataset);
            update(method, dataset, 1); // 1 寻优完成
            // 执行完成后将状态设置为"completed
            while (!queue.isEmpty()) {
                String[] q = queue.poll();
                status = 3;
                update(q[1], q[2], status);
                log.info("队列中的方法：running 方法= {}, 数据集={}", q[1], q[2]);
                // 异步返回状态码给前端
                executiveMethod(q[1], q[2]);
                update(q[1], q[2], 1);
            }
            status = 0;
            return R.success(1);
        }
    }

    /**
     * 更新数据库中的methods的status
     *
     * @param method
     * @param dataset
     * @param status
     */
    public void update(String method, String dataset, Integer status) {
        // 构建拉姆达表达式，更新状态码
        LambdaQueryWrapper<Methods> queryWrapper = new LambdaQueryWrapper<>();
        // 通过主方法和数据集构建条件
        queryWrapper.eq(Methods::getMainName, method).eq(Methods::getDataName, dataset);
        Methods methods = new Methods();
        methods.setStatus(status);
        methodsService.update(methods, queryWrapper);
    }

    /**
     * 执行matlab方法
     *
     * @param method
     * @param dataset
     */
    private void executiveMethod(String method, String dataset) {
        // 执行长时间耗时的方法
        try {
            MatlabEngine eng = MatlabEngine.startMatlab();
            eng.eval("addpath('H:\\MatlabForJava\\imgs\\')");
            Integer results = eng.feval(1, method, dataset);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
