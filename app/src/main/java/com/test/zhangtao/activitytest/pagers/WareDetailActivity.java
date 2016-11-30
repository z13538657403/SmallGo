package com.test.zhangtao.activitytest.pagers;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Response;
import com.test.zhangtao.activitytest.R;
import com.test.zhangtao.activitytest.bean.ShoppingCar;
import com.test.zhangtao.activitytest.bean.User;
import com.test.zhangtao.activitytest.bean.Wares;
import com.test.zhangtao.activitytest.http.OkHttpHelper;
import com.test.zhangtao.activitytest.http.SimpleCallBack;
import com.test.zhangtao.activitytest.msg.BaseRespMsg;
import com.test.zhangtao.activitytest.util.CarProvider;
import com.test.zhangtao.activitytest.widget.CnToolBar;
import com.test.zhangtao.activitytest.widget.Contants;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangtao on 16/10/31.
 */
public class WareDetailActivity extends BaseActivity
{
    @ViewInject(R.id.detail_toolBar)
    private CnToolBar mToolBar;
    @ViewInject(R.id.webView)
    private WebView webView;
    private Wares mWares;
    private WebAppInterface webAppInterface;
    private CarProvider carProvider;
    private ProgressDialog progressDialog;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_ware_detail);
        ViewUtils.inject(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("加载中.....");
        progressDialog.show();

        Serializable serializable = getIntent().getSerializableExtra(Contants.DETAIL_WARES);

        if (serializable == null)
        {
            this.finish();
        }
        mWares = (Wares) serializable;
        carProvider = CarProvider.getInstance();
        carProvider.setMyConext(this);
        initWebView();
        initToolBar();
    }

    private void initToolBar()
    {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                WareDetailActivity.this.finish();
            }
        });
    }

    private void initWebView()
    {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setBlockNetworkImage(false);
        settings.setAppCacheEnabled(true);
        webView.loadUrl(Contants.WARES_DETAIL);
        webAppInterface = new WebAppInterface(this);
        webView.addJavascriptInterface(webAppInterface , "appInterface");
        webView.setWebViewClient(new WC());
    }

    class WC extends WebViewClient
    {
        @Override
        public void onPageFinished(WebView view, String url)
        {
            super.onPageFinished(view, url);
            if (progressDialog != null && progressDialog.isShowing())
            {
                progressDialog.dismiss();
            }
            webAppInterface.showDetail();
        }
    }

    class WebAppInterface
    {
        private Context context;
        public WebAppInterface(Context context)
        {
            this.context = context;
        }

        @JavascriptInterface
        public void showDetail()
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    webView.loadUrl("javascript:showDetail(" + mWares.getId() +")");
                }
            });
        }

        @JavascriptInterface
        public void buy(long id)
        {
            carProvider.put(convertData(mWares));
            Toast.makeText(context , "已经添加到了购物车！" , Toast.LENGTH_SHORT).show();
        }

        @JavascriptInterface
        public void addToCart(long id)
        {
            addToFavorite(mWares);
        }
    }

    public void addToFavorite(Wares wares)
    {
        User user = StoreApplication.getInstance().getUser();
        if (user == null)
        {
            Toast.makeText(this , "请先登录账户！！！" , Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String , Object> params = new HashMap<>(2);
        params.put("user_id" , user.getId());
        params.put("ware_id" , wares.getId());
        okHttpHelper.post(Contants.FAVORITE_CREATE, params, new SimpleCallBack<BaseRespMsg>(this)
        {
            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg)
            {
                if (baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS)
                    Toast.makeText(WareDetailActivity.this , "已经添加到收藏夹" , Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Response response, int code, Exception e)
            {
            }
        });
    }

    public ShoppingCar convertData(Wares item)
    {
        ShoppingCar car = new ShoppingCar();
        car.setId(item.getId());
        car.setDescription(item.getDescription());
        car.setImgUrl(item.getImgUrl());
        car.setName(item.getName());
        car.setPrice(item.getPrice());
        return car;
    }
}
