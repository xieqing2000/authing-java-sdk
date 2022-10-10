package test;
import cn.authing.sdk.java.client.ManagementClient;
import cn.authing.sdk.java.dto.*;
import cn.authing.sdk.java.model.ManagementClientOptions;
import cn.authing.sdk.java.util.JsonUtils;



public class GetUserLoggedInIdentitiesTest {

    private static final String ACCESS_KEY_ID = "YOUR_ACCESS_KEY_ID";
    private static final String ACCESS_KEY_SECRET = "YOUR_ACCESS_KEY_SECRET";


    public static void main(String[] args) throws Throwable {
        ManagementClientOptions clientOptions = new ManagementClientOptions(ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        ManagementClient client = new ManagementClient(clientOptions);

        GetUserLoggedInIdentitiesDto request = new GetUserLoggedInIdentitiesDto();
        request.setUserId("userId_8873");
        request.setUserIdType("userIdType_6023");

        UserLoggedInIdentitiesRespDto response = client.getUserLoggedinIdentities(request);
        System.out.println(JsonUtils.serialize(response));
    }

}