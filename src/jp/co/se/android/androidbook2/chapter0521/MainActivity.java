package jp.co.se.android.androidbook2.chapter0521;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;

public class MainActivity extends Activity {
    // 画面に表示するWebViewのFragment
    private MyWebViewFragment mWebFragment;
    // 検索ボックスのView
    private SearchView mSearchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getFragmentManager();

        // 画面のインスタンスを取得
        mWebFragment = (MyWebViewFragment) fm.findFragmentById(android.R.id.content);

        if (mWebFragment == null) {
            // 見つからなければ、コンテンツ領域にFragmentを追加
            mWebFragment = new MyWebViewFragment();
            fm.beginTransaction().add(android.R.id.content, mWebFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        // 履歴があれば、Backキーで前のページヘ戻る
        WebView webView = mWebFragment.getWebView();
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            // 履歴がなければ通常通りBackキーの処理を行い、Activityを終了
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);

        // SearchViewを呼び出す
        final MenuItem searchMenu = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) searchMenu.getActionView();

        // 検索アイコンを検索ボックスの内側に配置
        mSearchView.setIconifiedByDefault(true);

        // 検索開始ボタンを表示
        mSearchView.setSubmitButtonEnabled(false);

        // 検索文字列を入力した際や検索実行時に呼ばれるリスナーを設定
        mSearchView.setOnSearchClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 現在のページのURLをセット
                WebView webView = mWebFragment.getWebView();
                mSearchView.setQuery(webView.getUrl(), false);
            }
        });

        // 検索文字列を入力した際や検索実行時に呼ばれるリスナーを設定
        mSearchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String url;
                if (query.startsWith("http://") || query.startsWith("https://")) {
                    // http://またはhttps://でURLが始まっている場合は、WebViewにURLをそのまま渡す
                    url = query;
                } else {
                    // それ以外の場合はGoogle検索を行う
                    String encodedQuery = Uri.encode(query);
                    url = "https://www.google.co.jp/search?q=" + encodedQuery;
                }
                // URLを読み込む
                mWebFragment.getWebView().loadUrl(url);

                // キーボードを閉じる
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);

                // 検索を閉じる
                searchMenu.collapseActionView();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 検索ボックスにて内容が変更された際に実行
                return false;
            }
        });

        return true;
    }
}
