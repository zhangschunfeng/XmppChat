package zcf.demo.utils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;

import com.google.gson.Gson;
import zcf.demo.bean.ChatMessage;
import zcf.demo.bean.ChatMessage.Type;
import zcf.demo.bean.Result;
public class HttpUtils {
	private static final String URL="http://www.tuling123.com/openapi/api";
	private static final String API_KEY="331c3b732407b75c17ee7c363aa49ae5";
	public static ChatMessage getChatMessage(String msg)
	{
		ChatMessage message=new ChatMessage();
		String jsonRes=doGet(msg);
		Gson gson=new Gson();
		Result result=new Result();
		//服务器端将数据转换成json字符串
		//客户端将json字符串转换为相应的javaBean
		//使用泛型获取javaBean（核心函数）
		try
		{
		result=gson.fromJson(jsonRes, Result.class);
		message.setMessage(result.getText());
		}
		catch(Exception e)
		{
			message.setMessage("服务器错误,请稍等。。。");
		}
		message.setDate(new Date());
		message.setType(Type.INCOME);
		message.setName("COCO");
		return message;
		
	}
	public static String doGet(String msg) {
		String result = null;
		String urlStr = setParam(msg);
		ByteArrayOutputStream baos = null;
		InputStream in = null;
		try {

			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5 * 1000);
			conn.setReadTimeout(5 * 1000);
			in = conn.getInputStream();
			int len = -1;
			byte[] buf = new byte[128];
			baos = new ByteArrayOutputStream();
			while ((len = in.read(buf)) != -1) {
				baos.write(buf, 0, len);
			}
			result = new String(baos.toByteArray());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return result;
	}

	private static String setParam(String msg) {
		
		String s=null;
		try {
			s = URL+"?key="+API_KEY+"&info="+URLEncoder.encode(msg,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
}
