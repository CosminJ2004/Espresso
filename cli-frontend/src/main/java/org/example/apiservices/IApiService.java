package org.example.apiservices;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.net.http.HttpClient;
import java.util.Scanner;

public interface IApiService {

    JsonArray handleGet(HttpClient client) throws Exception;

    JsonObject handlePost(String json, HttpClient client) throws Exception;

    JsonObject handlePut(String json, HttpClient client, long id) throws Exception;

    void handleDelete(HttpClient client, long id) throws Exception;
}
