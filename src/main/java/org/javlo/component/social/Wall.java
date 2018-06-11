package org.javlo.component.social;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.javlo.actions.IAction;
import org.javlo.component.properties.AbstractPropertiesComponent;
import org.javlo.context.ContentContext;
import org.javlo.helper.ComponentHelper;
import org.javlo.helper.StringHelper;
import org.javlo.service.RequestService;
import org.javlo.social.SocialFilter;
import org.javlo.social.SocialLocalService;
import org.javlo.social.bean.Post;
import org.javlo.user.User;
import org.javlo.utils.JSONMap;

public class Wall extends AbstractPropertiesComponent implements IAction {
	
	private static final int PAGE_SIZE = 6;

	public static final String TYPE = "wall";

	private static final List<String> FIELDS = new LinkedList<String>(Arrays.asList(new String[] { "name", "title", "roles", "noaccess" }));

	@Override
	public String getType() {
		return TYPE;
	}

	@Override
	public String getActionGroupName() {
		return TYPE;
	}

	@Override
	public String getFontAwesome() {
		return "comments-o";
	}
	
	private String getWallName() {
		return getFieldValue("name");
	}

	@Override
	public void prepareView(ContentContext ctx) throws Exception {
		super.prepareView(ctx);
		SocialFilter.getInstance(ctx.getRequest().getSession());
		User currentUser = ctx.getCurrentUser();
		if (currentUser == null) {
			ctx.getRequest().setAttribute("access", false);
		} else {
			SocialLocalService socialService = SocialLocalService.getInstance(ctx.getGlobalContext());
			ctx.getRequest().setAttribute("pageSize", PAGE_SIZE);
			long countResult = socialService.getPostListSize(SocialFilter.getInstance(ctx.getRequest().getSession()), ctx.getCurrentUserId(), getWallName());
			float pageCountFloat = ((float)countResult/(float)PAGE_SIZE);
			int pageCount = Math.round(pageCountFloat);
			if (pageCountFloat > pageCount) {
				pageCount++;
			}
			ctx.getRequest().setAttribute("pageCount", pageCount);
			ctx.getRequest().setAttribute("countResult", countResult);
			List<String> roles = StringHelper.stringToCollection(getFieldValue("roles"));
			if (!currentUser.validForRoles(roles)) {
				ctx.getRequest().setAttribute("access", false);
			} else {
				ctx.getRequest().setAttribute("access", true);
			}
		}
	}

	public static String performCreatepost(ContentContext ctx, RequestService rs) throws Exception {
		
		if (ctx.getCurrentUser() == null) {
			return "security error !";
		}
		String text = rs.getParameter("text");
		if (text == null) {
			return "bad request structure, need at least 'text' as parameter";
		}
		if (!StringHelper.isEmpty(text)) {
			Wall comp = (Wall)ComponentHelper.getComponentFromRequest(ctx);
			SocialLocalService socialService = SocialLocalService.getInstance(ctx.getGlobalContext());
			Post post = new Post();
			post.setGroup(comp.getWallName());
			post.setAuthor(ctx.getCurrentUserId());
			post.setText(text);
			if (rs.getParameter("parent") != null) {
				post.setParent(Long.parseLong(rs.getParameter("parent")));
			}
			if (rs.getParameter("main") != null) {
				post.setMainPost(Long.parseLong(rs.getParameter("main")));
			}
			socialService.createPost(post);
		}
		return performGetpost(ctx, rs);
	}

	public static String performDeletepost(ContentContext ctx, RequestService rs) throws Exception {
		if (ctx.getCurrentUserId() == null) {
			return "security error !";
		}
		long id = Long.parseLong(rs.getParameter("id"));
		SocialLocalService.getInstance(ctx.getGlobalContext()).deletePost(ctx.getCurrentUserId(), id);
		;
		return performGetpost(ctx, rs);
	}

	public static String performGetpost(ContentContext ctx, RequestService rs) throws Exception {
		SocialLocalService socialService = SocialLocalService.getInstance(ctx.getGlobalContext());
		Map<String, Object> outMap = new HashMap<String, Object>();
		String masterPost = rs.getParameter("master");
		if (masterPost == null) {
			Wall comp = (Wall)ComponentHelper.getComponentFromRequest(ctx);
			int pageNumber = Integer.parseInt(rs.getParameter("page", "1"));
			outMap.put("posts", socialService.getPost(SocialFilter.getInstance(ctx.getRequest().getSession()), ctx.getCurrentUserId(), comp.getWallName(), PAGE_SIZE, (pageNumber-1)*PAGE_SIZE));
		} else {
			outMap.put("posts", socialService.getReplies(Long.parseLong(masterPost)));
		}
		ctx.setSpecificJson(JSONMap.JSON.toJson(outMap));
		return null;
	}
	
	public static String performUpdatefilter(ContentContext ctx, RequestService rs) throws Exception {
		SocialFilter socialFilter = SocialFilter.getInstance(ctx.getRequest().getSession());
		if (StringHelper.isTrue(rs.getParameter("reset"))) {
			socialFilter.reset();
		} else {			
			socialFilter.setQuery(rs.getParameter("text-filter",null));
			socialFilter.setOnlyMine(StringHelper.isTrue(rs.getParameter("filter-mine", null)));
		}
		return null;
	}

	@Override
	public boolean isRealContent(ContentContext ctx) {
		return true;
	}

	@Override
	public List<String> getFields(ContentContext ctx) throws Exception {
		return FIELDS;
	}
}

