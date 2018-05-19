package mario.peric.helpers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HTTPHelper {

    public static final int CODE_SUCCESS = 200;
    public static final int CODE_INVALID_USER_PASS = 404;
    public static final int CODE_USER_EXISTS = 409;

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String EMAIL = "email";
    public static final String SESSION_ID = "sessionid";
    public static final String SENDER = "sender";
    public static final String RECEIVER = "receiver";
    public static final String DATA = "data";
    public static final String CONTACT = "contact";

    public static final String URL_SERVER = "http://18.205.194.168:80";
    public static final String URL_LOGIN = URL_SERVER + "/login";
    public static final String URL_REGISTER = URL_SERVER + "/register";
    public static final String URL_CONTACTS = URL_SERVER + "/contacts";
    public static final String URL_CONTACT = URL_SERVER + "/contact/";
    public static final String URL_MESSAGES = URL_SERVER + "/message/";
    public static final String URL_MESSAGE_SEND = URL_SERVER + "/message";
    public static final String URL_LOGOUT = URL_SERVER + "/logout";


    public JSONArray getJSONArrayFromURL(String urlString, String sessionID) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();

        urlConnection.setRequestMethod("GET");
        urlConnection.setRequestProperty("Accept", "application/json");
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.setRequestProperty(SESSION_ID, sessionID);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            return null;
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        String jsonString = sb.toString();

        int responseCode =  urlConnection.getResponseCode();
        urlConnection.disconnect();
        return responseCode == CODE_SUCCESS ? new JSONArray(jsonString) : null;
    }

    public HTTPResponse postJSONObjectFromURL(String urlString, JSONObject jsonObject) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        HTTPResponse res = new HTTPResponse();
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");
        urlConnection.setConnectTimeout(15000);
        urlConnection.setReadTimeout(1000);

        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            res.code = 404;
            res.message = "Server unreachable";
            return res;
        }
        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();
        res.code =  urlConnection.getResponseCode();
        res.message = urlConnection.getResponseMessage();
        res.sessionId = urlConnection.getHeaderField(SESSION_ID);

        urlConnection.disconnect();
        return res;
    }

    public HTTPResponse postJSONObjectFromURL(String urlString, JSONObject jsonObject, String sessionID) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        HTTPResponse res = new HTTPResponse();
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");
        urlConnection.setRequestProperty(SESSION_ID, sessionID);
        urlConnection.setConnectTimeout(15000);
        urlConnection.setReadTimeout(1000);

        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            res.code = 404;
            res.message = "Server unreachable";
            return res;
        }
        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();
        res.code =  urlConnection.getResponseCode();
        res.message = urlConnection.getResponseMessage();
        res.sessionId = urlConnection.getHeaderField(SESSION_ID);

        urlConnection.disconnect();
        return res;
    }

    public HTTPResponse deleteJSONObjectFromURL(String urlString, JSONObject jsonObject, String sessionID) throws IOException, JSONException {
        HttpURLConnection urlConnection = null;
        HTTPResponse res = new HTTPResponse();
        java.net.URL url = new URL(urlString);
        urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("DELETE");
        urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
        urlConnection.setRequestProperty("Accept","application/json");
        urlConnection.setRequestProperty(SESSION_ID, sessionID);
        urlConnection.setConnectTimeout(15000);
        urlConnection.setReadTimeout(1000);

        urlConnection.setDoOutput(true);
        urlConnection.setDoInput(true);
        try {
            urlConnection.connect();
        } catch (IOException e) {
            res.code = 404;
            res.message = "Server unreachable";
            return res;
        }
        DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());

        os.writeBytes(jsonObject.toString());
        os.flush();
        os.close();
        res.code =  urlConnection.getResponseCode();
        res.message = urlConnection.getResponseMessage();
        res.sessionId = urlConnection.getHeaderField(SESSION_ID);

        urlConnection.disconnect();
        return res;
    }

    public class HTTPResponse {
        public int code;
        public String message;
        public String sessionId;
    }
}
