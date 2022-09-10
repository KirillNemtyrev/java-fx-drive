package com.project;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.entity.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class API {

    public static final String host = "http://127.0.0.1:8745/";
    private final int codeOk = 200;

    private String token;

    public String getToken(){
        return token;
    }

    public API(){}

    public API(String token){
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static boolean pingHost() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("127.0.0.1", 8745), 10);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public Boolean checkToken(String token){

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpPost request = new HttpPost(host + "api/auth/token");
            request.setHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);

            CloseableHttpResponse response = client.execute(request);
            return response.getStatusLine().getStatusCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * The function is used to authorize the user according to the entered parameters.
     * @param login
     * @param password
     * @return AuthEntity
     */

    public AuthEntity authProfile(String login, String password){

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            String params = "{ \"usernameOrEmail\" :\"" + login + "\",\"password\" : \"" + password + "\"}";
            HttpPost request = new HttpPost(host + "api/auth/signin");
            request.setHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);
            request.setEntity(new StringEntity(params, "UTF-8"));

            CloseableHttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != codeOk) return null;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder stringBuffer = new StringBuilder();
            String lineForBuffer = "";

            while ((lineForBuffer = bufferedReader.readLine()) != null) {
                stringBuffer.append(lineForBuffer);
            }
            return new Gson().fromJson(stringBuffer.toString(), AuthEntity.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * The function is used to register a user according to the entered parameters.
     * @param login
     * @param name
     * @param email
     * @param password
     * @return Result register
     */

    public String registerProfile(String login, String name, String email, String password){

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            String params = "{ \"username\" :\"" + login + "\",\"name\" : \"" + name + "\",\"email\" : \"" + email + "\",\"password\" : \"" + password + "\"}";
            HttpPost request = new HttpPost(host + "api/auth/signup");
            request.setHeader("content-type", "application/json");
            request.setEntity(new StringEntity(params, "UTF-8"));

            CloseableHttpResponse response = client.execute(request);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder stringBuffer = new StringBuilder();
            String lineForBuffer = "";

            while ((lineForBuffer = bufferedReader.readLine()) != null) {
                stringBuffer.append(lineForBuffer);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Boolean changeAvatar(File file){

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpEntity httpEntity = MultipartEntityBuilder
                    .create()
                    .addBinaryBody("file", file, ContentType.APPLICATION_OCTET_STREAM, file.getName())
                    .build();

            HttpPost request = new HttpPost(host + "api/avatar");
            request.setHeader("Authorization", "Bearer " + token);
            request.setEntity(httpEntity);

            CloseableHttpResponse response = client.execute(request);
            return response.getStatusLine().getStatusCode() == 200;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Boolean changeUsername(String login) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            String params = "{ \"username\" :\"" + login + "\"}";
            HttpPatch request = new HttpPatch(host + "api/settings/username");
            request.setHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);
            request.setEntity(new StringEntity(params, "UTF-8"));

            CloseableHttpResponse response = client.execute(request);
            return response.getStatusLine().getStatusCode() == 200;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean changeName(String name) {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            String params = "{ \"name\" :\"" + name + "\"}";
            HttpPatch request = new HttpPatch(host + "api/settings/name");
            request.setHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);
            request.setEntity(new StringEntity(params, "UTF-8"));

            CloseableHttpResponse response = client.execute(request);
            return response.getStatusLine().getStatusCode() == 200;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean changePassword(String oldPassword, String newPassword){
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            String params = "{ \"oldPassword\" :\"" + oldPassword + "\"," + "\"newPassword\" :\"" + newPassword + "\"}";
            HttpPatch request = new HttpPatch(host + "api/settings/password");
            request.setHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);
            request.setEntity(new StringEntity(params, "UTF-8"));

            CloseableHttpResponse response = client.execute(request);
            return response.getStatusLine().getStatusCode() == 200;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Boolean sendCodeToChangeEmail(){
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpPost request = new HttpPost(host + "api/settings/email");
            request.setHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);

            CloseableHttpResponse response = client.execute(request);
            return response.getStatusLine().getStatusCode() == 200;

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String changeEmail(String code, String newEmail){

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            String params = "{\"code\" :\"" + code + "\", \"newEmail\" :\"" + newEmail + "\"}";
            HttpPatch request = new HttpPatch(host + "api/settings/email");
            request.setHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);
            request.setEntity(new StringEntity(params, "UTF-8"));

            CloseableHttpResponse response = client.execute(request);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder stringBuffer = new StringBuilder();
            String lineForBuffer = "";

            while ((lineForBuffer = bufferedReader.readLine()) != null) {
                stringBuffer.append(lineForBuffer);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }


    public String sendCodeRecovery(String login){

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            String params = "{ \"usernameOrEmail\" :\"" + login + "\"}";
            HttpPost request = new HttpPost(host + "api/recovery/password");
            request.setHeader("content-type", "application/json");
            request.setEntity(new StringEntity(params, "UTF-8"));

            CloseableHttpResponse response = client.execute(request);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder stringBuffer = new StringBuilder();
            String lineForBuffer = "";

            while ((lineForBuffer = bufferedReader.readLine()) != null) {
                stringBuffer.append(lineForBuffer);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String changePassword(String login, String code, String password){

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            String params = "{\"usernameOrEmail\" :\"" + login + "\", \"code\" :\"" + code + "\", \"password\" : \"" + password + "\"}";
            HttpPatch request = new HttpPatch(host + "api/recovery/password");
            request.setHeader("content-type", "application/json");
            request.setEntity(new StringEntity(params, "UTF-8"));

            CloseableHttpResponse response = client.execute(request);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder stringBuffer = new StringBuilder();
            String lineForBuffer = "";

            while ((lineForBuffer = bufferedReader.readLine()) != null) {
                stringBuffer.append(lineForBuffer);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * The function executes a request to start ticket execution.
     * @return TicketEntity
     */
    public TicketEntity startTicket() {
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpPost request = new HttpPost(host + "api/ticket/start");
            request.setHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);

            CloseableHttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != codeOk) return null;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder stringBuffer = new StringBuilder();
            String lineForBuffer = "";

            while ((lineForBuffer = bufferedReader.readLine()) != null) {
                stringBuffer.append(lineForBuffer);
            }
            return new Gson().fromJson(stringBuffer.toString(), TicketEntity.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * This function executes a request to close the ticket, with its results.
     * @param uuid
     * @param answers
     * @return TicketEndEntity
     */

    public TicketEndEntity endTicket(String uuid, Long[] answers){

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            String params = "{ \"uuid\" :\"" + uuid + "\",\"answers\" : " + Arrays.asList(answers) + "}";
            HttpPost request = new HttpPost(host + "api/ticket/end");
            request.setHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);
            request.setEntity(new StringEntity(params, "UTF-8"));

            CloseableHttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != codeOk) return null;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder stringBuffer = new StringBuilder();
            String lineForBuffer = "";

            while ((lineForBuffer = bufferedReader.readLine()) != null) {
                stringBuffer.append(lineForBuffer);
            }
            return new Gson().fromJson(stringBuffer.toString(), TicketEndEntity.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * This function is used to retake the test.
     * @param uuid
     * @return
     */

    public TicketEntity retryTicket(String uuid){

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            String params = "{ \"uuid\" :\"" + uuid + "\"}";
            HttpPut request = new HttpPut(host + "api/ticket/retry");
            request.setHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);
            request.setEntity(new StringEntity(params, "UTF-8"));

            CloseableHttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != codeOk) return null;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder stringBuffer = new StringBuilder();
            String lineForBuffer = "";

            while ((lineForBuffer = bufferedReader.readLine()) != null) {
                stringBuffer.append(lineForBuffer);
            }
            return new Gson().fromJson(stringBuffer.toString(), TicketEntity.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * This function returns user statistics.
     * @return StatisticEntity
     */

    public StatisticEntity getStatistic(){

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(host + "api/statistic");
            request.setHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);

            CloseableHttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != codeOk) return null;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder stringBuffer = new StringBuilder();
            String lineForBuffer = "";

            while ((lineForBuffer = bufferedReader.readLine()) != null) {
                stringBuffer.append(lineForBuffer);
            }
            return new Gson().fromJson(stringBuffer.toString(), StatisticEntity.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * This function is used to return ticket history.
     * @return TicketHistoryEntity
     */

    public List<TicketHistoryEntity> getHistory(){

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(host + "api/ticket/history");
            request.setHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);

            CloseableHttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != codeOk) return null;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder stringBuffer = new StringBuilder();
            String lineForBuffer = "";

            while ((lineForBuffer = bufferedReader.readLine()) != null) {
                stringBuffer.append(lineForBuffer);
            }
            return new Gson().fromJson(stringBuffer.toString(), new TypeToken<List<TicketHistoryEntity>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public List<SubscribeEntity> getSubscribes(){

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(host + "api/subscribe");
            request.setHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);

            CloseableHttpResponse response = client.execute(request);
            if(response.getStatusLine().getStatusCode() != codeOk) return null;

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder stringBuffer = new StringBuilder();
            String lineForBuffer = "";

            while ((lineForBuffer = bufferedReader.readLine()) != null) {
                stringBuffer.append(lineForBuffer);
            }
            return new Gson().fromJson(stringBuffer.toString(), new TypeToken<List<SubscribeEntity>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public String activateCode(String code){

        try (CloseableHttpClient client = HttpClients.createDefault()) {

            String params = "{\"code\" :\"" + code + "\"}";
            HttpPost request = new HttpPost(host + "api/subscribe/activate");
            request.setHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);
            request.setEntity(new StringEntity(params, "UTF-8"));

            CloseableHttpResponse response = client.execute(request);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder stringBuffer = new StringBuilder();
            String lineForBuffer = "";

            while ((lineForBuffer = bufferedReader.readLine()) != null) {
                stringBuffer.append(lineForBuffer);
            }
            return stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    public Date getDateSubscribe(){
        try (CloseableHttpClient client = HttpClients.createDefault()) {

            HttpGet request = new HttpGet(host + "api/subscribe/date");
            request.setHeader("content-type", "application/json");
            request.addHeader("Authorization", "Bearer " + token);

            CloseableHttpResponse response = client.execute(request);

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder stringBuffer = new StringBuilder();
            String lineForBuffer = "";

            while ((lineForBuffer = bufferedReader.readLine()) != null) {
                stringBuffer.append(lineForBuffer);
            }
            return new Date(Long.parseLong(stringBuffer.toString()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
