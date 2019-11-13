package com.example.implanthearts;

import android.util.Log;

import com.google.gson.Gson;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2018/12/4.
 */

public class Getshuju2 {
    public static HttpClient httpClient;
    String appId = Constant.APPID;
    String secret = Constant.SECRET;
    String urlLogin = Constant.APP_AUTH;
    String urldatahistory=Constant.QUERY_DEVICE_HISTORY_DATA;
    String deviceCommands=Constant.POST_ASYN_CMD;
//    String deviceId = "777070b2-ae5a-403a-972e-e8ad4c005324";
//    String gatewayId="777070b2-ae5a-403a-972e-e8ad4c005324";
    public  String deviceId;
    public  String gatewayId;

    String accessToken;

    public static String Temp,Light,Soil,CO2,Humidity;//温度，光，土壤，二氧化碳，湿度
    public static String Fan_State,Yellow_Light_State,White_Light_State,Spray_State;//风扇，黄灯，白灯，喷雾的状态


    public static int count;
    public static String wendu1,shidu1,guang1,turang1,wendu2,shidu2,guang2,turang2;
    public static String time4;

    public  static float avgwendu;//平均温度
    public  static float avgshidu;//平均湿度
    public  static float avgturang;//平均土壤湿度
    public  static float avgguang1;//场地一平均光照强度
    public  static float avgguang2;//场地二平均光照强度

     String wendu[];
     String shidu[];
     String turang[];
     String guang11[];
     String guang22[];

    String ss;

    Calendar c = Calendar.getInstance();
    int yy = c.get(Calendar.YEAR);//系统的年份
    int mm = c.get(Calendar.MONTH);//月份
    int dd = c.get(Calendar.DAY_OF_MONTH);//日


    String a(int year, int month, int dayOfMonth){
        String s;
        int y=year;
        int m=month+1;
        int d=dayOfMonth;
        int m1=0,m2=0,d1=0,d2=0;
        m2=m/10;//十位
        d2=d/10;
        if(m2==0 && d2!=0){
            s=String.valueOf(y)+"0"+String.valueOf(m)+String.valueOf(d);
        }
        else if(d2==0 && m2 !=0){
            s=String.valueOf(y)+String.valueOf(m)+"0"+String.valueOf(d);
        }
        else if(d2==0 && m2 ==0){
            s=String.valueOf(y)+"0"+String.valueOf(m)+"0"+String.valueOf(d);
        }
        else
        {
            s=String.valueOf(y)+String.valueOf(m)+String.valueOf(d);
        }
        return  s;
    }

    public  void GetaccessToken() throws Exception {

        // 服务器端需要验证的客户端证书
        KeyStore keyStore = KeyStore.getInstance("PKCS12");
        // 客户端信任的服务器端证书
        KeyStore trustStore = KeyStore.getInstance("BKS");

        InputStream ksIn = MyApplication.getCustomApplicationContext().getAssets().open(Constant.SELFCERTPATH);
        InputStream tsIn=MyApplication.getCustomApplicationContext().getAssets().open(Constant.TRUSTCAPATH);

        try {
            keyStore.load(ksIn, Constant.SELFCERTPWD.toCharArray());
            trustStore.load(tsIn, Constant.TRUSTCAPWD.toCharArray());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ksIn.close();
            } catch (Exception ignore) {
            }
            try {
                tsIn.close();
            } catch (Exception ignore) {
            }
        }

//        SSLContext sc = SSLContext.getInstance("TLS");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
        keyManagerFactory.init(keyStore, Constant.SELFCERTPWD.toCharArray());
//        sc.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

        SSLSocketFactory ssl = new SSLSocketFactory(keyStore, Constant.SELFCERTPWD, trustStore);
        ssl.setHostnameVerifier(new AllowAllHostnameVerifier());
//        ssl.getSocketFactory().setHostnameVerifier(new AllowAllHostnameVerifier());
        httpClient = new DefaultHttpClient();
        if (ssl != null) {
            Scheme sch = new Scheme("https", ssl, 443);   //从这里开始研究
            httpClient.getConnectionManager().getSchemeRegistry().register(sch);
        }
        Map<String, String> param = new HashMap<>();
        param.put("appId", appId);
        param.put("secret", secret);

