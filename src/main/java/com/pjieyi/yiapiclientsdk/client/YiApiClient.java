package com.pjieyi.yiapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.pjieyi.yiapiclientsdk.model.User;
import lombok.Data;
import java.util.HashMap;
import java.util.Map;
import static com.pjieyi.yiapiclientsdk.utils.SignUtils.genSign;

/**
 * @author pjieyi
 * @description 调用第三方接口的API
 */

@Data
public class YiApiClient {

    private String accessKey;
    private String secretKey;

    public YiApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    private Map<String,String> getHeadMap(String userName){
        Map<String,String> map=new HashMap<>();
        map.put("accessKey",accessKey);
        //生成一个5位随机数  需要保存 定时清楚
        map.put("nonce", RandomUtil.randomNumbers(5));
        //用户信息
        map.put("userName",userName);
        //通过除以1000，可以将毫秒数转换为秒数
        map.put("timestamp", String.valueOf(System.currentTimeMillis()/1000));
        //签名
        //注意 不能直接传递密钥 可以通过重放来再次发送请求
        map.put("sign",genSign(userName,secretKey));
        return map;
    }

    public YiApiClient() {
    }

    //get
    public String getNameByGet(String name){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result= HttpUtil.get("http://localhost:8081/api/name",paramMap);
        return result;
    }

    //post
    public String getNameByPost(String name){
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
       String result=HttpUtil.post("http://localhost:8081/api/name", paramMap);
       return result;
    }

    //post restful
    public String getNameByPostRestful(User user){
        String userName=user.getName();
        String json = JSONUtil.toJsonStr(user);
        String result2 = HttpRequest.post("http://localhost:8081/api/name/user")
                .addHeaders(getHeadMap(userName))
                .body(json)
                .execute().body();
        return result2;
    }
}
