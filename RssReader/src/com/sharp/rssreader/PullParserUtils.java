package com.sharp.rssreader;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
/*Not use*/
public class PullParserUtils {
    public static List<Person> parserXmlByPull(InputStream inputStream)
            throws Exception {
        List<Person> persons = null;
        Person person = null;

        // 创建XmlPullParserFactory解析工厂
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // 通过XmlPullParserFactory工厂类实例化一个XmlPullParser解析类
        XmlPullParser parser = factory.newPullParser();
        // 根据指定的编码来解析xml文档
        parser.setInput(inputStream, "utf-8");

        // 得到当前的事件类型
        int eventType = parser.getEventType();
        // 只要没有解析到xml的文档结束，就一直解析
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
            // 解析到文档开始的时候
            case XmlPullParser.START_DOCUMENT:
                persons = new ArrayList<Person>();
                break;
            // 解析到xml标签的时候
            case XmlPullParser.START_TAG:
                if ("person".equals(parser.getName())) {
                    person = new Person();
                    // 得到person元素的第一个属性，也就是ID
                    person.setId(Integer.parseInt(parser.getAttributeValue(0)));
                } else if ("name".equals(parser.getName())) {
                    // 如果是name元素，则通过nextText()方法得到元素的值
                    person.setName(parser.nextText());
                } else if ("age".equals(parser.getName())) {
                    person.setAge(Integer.parseInt(parser.nextText()));
                }
                break;
            // 解析到xml标签结束的时候
            case XmlPullParser.END_TAG:
                if ("person".equals(parser.getName())) {
                    persons.add(person);
                    person = null;
                }
                break;
            }
            // 通过next()方法触发下一个事件
            eventType = parser.next();
        }

        return persons;
    }

}
