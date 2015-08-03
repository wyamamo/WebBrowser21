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
    // ��ʂɕ\�����邽��WebView
    private WebView mWebView;
    private ProgressBar mProgressbar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Fragemnt�̃��C�A�E�g�𐶐�
        View view = inflater.inflate(R.layout.fragment_webview, container, false);

        // ��ʂ̃C���X�^���X���擾
        mWebView = (WebView) view.findViewById(R.id.webView);
        mProgressbar = (ProgressBar) view.findViewById(R.id.progressbar);

        // WebSettings�I�u�W�F�N�g���擾
        WebSettings settings = mWebView.getSettings();

        // JavaScript��L��
        settings.setJavaScriptEnabled(true);

        // Flash���̃v���O�C����L��
        settings.setPluginState(PluginState.ON_DEMAND);

        // Web�y�[�W�ǂݍ��ݎ��̃C�x���g�̃��X�i�[��ݒ�
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // �y�[�W�̓ǂݍ��݂��J�n�����ہA�v���O���X�o�[��\��
                mProgressbar.setVisibility(View.VISIBLE);

                // URL���A�N�V�����o�[�̃T�u�^�C�g���ɐݒ�
                ActionBar actionBar = getActivity().getActionBar();
                if (actionBar != null) {
                    actionBar.setSubtitle(url);
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // �y�[�W�̓ǂݍ��݂��I�������ہA�v���O���X�o�[���\��
                mProgressbar.setVisibility(View.GONE);
            }
        });

        // Web�y�[�W�Ɋւ�������󂯎��
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // �v���O���X�o�[�Ɍ��݂̐i���𔽉f����
                mProgressbar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                // �y�[�W�̃^�C�g�����擾�ł���^�C�~���O�ɂȂ�����
                // �A�N�V�����o�[�Ƀ^�C�g�����Z�b�g����
                ActionBar actionBar = getActivity().getActionBar();
                if (actionBar != null) {
                    actionBar.setTitle(title);
                }
            }
        });

        // �A�N�e�B�r�e�B�N������URL��ǂݍ���
        if (savedInstanceState == null) {
            mWebView.loadUrl("https://www.google.co.jp/");
        } else {
            // WebView�̏�Ԃ𕜌�
            mWebView.restoreState(savedInstanceState);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // WebView�̏�Ԃ�ۑ�
        mWebView.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    public WebView getWebView() {
        return mWebView;
    }

    @Override
    public void onPause() {
        super.onPause();
        // WebView�Ŏ��s���̏������~
        mWebView.onPause();
    }

    @Override
    public void onResume() {
        // WebView�̏������J�n
        mWebView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        // WebView�����S�ɏI��
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
