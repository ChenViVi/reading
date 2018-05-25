package com.vivi.reading.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.vivi.reading.R;
import com.vivi.reading.adapter.NavigationDrawerAdapter;
import com.vivi.reading.bean.NavDrawerItem;
import com.vivi.reading.ui.activity.LoginActivity;
import com.vivi.reading.ui.activity.SignActivity;
import com.vivi.reading.ui.activity.UserActivity;
import com.vivi.reading.util.BitmapCache;
import com.vivi.reading.util.ConstUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ViVi on 2016/1/12.
 */
public class DrawerFragment extends Fragment implements OnClickListener{

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles = null;
    private FragmentDrawerListener drawerListener;

    private RequestQueue mQueue;
    private SharedPreferences pref;

    private NetworkImageView nivUserImg;
    private TextView userNickName;
    private LinearLayout userHeadLayout;
    private Button btnLogin;
    private Button btnSign;
    private LinearLayout layoutLog;
    private Button btnEdit;


    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItem> getData() {
        List<NavDrawerItem> data = new ArrayList<>();
        // preparing navigation drawer items
        int[] imgId = {R.drawable.ic_drawer_collect,R.drawable.ic_drawer_catalog, R.drawable.ic_drawer_discuss, R.drawable.ic_drawer_submit,R.drawable.ic_drawer_setting};
        for (int i = 0; i < titles.length; i++) {
            NavDrawerItem navItem = new NavDrawerItem(titles[i],imgId[i]);
            data.add(navItem);
        }
        return data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.fragment_drawer, container, false);
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
        mQueue = Volley.newRequestQueue(getActivity());
        pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);
        layoutLog = (LinearLayout) layout.findViewById(R.id.layout_log);
        btnSign = (Button) layout.findViewById(R.id.btn_sign);
        btnLogin = (Button) layout.findViewById(R.id.btn_login);
        btnEdit = (Button) layout.findViewById(R.id.btn_edit);
        btnLogin.setOnClickListener(this);
        btnSign.setOnClickListener(this);
        btnEdit.setOnClickListener(this);
        layout.setOnClickListener(this);
        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        nivUserImg = (NetworkImageView) layout.findViewById(R.id.niv_user_img);
        userNickName = (TextView) layout.findViewById(R.id.tv_user_name);
        nivUserImg.setDefaultImageResId(R.drawable.ic_account);
        nivUserImg.setErrorImageResId(R.drawable.ic_account);
        /*userHeadLayout = (LinearLayout) layout.findViewById(R.id.layout_header);
        userHeadLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.closeDrawer(containerView);
                Intent intent;
                if (pref.getBoolean("isLogin",false)) {
                    intent = new Intent(getActivity(),UserActivity.class);
                }
                else{
                    intent = new Intent(getActivity(), LoginActivity.class);
                }
                startActivity(intent);
            }
        });*/

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (pref.getBoolean("isLogin",false)){
            nivUserImg.setImageUrl(ConstUtils.BASEURL +pref.getString("imgUrl",""),new ImageLoader(mQueue,new BitmapCache()));
            nivUserImg.setDefaultImageResId(R.drawable.ic_account);
            nivUserImg.setErrorImageResId(R.drawable.ic_account);
            userNickName.setText(pref.getString("name","读者 "));
            userNickName.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
            btnLogin.setVisibility(View.GONE);
            btnSign.setVisibility(View.GONE);
        }
        else{
            userNickName.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);
            btnLogin.setVisibility(View.VISIBLE);
            btnSign.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:startActivity(new Intent(getContext(),LoginActivity.class));break;
            case R.id.btn_sign:startActivity(new Intent(getContext(),SignActivity.class));break;
            case R.id.btn_edit:startActivity(new Intent(getContext(),UserActivity.class));break;
        }
        mDrawerLayout.closeDrawer(containerView);
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }


    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }
}
