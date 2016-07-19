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

        // ����XmlPullParserFactory��������
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        // ͨ��XmlPullParserFactory������ʵ����һ��XmlPullParser������
        XmlPullParser parser = factory.newPullParser();
        // ����ָ���ı���������xml�ĵ�
        parser.setInput(inputStream, "utf-8");

        // �õ���ǰ���¼�����
        int eventType = parser.getEventType();
        // ֻҪû�н�����xml���ĵ���������һֱ����
        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
            // �������ĵ���ʼ��ʱ��
            case XmlPullParser.START_DOCUMENT:
                persons = new ArrayList<Person>();
                break;
            // ������xml��ǩ��ʱ��
            case XmlPullParser.START_TAG:
                if ("person".equals(parser.getName())) {
                    person = new Person();
                    // �õ�personԪ�صĵ�һ�����ԣ�Ҳ����ID
                    person.setId(Integer.parseInt(parser.getAttributeValue(0)));
                } else if ("name".equals(parser.getName())) {
                    // �����nameԪ�أ���ͨ��nextText()�����õ�Ԫ�ص�ֵ
                    person.setName(parser.nextText());
                } else if ("age".equals(parser.getName())) {
                    person.setAge(Integer.parseInt(parser.nextText()));
                }
                break;
            // ������xml��ǩ������ʱ��
            case XmlPullParser.END_TAG:
                if ("person".equals(parser.getName())) {
                    persons.add(person);
                    person = null;
                }
                break;
            }
            // ͨ��next()����������һ���¼�
            eventType = parser.next();
        }

        return persons;
    }

}
