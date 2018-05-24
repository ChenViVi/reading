package com.vivi.reading.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vivi.reading.R;
import com.vivi.reading.adapter.DiscussAdapter;
import com.vivi.reading.bean.Discuss;
import com.vivi.reading.util.ConstUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vivi on 2016/9/3.
 */
public class DiscussFragment extends Fragment{

    private RequestQueue mQueue;
    protected ArrayList<Discuss> data = new ArrayList<>();
    private DiscussAdapter adapter;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_discuss, container, false);
        mQueue = Volley.newRequestQueue(getActivity());
        listView =  rootView.findViewById(R.id.list_view);
        adapter = new DiscussAdapter(getActivity(),data);
        listView.setAdapter(adapter);
        mQueue.add(getDiscussRequest(getArguments().getInt("id")));
        return rootView;
    }

    private StringRequest getDiscussRequest(final int id) {
        StringRequest request = new StringRequest(Request.Method.POST, ConstUtils.BASEURL + "getdiscussbytype.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int result = 0;
                        String list = "";
                        try {
                            JSONObject json= new JSONObject(response);
                            result = json.getInt("result");
                            list = json.getJSONArray("list").toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (result != 0){
                            Toast.makeText(getActivity(), "网络连接失败", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Gson gson = new Gson();
                            List<Discuss> items = gson.fromJson(list, new TypeToken<List<Discuss>>() {}.getType());
                            data.clear();
                            if (items != null){
                                data.addAll(items);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "网络连接超时", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<>();
                map.put("typeId",String.valueOf(id));
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(10000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        return request;
    }
}
