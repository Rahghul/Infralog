package com.sncf.itif.Services;

import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Rahghul on 06/02/2016.
 */
public class WebServiceUtil {

    private static final int CONNECTION_TIMEOUT = 15000;
    private static final int DATARETRIEVAL_TIMEOUT = 10000;
    private static final String LOGGER_TAG = "----------->Service";


    public static int requestWebServicePOST(String url, String postParameters) {
        if (Log.isLoggable(LOGGER_TAG, Log.INFO)) {
            Log.i(LOGGER_TAG, "Requesting service: " + url);
        }

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(url);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            // handle POST parameters

            if (Log.isLoggable(LOGGER_TAG, Log.INFO)) {
                Log.i(LOGGER_TAG, "POST parameters: " + postParameters);
            }

            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setFixedLengthStreamingMode(postParameters.getBytes().length);
            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");

            //send the POST out
            PrintWriter out = new PrintWriter(urlConnection.getOutputStream());
            out.print(postParameters);
            out.close();


            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                // throw some exception
                Log.d("Http status ERROR", ""+statusCode);
            }
            return statusCode;


        } catch (Exception e) {
            // handle invalid URL
        }  finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return 0;
    }

    public static int requestWebServiceDELETE(String url) {
        if (Log.isLoggable(LOGGER_TAG, Log.INFO)) {
            Log.i(LOGGER_TAG, "Requesting service: " + url);
        }

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(url);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            urlConnection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            urlConnection.setRequestMethod("DELETE");

            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                // throw some exception
                Log.d("Http status ERROR", ""+statusCode);
            }
            Log.d("Http status", "" + statusCode);

            // read output (only for GET)
            return statusCode;

        } catch (Exception e) {
            // handle invalid URL
        }  finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return 0;
    }

    public static String requestWebServiceGET(String url) {
        if (Log.isLoggable(LOGGER_TAG, Log.INFO)) {
            Log.i(LOGGER_TAG, "Requesting service: " + url);
        }

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(url);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);



            // handle issues
            int statusCode = urlConnection.getResponseCode();
            Log.i(LOGGER_TAG, "Http status code " + statusCode);
            if (statusCode != HttpURLConnection.HTTP_OK) {
                // throw some exception
                Log.d("Http status ERROR", ""+statusCode);
                return null;

            }


            // read output (only for GET)
            // create JSON object from content
            InputStream in = urlConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();

        } catch (Exception e) {
            // handle invalid URL
            Log.d("Exception : ",e.toString());
        }  finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }


    public static int requestWebServicePUT(String url, String jSonParam) {
        if (Log.isLoggable(LOGGER_TAG, Log.INFO)) {
            Log.i(LOGGER_TAG, "Requesting service: " + url);
        }

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(url);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);

            // handle POST parameters

            if (Log.isLoggable(LOGGER_TAG, Log.INFO)) {
                Log.i(LOGGER_TAG, "jSon parameters: " + jSonParam);
            }
            urlConnection.setDoOutput(true);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setFixedLengthStreamingMode(jSonParam.getBytes().length);
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");


            OutputStreamWriter osw = new OutputStreamWriter(urlConnection.getOutputStream());
            osw.write(String.format(jSonParam));
            osw.flush();
            osw.close();



            // handle issues
            int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpURLConnection.HTTP_OK) {
                // throw some exception
                Log.d("Http status ERROR", ""+statusCode);
            }
            Log.d("Http status PUT", ""+statusCode);
            return statusCode;


        } catch (Exception e) {
            // handle invalid URL
        }  finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return 0;
    }

    public static String requestWebServiceGETWithAuthBasic(String url) {
        if (Log.isLoggable(LOGGER_TAG, Log.INFO)) {
            Log.i(LOGGER_TAG, "Requesting service: " + url);
        }

        HttpURLConnection urlConnection = null;
        try {
            // create connection
            URL urlToRequest = new URL(url);
            urlConnection = (HttpURLConnection) urlToRequest.openConnection();

            String userCredentials = "a17e2db5-8d10-4747-b709-c216aaf80ae4:"; //"username:password";
            String basicAuth = "Basic " + new String(Base64.encode(userCredentials.getBytes(), Base64.DEFAULT));
            urlConnection.setRequestProperty ("Authorization", basicAuth);
//          urlConnection.setRequestProperty("Authorization", "Basic YTE3ZTJkYjUtOGQxMC00NzQ3LWI3MDktYzIxNmFhZjgwYWU0Og==");
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
            urlConnection.setReadTimeout(DATARETRIEVAL_TIMEOUT);



            // handle issues
            int statusCode = urlConnection.getResponseCode();
            Log.i(LOGGER_TAG, "Http status code " + statusCode);
            if (statusCode != HttpURLConnection.HTTP_OK) {
                // throw some exception
                Log.d("Http status ERROR", ""+statusCode);
                return null;

            }


            // read output (only for GET)
            // create JSON object from content
            InputStream in = urlConnection.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();

        } catch (Exception e) {
            // handle invalid URL
            Log.d("Exception : ",e.toString());
        }  finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }


}
