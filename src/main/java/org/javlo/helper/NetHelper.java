package org.javlo.helper;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.zip.CRC32;

import javax.imageio.ImageIO;
import javax.mail.internet.InternetAddress;
import javax.servlet.http.HttpServletResponse;

import org.javlo.config.StaticConfig;
import org.javlo.context.ContentContext;
import org.javlo.context.GlobalContext;
import org.javlo.image.ImageHelper;
import org.javlo.image.ImageSize;
import org.javlo.mailing.MailService;
import org.javlo.service.resource.Resource;
import org.javlo.utils.MapCollectionWrapper;
import org.javlo.ztatic.FileCache;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class NetHelper {

	/**
	 * create a static logger.
	 */
	protected static java.util.logging.Logger logger = java.util.logging.Logger.getLogger(NetHelper.class.getName());

	private static final String IMAGE_PAGE_FILE_KEY = "_images_from_page_analyse";

	private static final int MIN_IMAGE_SIZE = 640 * 480;

	public static final String HEADER_LAST_MODIFIED = "Last-Modified";
	public static final String HEADER_IF_MODIFIED_SINCE = "If-Modified-Since";

	public static String readPage(URL url) throws Exception {
		return readPage(url, false);
	}

	public static String readPage(String inURL, boolean cssInline) throws Exception {
		return readPage(new URL(inURL), cssInline);
	}

	/**
	 * read a page a put content in a String.
	 * 
	 * @param url
	 *            a valid URL
	 * @return code returned by the http request on the URL.
	 * @throws IOException
	 */
	public static String readPage(URL url, boolean cssInline) throws Exception {

		if (StringHelper.isVideo(url.getPath())) {
			return "";
		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		InputStream in = null;
		try {
			URLConnection conn = url.openConnection();

			if (conn instanceof HttpURLConnection) {
				HttpURLConnection httpConn = (HttpURLConnection) conn;
				httpConn.setInstanceFollowRedirects(true);
				if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK && httpConn.getResponseCode() != HttpURLConnection.HTTP_MOVED_TEMP && httpConn.getResponseCode() != HttpURLConnection.HTTP_MOVED_PERM) {
					logger.warning("help url '" + url + "' return error code : " + ((HttpURLConnection) conn).getResponseCode());
					return null;
				}

				if (url.getProtocol().equalsIgnoreCase("http") || url.getProtocol().equalsIgnoreCase("https")) {
					if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK && httpConn.getResponseCode() != HttpURLConnection.HTTP_MOVED_TEMP && httpConn.getResponseCode() != HttpURLConnection.HTTP_MOVED_PERM) {
						return null;
					}
				}
			}
			in = conn.getInputStream();
			ResourceHelper.writeStreamToStream(in, out);
		} finally {
			ResourceHelper.closeResource(in);
		}
		if (cssInline) {
			return CSSParser.mergeCSS(new String(out.toByteArray(), ContentContext.CHARACTER_ENCODING));
		} else {
			return new String(out.toByteArray(), ContentContext.CHARACTER_ENCODING);
		}
	}

	/**
	 * read a page a put content in a String.
	 * 
	 * @param url
	 *            a valid URL
	 * @return code returned by the http request on the URL.
	 * @throws IOException
	 */
	public static JsonElement readJson(URL url) throws Exception {

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		InputStream in = null;
		try {
			URLConnection conn = url.openConnection();

			if (conn instanceof HttpURLConnection) {
				if (((HttpURLConnection) conn).getResponseCode() != HttpURLConnection.HTTP_OK) {
					logger.warning("help url '" + url + "' return error code : " + ((HttpURLConnection) conn).getResponseCode());
					return null;
				}
			}
			if (url.getProtocol().equalsIgnoreCase("http") || url.getProtocol().equalsIgnoreCase("https")) {
				HttpURLConnection httpConn = (HttpURLConnection) conn;
				if (httpConn.getResponseCode() != HttpURLConnection.HTTP_OK) {
					return null;
				}
			}
			in = conn.getInputStream();
			ResourceHelper.writeStreamToStream(in, out);
		} finally {
			ResourceHelper.closeResource(in);
		}

		JsonParser parser = new JsonParser();
		return parser.parse(new String(out.toByteArray()));
	}

	public static Long readDate(URL url) throws Exception {
		URLConnection conn = url.openConnection();
		conn.connect();
		return conn.getDate();
	}

	/**
	 * read a page a put content in a Stream.
	 * 
	 * @param out
	 *            the output stream, it receive the url inputstream
	 * @return code returned by the http request on the URL.
	 * @throws IOException
	 */
	public static void readPage(URL url, OutputStream out) throws Exception {
		InputStream in = null;
		try {
			in = url.openConnection().getInputStream();
			ResourceHelper.writeStreamToStream(in, out);
		} finally {
			ResourceHelper.closeResource(in);
		}
	}

	/**
	 * extract the title of a web page.
	 * 
	 * @param url
	 * @return the title of the page.
	 * @throws Exception
	 */
	public static String getPageTitle(URL url) throws Exception {
		return getPageTitle(readPage(url));
	}

	/**
	 * extract the title of a web page.
	 * 
	 * @param URL
	 * @return the title of the page.
	 */
	public static String getPageTitle(String content) {

		String contentLowerCase = content.toLowerCase();
		int indexTitleStart = contentLowerCase.indexOf("<title>");
		int indexTitleEnd = contentLowerCase.indexOf("</title>");

		if ((indexTitleStart >= 0) && (indexTitleEnd >= 0) && indexTitleEnd > indexTitleStart) {
			return content.substring(indexTitleStart + "<title>".length(), indexTitleEnd);
		}

		return null;
	}

	/**
	 * extract the title of a web page.
	 * 
	 * @param URL
	 * @return the title of the page.
	 */
	public static String getPageDescription(String content) {

		String contentLowerCase = content.toLowerCase();
		int indexDescriptionStart = contentLowerCase.indexOf("name=\"description\"");
		indexDescriptionStart = contentLowerCase.indexOf("content=\"", indexDescriptionStart) + "content=\"".length();
		int indexDescriptionEnd = contentLowerCase.indexOf("\"", indexDescriptionStart + "content=\"".length() + 1);

		if ((indexDescriptionStart >= 0) && (indexDescriptionEnd >= 0) && indexDescriptionEnd > indexDescriptionStart) {
			return content.substring(indexDescriptionStart, indexDescriptionEnd);
		}

		return null;
	}

	public static boolean isUserAgentRobot(String userAgent) {
		if (userAgent == null) {
			return false;
		}
		return userAgent.contains("robo");
	}

	public static void main(String[] args) {
		System.out.println("***** NetHelper.main : connected: "+isConnected()); //TODO: remove debug trace
	}

	public static List<Resource> extractImage(URL inURL, String content) {
		List<Resource> urlList = new LinkedList<Resource>();

		int srcIndex = content.toLowerCase().indexOf("src=\"") + "src=\"".length();
		while (srcIndex >= "src=\"".length()) {
			int closeLink = content.indexOf("\"", srcIndex + 1);
			int closeTag = content.indexOf(">", srcIndex + 1);
			if (closeLink >= 0) {
				String url = content.substring(srcIndex, closeLink);

				int altIndex = content.toLowerCase().indexOf("alt=\"", srcIndex) + "alt=\"".length();
				String description = "";
				if (altIndex >= "alt=\"".length() && altIndex < closeTag) {
					description = content.substring(altIndex, content.indexOf("\"", altIndex + 1));
				}

				if (StringHelper.isImage(url)) {

					if (!URLHelper.isAbsoluteURL(url)) {
						if (!url.trim().startsWith("/")) {
							url = URLHelper.mergePath(URLHelper.extractPath(inURL.toString()), url);
						} else {
							url = "http://" + URLHelper.mergePath(URLHelper.extractHost(inURL.toString()), url);
						}
					}

					Resource res = new Resource();
					res.setId(url);
					res.setUri(url);
					res.setName(StringHelper.getFileNameFromPath(url));
					res.setDescription(StringHelper.removeTag(description));
					urlList.add(res);
				}
			}
			srcIndex = content.toLowerCase().indexOf("src=\"", srcIndex) + "src=\"".length();
		}

		return urlList;
	}

	public static List<String> extractURL(URL inURL, String content) {
		List<String> urlList = new LinkedList<String>();

		int httpIndex = content.toLowerCase().indexOf("http://");
		while (httpIndex >= 0) {
			int closeLink = content.indexOf("\"", httpIndex);
			if (closeLink >= 0) {
				String url = content.substring(httpIndex, closeLink);
				if (!url.contains(">")) {
					if (!urlList.contains(url)) {
						urlList.add(url);
					}
				}
			}
			httpIndex = content.toLowerCase().indexOf("http://", httpIndex + "http://".length());
		}

		int hrefIndex = content.toLowerCase().indexOf("href=\"") + "href=\"".length();
		while (hrefIndex >= "href=\"".length()) {
			int closeLink = content.indexOf("\"", hrefIndex + 1);
			if (closeLink >= 0) {
				String url = content.substring(hrefIndex, closeLink);
				if (!URLHelper.isAbsoluteURL(url)) {
					url = URLHelper.mergePath(URLHelper.extractPath(inURL.toString()), url);
				}
				if (!url.contains(">")) {
					if (!urlList.contains(url)) {
						urlList.add(url);
					}
				}
			}
			hrefIndex = content.toLowerCase().indexOf("href=\"", hrefIndex) + "href=\"".length();
		}

		int srcIndex = content.toLowerCase().indexOf("src=\"") + "src=\"".length();
		while (srcIndex >= "src=\"".length()) {
			int closeLink = content.indexOf("\"", srcIndex + 1);
			if (closeLink >= 0) {
				String url = content.substring(srcIndex, closeLink);
				if (!URLHelper.isAbsoluteURL(url)) {
					url = URLHelper.mergePath(URLHelper.extractPath(inURL.toString()), url);
				}
				if (!url.contains(">")) {
					if (!urlList.contains(url)) {
						urlList.add(url);
					}
				}
			}
			srcIndex = content.toLowerCase().indexOf("src=\"", srcIndex) + "src=\"".length();
		}

		return urlList;
	}

	public static List<String> extractExternalURL(URL inURL, String content) {
		List<String> urlList = new LinkedList<String>();

		int hrefIndex = content.toLowerCase().indexOf("href=\"") + "href=\"".length();
		while (hrefIndex >= "href=\"".length()) {
			int closeLink = content.indexOf("\"", hrefIndex + 1);
			if (closeLink >= 0) {
				String url = content.substring(hrefIndex, closeLink);
				if (!URLHelper.isAbsoluteURL(url)) {
					url = URLHelper.mergePath(URLHelper.extractPath(inURL.toString()), url);
				}
				if (!url.contains(">")) {
					if (!urlList.contains(url)) {
						urlList.add(url);
					}
				}
			}
			hrefIndex = content.toLowerCase().indexOf("href=\"", hrefIndex) + "href=\"".length();
		}

		return urlList;
	}

	/**
	 * analyse a page and retreive a image
	 * 
	 * @param URL
	 *            the url of the page
	 * @param content
	 *            the content
	 * @return the uri to the local file
	 */
	public static String getLocalCopyOfPageImage(ContentContext ctx, URL inURL, String content, CRC32 crc32, boolean preferVertical, boolean needVertical) {
		StaticConfig staticConfig = StaticConfig.getInstance(ctx.getRequest().getSession());
		GlobalContext globalContext = GlobalContext.getInstance(ctx.getRequest());
		return getLocalCopyOfPageImage(staticConfig.getCacheFolder(), globalContext.getDataFolder(), inURL, null, content, crc32, preferVertical, needVertical);
	}

	public static List<URL> extractMostSimilarLinks(URL url) throws Exception {
		String content = readPage(url);
		List<String> allLinks = extractExternalURL(url, content);

		MapCollectionWrapper<String, URL> urlByParentFolder = new MapCollectionWrapper<String, URL>();
		for (String link : allLinks) {
			urlByParentFolder.add(URLHelper.getParentURL(link), new URL(link));
		}
		// search the biggest url with the same parent folder.
		int biggestList = 1;
		String biggestKey = null;
		for (String key : urlByParentFolder.keySet()) {
			if (urlByParentFolder.get(key).size() > biggestList) {
				biggestList = urlByParentFolder.get(key).size();
				biggestKey = key;
			}
		}
		if (biggestKey == null) {
			return null;
		} else {
			return urlByParentFolder.get(biggestKey);
		}
	}

	/**
	 * analyse a page and retreive a image
	 * 
	 * @parem staticConfig pass the config (for call from a Thread )
	 * @param URL
	 *            the url of the page
	 * @param content
	 *            the content
	 * @return the uri to the local file
	 */
	public static String getLocalCopyOfPageImage(String cacheFolder, String dataFolder, URL pageURL, URL imageURL, String content, CRC32 crc32, boolean preferVertical, boolean needVertical) {
		if (needVertical) {
			preferVertical = true;
		}
		List<String> urls;
		if (imageURL != null) {
			urls = new LinkedList<String>();
			urls.add(imageURL.toString());
		} else {
			urls = extractExternalURL(pageURL, content);
		}
		int maxSizeFound = -1;
		ImageSize bestImage = null;
		boolean bestImageJpg = false;
		String finalURL = null;
		ByteArrayOutputStream finalImgBuffer = null;

		boolean imageFoundedOk = false;

		for (Iterator<String> iterator = urls.iterator(); iterator.hasNext() && !imageFoundedOk;) {
			String url = iterator.next();
			if (url.contains("?")) {
				url = url.substring(0, url.indexOf('?'));
			}
			if (StringHelper.isImage(url)) {
				URLConnection conn;
				try {
					ByteArrayOutputStream imgBuffer = new ByteArrayOutputStream();
					int imageSize = 0;
					conn = (new URL(url)).openConnection();
					conn.setReadTimeout(5000);
					InputStream in = conn.getInputStream();
					try {
						ResourceHelper.writeStreamToStream(in, imgBuffer);
					} finally {
						ResourceHelper.closeResource(in);
					}

					imgBuffer.close();
					// BufferedImage img = ImageIO.read(new ByteArrayInputStream(imgBuffer.toByteArray()));

					byte[] imageArray = imgBuffer.toByteArray();
					ImageSize img = ImageHelper.getExifSize(new ByteArrayInputStream(imageArray));
					if (img == null) {
						img = ImageHelper.getJpegSize(new ByteArrayInputStream(imageArray));
					}
					if (img == null) {
						logger.warning("no imagesize found for : " + url);
						BufferedImage bufImg = ImageIO.read(new ByteArrayInputStream(imgBuffer.toByteArray()));
						img = new ImageSize(bufImg.getWidth(), bufImg.getHeight());
					}
					imageArray = null;

					if (img != null) {

						int readImageSize = img.getWidth() * img.getHeight();

						// DEBUG
						/*
						 * File imgDir = new File("/tmp/tmp-images"); imgDir.mkdirs(); ImageIO.write(img, "jpg", new File("/tmp/tmp-images/img_"+StringHelper.getRandomId()+".jpg"));
						 */

						if ((float) img.getWidth() / (float) img.getHeight() > 0.40) { // no
							// banner
							if ((float) img.getHeight() / (float) img.getWidth() > 0.40) { // no
								// banner
								if (readImageSize > MIN_IMAGE_SIZE) {
									bestImage = img;
									maxSizeFound = imageSize;
									finalURL = url;
									finalImgBuffer = imgBuffer;
									String ext = StringHelper.getFileExtension(url).toLowerCase();
									if (ext.equals("jpg") || ext.equals("jpeg")) {
										bestImageJpg = true;
									}
								} else if (imageSize > maxSizeFound) {
									String ext = StringHelper.getFileExtension(url).toLowerCase();
									if (ext.equals("jpg") || ext.equals("jpeg")) {
										bestImage = img;
										maxSizeFound = imageSize;
										finalURL = url;
										finalImgBuffer = imgBuffer;
										bestImageJpg = true;
									} else if (!bestImageJpg) {
										bestImage = img;
										maxSizeFound = imageSize;
										finalURL = url;
										finalImgBuffer = imgBuffer;
									}
								}
								if (readImageSize > MIN_IMAGE_SIZE) {
									if (preferVertical) {
										if (bestImage.getWidth() < bestImage.getHeight()) {
											imageFoundedOk = true;
										}
									} else {
										imageFoundedOk = true;
									}
								}
							}
						}
					}
				} catch (Exception e) {
					// just next image
				}
			}
		}

		if (bestImage != null) {
			if (needVertical) {
				if (bestImage.getWidth() > bestImage.getHeight()) {
					return null;
				}
			}
			String fileName = StringHelper.createFileName(finalURL);
			try {

				String fullFileName = URLHelper.mergePath(dataFolder, cacheFolder);
				fullFileName = URLHelper.mergePath(fullFileName, fileName);

				crc32.update(finalImgBuffer.toByteArray());

				FileCache.saveFile(fullFileName, new ByteArrayInputStream(finalImgBuffer.toByteArray()));

				return URLHelper.mergePath(cacheFolder, fileName);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		} else {
			logger.warning("image not found for url : " + pageURL);
			return null;
		}
	}

	public static boolean isURLValid(URL url) {
		try {
			URLConnection urlConnection = url.openConnection();
			if (urlConnection instanceof HttpURLConnection) {
				int respondeCode = ((HttpURLConnection) urlConnection).getResponseCode();
				return (respondeCode >= 200) && (respondeCode < 300);
			}
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	public static List<URL> getLinks(String content, String inURL) {
		List<URL> urlList = new LinkedList<URL>();
		List<String> urlStrList = new LinkedList<String>();

		String lowerContext = content.toLowerCase();

		int hrefIndex = lowerContext.indexOf("href=\"") + "href=\"".length();
		while (hrefIndex >= "href=\"".length()) {
			int closeLink = lowerContext.indexOf("\"", hrefIndex + 1);
			if (closeLink >= 0) {
				String url = lowerContext.substring(hrefIndex, closeLink);
				if (!URLHelper.isAbsoluteURL(url)) {
					url = URLHelper.mergePath(URLHelper.extractPath(inURL.toString()), url);
				}
				if (!url.contains(">")) {
					if (!urlStrList.contains(url)) {
						try {
							URL newURL = new URL(url);
							urlStrList.add(url);
							urlList.add(newURL);
						} catch (MalformedURLException e) {
							// if URL malformed she is not added in the list
						}
					}
				}
			}
			hrefIndex = lowerContext.indexOf("href=\"", hrefIndex) + "href=\"".length();
		}

		return urlList;
	}

	public static List<URL> getExternalLinks(String content) {
		List<URL> urlList = new LinkedList<URL>();
		List<String> urlStrList = new LinkedList<String>();

		int hrefIndex = content.toLowerCase().indexOf("href=\"") + "href=\"".length();
		while (hrefIndex >= "href=\"".length()) {
			int closeLink = content.indexOf("\"", hrefIndex + 1);
			if (closeLink >= 0) {
				String url = content.substring(hrefIndex, closeLink);
				if (URLHelper.isAbsoluteURL(url)) {
					if (!url.contains(">")) {
						if (!urlStrList.contains(url)) {
							try {
								URL newURL = new URL(url);
								urlStrList.add(url);
								urlList.add(newURL);
							} catch (MalformedURLException e) {
								// if URL malformed she is not added in the list
							}
						}
					}
				}
			}
			hrefIndex = content.toLowerCase().indexOf("href=\"", hrefIndex) + "href=\"".length();
		}

		return urlList;
	}
	
	/**
	 * check internet connection with stable server.
	 * @return
	 */
	public static boolean isConnected() {		
		try {
			(new URL("http://www.google.com")).openConnection();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			(new URL("http://www.belgium.be")).openConnection();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			(new URL("http://www.javlo.org")).openConnection();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	public static void sendMailToAdministrator(ContentContext ctx, String subject, String content) {
		GlobalContext globalContext = GlobalContext.getInstance(ctx.getRequest());
		StaticConfig staticConfig = StaticConfig.getInstance(ctx.getRequest().getSession());
		MailService mailService = MailService.getInstance(staticConfig);
		try {
			mailService.sendMail(new InternetAddress(globalContext.getAdministratorEmail()), new InternetAddress(globalContext.getAdministratorEmail()), subject, content, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void sendRedirectPermanently(HttpServletResponse response, String url) {
		response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
		response.setHeader("Location", url);
		response.setHeader("Connection", "close");
	}

	public static void sendRedirectTemporarily(HttpServletResponse response, String url) {
		response.setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
		response.setHeader("Location", url);
		response.setHeader("Connection", "close");
	}

}
