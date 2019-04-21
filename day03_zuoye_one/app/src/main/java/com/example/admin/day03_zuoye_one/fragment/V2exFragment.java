package com.example.admin.day03_zuoye_one.fragment;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;

import com.example.admin.day03_zuoye_one.R;
import com.example.admin.day03_zuoye_one.adapter.V2exVpAdapter;
import com.example.admin.day03_zuoye_one.base.BaseFragment;
import com.example.admin.day03_zuoye_one.bean.V2exTabBean;
import com.example.admin.day03_zuoye_one.pressert.V2exP;
import com.example.admin.day03_zuoye_one.v2exfragment.V2exListFragment;
import com.example.admin.day03_zuoye_one.view.V2exV;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by admin on 2019/4/3.
 * 1808A邢鑫鑫
 */

public class V2exFragment extends BaseFragment<V2exV, V2exP> implements V2exV {
    @BindView(R.id.v2ex_tab)
    TabLayout v2exTab;
    @BindView(R.id.v2ex_img)
    ImageView v2exImg;
    @BindView(R.id.v2ex_vp)
    ViewPager v2exVp;
    private String url = "https://www.v2ex.com/";

    @SuppressLint("HandlerLeak")
    Handler hand = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ArrayList<V2exTabBean> list = (ArrayList<V2exTabBean>) msg.obj;
            ArrayList<Fragment> fragments = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                fragments.add(new V2exListFragment());
                v2exTab.addTab(v2exTab.newTab().setText(list.get(i).getTab()));
            }
            V2exVpAdapter v2exVpAdapter = new V2exVpAdapter(getChildFragmentManager(), fragments);
            v2exVp.setAdapter(v2exVpAdapter);
            v2exTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    v2exVp.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
            v2exVp.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(v2exTab));
        }
    };

    @Override
    protected V2exP initPressert() {
        return new V2exP();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_v2ex;
    }

    @Override
    protected void initData() {
        super.initData();
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Document document = Jsoup.connect(url).get();
                    //class等于masthead的div标签
                    Element tabs = document.select("div#Tabs").first();
                    Elements allTabls = tabs.select("a[href]");
                    ArrayList<V2exTabBean> mTabList = new ArrayList<>();
                    for (Element element : allTabls) {
                        //获取href属性
                        String href = element.attr("href");
                        //获取标签里的文本
                        String linkText = element.text();
                        V2exTabBean v2exTabBean = new V2exTabBean(href, linkText);
                        mTabList.add(v2exTabBean);
                        Message message = new Message();
                        message.obj = mTabList;
                        hand.sendMessage(message);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
