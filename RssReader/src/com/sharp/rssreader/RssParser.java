package com.sharp.rssreader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Xml;

public class RssParser {
    public static List<RSSItem> getRssItems(InputStream is) {

        List<RSSItem> list = null;

        RSSItem item = null;

        try {

            XmlPullParser parser = Xml.newPullParser();

            parser.setInput(is, "utf-8");

            int type = parser.getEventType();

            while (type != XmlPullParser.END_DOCUMENT) {

                switch (type) {

                case XmlPullParser.START_DOCUMENT:

                    list = new ArrayList<RSSItem>();

                    break;

                case XmlPullParser.START_TAG:

                    if ("item".equals(parser.getName())) {

                        item = new RSSItem();

                    }

                    if (item != null) {

                        if ("title".equals(parser.getName())) {

                            item.setTitle(parser.nextText());// 获取元素内容

                        } else if ("link".equals(parser.getName())) {

                            item.setLink(parser.nextText());

                        } else if ("description".equals(parser.getName())) {

                            item.setDescription(parser.nextText());

                        } else if ("pubDate".equals(parser.getName())) {

                            item.setPubDate(parser.nextText());

                        }

                    }

                    break;

                case XmlPullParser.END_TAG:

                    if ("item".equals(parser.getName())) {

                        list.add(item);

                        item = null;

                    }

                    break;

                }

                type = parser.next();

            }

            return list;

        } catch (Exception e) {

            // TODO Auto-generated catch block

            e.printStackTrace();

            return null;

        }

    }

}
