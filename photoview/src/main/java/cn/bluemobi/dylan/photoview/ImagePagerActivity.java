package cn.bluemobi.dylan.photoview;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


public class ImagePagerActivity extends FragmentActivity {
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";
    public static final String EXTRA_IS_SHOW_SAVE_BUTTON = "is_show_save_button";

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator;
    private ImageView iv_save;
    private boolean isShowSave;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_pager);

        iv_save = (ImageView) findViewById(R.id.iv_save);


        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        final String[] urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);
        if (getIntent().hasExtra(EXTRA_IS_SHOW_SAVE_BUTTON)) {
            isShowSave = getIntent().getBooleanExtra(EXTRA_IS_SHOW_SAVE_BUTTON, false);
            if (isShowSave) {
                iv_save.setVisibility(View.VISIBLE);
            } else {
                iv_save.setVisibility(View.GONE);
            }
        } else {
            iv_save.setVisibility(View.GONE);
        }

        mPager = (HackyViewPager) findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(
                getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        indicator = (TextView) findViewById(R.id.indicator);

        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
                .getAdapter().getCount());
        indicator.setText(text);
        // 更新下标
        mPager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator,
                        arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
        iv_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new DownLoadImageService(ImagePagerActivity.this, urls[mPager.getCurrentItem()], new ImageCallBack() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ImagePagerActivity.this, "已成功保存到相册", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFail() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ImagePagerActivity.this, "保存失败，请稍后再试", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })).start();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public String[] fileList;

        public ImagePagerAdapter(FragmentManager fm, String[] fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.length;
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList[position];
            return ImageDetailFragment.newInstance(url);
        }

    }
}