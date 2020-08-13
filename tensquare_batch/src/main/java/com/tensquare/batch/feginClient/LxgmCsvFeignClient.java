package com.tensquare.batch.feginClient;

import com.tensquare.req.LxgmRepaymentPlanReq;
import com.tensquare.result.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.feginClient
 * @date: 2020-08-06 23:49:04
 * @describe:
 */
@FeignClient(name = "tensquare-test",serviceId = "lxgmCsvFeignClient",url = "127.0.0.1:9023")
public interface LxgmCsvFeignClient {
    @RequestMapping("test/saveRepaymentPlan")
    Result saveRepaymentPlan(@RequestBody List<LxgmRepaymentPlanReq> lxgmRepaymentPlanReqs);
}
