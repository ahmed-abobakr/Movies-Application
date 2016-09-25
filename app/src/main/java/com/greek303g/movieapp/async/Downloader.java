package com.greek303g.movieapp.async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.greek303g.movieapp.R;
import com.greek303g.movieapp.data.ParserJson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import de.greenrobot.event.EventBus;

/**
 * Created by ahmed-abobakr on 01/08/16.
 */
public class Downloader extends AsyncTask<String, Void, ParserJson> {

    private final String TAG = Downloader.class.getName();
    boolean errorHandlingData = false, connectingToServer = false;
    String classNameToBeParsed;
    private Context mContext;

    public Downloader(String classNameToBeParsed, Context context){
        this.classNameToBeParsed = classNameToBeParsed;
        this.mContext = context;
    }


    @Override
    protected ParserJson doInBackground(String... strings) {
        HttpURLConnection con = null;
        try {
            URL url = new URL(strings[0]);
             con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            InputStream is = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuffer buffer = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null){
                buffer.append(line);
            }
            String responseStr = buffer.toString();
            Log.i(TAG, "Response " + responseStr);
            ParserJson mData = null;
            try {
                mData = (ParserJson) new Gson().fromJson(responseStr, Class.forName(classNameToBeParsed));
            } catch (ClassNotFoundException e) {
                errorHandlingData = true;
                e.printStackTrace();
            }
            return mData;
        } catch (MalformedURLException e) {
            connectingToServer = true;
            e.printStackTrace();
        } catch (IOException e) {
            errorHandlingData = true;
            e.printStackTrace();
        }finally {
            if(con != null){
                con.disconnect();
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(ParserJson parserJson) {
        super.onPostExecute(parserJson);
        if(parserJson != null) {
            if (!connectingToServer && !errorHandlingData)
                EventBus.getDefault().post(parserJson);
            else if (errorHandlingData)
                Toast.makeText(mContext, mContext.getString(R.string.error_handling_data), Toast.LENGTH_SHORT).show();
            else if (connectingToServer)
                Toast.makeText(mContext, mContext.getString(R.string.error_connection_api), Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(mContext, mContext.getString(R.string.error_handling_data), Toast.LENGTH_SHORT).show();
        }
    }
}
