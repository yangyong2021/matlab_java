package cn.edu.swjtu.yangyong.service.serviceImpl;

import cn.edu.swjtu.yangyong.entity.Methods;
import cn.edu.swjtu.yangyong.mapper.MethodsMapper;
import cn.edu.swjtu.yangyong.service.MethodsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author Yangyong
 * 2023-05-29 16:02
 */
@Service
public class MethodsServiceImpl extends ServiceImpl<MethodsMapper, Methods> implements MethodsService {
}
