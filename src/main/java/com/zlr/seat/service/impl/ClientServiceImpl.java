package com.zlr.seat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zlr.seat.common.aop.cache.Cache;
import com.zlr.seat.dao.mapper.ClientMapper;
import com.zlr.seat.entity.pojo.Client;
import com.zlr.seat.service.IClientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.service.impl
 * @Description
 * @create 2022-09-17-下午11:01
 */
@Service
@Slf4j
public class ClientServiceImpl extends ServiceImpl<ClientMapper, Client> implements IClientService {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    /**
     * 根据客户端id获取客户端
     *
     * @param clientId
     * @return
     */
    @Override
    @Cacheable(value = "client", key = "#clientId")
//    @Cache(expire = 5 * 60 * 1000,name = "Client")
    public Client getClientByClientId(String clientId) {
        QueryWrapper<Client> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Client::getClientId,clientId);
        return getOne(queryWrapper,false);
    }

    /**
     * 清空缓存
     */
    @Override
    @CacheEvict(value = "client",allEntries = true)
    public void clearCache() {
//        Set<String> keys = stringRedisTemplate.keys("Client" + "*");
//        Long deleteNum = stringRedisTemplate.delete(keys);
        log.info("清空client缓存");
    }
}
