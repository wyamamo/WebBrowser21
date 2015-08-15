package jp.co.se.android.androidbook2.chapter0521;

import android.app.ActionBar;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class MyWebViewFragment extends Fragment {
    // 画面に表示するためWebView
    private WebView mWebView;
    private ProgressBar mProgressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Fragemntのレイアウトを生成
        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        // 画面のインスタンスを取得
        mWebView = (WebView) view.findViewById(R.id.webView);
        mProgressbar = (ProgressBar) view.findViewById(R.id.progressbar);

        // WebSettingsオブジェクトを取得
        WebSettings settings = mWebView.getSettings();

        // JavaScriptを有効
        settings.setJavaScriptEnabled(true);

        // Flash等のプラグインを有効
        settings.setPluginState(PluginState.ON_DEMAND);

        // Webページ読み込み時のイベントのリスナーを設定
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // ページの読み込みを開始した際、プログレスバーを表示
                mProgressbar.setVisibility(View.VISIBLE);

                // URLをアクションバーのサブタイトルに設定
                ActionBar actionBar = getActivity().getActionBar();
                if (actionBar != null) {
                    actionBar.setSubtitle(url);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // ページの読み込みを終了した際、プログレスバーを非表示
                mProgressbar.setVisibility(View.GONE);
            }
        });

        // Webページに関する情報を受け取る
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // プログレスバーに現在の進捗を反映する
                mProgressbar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                // ページのタイトルを取得できるタイミングになったら
                // アクションバーにタイトルをセットする
                ActionBar actionBar = getActivity().getActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(title);
                }
            }
        });

        // アクティビティ起動時にURLを読み込む
        if (savedInstanceState == null) {
            mWebView.loadUrl("https://www.google.co.jp/");
        } else {
            // WebViewの状態を復元
            mWebView.restoreState(savedInstanceState);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // WebViewの状態を保存
        mWebView.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    public WebView getWebView() {
        return mWebView;
    }

    @Override
    public void onPause() {
        super.onPause();
        // WebViewで実行中の処理を停止
        mWebView.onPause();
    }

    @Override
    public void onResume() {
        // WebViewの処理を開始
        mWebView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // WebViewを安全に終了
        if (mWebView != null) {
            mWebView.stopLoading();
            mWebView.setWebChromeClient(null);
            mWebView.setWebViewClient(null);
            mWebView.destroy();
            mWebView = null;
        }
        super.onDestroy();
    }
}
