package com.ivanfrankov.android.farfor;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class FlickFetch {
    private static final String XML_OFFER = "offer";
    private static final String TAG = "FlickFetch";
    private Context mContext;

    public byte[] getUrlByte(String urlSpec) throws IOException {

        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() +
                        ": with " +
                        urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrl(String urlSpec) throws IOException{
        return new String(getUrlByte(urlSpec),"WINDOWS-1251");
    }

    void parseItems(ArrayList<Dish> dish, XmlPullParser parser) throws IOException, XmlPullParserException {
        int eventType = parser.next();
        Dish d;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_TAG && parser.getName().equals(XML_OFFER)) {
                parser.nextTag();
                d = new Dish();
                while (parser.getEventType() != XmlPullParser.END_TAG){
                    if (parser.getName().equals("name")) {
                        parser.next();
                        d.setName(parser.getText());
                        parser.nextTag();
                        parser.next();
                        parser.nextTag();
                    } else if (parser.getName().equals("price")) {
                        parser.next();
                        d.setPrice(parser.getText());
                        parser.nextTag();
                        parser.next();
                        parser.nextTag();
                    } else if (parser.getName().equals("picture")) {
                        parser.next();
                        d.setUrl(parser.getText());
                        parser.nextTag();
                        parser.next();
                        parser.nextTag();
                    } else if (parser.getName().equals("categoryId")) {
                        parser.next();
                        d.setId(Integer.parseInt(parser.getText()));
                        parser.nextTag();
                        parser.next();
                        parser.nextTag();
                    } else if (parser.getName().equals("description")) {
                        parser.next();
                        d.setDescription(parser.getText());
                        parser.nextTag();
                        parser.next();
                        parser.nextTag();
                    } else if (parser.getName().equals("param") && parser.getAttributeValue(0).equals("Вес")) {
                        parser.next();
                        d.setWeight(parser.getText());
                        parser.nextTag();
                        parser.next();
                        parser.nextTag();
                    }  else {
                        parser.next();
                        parser.nextTag();
                        parser.next();
                        parser.nextTag();
                    }
                }
                dish.add(d);
            }
            eventType = parser.next();
        }
    }

    public ArrayList<Dish> fetchItems() {
        ArrayList<Dish> dish = new ArrayList<Dish>();
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            /*String url = Uri.parse("http://ufa.farfor.ru/getyml/")
                    .buildUpon()
                    .appendQueryParameter("key", "ukAXxeJYZN")
                    .build().toString();
            */

            String xmlString = getUrl("http://farfor.my1.ru/getyml.xml");
            parser.setInput(new StringReader(xmlString));
            parseItems(dish, parser);

        } catch (IOException ioe) {
            Log.e(TAG, "Ошибка" + ioe);
        } catch (XmlPullParserException xppe) {
            Log.e(TAG, "Ошибка" + xppe);
        }

        return dish;

    }
}
