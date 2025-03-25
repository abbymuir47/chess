package exception;

import com.google.gson.Gson;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ResponseException extends Exception {

    public ResponseException(String message) {
        super(message);
    }

    public String toJson() {
        return new Gson().toJson(Map.of("message", getMessage()));
    }

    public static ResponseException fromJson(InputStream stream) {
        var message = new Gson().fromJson(new InputStreamReader(stream), ExceptionMessage.class);
        return new ResponseException(message.message());
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
