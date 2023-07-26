package org.javlo.macro;

import org.javlo.context.ContentContext;
import org.javlo.helper.ElementaryURLHelper;
import org.javlo.helper.StringHelper;
import org.javlo.helper.URLHelper;
import org.javlo.image.ImageEngine;
import org.javlo.user.IUserFactory;
import org.javlo.user.IUserInfo;
import org.javlo.user.UserFactory;
import org.javlo.ztatic.FileCache;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.logging.Logger;

public class DetectImageFileForUser extends AbstractMacro {

	protected static Logger logger = Logger.getLogger(DetectImageFileForUser.class.getName());

	@Override
	public String getName() {
		return "detect-user-photo";
	}

	@Override
	public String perform(ContentContext ctx, Map<String, Object> params) throws Exception {


		IUserFactory userFactory = UserFactory.createUserFactory(ctx.getGlobalContext(), ctx.getRequest().getSession());

		logger.info("Detect user photo on "+userFactory.getUserInfoList().size()+" users.");

		for (IUserInfo user : userFactory.getUserInfoList()) {
			detectAvatar(ctx, user);
		}
		userFactory.store();
		return null;
	}
	public static void detectAvatar(ContentContext ctx, IUserInfo userInfo) throws IOException {
		String avatarFileName = userInfo.getLogin() + ".webp";
		File avatarDir = new File(ElementaryURLHelper.mergePath(ctx.getGlobalContext().getStaticFolder(), "avatar_to_import"));
		File avatarFile = new File(URLHelper.mergePath(ctx.getGlobalContext().getDataFolder(), ctx.getGlobalContext().getStaticConfig().getAvatarFolder(), avatarFileName));
		if (!avatarFile.exists()) {
			if (!avatarDir.exists()) {
				avatarFile.mkdirs();
			}
			for (File imageFile : avatarDir.listFiles()) {
				if (StringHelper.isImage(imageFile.getName())) {
					if (StringHelper.containsWidthTransliteration(imageFile.getName(), userInfo.getFirstName(), userInfo.getLastName()) == 2) {
						try (InputStream in = new FileInputStream(imageFile)) {
							logger.info("create avatar file : " + avatarFile);
							BufferedImage img = ImageIO.read(in);
							System.out.println("1/ img width : "+img.getWidth());
							img = ImageEngine.resizeWidth(img, 640, true);
							System.out.println("2/ img width : "+img.getWidth());
							//avatarFile.getParentFile().mkdirs();
							avatarFile.createNewFile();
							System.out.println("3/ created : "+avatarFile);
							ImageIO.write(img, "webp", avatarFile);
						}
						logger.info("delete file : " + imageFile);
						imageFile.delete();
					}
				}
			}
			FileCache.getInstance(ctx.getRequest().getSession().getServletContext()).deleteAllFile(ctx.getGlobalContext().getContextKey(), avatarFileName);
		}
	}

	@Override
	public boolean isPreview() {
		return false;
	}

	@Override
	public boolean isAdmin() {
		return true;
	}
}
