package com.project;

import com.google.gson.Gson;
import com.project.entity.TicketEndEntity;
import com.project.entity.TicketEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class API {

    public static final String host = "http://127.0.0.1:8745/";
    private final int codeOk = 200;

    private String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiaWF0IjoxNjU5NjIzMTc2LCJleHAiOjE2NjAyMjc5NzZ9.vJX-VRYSvPlnKHxOZWyxc6y4kk4MI9f6vbZbda2K9CdbMt-hHMGxMEVyb1mcpqAAO8wX49OdHU_TavEgeVTRmQ";

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

}
