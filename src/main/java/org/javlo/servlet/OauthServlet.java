package org.javlo.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.oltu.oauth2.client.response.OAuthAuthzResponse;
import org.apache.oltu.oauth2.common.exception.OAuthProblemException;
import org.javlo.context.ContentContext;
import org.javlo.helper.NavigationHelper;
import org.javlo.helper.StringHelper;
import org.javlo.helper.URLHelper;
import org.javlo.navigation.MenuElement;
import org.javlo.service.social.ISocialNetwork;
import org.javlo.service.social.SocialService;

public class OauthServlet extends HttpServlet {

	private static Logger logger = Logger.getLogger(OauthServlet.class.getName());

	public OauthServlet() {
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String socialNetworkName;
		if (request.getParameter("state") != null) {
			Map<String, String> params = StringHelper.stringToMap(request.getParameter("state"));
			socialNetworkName = params.get("name");
			try {
				ContentContext ctx = ContentContext.getContentContext(request, response);
				MenuElement targetPage = NavigationHelper.getPageById(ctx, params.get("page"));
				ISocialNetwork social = SocialService.getInstance(ctx).getNetwork(socialNetworkName);
				if (social == null || targetPage == null) {
					if (social == null) {
						logger.warning("social network not found : " + socialNetworkName);
					}
					if (targetPage == null) {
						logger.warning("page not found : " + params.get("page"));
					}
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				} else {
					social.performRedirect(request, response);
					response.sendRedirect(URLHelper.createURL(ctx, targetPage));
				}
			} catch (Exception e) {
				throw new ServletException(e);
			}
		} else {
			OAuthAuthzResponse oar;
			try {
				oar = OAuthAuthzResponse.oauthCodeAuthzResponse(request);
				String code = oar.getCode();
				System.out.println("***** OauthServlet.doGet : code = "+code); //TODO: remove debug trace
			} catch (OAuthProblemException e) {
				e.printStackTrace();
			}
			
		}

		
	}

}