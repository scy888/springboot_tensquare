package com.tensquare.batch.feginClient;

import com.tensquare.client.UserClient;
import com.tensquare.req.UserDtoReq;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.feginClient
 * @date: 2020-08-03 21:59:49
 * @describe:
 */
@FeignClient(name = "tensquare-test",serviceId="userFeignClient",url = "127.0.0.1:9023/test")
@Component
public interface UserFeignClient extends UserClient {
}
