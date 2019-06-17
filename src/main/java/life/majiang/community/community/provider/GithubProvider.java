package life.majiang.community.community.provider;

import com.alibaba.fastjson.JSON;
import life.majiang.community.community.dto.AccessTokenDTO;
import life.majiang.community.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static okhttp3.RequestBody.*;

/**
 *
 */
@Component
public class GithubProvider {

    // @Autowired

    public String getAccessToken(AccessTokenDTO accessTokenDTO) {

        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();

        RequestBody body = create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {

            String string = response.body().string();
            System.out.println("string = " + string);

            final String[] tokenSplit = string.split("&");
            String tokenStr = tokenSplit[0];
            final String[] split = tokenStr.split("=");
            final String token = split[1];

            return token;
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }


    /**
     * @param accesessToken
     * @return
     */
    public GithubUser getUser(String accesessToken) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token=" + accesessToken)
                .build();

        GithubUser githubUser = null;

        try {
            Response response = client.newCall(request).execute();

            String string = response.body().string();

            System.out.println("getUser -- string = "+string);

            githubUser = JSON.parseObject(string, GithubUser.class);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return githubUser;
    }


}
