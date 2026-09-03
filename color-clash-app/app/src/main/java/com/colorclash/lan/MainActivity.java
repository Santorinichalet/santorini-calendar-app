package com.colorclash.lan;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.util.Collections;

public class MainActivity extends Activity {
    private static GameServer server;
    private static final int PORT = 8765;
    private String publicUrl;
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView status=findViewById(R.id.statusText), link=findViewById(R.id.linkText);
        ImageView qr=findViewById(R.id.qrImage);
        Button open=findViewById(R.id.openButton), share=findViewById(R.id.shareButton);
        try {
            if(server==null){ server=new GameServer(PORT,getAssets()); server.start(); }
            String ip=localIpv4();
            if(ip==null){ publicUrl="http://127.0.0.1:"+PORT; status.setText("السيرفر يعمل. اتصل بالـ Wi‑Fi لإظهار رابط الأجهزة الأخرى."); }
            else { publicUrl="http://"+ip+":"+PORT; status.setText("اللعبة جاهزة. افتح هذا الرابط من الأجهزة على نفس الـ Wi‑Fi:"); }
            link.setText(publicUrl); qr.setImageBitmap(makeQr(publicUrl,700));
        } catch(Exception e){ status.setText("تعذر تشغيل السيرفر: "+e.getMessage()); open.setEnabled(false); share.setEnabled(false); }
        open.setOnClickListener(v->{ Intent i=new Intent(this,GameActivity.class); i.putExtra("url","http://127.0.0.1:"+PORT); startActivity(i); });
        share.setOnClickListener(v->{ Intent send=new Intent(Intent.ACTION_SEND); send.setType("text/plain"); send.putExtra(Intent.EXTRA_TEXT,"ادخل لعبة Color Clash من نفس شبكة Wi‑Fi:\n"+publicUrl); startActivity(Intent.createChooser(send,"مشاركة رابط اللعبة")); });
    }
    private String localIpv4(){ try { for(NetworkInterface ni:Collections.list(NetworkInterface.getNetworkInterfaces())){ if(!ni.isUp()||ni.isLoopback()) continue; for(java.net.InetAddress a:Collections.list(ni.getInetAddresses())) if(a instanceof Inet4Address && a.isSiteLocalAddress()) return a.getHostAddress(); } } catch(Exception ignored){} return null; }
    private Bitmap makeQr(String text,int size)throws Exception{ BitMatrix m=new MultiFormatWriter().encode(text,BarcodeFormat.QR_CODE,size,size); Bitmap b=Bitmap.createBitmap(size,size,Bitmap.Config.RGB_565); for(int y=0;y<size;y++) for(int x=0;x<size;x++) b.setPixel(x,y,m.get(x,y)?Color.BLACK:Color.WHITE); return b; }
}
