package ankit.oromap.helpo.extras;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import ankit.oromap.helpo.R;

public class pager_views extends Fragment {
    private int a = 0;
    private View view = null;
    private Handler task_handler;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        switch (a)
        {
            case 1:
                view = inflater.inflate(R.layout.login_frag,null);
                view.findViewById(R.id.loginbut).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextInputEditText usernameet = (TextInputEditText)view.findViewById(R.id.username);
                        TextInputEditText passwordet = (TextInputEditText)view.findViewById(R.id.password);
                        String username = usernameet.getText().toString().trim();
                        String password =passwordet.getText().toString().trim();
                        if(username.isEmpty())
                        {
                            usernameet.setError("Required Field");
                        }
                        else
                        {
                            if(password.isEmpty())
                            {
                                passwordet.setError("Required Field");
                            }
                            else
                            {
                                Message m = new Message();
                                Bundle bun =new Bundle();
                                bun.putString("username",username);
                                bun.putString("password",password);
                                bun.putInt("code",0);
                                m.setData(bun);
                                task_handler.sendMessage(m);
                            }
                        }
                    }
                });
                break;
            case 2:
                view = inflater.inflate(R.layout.signup_frag,null);
                view.findViewById(R.id.loginbut).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        TextInputEditText usernameet = (TextInputEditText)view.findViewById(R.id.username);
                        TextInputEditText passwordet = (TextInputEditText)view.findViewById(R.id.password);
                        TextInputEditText conpasswordet = (TextInputEditText)view.findViewById(R.id.passwordcon);
                        String username = usernameet.getText().toString().trim();
                        String password =passwordet.getText().toString().trim();
                        String conpassword = conpasswordet.getText().toString().trim();
                        if(username.isEmpty())
                        {
                            usernameet.setError("Required Field");
                        }
                        else
                        {
                            if(password.isEmpty())
                            {
                                passwordet.setError("Required Field");
                            }
                            else
                            {
                                if(password.length() < 6)
                                {
                                    passwordet.setError("Password must be 6 characters");
                                }
                                else
                                {
                                    if(conpassword.isEmpty())
                                    {
                                        conpasswordet.setError("Required Field");
                                    }
                                    else
                                    {
                                        if(!conpassword.equals(password))
                                        {
                                            conpasswordet.setError("Passwords do not match");
                                        }
                                        else
                                        {
                                            Message m = new Message();
                                            Bundle bun =new Bundle();
                                            bun.putString("username",username);
                                            bun.putString("password",password);
                                            bun.putInt("code",1);
                                            m.setData(bun);
                                            task_handler.sendMessage(m);
                                        }
                                    }
                                }
                            }
                        }
                    }
                });
                break;
            default:
                Log.e("Error","Formation Error");
        }
        return view;
    }
    public pager_views(int num, Handler task_handler)
    {
        this.task_handler = task_handler;
        this.a = num;
    }
}
