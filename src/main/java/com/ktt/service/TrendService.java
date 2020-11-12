package com.ktt.service;

import com.ktt.common.ServerResponse;
import com.ktt.pojo.Trend;
import com.ktt.vo.TrendInformation;

import java.util.List;

public interface TrendService {

    // 发布动态(视频)
    ServerResponse<Trend> submitTrend(String userId,Trend trend);

    // 根据id获取所有动态
    ServerResponse<List<TrendInformation>> getUserTrend(String userId);

    // 点赞动态
    ServerResponse<Trend> starTrend(String userId,String trendId,int type);
}
