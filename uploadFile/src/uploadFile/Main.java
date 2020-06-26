package uploadFile;

import okhttp3.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {
    public static String urlHOST = "";
    public static final HashMap<String, List<Cookie>> cookieStore = new HashMap<>();

    public static CookieJar cookieJar =
            new CookieJar() {
                private List<Cookie> cookies = new ArrayList<>();

                @Override
                public void saveFromResponse(HttpUrl arg0, List<Cookie> arg1) {
                    System.out.println(">>>>>>>>>>>>>>>>>>>>>save" + arg1.get(0).toString());
                    cookies.addAll(arg1);
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl arg0) {
                    if (cookies.size() > 0)
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>load" + cookies.get(0).toString());
                    return cookies;
                }
            };
    public static OkHttpClient okHttpClient =
            new OkHttpClient.Builder().cookieJar(cookieJar).build(); // OkHttpClient对象

    public static Response getSessionId() {
        String url = urlHOST + "/login.htm"; // 请求链接
        Response response = null;

        Request request = new Request.Builder().url(url).get().build(); // 请求
        Call call = okHttpClient.newCall(request);
        try {
            response = call.execute();
            System.out.println(">>>>>>>>>getSessionId_request：" + request.headers().toString());
            System.out.println(">>>>>>>>>getSessionId_response：" + response.headers().toString());
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static Response loginFcon() {
        String url = urlHOST + "/login.htm"; // 请求链接
        String username = ""; // 请求参数
        String password = ""; // 请求参数
        Response response = null;
        RequestBody formBody =
                new FormBody.Builder().add("username", username).add("password", password).build(); // 表单键值对
        Request request = new Request.Builder().url(url).post(formBody).build(); // 请求
        Call call = okHttpClient.newCall(request);
        try {
            response = call.execute();
            System.out.println(">>>>>>>>>loginFcon_request：" + request.headers().toString());
            System.out.println(">>>>>>>>>loginFcon_response：" + response.headers().toString());
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    public static void uploadFile(String url, String ossId, String filePath, String fileName,
                                  String oss, String bucketname, String ossFileName) {
        RequestBody requestBody =
                new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("file", fileName, RequestBody.create(MediaType.parse("application/octet-stream"), new File(filePath)))
                        .addFormDataPart("ossId", ossId)
                        .addFormDataPart("oss", oss)
                        .addFormDataPart("bucketName", bucketname)
                        .addFormDataPart("ossFileName", ossFileName)
                        .build();
        Request request = new Request.Builder().url(url).post(requestBody).build();
        try {
            System.out.println(">>>>>>>>>>_uploadFile_request:" + request.headers().toString());
            Response response = okHttpClient.newCall(request).execute();
            System.out.println(">>>>>>>>>>_uploadFile_response:" + response.headers().toString());
            System.out.println(">>>>>>>>>>_uploadFile_response_body:" + response.body().string());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String oss = "阿里云";
        String ossid = "100";
        String bucketname = "jellyfish-flash";
        String fileName = "xxx";
        String filePath = "/Users/kascend/Downloads/2/" + fileName;
        String ossFileName = "jellyfish/animation/gift/" + fileName;
        String url = "xxx";

        getSessionId();
        loginFcon();
        uploadFile(url, ossid, filePath, fileName, oss, bucketname, ossFileName);
    }
}
