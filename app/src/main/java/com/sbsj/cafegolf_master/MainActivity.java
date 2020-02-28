package com.sbsj.cafegolf_master;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.sbsj.cafegolf_master.Adapter.BookingAdapter;
import com.sbsj.cafegolf_master.Adapter.LessonAdapter;
import com.sbsj.cafegolf_master.Adapter.MemberAdapter;
import com.sbsj.cafegolf_master.network.APIClient;
import com.sbsj.cafegolf_master.network.BookingInfo;
import com.sbsj.cafegolf_master.network.BookingInfoColumn;
import com.sbsj.cafegolf_master.network.DB_Service;
import com.sbsj.cafegolf_master.network.LessonInfo;
import com.sbsj.cafegolf_master.network.LessonInfoColumn;
import com.sbsj.cafegolf_master.network.MasterInfo;
import com.sbsj.cafegolf_master.network.MasterInfoColumn;
import com.sbsj.cafegolf_master.network.MemberInfo;
import com.sbsj.cafegolf_master.network.MemberInfoColumn;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    final String TAG = "Main_Activity";
    private DB_Service dbService;

    private RecyclerView recyclerView;
    private BottomNavigationView bottomNavigationView;
    private LinearLayout ll_btn;
    private Button btn_add;
    private Button btn_save;
    private TextView tv_notext;

    private BookingAdapter bookingAdapter;
    private MemberAdapter memberAdapter;
    private LessonAdapter lessonAdapter;

    private ArrayList<MemberInfoColumn> memberlist = new ArrayList<>();
    private ArrayList<BookingInfoColumn> bookinglist = new ArrayList<>();
    private ArrayList<LessonInfoColumn> lessonlist = new ArrayList<>();
    private ArrayList<MasterInfoColumn> masterlist = new ArrayList<>();

    private SimpleDateFormat format;
    private Date today;

    private int lesson_count;

    private final String FCM_MESSAGE_URL = "https://fcm.googleapis.com/fcm/send";
    private final String SERVER_KEY = "AAAA2Cx-nZU:APA91bGu9togdsBCtnW-L2TI0UWzm7jKjVWbEWmn5egCpUAD94NZoBpNRVCqVGShznaKg96IL3xEzKP79n2oTjJr3xocZvDe-tuVyQd5UwvFgtrOkNKsxBzMRaIr3Fw29TwRpVTCklfw";
