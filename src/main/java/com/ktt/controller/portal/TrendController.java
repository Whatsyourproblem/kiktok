package com.ktt.controller.portal;

import com.ktt.common.ServerResponse;
import com.ktt.pojo.Trend;
import com.ktt.service.TrendService;
import com.ktt.vo.TrendInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/trend")
public class TrendController {

    @Autowired
    private TrendService trendService;

    // 发布动态
    @PostMapping("/submit_trend_user/{userId}")
    public ServerResponse<Trend> submitTrend(@PathVariable String userId,@RequestBody Trend trend){
        return trendService.submitTrend(userId,trend);
    }

    // 获取某用户的全部动态
    @GetMapping("/get_trends_user/{userId}")
    public ServerResponse<List<TrendInformation>> getUserTrends(@PathVariable String userId){
        return trendService.getUserTrend(userId);
    }

    // 点赞动态
    @PutMapping("/recommend_trend_user/{userId}/{trendId}/{type}")
    public ServerResponse<Trend> starTrend(@PathVariable String userId,@PathVariable String trendId,int type){
        return trendService.starTrend(userId,trendId,type);
    }
}
