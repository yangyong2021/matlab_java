package cn.edu.swjtu.yangyong.controller;

import cn.edu.swjtu.yangyong.common.R;
import cn.edu.swjtu.yangyong.entity.Datasets;
import cn.edu.swjtu.yangyong.service.DatasetsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Yangyong
 * 2023-05-29 12:15
 */
@RequestMapping("/para")
@Slf4j
@RestController
public class paraController {

    @Autowired
    private DatasetsService datasetsService;

    @GetMapping("/options")
    public R<List<Datasets>> getOptions(){
        List<Datasets> list = datasetsService.list();
        return R.success(list);
    }
}
