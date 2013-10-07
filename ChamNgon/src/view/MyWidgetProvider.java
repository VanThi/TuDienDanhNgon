package view;


import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Random;

import model.ChamNgon;
import model.SQLiteHelper;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViews.RemoteView;

import cyber.app.chamngon.R;

public class MyWidgetProvider extends AppWidgetProvider{

	private final int ALARM_TIME = 1000*60*30;

	private static SQLiteHelper sh;
	private int chidRD;
	private static ChamNgon cn= new ChamNgon();
	private final String CHANGED="Changed"; 
	private static boolean check = true;
	private SharedPreferences savedata;
	private SharedPreferences.Editor editor;
	
	
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		startAlarm(context, ALARM_TIME);
		random(context);
		/*Intent mintent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
		intent.setClass(context, getClass());
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, mintent, PendingIntent.FLAG_CANCEL_CURRENT);
*/
		// Get the component name of widget
		ComponentName thisWidget = new ComponentName(context,
				MyWidgetProvider.class);


		// Get all IDs of current widgets in Home screen
		int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);
		for (int appWidgetId : allWidgetIds) {

			// Create an Intent to launch ExampleActivity
			Intent intent = new Intent(context, MyWidgetProvider.class);
			intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
			intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

			PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
					0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

			// Get the layout for the App Widget and attach an on-click listener
			// to the button
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.widget_layout);

			GregorianCalendar ngay = new GregorianCalendar();

			String ddngay;
			SimpleDateFormat sd =new SimpleDateFormat("dd/MM/yyyy");

			ddngay=sd.format(ngay.getTime());

			views.setTextViewText(R.id.ngay, ddngay);

			views.setTextViewText(R.id.noidung, "    "+cn.getnDungAnh());

			views.setTextViewText(R.id.tacgia, cn.getTacGia());
			if(cn.getnDungAnh().length()>150)
				views.setTextViewText(R.id.seemore, "More...");
			else views.setTextViewText(R.id.seemore, "");
			check=true;

			views.setOnClickPendingIntent(R.id.v, getPendingSelfIntent(context, CHANGED));

			views.setOnClickPendingIntent(R.id.imageView1,getPendingSelfIntent(context, "next"));

			views.setOnClickPendingIntent(R.id.noidung,getPendingSelfIntent(context, "noidung"));
			views.setOnClickPendingIntent(R.id.seemore,getPendingSelfIntent(context, "noidung"));
			System.out.println("kết thuc");

			appWidgetManager.updateAppWidget(appWidgetId, views);	
		}


	}

	private void random(Context context) {
		while(true){
			try{
				sh= new SQLiteHelper(context);
				Random rd= new Random();
				chidRD= (rd.nextInt(1283)+1);
				System.out.println("random: " +chidRD);
				cn = sh.getChamNgon(chidRD);
				savedata= PreferenceManager.getDefaultSharedPreferences(context);
				editor= savedata.edit();
				editor.putString("noidungviet", cn.getnDungViet());
				editor.putString("noidunganh",cn.getnDungAnh());
				editor.putBoolean("check", check);
				editor.putInt("chid", chidRD);
				editor.commit();
				break;
			}catch(Exception e){
				System.out.println("loi_____________________________");
				e.printStackTrace();
			}
		}
	}

	/*@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
	}
	 */
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
	}

	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
	}
	
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
		startAlarm(context, 3000);
		AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
		RemoteViews remoteViews;
		ComponentName watchWidget;

		if (CHANGED.equals(intent.getAction())) {
			System.out.println("doi viet anh");
			remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			watchWidget = new ComponentName(context, MyWidgetProvider.class);
			savedata = PreferenceManager.getDefaultSharedPreferences(context);
			editor= savedata.edit();

			check=savedata.getBoolean("check", true);
			System.out.println("check"+ check);

			if(check==true){
				System.out.println("ok viet");
				remoteViews.setTextViewText(R.id.noidung,"    "+savedata.getString("noidungviet", ""));
				remoteViews.setImageViewResource(R.id.v, R.drawable.eicon);
				if(cn.getnDungViet().length()>150)
					remoteViews.setTextViewText(R.id.seemore, "More...");
				else remoteViews.setTextViewText(R.id.seemore, "");
				check=false;
				editor= savedata.edit();
				editor.putBoolean("check", check);
				editor.commit();
			}else{
				remoteViews.setTextViewText(R.id.noidung,"    "+savedata.getString("noidunganh", ""));
				remoteViews.setImageViewResource(R.id.v, R.drawable.vicon);
				if(cn.getnDungAnh().length()>150)
					remoteViews.setTextViewText(R.id.seemore, "More...");
				else remoteViews.setTextViewText(R.id.seemore, "");
				System.out.println("ok anh");
				check=true;
				editor.putBoolean("check", check);
				editor.commit();
			}
			appWidgetManager.updateAppWidget(watchWidget, remoteViews);

		}

		else if(intent.getAction().equals("next")){

			System.out.println("button next");
			remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
			watchWidget = new ComponentName(context, MyWidgetProvider.class);
			random(context);
			remoteViews.setTextViewText(R.id.noidung, "    "+cn.getnDungAnh());

			remoteViews.setTextViewText(R.id.tacgia, cn.getTacGia());

			remoteViews.setTextViewText(R.id.noidung,"    "+savedata.getString("noidunganh", ""));

			remoteViews.setImageViewResource(R.id.v, R.drawable.vicon);

			if(cn.getnDungAnh().length()>150)
				remoteViews.setTextViewText(R.id.seemore, "see more...");
			else remoteViews.setTextViewText(R.id.seemore, "");
			check=true;
			editor.putBoolean("check", check);
			editor.commit();


			appWidgetManager.updateAppWidget(watchWidget, remoteViews);
		}


		else if(intent.getAction().equals("noidung")){
			System.out.println("chuyen noi dung");
			Intent mIntent= new Intent(context.getApplicationContext(),DetailActivity.class);
			savedata = PreferenceManager.getDefaultSharedPreferences(context);
			mIntent.putExtra("chid", savedata.getInt("chid", 0));
			mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(mIntent);
		}
	}

	private void startAlarm(Context pContext, long nTime) {
		AlarmManager am = (AlarmManager) pContext.getSystemService(Context.ALARM_SERVICE);

		Intent intent = new Intent("android.appwidget.action.APPWIDGET_UPDATE");
		intent.setClass(pContext, getClass());
		PendingIntent pi = PendingIntent.getBroadcast(pContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

		am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
				(nTime), pi);
	}

	protected PendingIntent getPendingSelfIntent(Context context, String action) {
		Intent intent = new Intent(context, getClass());
		intent.setAction(action);
		return PendingIntent.getBroadcast(context, 0, intent, 0);
	}  
}
