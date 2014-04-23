
package com.xnp.wimp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;

public class MainActivity extends ActionBarActivity {

    private static final String TAG = "MainActivity";
    private static final boolean DBG = BuildConfig.DEBUG;

    private BMapManager mBMapMan = null;
    private MapView mMapView = null;

    private ItemizedOverlay mOverlayTest;

    private GeoPoint[] gps = {
            new GeoPoint((int) (39.550000 * 1e6), (int) (116.00100 * 1e6)),
            new GeoPoint((int) (39.550050 * 1e6), (int) (116.00140 * 1e6)),
            new GeoPoint((int) (39.550200 * 1e6), (int) (116.00170 * 1e6)),
            new GeoPoint((int) (39.550000 * 1e6), (int) (116.00200 * 1e6)),
            new GeoPoint((int) (39.549800 * 1e6), (int) (116.00220 * 1e6)),
            new GeoPoint((int) (39.549400 * 1e6), (int) (116.00250 * 1e6)),
            new GeoPoint((int) (39.549600 * 1e6), (int) (116.00280 * 1e6)),
            new GeoPoint((int) (39.550000 * 1e6), (int) (116.00300 * 1e6)),
            new GeoPoint((int) (39.550400 * 1e6), (int) (116.00340 * 1e6)),
            new GeoPoint((int) (39.550800 * 1e6), (int) (116.00370 * 1e6))

    };

    private int mPointCount = gps.length;
    private int mCurPoint = 0;

    private synchronized void autoChangeViewPagerPosition() {

        // mCheckTS = System.currentTimeMillis();

        Message message = Message.obtain();
        message.what = mCurPoint++;

        handler.sendMessageDelayed(message, 3500l);
    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            int position = msg.what;

            // mViewPg.setCurrentItem((position + 1) % mDotCount);
            
            OverlayItem item = new OverlayItem(gps[mCurPoint % mPointCount], "here id" + mCurPoint,
                    "here name");
            item.setAnchor(OverlayItem.ALIGN_BOTTON);
            // item.setMarker(MainActivity.this.getResources().getDrawable(R.drawable.ic_launcher));

            mOverlayTest.addItem(item);

            if (DBG)
                Log.e(TAG, "gps:" + item.getTitle());

            mMapView.refresh();
            autoChangeViewPagerPosition();
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // if (savedInstanceState == null) {
        // getSupportFragmentManager().beginTransaction()
        // .add(R.id.container, new PlaceholderFragment())
        // .commit();
        // }
        mBMapMan = new BMapManager(getApplication());
        mBMapMan.init(null);
        // 注意：请在试用setContentView前初始化BMapManager对象，否则会报错
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.bmapsView);
        mMapView.setBuiltInZoomControls(true);
        // 设置启用内置的缩放控件
        MapController mMapController = mMapView.getController();
        // 得到mMapView的控制权,可以用它控制和驱动平移和缩放
        // GeoPoint point = new GeoPoint((int) (39.915 * 1E6), (int) (116.404 *
        // 1E6));
        GeoPoint point = gps[0];
        // 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
        mMapController.setCenter(point);// 设置地图中心点
        mMapController.setZoom(15);// 设置地图zoom级别

        mOverlayTest = new ItemizedOverlay(MainActivity.this.getResources()
                .getDrawable(R.drawable.ic_launcher), mMapView);
        mOverlayTest.removeAll();
        mMapView.getOverlays().add(mOverlayTest);
        autoChangeViewPagerPosition();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        mMapView.destroy();
        if (mBMapMan != null) {
            mBMapMan.destroy();
            mBMapMan = null;
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        if (mBMapMan != null) {
            mBMapMan.stop();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        if (mBMapMan != null) {
            mBMapMan.start();
        }
        super.onResume();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