        List<NameValuePair> nvps = new LinkedList<NameValuePair>();
        Set<Map.Entry<String, String>> paramsSet = param.entrySet();
        for (Map.Entry<String, String> paramEntry : paramsSet) {
            nvps.add(new BasicNameValuePair(paramEntry.getKey(), paramEntry
                    .getValue()));
        }
        HttpPost request = new HttpPost(urlLogin);

        request.setEntity(new UrlEncodedFormEntity(nvps));
        //get accessToken
        HttpResponse response = executeHttpRequest(request);
    }
    private HttpResponse executeHttpRequest(HttpUriRequest request) {
        HttpResponse response = null;
        String result = "ERROR";
        try {
            response = httpClient.execute(request);
        } catch (Exception e) {
            System.out.println("executeHttpRequest failed.");
        } finally {
            if (response.getStatusLine().getStatusCode() == 200)
            {
                try {
                    result= convertStreamToString(response.getEntity().getContent());
                    Log.w("bbb","result:"+result);
                    //{"accessToken":"13e85324da547a3577feb8f3ac09ce6","tokenType":"bearer","refreshToken":"6629faa00902da8f879d131d821a6","expiresIn":3600,"scope":"default"}

                    parseJSONWithGSON(result);
                    //Log.d("a","accessToken:"+app.getAccessToken());
                    //accessToken=app.getAccessToken();


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
            else
            {
                try {
                    Log.e(TAG , "CommonPostWithJson | response error | "
                            + response.getStatusLine().getStatusCode()
                            + " error :"
                            + EntityUtils.toString(response.getEntity()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
//            try {
//                System.out.println("aaa  ==  response -> " + response);
//                HttpEntity entity = response.getEntity();
//
//            } catch (Exception e) {
//                System.out.println("IOException: " + e.getMessage());
//            }
        }

        return response;
    }

    //get数据和时间，只有一个数据
    public void GetDataandTime() {
        try {
            // 服务器端需要验证的客户端证书
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            // 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance("BKS");
            InputStream ksIn = MyApplication.getCustomApplicationContext().getAssets().open(Constant.SELFCERTPATH);
            InputStream tsIn=MyApplication.getCustomApplicationContext().getAssets().open(Constant.TRUSTCAPATH);

            try {
                keyStore.load(ksIn, Constant.SELFCERTPWD.toCharArray());
                trustStore.load(tsIn, Constant.TRUSTCAPWD.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    ksIn.close();
                } catch (Exception ignore) {
                }
                try {
                    tsIn.close();
                } catch (Exception ignore) {
                }
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            keyManagerFactory.init(keyStore, Constant.SELFCERTPWD.toCharArray());


            SSLSocketFactory ssl = new SSLSocketFactory(keyStore, Constant.SELFCERTPWD, trustStore);
            ssl.setHostnameVerifier(new AllowAllHostnameVerifier());




            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    //不校验服务器端证书域名
                    return true;
                }
            };

            OkHttpClient mOkHttpClient = new OkHttpClient().newBuilder().hostnameVerifier(hostnameVerifier)
                    .sslSocketFactory(sc.getSocketFactory()).build();
//            RequestBody requestBodyPost = new FormBody.Builder()
//                    .add("appId", Constant.APPID)
//                    .add("secret", Constant.SECRET)
//                    .add("refreshToken","123")
//                    .build();


            Request request = null;

            request = new Request.Builder()
                    .url(urldatahistory + "?deviceId=" + deviceId +
                            "&gatewayId=" + gatewayId+"&appId="+appId)
                    .addHeader("app_key", appId)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("Content-Type", "application/json")
                    .build();


            Call call = mOkHttpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String str = response.body().string();
                    parseJSONWithGSON2(str);
                    Log.w("sss","str:"+str);

                }
            });

        } catch (Exception e) {
            Log.w("sss","str:"+e.getMessage());
        }
    }

    //post 命令
    public void post(String deviceId,String ServiceId,String Method,String x,String Control) {
        try {
            // 服务器端需要验证的客户端证书
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            // 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance("BKS");

            InputStream ksIn = MyApplication.getCustomApplicationContext().getAssets().open(Constant.SELFCERTPATH);
            InputStream tsIn = MyApplication.getCustomApplicationContext().getAssets().open(Constant.TRUSTCAPATH);
            try {
                keyStore.load(ksIn, Constant.SELFCERTPWD.toCharArray());
                trustStore.load(tsIn, Constant.TRUSTCAPWD.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    ksIn.close();
                } catch (Exception ignore) {
                }
                try {
                    tsIn.close();
                } catch (Exception ignore) {
                }
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            keyManagerFactory.init(keyStore, Constant.SELFCERTPWD.toCharArray());


            SSLSocketFactory ssl = new SSLSocketFactory(keyStore, Constant.SELFCERTPWD, trustStore);
            ssl.setHostnameVerifier(new AllowAllHostnameVerifier());




            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    //不校验服务器端证书域名
                    return true;
                }
            };

            OkHttpClient mOkHttpClient = new OkHttpClient().newBuilder().hostnameVerifier(hostnameVerifier)
                    .sslSocketFactory(sc.getSocketFactory()).build();
            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            JsonBean jsonBean=new JsonBean();
            Command command=new Command();
            Paras paras=new Paras();
            jsonBean.setDeviceId(deviceId);
            command.setServiceId(ServiceId);//"Light_LED"
            command.setMethod(Method);//"Control"
            paras.setControl_Peripheral(Control);//"LIGHTOFF"

            jsonBean.setCommand(command);
            command.setParas(paras);
            Gson gson = new Gson();
            String json = gson.toJson(jsonBean);
            Log.w("sss",json);
            RequestBody requestBody = RequestBody.create(JSON, json);

        /*    RequestBody requestBodyPost = new FormBody.Builder()
                    .add(requestBody)
                    .build();
        */

            Request request = null;

            request = new Request.Builder()
                    .url(deviceCommands + "?appId="+appId)//2018/11/11 16:10:10  和实际相差8个小时  20181111T150000Z  2018/11/11 23:00:00 ? 获取11/11那天的数据 20181111T081000Z
                    .addHeader("app_key", appId)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("Content-Type", "application/json")
                    .post(requestBody)
                    .build();

            Call call = mOkHttpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {


                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String str = response.body().string();
                    Log.w("sss",str);
                    //parseJSONWithGSON2(str);


                }
            });

        } catch (Exception e) {
            Log.w("sss",e.getMessage());
        }
    }


    //get当天的全部数据，用来计算出平均数据
    public void GetData(String serviceId) {
        try {
            // 服务器端需要验证的客户端证书
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            // 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance("BKS");
            InputStream ksIn = MainApplication.getCustomApplicationContext().getAssets().open(Constant.SELFCERTPATH);
            InputStream tsIn=MainApplication.getCustomApplicationContext().getAssets().open(Constant.TRUSTCAPATH);

            try {
                keyStore.load(ksIn, Constant.SELFCERTPWD.toCharArray());
                trustStore.load(tsIn, Constant.TRUSTCAPWD.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    ksIn.close();
                } catch (Exception ignore) {
                }
                try {
                    tsIn.close();
                } catch (Exception ignore) {
                }
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            keyManagerFactory.init(keyStore, Constant.SELFCERTPWD.toCharArray());


            SSLSocketFactory ssl = new SSLSocketFactory(keyStore, Constant.SELFCERTPWD, trustStore);
            ssl.setHostnameVerifier(new AllowAllHostnameVerifier());




            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            final HostnameVerifier hostnameVerifier = new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    //不校验服务器端证书域名
                    return true;
                }
            };

            OkHttpClient mOkHttpClient = new OkHttpClient().newBuilder().hostnameVerifier(hostnameVerifier)
                    .sslSocketFactory(sc.getSocketFactory()).build();
//            RequestBody requestBodyPost = new FormBody.Builder()
//                    .add("appId", Constant.APPID)
//                    .add("secret", Constant.SECRET)
//                    .add("refreshToken","123")
//                    .build();



            String time =a(yy,mm,dd);
            Log.w("ttt",time+"");

            Request request = null;

            request = new Request.Builder()
                    .url(urldatahistory + "?deviceId=" + deviceId +
                            "&gatewayId=" + gatewayId +
                            "&serviceId=" +serviceId+
                    "&startTime=20190414T010000Z")  //改为："&startTime="+time+"T010000Z"
                    .addHeader("app_key", appId)
                    .addHeader("Authorization", "Bearer " + accessToken)
                    .addHeader("Content-Type", "application/json")
                    .build();


            Call call = mOkHttpClient.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, final IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    final String str = response.body().string();
                    parseJSONWithGSON3(str);
                    Log.w("sss","str:"+str);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //解析accessToken
    private void parseJSONWithGSON(String jsonData) {
        try
        {
            //app = new Gson().fromJson(jsonData, JsonRootBean.class);
            JSONObject jsonObject = new JSONObject(jsonData);
            accessToken = jsonObject.getString("accessToken");
            accessToken = accessToken.trim();
            Log.w("sss", "accessToken:"+accessToken);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    //解析数据和时间
    private void parseJSONWithGSON2(String jsonData) {

        try
        {
            //JsonRootBean2 app2 = new Gson().fromJson(jsonData, JsonRootBean2.class);
      /*      List<DeviceDataHistoryDTOs> deviceDataHistoryDTOs = app2.getDeviceDataHistoryDTOs();
            for(int i=0;i<deviceDataHistoryDTOs.size();i++) {
                int batteryLevel = deviceDataHistoryDTOs.get(i).getData().getBatteryLevel();
                Log.d("a", "" + batteryLevel);
                String time = app2.getDeviceDataHistoryDTOs().get(i).getTimestamp();
                Log.d("a", time);
            }
*/
            JSONObject jsonObject = new JSONObject(jsonData);
            String  json=jsonObject.getString("deviceDataHistoryDTOs");

            JSONArray jsonArray = new JSONArray(json);
            count= jsonObject.getInt("totalCount");
            Log.w("ttt","count:"+count);


        //正确的代码

            //for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                String ss=jsonObject1.getString("serviceId");
                Log.w("ttt",ss);
                String data= jsonObject1.getString("data");//{"S1Cur":92}
//                String data2 = data.substring(data.indexOf(":") + 1, data.indexOf("}"));//92
                //Log.d("a",data);

                //data打印
                // {"Temp":0,"Light":86,"Soil":86,"CO2":551,"Humidity":0}
//{"Temp":27,"Light":91,"Soil":62,"CO2":795,"Humidity":81,"Fan_State":0,"Yellow_Light_State":1,"White_Light_State":1,"Spray_State":0}
            String[]  t1=data.split(",");
            for(int i=0;i<t1.length;i++){
                Log.w("ttt","data:"+t1[i].toString());
            }

            //t1打印
            //{"Temp":0
            //"Light":86
            //"Soil":86
            //"CO2":551
            //"Humidity":0}

                 Temp = t1[0].substring(t1[0].indexOf(":") + 1);//0
                 Light = t1[1].substring(t1[1].indexOf(":") + 1);//86
                 Soil = t1[2].substring(t1[2].indexOf(":") + 1);//86
                 CO2 =  t1[3].substring(t1[3].indexOf(":") + 1);//551
                Humidity = t1[4].substring(t1[4].indexOf(":") + 1);//0

            Fan_State = t1[5].substring(t1[5].indexOf(":") + 1);//0
            Yellow_Light_State = t1[6].substring(t1[6].indexOf(":") + 1);//0
            White_Light_State = t1[7].substring(t1[7].indexOf(":") + 1);//0
            Spray_State = t1[8].substring(t1[8].indexOf(":") + 1, t1[8].indexOf("}"));//0


            Log.w("ttt","Temp:"+Temp);
            Log.w("ttt","Light:"+Light);
            Log.w("ttt","Soil:"+Soil);
            Log.w("ttt","CO2:"+CO2);
            Log.w("ttt","Humidity:"+Humidity);
            Log.w("rrr","Fan_State:"+Fan_State);
            Log.w("rrr","Yellow_Light_State:"+Yellow_Light_State);
            Log.w("rrr","White_Light_State:"+White_Light_State);
            Log.w("rrr","Spray_State:"+Spray_State);


                String time=jsonObject1.getString("timestamp");//20181111T031758Z
            Log.w("hyx","time:"+time);
                String time2 = time.substring(time.indexOf("T") + 1, time.indexOf("Z"));//031758
            Log.w("hyx","time2:"+time2);

                String time3= String.valueOf(Integer.parseInt(time2.substring(0, 2))+8)+time2.substring(2,6);//111758
            Log.w("hyx","time3:"+time3);

            int length = time3.length();
            Log.w("hyx","length:"+length);
            if(length == 5){
                time3 = "0"+time3;
                Log.w("hyx","time3:"+time3);
            }

            StringBuilder  sb = new StringBuilder (time3);
                sb.insert(4, ":");
                sb.insert(2, ":");
                time4 = sb.toString();//11:17:58
                //Log.d("a",time);
                Log.w("hyx","time4:"+time4);

            //Log.d("a",time);

//               if(ss.equals("Temperature")){
//                   wendu1=tttdata2;
//                   wendu2=data2;
//                   Log.w("sss",wendu1);
//               }
//               if(ss.equals("Humidity")){
//                   shidu1=data2;
//                   shidu2=data2;
//                   Log.w("sss",wendu2);
//               }
//               if(ss.equals("S1")){
//                   turang1=data2;
//                   Log.w("sss",turang1);
//               }
//               if(ss.equals("L1")){
//                   guang1=data2;
//                   Log.w("sss",turang1);
//               }
//               if(ss.equals("S2")){
//                   turang2=data2;
//                   Log.w("sss",turang2);
//               }
//               if(ss.equals("L2")){
//                   guang2=data2;
//                   Log.w("sss",guang2);
//               }


            //}


        }
        catch (Exception e)
        {
            Log.w("ttt",e.getMessage());
        }
    }


    //解析数据
    private void parseJSONWithGSON3(String jsonData) {

        try
        {
            //JsonRootBean2 app2 = new Gson().fromJson(jsonData, JsonRootBean2.class);
      /*      List<DeviceDataHistoryDTOs> deviceDataHistoryDTOs = app2.getDeviceDataHistoryDTOs();
            for(int i=0;i<deviceDataHistoryDTOs.size();i++) {
                int batteryLevel = deviceDataHistoryDTOs.get(i).getData().getBatteryLevel();
                Log.d("a", "" + batteryLevel);
                String time = app2.getDeviceDataHistoryDTOs().get(i).getTimestamp();
                Log.d("a", time);
            }
*/
            JSONObject jsonObject = new JSONObject(jsonData);
            String  json=jsonObject.getString("deviceDataHistoryDTOs");

            JSONArray jsonArray = new JSONArray(json);
            count= jsonObject.getInt("totalCount");
            Log.w("sss","count:"+count);


            wendu = new String [jsonArray.length()];
            shidu = new String [jsonArray.length()];
            turang = new String [jsonArray.length()];
            guang11 = new String [jsonArray.length()];
            guang22 = new String [jsonArray.length()];

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                ss=jsonObject1.getString("serviceId");
                Log.w("sss",ss);
                String data= jsonObject1.getString("data");//{"S1Cur":92}
                String data2 = data.substring(data.indexOf(":") + 1, data.indexOf("}"));//92
                //Log.d("a",data);
                Log.w("sss","data2:"+data2);

//                String time=jsonObject1.getString("timestamp");//20181111T031758Z
//                String time2 = time.substring(time.indexOf("T") + 1, time.indexOf("Z"));//031758
//                String time3=String.valueOf(Integer.parseInt(time2.substring(0, 2))+8)+time2.substring(2,6);//111758
//
//                if(time3.length()==5){
//                    time3="0"+time3;
//                }
//
//                StringBuilder  sb = new StringBuilder (time3);
//                sb.insert(4, ":");
//                sb.insert(2, ":");
//                time4 = sb.toString();//11:17:58
//                Log.d("qqq",time);
//                Log.d("qqq",time2);
//                Log.d("qqq",time3);
//                //Log.d("a",time);
//                Log.w("qqq","time4:"+time4);


                if(ss.equals("Temperature")){
                    wendu1=data2;
                    wendu2=data2;
                    wendu[i] = data2;
                    Log.w("sss",wendu1);
                }
                if(ss.equals("Humidity")){
                    shidu1=data2;
                    shidu2=data2;
                    shidu[i] = data2;
                    Log.w("sss",wendu2);
                }
                if(ss.equals("S1")){
                    turang1=data2;
                    turang[i]= data2;
                    Log.w("sss",turang1);
                }
                if(ss.equals("L1")){
                    guang1=data2;
                    guang11 [i] = data2;
                    Log.w("sss",turang1);
                }
                if(ss.equals("S2")){
                    turang2=data2;
                    Log.w("sss",turang2);
                }
                if(ss.equals("L2")){
                    guang2=data2;
                    guang22 [i] = data2;
                    Log.w("sss",guang2);
                }


            }

            float sumwendu = 0;
            float sumshidu = 0;
            float sumturang = 0;
            float sumguang1 = 0;
            float sumguang2 = 0;

            //计算平均温度
            if(ss.equals("Temperature")) {

                for (int j = 0; j < jsonArray.length(); j++) {
                    sumwendu += Integer.parseInt(wendu[j]);
                    avgwendu = sumwendu / wendu.length;
                    Log.w("ddd","温度="+wendu[j]);
                }
                Log.w("ddd","平均温度="+avgwendu);
            }

            //计算平均湿度
            if(ss.equals("Humidity")) {
                for (int j = 0; j < jsonArray.length(); j++) {
                    sumshidu += Integer.parseInt(shidu[j]);
                    avgshidu = sumshidu / shidu.length;
                    Log.w("ddd", "湿度=" + shidu[j]);
                }
                Log.w("ddd","平均湿度="+avgshidu);

            }

            //计算平均土壤湿度
            if(ss.equals("S1")) {
                for (int j = 0; j < jsonArray.length(); j++) {
                    sumturang += Integer.parseInt(turang[j]);
                    avgturang = sumturang / turang.length;
                    Log.w("ddd","土壤湿度="+turang[j]);
                }
                Log.w("ddd","平均土壤湿度="+avgturang);

            }

            //计算场地一平均光照强度
            if(ss.equals("L1")){
                for (int j = 0; j < jsonArray.length(); j++) {
                    sumguang1 += Integer.parseInt(guang11[j]);
                    avgguang1 = sumguang1 / guang11.length;
                    Log.w("ddd","光照强度="+guang11[j]);
                }Log.w("rrr","平均光照强度="+avgguang1);
            }


            //计算场地二平均光照强度
            if(ss.equals("L2")){
                for (int j = 0; j < jsonArray.length(); j++) {
                    sumguang2 += Integer.parseInt(guang22[j]);
                    avgguang2 = sumguang2 / guang22.length;
                    Log.w("ddd","光照强度="+guang22[j]);
                }Log.w("rrr","平均光照强度="+avgguang2);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public static String convertStreamToString(InputStream is) {
        /*
          * To convert the InputStream to String we use the BufferedReader.readLine()
          * method. We iterate until the BufferedReader return null which means
          * there's no more data to read. Each line will appended to a StringBuilder
          * and returned as String.
          */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }


}
