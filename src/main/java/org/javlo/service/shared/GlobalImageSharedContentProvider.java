package org.javlo.service.shared;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;

import org.javlo.component.core.ComponentBean;
import org.javlo.component.image.GlobalImage;
import org.javlo.config.StaticConfig;
import org.javlo.context.ContentContext;
import org.javlo.filter.ImageFileFilter;
import org.javlo.helper.ResourceHelper;
import org.javlo.helper.URLHelper;
import org.javlo.ztatic.StaticInfo;

public class GlobalImageSharedContentProvider extends LocalImageSharedContentProvider {
	
	private Collection<SharedContent> content = new LinkedList<SharedContent>();
	
	public static final String NAME = "global-image";
	
	GlobalImageSharedContentProvider() {
		setName(NAME);
	}
	
	protected File getRootFolder(ContentContext ctx) {
		StaticConfig sc = ctx.getGlobalContext().getStaticConfig();
		File dir;
		try {
			dir = new File(URLHelper.mergePath(ctx.getGlobalContext().getSharedDataFolder(ctx.getRequest().getSession()), sc.getShareImageFolder()));
			return dir;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public Collection<SharedContent> getContent(ContentContext ctx) {		
		setCategories(new HashMap<String, String>());
		content.clear();		
		File imageFolder = getRootFolder(ctx);
		StaticConfig staticConfig = ctx.getGlobalContext().getStaticConfig();
		for (File imageFile : ResourceHelper.getAllFiles(imageFolder, new ImageFileFilter() ) ) {			
			String category = imageFile.getParentFile().getAbsolutePath().replace(imageFolder.getAbsolutePath(), "");
			category = category.replace('\\', '/');
			if (category.startsWith("/")) {
				category = category.substring(1);
			}
			if (!categories.containsKey(category)) {
				categories.put(category, category);
			}
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			PrintStream out = new PrintStream(outStream);
			out.println("dir=" + category);
			out.println("file-name="+ URLHelper.mergePath(staticConfig.getShareDataFolderKey(),imageFile.getName()));
			out.println(GlobalImage.IMAGE_FILTER + "=full");
			out.close();
			String value = new String(outStream.toByteArray());
			ComponentBean imageBean = new ComponentBean(GlobalImage.TYPE, value, ctx.getRequestContentLanguage());
			imageBean.setArea(ctx.getArea());
			SharedContent sharedContent;
			try {
				StaticInfo staticInfo = StaticInfo.getInstance(ctx, imageFile);
				sharedContent = new SharedContent(""+imageFile.hashCode(), imageBean);
				sharedContent.addCategory(category);
				sharedContent.setSortOn(staticInfo.getCreationDate(ctx).getTime());
				content.add(sharedContent);				
				GlobalImage image = new GlobalImage();
				image.init(imageBean, ctx);
				String imageURL = image.getPreviewURL(ctx, "shared-preview");
				sharedContent.setTitle(imageFile.getName());				
				sharedContent.setDescription(staticInfo.getTitle(ctx));
				sharedContent.setImageUrl(imageURL);
			} catch (Exception e) {				
				e.printStackTrace();
			}			
		}
		return content;
	}
}