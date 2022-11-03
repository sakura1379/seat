package com.zlr.seat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zlr.seat.entity.pojo.Client;

/**
 * @author Zenglr
 * @program: seat
 * @packagename: com.zlr.seat.service
 * @Description
 * @create 2022-09-17-下午11:00
 */
public interface IClientService extends IService<Client> {

    /**
     * 根据客户端id获取客户端
     * @param clientId
     * @return
     */
    Client getClientByClientId(String clientId);

    /**
     * 清空缓存
     */
    void clearCache();
}

