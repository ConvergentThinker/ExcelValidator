package org.main.UtilsClass;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.main.model.CloudImageModel;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HttpClient {


    public static List<CloudImageModel> usingHTTPClientGET(String endpoint) throws IOException, ParseException {


        String string = "";

        okhttp3.Response response = null;
        OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        }).build();
        Request request = new Request.Builder().url(endpoint).get().build();
        try {
            response = httpClient.newCall(request).execute();
            string = response.body().string();
            response.body().close();
            System.out.println("response : " + string);
        } catch (IOException e) {
            System.err.println("Failed scraping");
            e.printStackTrace();
        }

        List<CloudImageModel> data = new ArrayList<>();


        JSONParser parser = new JSONParser();

       // JSON Parsing of data
        JSONObject object = (JSONObject) parser.parse(string);

        JSONArray jsonArray = (JSONArray) object.get("values");
        System.out.println("jsonArray "+ jsonArray);



        for (int i = 1; i < jsonArray.size(); i++) {

            JSONArray subArray = (JSONArray) jsonArray.get(i);

            CloudImageModel book = new CloudImageModel();

            for (int j = 0; j < subArray.size(); j++) {

                if (j == 0) {
                    book.setId(subArray.get(j).toString().trim());
                } else if (j == 1) {
                    book.setUrl(subArray.get(j).toString().trim());
                } else if (j == 2) {
                    book.setPath(subArray.get(j).toString().trim());

                }
            }

            data.add(book);


        }



        return data;
    }


    public static void main(String[] args) throws IOException, ParseException {


        HttpClient client = new HttpClient();

        List<CloudImageModel> data =   usingHTTPClientGET("https://sheets.googleapis.com/v4/spreadsheets/1C2tp5m_HaJWRkk1Fc6cGJgr7lFeHHD_zj4_nCGn07yY/values/Sheet1!A1:D5?key=AIzaSyCm0iM9CZjxUGy7TUbz_JsK2F89dKRy8fA");


        for (int i = 0; i < data.size(); i++) {
            System.out.println("data "+ data.get(i).getUrl());
        }







    }







}
