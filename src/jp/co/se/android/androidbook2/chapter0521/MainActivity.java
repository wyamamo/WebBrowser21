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
    // ��ʂɕ\������WebView��Fragment
    private MyWebViewFragment mWebFragment;
    // �����{�b�N�X��View
    private SearchView mSearchView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FragmentManager fm = getFragmentManager();

        // ��ʂ̃C���X�^���X���擾
        mWebFragment = (MyWebViewFragment) fm.findFragmentById(android.R.id.content);

        if (mWebFragment == null) {
            // ������Ȃ���΁A�R���e���c�̈��Fragment��ǉ�
            mWebFragment = new MyWebViewFragment();
            fm.beginTransaction().add(android.R.id.content, mWebFragment).commit();
        }
    }

    @Override
    public void onBackPressed() {
        // ����������΁ABack�L�[�őO�̃y�[�W�w�߂�
        WebView webView = mWebFragment.getWebView();
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            // �������Ȃ���Βʏ�ʂ�Back�L�[�̏������s���AActivity���I��
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);

        // SearchView���Ăяo��
        final MenuItem searchMenu = menu.findItem(R.id.menu_search);
        mSearchView = (SearchView) searchMenu.getActionView();

        // �����A�C�R���������{�b�N�X�̓����ɔz�u
        mSearchView.setIconifiedByDefault(true);

        // �����J�n�{�^����\��
        mSearchView.setSubmitButtonEnabled(false);

        // �������������͂����ۂ⌟�����s���ɌĂ΂�郊�X�i�[��ݒ�
        mSearchView.setOnSearchClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // ���݂̃y�[�W��URL���Z�b�g
                WebView webView = mWebFragment.getWebView();
                mSearchView.setQuery(webView.getUrl(), false);
            }
        });

        // �������������͂����ۂ⌟�����s���ɌĂ΂�郊�X�i�[��ݒ�
        mSearchView.setOnQueryTextListener(new OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String url;
                if (query.startsWith("http://") || query.startsWith("https://")) {
                    // http://�܂���https://��URL���n�܂��Ă���ꍇ�́AWebView��URL�����̂܂ܓn��
                    url = query;
                } else {
                    // ����ȊO�̏ꍇ��Google�������s��
                    String encodedQuery = Uri.encode(query);
                    url = "https://www.google.co.jp/search?q=" + encodedQuery;
                }
                // URL��ǂݍ���
                mWebFragment.getWebView().loadUrl(url);

                // �L�[�{�[�h�����
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(mSearchView.getWindowToken(), 0);

                // ���������
                searchMenu.collapseActionView();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // �����{�b�N�X�ɂē��e���ύX���ꂽ�ۂɎ��s
                return false;
            }
        });

        return true;
    }
}
