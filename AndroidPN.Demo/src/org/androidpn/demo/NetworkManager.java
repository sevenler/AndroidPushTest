package org.androidpn.demo;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Proxy;
import android.text.TextUtils;

public class NetworkManager {
	private static NetworkManager instance = null;

	private NetworkManager() {
	}

	public static NetworkManager getInstance() {
		if (instance == null) {
			synchronized (NetworkManager.class) {
				if (instance == null) {
					instance = new NetworkManager();
				}
			}
		}
		return instance;
	}

	/**
	 * 是否有可用网�?	 * 
	 * @param context
	 * @return
	 */
	public boolean isNetworkAvailable(Context context) {
		return isWifiConnected(context) || isMobileConnected(context);
	}

	/**
	 * WIFI是否连接
	 * 
	 * @param context
	 * @return
	 */
	public boolean isWifiConnected(Context context) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo wifi = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (wifi != null && wifi.isConnected()) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 2G/3G是否连接
	 * 
	 * @param context
	 * @return
	 */
	public boolean isMobileConnected(Context context) {
		try {
			ConnectivityManager connectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobile = connectivityManager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			if (mobile != null && mobile.isConnected()) {
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

	public String httpGET(Context context, String fullURL) {
		URI u;
		try {
			URL sUrl = new URL(fullURL);
			u = sUrl.toURI();
		} catch (MalformedURLException e) {
			return null;
		} catch (URISyntaxException e) {
			return null;
		}
		HttpClient httpClient = createHttpClient(context);
		InputStream is = null;
		try {
			HttpGet httpGet = new HttpGet(u);
			httpGet.setHeader("Accept", "application/json");
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new IOException("statusCode=" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				is = entity.getContent();
				return readStream(is);
			}
		} catch (OutOfMemoryError e) {
			System.gc();
		} catch (ClientProtocolException e) {
		} catch (Exception e) {
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			httpClient.getConnectionManager().shutdown();
			httpClient = null;
		}
		return null;
	}

	public HttpEntity httpGETEntity(Context context, String fullURL) {
		URI u;
		try {
			URL sUrl = new URL(fullURL);
			u = sUrl.toURI();
		} catch (MalformedURLException e) {
			return null;
		} catch (URISyntaxException e) {
			return null;
		}
		HttpClient httpClient = createHttpClient(context);
		try {
			HttpGet httpGet = new HttpGet(u);
			httpGet.setHeader("Accept", "application/json");
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new IOException("statusCode=" + statusCode);
			}
			return response.getEntity();
		} catch (OutOfMemoryError e) {
			System.gc();
		} catch (ClientProtocolException e) {
		} catch (Exception e) {
		} finally {
			// httpClient.getConnectionManager().shutdown();
			// httpClient = null;
		}
		return null;
	}

	public HttpResponse httpGETResponse(Context context, String fullURL) {
		URI u;
		try {
			URL sUrl = new URL(fullURL);
			u = sUrl.toURI();
		} catch (MalformedURLException e) {
			return null;
		} catch (URISyntaxException e) {
			return null;
		}
		HttpClient httpClient = createHttpClient(context);
		try {
			HttpGet httpGet = new HttpGet(u);
			httpGet.setHeader("Accept", "application/json");
			return httpClient.execute(httpGet);
		} catch (OutOfMemoryError e) {
			System.gc();
		} catch (ClientProtocolException e) {
		} catch (Exception e) {
		} finally {
			// httpClient.getConnectionManager().shutdown();
			// httpClient = null;
		}
		return null;
	}

	public byte[] httpGet(Context context, String fullURL) {
		URI u;
		try {
			URL sUrl = new URL(fullURL);
			u = sUrl.toURI();
		} catch (MalformedURLException e) {
			return null;
		} catch (URISyntaxException e) {
			return null;
		}
		HttpClient httpClient = createHttpClient(context);
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		try {
			HttpGet httpGet = new HttpGet(u);
			httpGet.setHeader("Accept", "application/json");
			HttpResponse response = httpClient.execute(httpGet);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new IOException("statusCode=" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			long size = entity.getContentLength();
			is = entity.getContent();
			if (is != null && size > 0) {
				baos = new ByteArrayOutputStream();
				byte[] buf = new byte[8192];
				int ch = -1;
				long count = 0;
				while ((ch = is.read(buf)) != -1) {
					count += ch;
					baos.write(buf, 0, ch);
				}
				baos.flush();
				if (count == size) {
					return baos.toByteArray();
				} else {
					return null;
				}
			}
		} catch (OutOfMemoryError e) {
			System.gc();
		} catch (ClientProtocolException e) {
		} catch (Exception e) {
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			if (baos != null) {
				try {
					baos.close();
				} catch (IOException e) {
				}
			}
			httpClient.getConnectionManager().shutdown();
			httpClient = null;
		}
		return null;
	}

	public String httpPOST(Context context, String fullURL,
			Map<String, String> paramsMap) {
		URI u;
		try {
			URL sUrl = new URL(fullURL);
			u = sUrl.toURI();
		} catch (MalformedURLException e) {
			return null;
		} catch (URISyntaxException e) {
			return null;
		}
		HttpClient httpClient = createHttpClient(context);
		InputStream is = null;
		try {
			HttpPost httpPost = new HttpPost(u);
			httpPost.setHeader("Accept", "application/json");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> map : paramsMap.entrySet()) {
				nameValuePairs.add(new BasicNameValuePair(map.getKey(), map
						.getValue()));
			}
//			UrlEncodedFormEntity formEntity;
//			formEntity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
//			HttpPost post = new HttpPost(C.URL.LOG_REPORT);
//			if (formEntity != null)
//				post.setEntity(formEntity);

			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(httpPost);
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new IOException("statusCode=" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				is = entity.getContent();
				Header contentEncoding = response
						.getFirstHeader("Content-Encoding");
				if (contentEncoding != null
						&& contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					return readStream(is);
				} else {
					return readStream(is);
				}
			}
		} catch (OutOfMemoryError e) {
			System.gc();
		} catch (ClientProtocolException e) {
		} catch (Exception e) {
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			httpClient.getConnectionManager().shutdown();
			httpClient = null;
		}
		return null;
	}

	/**
	 * 上传图片
	 * @param context
	 * @param fullURL
	 * @param paramsMap
	 * @return
	 */
	public String httpPOSTImage(Context context, String fullURL,
			Map<String, String> paramsMap,String imageName) {
		URI u;
		try {
			URL sUrl = new URL(fullURL);
			u = sUrl.toURI();
		} catch (MalformedURLException e) {
			return null;
		} catch (URISyntaxException e) {
			return null;
		}
		HttpClient httpClient = createHttpClient(context);
		InputStream is = null;
		try {
			HttpPost httpPost = new HttpPost(u);
			httpPost.setHeader("Accept", "application/json");
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> map : paramsMap.entrySet()) {
				nameValuePairs.add(new BasicNameValuePair(map.getKey(), map
						.getValue()));
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			HttpResponse response = httpClient.execute(httpPost);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode != HttpStatus.SC_OK) {
				throw new IOException("statusCode=" + statusCode);
			}
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				is = entity.getContent();
				Header contentEncoding = response
						.getFirstHeader("Content-Encoding");
				if (contentEncoding != null
						&& contentEncoding.getValue().equalsIgnoreCase("gzip")) {
					return readStream(is);
				} else {
					return readStream(is);
				}
			}
		} catch (OutOfMemoryError e) {
			System.gc();
		} catch (ClientProtocolException e) {
		} catch (Exception e) {
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
			httpClient.getConnectionManager().shutdown();
			httpClient = null;
		}
		return null;
	}
	private HttpClient createHttpClient(Context context) {
		HttpParams httpParams = new BasicHttpParams();
		HttpConnectionParams.setConnectionTimeout(httpParams,
				10000 * 2);
		HttpConnectionParams.setSoTimeout(httpParams, 15000 * 2);
		HttpConnectionParams
				.setSocketBufferSize(httpParams, 8192);
		HttpClientParams.setRedirecting(httpParams, true);
		httpParams.setParameter("Connection", "closed");
		String proxyHost = Proxy.getHost(context);
		int proxyPort = Proxy.getPort(context);
		boolean isWifiConnected = this.isWifiConnected(context);
		if (!isWifiConnected && !TextUtils.isEmpty(proxyHost)) {
			HttpHost proxy = new HttpHost(proxyHost, proxyPort);
			httpParams.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
		}
		HttpClient httpClient = new DefaultHttpClient(httpParams);
		return httpClient;
	}

	private String readStream(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();
		try {
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			return sb.toString();
		} catch (IOException e) {
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	public String getLocalIpAddress(Context context) {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
		}
		return null;
	}
}
