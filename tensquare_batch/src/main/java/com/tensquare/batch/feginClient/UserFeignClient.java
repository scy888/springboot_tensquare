package com.tensquare.batch.feginClient;

import com.tensquare.client.UserClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

/**
 * @author: scyang
 * @program: tensquare_parent
 * @package: com.tensquare.batch.feginClient
 * @date: 2020-08-03 21:59:49
 * @describe:
 */
@FeignClient(name = "tensquare-test",serviceId="userFeignClient",url = "127.0.0.1:9023")
@Component
public interface UserFeignClient extends UserClient {
}
