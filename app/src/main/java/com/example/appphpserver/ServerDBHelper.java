package com.example.appphpserver;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.contentcapture.ContentCaptureCondition;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ServerDBHelper implements DBHelper{
    RequestQueue requestQueue;
    Context context;
    public Map<String, String> server_urls = new HashMap<String, String>();
    String server_addr = "http://10.0.2.2/androidcrud";
    ServerDBHelper(Context ctx, String addr){
        context = ctx;
        requestQueue = Volley.newRequestQueue(context);
        server_addr = addr;
        server_urls.put("insert", server_addr + "/insert.php");
        server_urls.put("show", server_addr + "/show.php");
        server_urls.put("update", server_addr + "/update.php");
        server_urls.put("delete", server_addr + "/delete.php");
        server_urls.put("list", server_addr + "/viewList.php");

    }

    @Override
    public boolean Add(String name, String address) {
        String server_insert_url = server_urls.get("insert");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_insert_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Toast.makeText(context, jsonObject.toString(), Toast.LENGTH_LONG).show();
                            MainActivity.txt_id.setText("");
                            MainActivity.txt_name.setText("");
                            MainActivity.txt_address.setText("");
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "e" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "err" + error.toString(), Toast.LENGTH_LONG).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", name);
                params.put("address", address);
                return params;
            }
        };

        requestQueue.add(stringRequest);
        return true;
    }

    @Override
    public boolean Show(String id) {
        String server_show_url = server_urls.get("show") + "?id=" + MainActivity.txt_id.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, server_show_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = "";
                            if (jsonObject.getInt("success") == 0) {
                                message = "The Request Resource Does not Exist !";
                                MainActivity.txt_id.setText("");
                                MainActivity.txt_name.setText("");
                                MainActivity.txt_address.setText("");
                            } else {
                                message = "Operation Success";
                                MainActivity.txt_name.setText(jsonObject.getJSONObject("order").getString("name"));
                                MainActivity.txt_address.setText(jsonObject.getJSONObject("order").getString("address"));
                            }
                            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "e" + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "err" + error.toString(), Toast.LENGTH_LONG).show();

                    }
                }
        );
        requestQueue.add(stringRequest);
        return true;
    }

    @Override
    public boolean Update(String id, String new_name, String new_address) {
        String server_update_url = server_urls.get("update");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, server_update_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = "";
                            if (jsonObject.getInt("success") == 1) {
                                message = "Update Success !";
                                MainActivity.txt_id.setText("");
                                MainActivity.txt_name.setText("");
                                MainActivity.txt_address.setText("");
                            } else
                                message = "Error Updating Resource ! ";
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "e" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "err" + error.toString(), Toast.LENGTH_LONG).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("id", id);
                params.put("name", new_name);
                params.put("address", new_address);
                return params;
            }
        };

        requestQueue.add(stringRequest);
        return true;
    }

    @Override
    public boolean Delete(String id) {
        String server_delete_url = server_urls.get("delete") + "?id=" + MainActivity.txt_id.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, server_delete_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String message = "";
                            if (jsonObject.getInt("success") == 1) {
                                message = "Operation Success";
                                MainActivity.txt_id.setText("");
                                MainActivity.txt_name.setText("");
                                MainActivity.txt_address.setText("");
                            } else
                                message = "Error Deleting Resource";
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "e" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "err" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }
        );

        requestQueue.add(stringRequest);
        return true;
    }

    @Override
    public boolean List() {
        String server_list_url = server_urls.get("list");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, server_list_url,
                new Response.Listener<String>() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onResponse(String response) {
                        try {
                            MainActivity.txt_data.setText("");
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("success") == 1) {
                                StringBuilder orders = new StringBuilder();
                                orders.append("ID | NAME | ADDRESS \n");
                                orders.append("----------------------------------------------- \n");
                                JSONArray liste = jsonObject.getJSONArray("orders");
                                for (int i = 0; i < liste.length(); i++) {
                                    int id = liste.getJSONObject(i).getInt("id");
                                    String nom = liste.getJSONObject(i).getString("name");
                                    String addr = liste.getJSONObject(i).getString("address");
                                    orders.append(String.format("%d | %s | %s \n", id, nom, addr));
                                    orders.append("-------------------------------------------- \n");
                                }
                                MainActivity.txt_data.setText(orders);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "e" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "err" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }
        );
        requestQueue.add(stringRequest);
        return true;
    }

    public void Merge(LocalDBHelper localDBHelper){
        String server_list_url = server_urls.get("list");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, server_list_url,
                new Response.Listener<String>() {
                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getInt("success") == 1) {
                                JSONArray liste = jsonObject.getJSONArray("orders");
                                for (int i = 0; i < liste.length(); i++) {
                                    int id = liste.getJSONObject(i).getInt("id");
                                    String nom = liste.getJSONObject(i).getString("name");
                                    String addr = liste.getJSONObject(i).getString("address");
                                    localDBHelper.Add(nom, addr); // call your Add function to insert the data into the local database
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "e" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(context, "err" + error.toString(), Toast.LENGTH_SHORT).show();

                    }
                }
        );
        requestQueue.add(stringRequest);
    }


    public void close(){

    }
}
