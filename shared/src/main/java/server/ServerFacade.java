package server;

import com.google.gson.Gson;
import exception.*;
import handlermodel.*;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public RegisterResult register(RegisterRequest req) throws ResponseException {
        var path = "/user";
        //System.out.println("about to make register request from serverfacade, req:" + req);
        return this.makeRequest("POST", path, req, RegisterResult.class);
    }

    public LoginResult login(LoginRequest req) throws ResponseException {
        var path = "/session";
        return this.makeRequest("POST", path, req, LoginResult.class);
    }

    public CreateResult createGame(CreateRequest req) throws ResponseException{
        var path = "/game";
        return this.makeRequest("POST", path, req, CreateResult.class);
    }

    public ListResult listGames() throws ResponseException {
        var path = "/game";
        return this.makeRequest("GET", path, null, ListResult.class);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            System.out.println("current url:" + url);
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            System.out.println("successfully connected");
            throwIfNotSuccessful(http);

            System.out.println("about to readBody");
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            System.out.println("in writeBody, request:" + request);
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        System.out.println("in throwIfNotSuccessful");
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            System.out.println("not successful, status:" + status);
            try (InputStream respErr = http.getErrorStream()) {
                System.out.println("respErr:" + respErr);
                if (respErr != null) {
                    throw ResponseException.fromJson(respErr);
                }
            }
            throw new ResponseException(status, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        System.out.println("in readBody");
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    System.out.println("about to form response");
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
