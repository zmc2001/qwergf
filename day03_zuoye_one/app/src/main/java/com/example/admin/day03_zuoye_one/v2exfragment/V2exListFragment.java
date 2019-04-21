package com.example.admin.day03_zuoye_one.v2exfragment;

import android.util.Log;

import com.example.admin.day03_zuoye_one.R;
import com.example.admin.day03_zuoye_one.base.BaseFragment;
import com.example.admin.day03_zuoye_one.pressert.EmptyP;
import com.example.admin.day03_zuoye_one.view.EmptyV;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class V2exListFragment extends BaseFragment<EmptyV,EmptyP> implements EmptyV {
    private static final String TAG = "V2exListFragment";
    private String url = "https://www.v2ex.com/";

    @Override
    protected EmptyP initPressert() {
        return new EmptyP();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_v2ex_list;
    }

    @Override
    protected void initData() {
        super.initData();
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Document doc = Jsoup.connect(url).get();
                    //新闻item数据
                    Elements items = doc.select("div.cell.item");
                    for (Element item :items) {
                        //图片
                        Element image = item.select("table tr td a > img.avatar").first();
                        String src = image.attr("src");
                        //Log.d(TAG, "src: "+src);

                        //评论数量和评论链接地址
                        Element comment = item.select("table tbody tr td a.count_livid").first();
                        if (comment != null) {
                            String href = comment.attr("href");
                            String text = comment.text();
                            //Log.d(TAG, "评论: "+",链接:"+href+",数量:"+text);
                        }
                        //标题
                        Element title = item.select("table tbody tr td span.item_title > a").first();
                        String text = title.text();
                        Log.d(TAG, "标题: " + text);

                        //topic_info
                        Element topic = item.select("table tbody tr td span.topic_info").first();
                        Element secondaryTab = topic.select("a.node").first();
                        String secTab = secondaryTab.text();
                        Log.d(TAG, "secTab: " + secTab);

                        String topicText = topic.text();
                        Log.d(TAG, "topicText: " + topicText);

                        Elements people = topic.select("strong > a");
                        if (people.size() > 0) {
                            //作者
                            Element element = people.get(0);
                            Log.d(TAG, "作者: " + element.text());
                        }

                        if (people.size() > 1) {
                            //最后的评论者
                            Element element = people.get(1);
                            Log.d(TAG, "最后的评论者: " + element.text());
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
    }
}
