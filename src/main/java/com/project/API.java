package com.project;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.entity.StatisticEntity;
import com.project.entity.TicketEndEntity;
import com.project.entity.TicketEntity;
import com.project.entity.TicketHistoryEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
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

}