//    private String token = "d3PkF0-86bY:APA91bEDoGUzbct6kqmf65i7NJDWVM-4g8OttdPa5j0XIug4awnFQSHsz-Cy-3xDGsWvqvmJUZh8UE-ZpJ7RvrYhE2CEX-jDJFrJEzX_e7q5MOcH_-Y7TZRHzwxldqlB52zCyTZwW12L";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbService = APIClient.getClient().create(DB_Service.class);
        format = new SimpleDateFormat(getString(R.string.dateformat));

        getFirebaseInstanceID();

        bookingAdapter = new BookingAdapter(this, bookinglist, new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                BookingInfoColumn column = bookingAdapter.getItem(position);
                    if (column.getIsapproved() > 0) {
                    updateBookingInfo(column.getId(), 0);
                } else {
                    updateBookingInfo(column.getId(), 1);
                    Log.i(TAG, "token : " + column.getFb_token());
                    new Thread(new MyThread(column.getFb_token())).start();
                }
            }

            @Override
            public void onLongClick(View v, int position) {
                final BookingInfoColumn bookinginfo = bookingAdapter.getItem(position);
                final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("예약 관리")
                        .setMessage("예약 삭제를 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteBookinginfo(bookinginfo.getId());
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create().show();
            }
        });

        memberAdapter = new MemberAdapter(this, memberlist, new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                final MemberInfoColumn memberinfo = memberAdapter.getItem(position);
                final EditText et = new EditText(getApplicationContext());
                et.setInputType(InputType.TYPE_CLASS_PHONE);
                final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("레슨 관리")
                        .setMessage("추가 할 레슨 수를 입력하세요.")
                        .setView(et)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int count = memberinfo.getLesson_count() + Integer.parseInt(et.getText().toString());
                                updateMemberInfo(memberinfo.getId(), count);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create().show();
            }

            @Override
            public void onLongClick(View v, int position) {
                final MemberInfoColumn memberinfo = memberAdapter.getItem(position);
                final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("회원 관리")
                        .setMessage("회원 삭제를 하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteMemberinfo(memberinfo.getId());
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create().show();
            }
        });

        lessonAdapter = new LessonAdapter(this, lessonlist, new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                if (v.getId() == R.id.btn_lesson_edit) {
                    final int list_position = position;
                    final int time_hour=lessonAdapter.getItem(position).getHour_before();
                    final int  time_minute = lessonAdapter.getItem(position).getMinute_before();

                    TimePickerDialog dialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int hour, int min) {
                            lessonAdapter.getItem(list_position).setHour_before(hour);
                            lessonAdapter.getItem(list_position).setMinute_before(min);
                            lessonAdapter.notifyDataSetChanged();
                        }
                    }, time_hour, time_minute, true);  //마지막 boolean 값은 시간을 24시간으로 보일지 아닐지
                    dialog.show();
                }
            }

            @Override
            public void onLongClick(View v, int position) {
                final int list_position = position;
                final LessonInfoColumn memberinfo = lessonAdapter.getItem(position);
                final android.app.AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("수업 관리")
                        .setMessage("수업 시간을 삭제하시겠습니까?")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                lessonAdapter.notifyItemRemoved(list_position);
                                lessonAdapter.getDatalist().remove(list_position);
                            }
                        })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create().show();
            }
        });

        recyclerView = findViewById(R.id.rcv_main);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(memberAdapter);

        today = new Date();

        getMemberInfo();

        bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        ll_btn = findViewById(R.id.ll_btn);

        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_notext.setVisibility(View.GONE);
                addCategory();
            }
        });

        btn_save = findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ArrayList<Integer> id_list = new ArrayList<>();
                final ArrayList<Integer> hour_before_list = new ArrayList<>();
                final ArrayList<Integer> minute_before_list = new ArrayList<>();

                for (int i = 0; i < lessonAdapter.getItemCount(); ++i) {
                    id_list.add(lessonAdapter.getItem(i).getId());
                    hour_before_list.add(lessonAdapter.getItem(i).getHour_before());
                    minute_before_list.add(lessonAdapter.getItem(i).getMinute_before());
                }

                if (lesson_count == lessonAdapter.getItemCount()) {
                    Call<String> call = dbService.updatelessoninfo(id_list, hour_before_list, minute_before_list); // get
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.message().equals("OK")) {
                                getLessonInfo();
                                Toast.makeText(MainActivity.this, "저장 완료", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.i(TAG, "updateLessonInfo Error : " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.i(TAG, "updateLessonInfo fail : " + t.getMessage());
                        }
                    });
                } else {
                    Call<String> call = dbService.deletelessoninfo(0);//insert -> get
                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if (response.message().equals("OK")) {
                                insertBookinginfo(hour_before_list, minute_before_list);
                            } else {
                                Log.i(TAG, "deleteLessonInfo Error : " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            Log.i(TAG, "deleteLessonInfo fail : " + t.getMessage());
                        }
                    });
                }
            }
        });

        tv_notext = findViewById(R.id.tv_nolist);
    }

    private void getMemberInfo() {
        Call<MemberInfo> call = dbService.getmemberinfo();
        call.enqueue(new Callback<MemberInfo>() {
            @Override
            public void onResponse(Call<MemberInfo> call, Response<MemberInfo> response) {
                if (response.message().equals("OK")) {
                    memberlist.clear();
                    memberlist = response.body().getMemberlist();
                    if(memberlist.get(0).getId() > 0) {
                        tv_notext.setVisibility(View.GONE);
                        memberAdapter.setDatalist(memberlist);
                    } else {
                        tv_notext.setVisibility(View.VISIBLE);
                    }
                    memberAdapter.notifyDataSetChanged();
                } else {
                    Log.i(TAG, "getMemberInfo Error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MemberInfo> call, Throwable t) {
                Log.i(TAG, "getMemberInfo fail : " + t.getMessage());
            }
        });
    }

    private void updateMemberInfo(int id, int count) {
        Call<String> call = dbService.updatememberinfo(id, count);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    getMemberInfo();
                } else {
                    Log.i(TAG, "updateMemberInfo Error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "updateMemberInfo fail : " + t.getMessage());
            }
        });
    }

    private void getBookingInfo(Date date) {
        Call<BookingInfo> call = dbService.getBookingInfo(format.format(date));
        call.enqueue(new Callback<BookingInfo>() {
            @Override
            public void onResponse(Call<BookingInfo> call, Response<BookingInfo> response) {
                if (response.message().equals("OK")) {
                    bookinglist = response.body().getBookinglist();
                    if(bookinglist.get(0).getId() > 0) {
                        bookingAdapter.setDatalist(bookinglist);
                        tv_notext.setVisibility(View.GONE);
                    } else {
                        tv_notext.setVisibility(View.VISIBLE);
                    }
                    bookingAdapter.notifyDataSetChanged();
                } else {
                    Log.i(TAG, "getBookingInfo_Error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<BookingInfo> call, Throwable t) {
                Log.i(TAG, "getBookingInfo_fail : " + t.getMessage());
            }
        });
    }

    private void getLessonInfo() {
        Call<LessonInfo> call = dbService.getLessonInfo();
        call.enqueue(new Callback<LessonInfo>() {
            @Override
            public void onResponse(Call<LessonInfo> call, Response<LessonInfo> response) {
                if (response.message().equals("OK")) {
                    lessonlist = response.body().getLessonlist();
                    if(lessonlist.get(0).getId() > 0) {
                        lessonAdapter.setDatalist(lessonlist);
                        tv_notext.setVisibility(View.GONE);
                    } else {
                        tv_notext.setVisibility(View.VISIBLE);
                    }
                    lessonAdapter.notifyDataSetChanged();

                    lesson_count = lessonAdapter.getDatalist().size();
                } else {
                    Log.i(TAG, "getLessonInfo Error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<LessonInfo> call, Throwable t) {
                Log.i(TAG, "getLessonInfo fail : " + t.getMessage());
            }
        });
    }

    private void insertBookinginfo(ArrayList<Integer> list1, ArrayList<Integer> list2) {
        Call call = dbService.insertlessoninfo(list1, list2);
        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                if (response.message().equals("OK")) {
                    getLessonInfo();
                    Toast.makeText(MainActivity.this, "저장 완료", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "insert LessonInfo Error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Log.i(TAG, "insert LessonInfo fail : " + t.getMessage());
            }
        });
    }

    private void updateBookingInfo(int booking_id, int isapproved) {
        Call<String> call = dbService.updatebookinginfo(booking_id, isapproved);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    Log.i(TAG, "updateBookingInfo body : " + response.body());
                    getBookingInfo(today);
                } else {
                    Log.i(TAG, "updateBookingInfo Error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "updateBookingInfo fail : " + t.getMessage());
            }
        });
    }

    private void sendFCM(String master_token) {
        try {
            JSONObject jsonObject = new JSONObject();
            JSONObject notification = new JSONObject();
            notification.put("body", "예약 접수 확인");
            notification.put("title", "예약한 수업이 접수되었습니다.");
            jsonObject.put("notification", notification);
            jsonObject.put("to", master_token);

            URL url = new URL(FCM_MESSAGE_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.addRequestProperty("Authorization", "key=" + SERVER_KEY);
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(jsonObject.toString().getBytes("utf-8"));
            os.flush();
            conn.getResponseCode();
            Log.i(TAG, "Pass");
        } catch (MalformedURLException e) {
            e.printStackTrace();
            Log.i(TAG, "SendFCM MalformedURLException Error : " + e.getMessage());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i(TAG, "SendFCM MalformedURLException Error : " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "SendFCM MalformedURLException Error : " + e.getMessage());
        }
    }

    class MyThread implements Runnable {
        private String master_token;

        public MyThread(String master_token) {
            this.master_token = master_token;
        }

        @Override
        public void run() {
            sendFCM(master_token);
        }
    }

    private void deleteMemberinfo(int id) {
        Call<String> call = dbService.deletememberinfo(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    Log.i(TAG, "deleteMemberinfo body : " + response.body());
                    getMemberInfo();
                } else {
                    Log.i(TAG, "deleteMemberinfo Error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "deleteMemberinfo fail : " + t.getMessage());
            }
        });
    }

    private void deleteBookinginfo(int id) {
        Call<String> call = dbService.deletebookinginfo(id);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.message().equals("OK")) {
                    getBookingInfo(today);
                    Log.i(TAG, "deleteBookinginfo body : " + response.body());
                } else {
                    Log.i(TAG, "deleteBookinginfo Error : " + response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "deleteBookinginfo fail : " + t.getMessage());
            }
        });
    }

    private void get_masterinfo(String fb_token) {
        final String f = fb_token;
        Call<MasterInfo> call = dbService.get_masterinfo();
        call.enqueue(new Callback<MasterInfo>() {
            @Override
            public void onResponse(Call<MasterInfo> call, Response<MasterInfo> response) {
                if(response.message().equals("OK")) {
                    masterlist = response.body().getMasterlist();

                    boolean  hasToken = false;
                    for(int i=0;i<masterlist.size();++i) {
                        if(masterlist.get(i).getFb_token().equals(f)) {
                            hasToken = true;
                            break;
                        }
                    }
                    if(!hasToken) {
                        insert_masterinfo(f);
                    }
                } else {
                    Log.i(TAG, "getMasterinfo error : "+ response.message());
                }
            }

            @Override
            public void onFailure(Call<MasterInfo> call, Throwable t) {
                Log.i(TAG, "getmasterinfo fail : " + t.getMessage());
            }
        });
    }

    private void insert_masterinfo(String fb_token) {
        Call<String> call = dbService.insert_masterinfo(fb_token);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if(response.message().equals("OK")) {
                    Log.i(TAG, "insert_masterinfo success : " + response.body());
                } else {
                    Log.i(TAG, "insert_masterinfo error : "+ response.message());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.i(TAG, "insert_masterinfo fail : " + t.getMessage());
            }
        });
    }
    public void getFirebaseInstanceID() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("BaseActivity", "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        Log.i("BaseActivity", "token : " + task.getResult().getToken());
//                        BaseActivity.b_fb_token = task.getResult().getToken();

                        get_masterinfo(task.getResult().getToken());
                    }
                });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            for (int i = 0; i < bottomNavigationView.getMenu().size(); i++) {
                MenuItem menuItem = bottomNavigationView.getMenu().getItem(i);
                boolean isChecked = menuItem.getItemId() == item.getItemId();
                menuItem.setChecked(isChecked);
            }

            switch (item.getItemId()) {
                case R.id.navigation_booking:
                    getBookingInfo(today);
                    recyclerView.setAdapter(bookingAdapter);
                    ll_btn.setVisibility(View.GONE);
                    break;
                case R.id.navigation_lesson:
                    getLessonInfo();
                    recyclerView.setAdapter(lessonAdapter);
                    ll_btn.setVisibility(View.VISIBLE);
                    break;
                case R.id.navigation_member:
                    getMemberInfo();
                    recyclerView.setAdapter(memberAdapter);
                    ll_btn.setVisibility(View.GONE);
                    break;
            }

            return true;
        }
    };

    private void addCategory() {
        LessonInfoColumn cateInfoColumn = new LessonInfoColumn(0,0);
        lessonAdapter.getDatalist().add(cateInfoColumn);
        lessonAdapter.notifyDataSetChanged();
    }
}
