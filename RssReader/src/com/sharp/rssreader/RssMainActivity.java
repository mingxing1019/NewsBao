package com.sharp.rssreader;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class RssMainActivity extends ListActivity implements OnClickListener {
    private Spinner rssSelectSpinner;
    // private ListView listView;
    private PullToRefreshListView listView;
    private String[] rssurlarray;
    private String[] rssnamearray;
    private String currentRssurl;
    private static final String TAG = "RssMainActivity";
    private static final int RssMessge = 0;
    private static final int RssTitleMessge = 1;
    private static final int RssPubDateMessge = 2;
    private static final int RssDescriptionMessge = 3;
    private static final int RssLinkMessge = 4;
    private static final int refreshEnd = 5;

    private TextView tuijianTextView = null;
    private TextView redianTextView = null;
    private TextView nanjingTextView = null;
    private TextView shipingTextView = null;
    private TextView shehuiTextView = null;
    private TextView dingyueTextView = null;
    private TextView yuleTextView = null;
    private TextView zhaopianTextView = null;

    Handler RssHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case RssMessge:
                updateNewsTitleListView(msg);
                listView.onRefreshComplete();
                break;
            case refreshEnd:
            	listView.onRefreshComplete();
                break;
                
            /**
             * case RssPubDateMessge: // break; case RssDescriptionMessge: //
             * break; case RssLinkMessge: // break;
             **/
            }

        }

    };

    private void updateNewsTitleListView(Message msg) {
        Bundle mBundle = new Bundle();
        mBundle = msg.getData();
        List<String> titles = mBundle.getStringArrayList("newstile");
        final List<String> links = mBundle.getStringArrayList("newslink");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                RssMainActivity.this,
                android.R.layout.simple_expandable_list_item_1, titles);
        listView.setAdapter(adapter);
        Log.d(TAG, "listView.setAdapter(adapter)");

        /* click listener */
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                    long arg3) {
                // TODO Auto-generated method stub
                Log.d(TAG, "onItemClick position->" + arg3);
                String address = links.get((int) arg3);

                /* ��һ�ַ�����ʹ��Ĭ�ϻ��ߵ�������������� */
                /*
                 * Intent viewMessage = new Intent(Intent.ACTION_VIEW,
                 * Uri.parse(address)); startActivity(viewMessage);
                 */

                /* �ڶ��ַ�����ʹ��Webview�ڱ�Ӧ���� ��activity���� */
                /*
                 * setContentView(R.layout.webview); WebView webView = (WebView)
                 * findViewById(R.id.newswebView); webView.loadUrl(address);
                 * //����WebViewĬ��ʹ�õ�������ϵͳĬ�����������ҳ����Ϊ��ʹ��ҳ��WebView��
                 * webView.setWebViewClient(new WebViewClient(){
                 * 
                 * @Override public boolean shouldOverrideUrlLoading(WebView
                 * view, String url) { // TODO Auto-generated method stub
                 * //����ֵ��true��ʱ�����ȥWebView�򿪣�Ϊfalse����ϵͳ�����������������
                 * view.loadUrl(url); return true; } });
                 */

                /* �����ַ�����ʹ��Webview�ڱ�Ӧ���� ��activity���� */
                Intent intent = new Intent();
                intent.putExtra("netaddress", address);
                intent.setAction("com.sharp.rssreader.web.LwebActivity");
                startActivity(intent);
            }

        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        tuijianTextView = (TextView) findViewById(R.id.tuijian);
        redianTextView = (TextView) findViewById(R.id.redian);
        nanjingTextView = (TextView) findViewById(R.id.nanjing);
        shipingTextView = (TextView) findViewById(R.id.shiping);
        shehuiTextView = (TextView) findViewById(R.id.shehui);
        dingyueTextView = (TextView) findViewById(R.id.dingyue);
        yuleTextView = (TextView) findViewById(R.id.yule);
        zhaopianTextView = (TextView) findViewById(R.id.zhaopian);

        rssSelectSpinner = (Spinner) findViewById(R.id.rssSourceSpinner);
        listView = (PullToRefreshListView) findViewById(R.id.newsList);
        initRssUrlArray();
        //loadSpinner();
        
        //init url
        currentRssurl = RssMainActivity.this.rssurlarray[0];
        
        tuijianTextView.setOnClickListener(this);
        redianTextView.setOnClickListener(this);
        nanjingTextView.setOnClickListener(this);
        shipingTextView.setOnClickListener(this);
        shehuiTextView.setOnClickListener(this);
        dingyueTextView.setOnClickListener(this);
        yuleTextView.setOnClickListener(this);
        zhaopianTextView.setOnClickListener(this);

        /* pull listener */
        listView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                startloadRssThread();
            }
        });

        // Add an end-of-list listener
        listView.setOnLastItemVisibleListener(new OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {
                Toast.makeText(RssMainActivity.this, "End of List!",
                        Toast.LENGTH_SHORT).show();
            }
        });
        
        startloadRssThread();
    }

    /*
     * ��ʼ��Rss����Դ��url���� ����ǰ����õ�rssԴ��xml�ļ��л�ȡrssԴ������
     */
    public void initRssUrlArray() {
        this.rssurlarray = getResources().getStringArray(R.array.source_url);
        this.rssnamearray = getResources().getStringArray(R.array.source_title);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        switch (item_id) {
        // ����ǵ���˸��²˵�,�����¼���һ��rss����
        case R.id.refresh:
            startloadRssThread();
            Log.d(TAG, "Has been done loadRSS().");
            break;
        // ����ǵ���˹��ڲ˵�,�򵯳��Ի���,��������ʾһЩ�����ҵ���Ϣ,���ڲ���Ӯ���Թ��
        case R.id.about:
            //
            break;
        // �����������˳��˵�,���˳�Activity,��������Ӧ�ó���ֻ��һ��Activity,���˳���Ӧ�ó���
        case R.id.exit:
            this.finish();
            Log.d(TAG, "Has been finish().");
            break;
        }
        return true;
    }

    public void startloadRssThread() {
        Runnable mRunnable = new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                try {
                    loadRss();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        };
        new Thread(mRunnable).start();
    }

    /*
     * ��������ѡ�е�spinner�����ض�Ӧ������
     */
    public void loadRss() throws IOException {
        Log.d(TAG, "currentRssurl->" + currentRssurl);
        if (this.currentRssurl == null) {
            showTips("chose a rss url first!");
        } else {
            URL url = null;
            try {
                url = new URL(currentRssurl);
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            InputStream rssInputStream = null;
            rssInputStream = url.openStream();

            if (rssInputStream != null) {
                Log.d(TAG, "rssInputStream->" + rssInputStream);
                List<RSSItem> RSSItemList = null;
                RSSItemList = RssParser.getRssItems(rssInputStream);
                if (RSSItemList != null) {
                    Log.d(TAG, "RSSItemList->" + RSSItemList);
                    // title,pubDate,description,link;
                    List<String> titles = new ArrayList<String>(
                            RSSItemList.size());
                    List<String> pubDates = new ArrayList<String>(
                            RSSItemList.size());
                    List<String> descriptions = new ArrayList<String>(
                            RSSItemList.size());
                    List<String> links = new ArrayList<String>(
                            RSSItemList.size());

                    for (RSSItem item : RSSItemList) {
                        titles.add(item.getTitle());
                        pubDates.add(item.getPubDate());
                        descriptions.add(item.getDescription());
                        links.add(item.getLink());
                        Log.d(TAG, "Add title->" + item.getTitle());
                    }
                    Message msg = new Message();
                    Bundle bundle = new Bundle();
                    bundle.putStringArrayList("newstile",
                            (ArrayList<String>) titles);
                    bundle.putStringArrayList("newspubDate",
                            (ArrayList<String>) pubDates);
                    bundle.putStringArrayList("newsdescription",
                            (ArrayList<String>) descriptions);
                    bundle.putStringArrayList("newslink",
                            (ArrayList<String>) links);
                    msg.setData(bundle);
                    msg.what = RssMessge;
                    RssHandler.sendMessage(msg);
                }

            }
        }
    }

    public void showTips(String tip) {
        Toast toast = Toast.makeText(this, tip, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 10);
        toast.show();
    }

    /*
     * ��ʼ��rssԴ�������б�
     */

    public void loadSpinner() {
        // ����Adapter���Ұ�����Դ
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, rssnamearray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // �� Adapter���ؼ�
        rssSelectSpinner.setAdapter(adapter);
        rssSelectSpinner
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                            View view, int pos, long id) {
                        parent.setVisibility(View.VISIBLE);
                        RssMainActivity.this.currentRssurl = RssMainActivity.this.rssurlarray[pos];
                        Log.d(TAG, "rssSelectSpinner onItemSelected->" + pos);
                        startloadRssThread();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Another interface callback
                    }
                });

    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View.OnClickListener#onClick(android.view.View)
     */
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        int id = v.getId();
        int pos = 0;
        switch (id) {
        case R.id.tuijian:
            pos = 0;
            break;
        case R.id.redian:
            pos = 1;
            break;
        case R.id.nanjing:
            pos = 2;
            break;
        case R.id.shiping:
            pos = 3;
            break;
        case R.id.shehui:
            pos = 4;
            break;
        case R.id.dingyue:
            pos = 5;
            break;
        case R.id.yule:
            pos = 6;
            break;
        case R.id.zhaopian:
            pos = 7;
            break;
        }
        // �����ɫ
        // TextView tv = (TextView) v;
        // tv.setTextColor(Color.parseColor("#ffffffff"));

        // rssԴ����
        RssMainActivity.this.currentRssurl = RssMainActivity.this.rssurlarray[pos];
        startloadRssThread();

    }

}
