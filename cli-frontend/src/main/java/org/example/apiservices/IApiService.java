package org.example.apiservices;

import com.google.gson.JsonArray;

import java.net.http.HttpClient;
import java.util.Scanner;

public interface IApiService {

    JsonArray handleGet(HttpClient client) throws Exception;

    void handlePost(Scanner scanner, HttpClient client) throws Exception;

    void handlePut(Scanner scanner, HttpClient client) throws Exception;

    void handleDelete(Scanner scanner, HttpClient client) throws Exception;
}
