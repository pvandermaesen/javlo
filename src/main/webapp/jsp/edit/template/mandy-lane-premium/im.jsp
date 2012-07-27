<%@page import="
java.util.List,
java.util.ArrayList,
java.util.Map,
java.util.LinkedHashMap,
java.security.Principal,
org.javlo.context.GlobalContext,
org.javlo.context.ContentContext,
org.javlo.data.InfoBean,
org.javlo.service.IMService,
org.javlo.service.IMService.IMItem,
org.javlo.helper.XHTMLHelper,
org.javlo.helper.StringHelper
"%><%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"
%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %><%
GlobalContext globalContext = GlobalContext.getInstance(request);
IMService imService = IMService.getInstance(globalContext, request.getSession());
ContentContext ctx = ContentContext.getContentContext(request, response);
String currentUser = ctx.getCurrentUserId();

List<Principal> list = globalContext.getAllPrincipals();
Map<String, Map<String, String>> users = new LinkedHashMap<String, Map<String, String>>();
for (Principal principal : list) {
	Map<String, String> user = new LinkedHashMap<String, String>();
	user.put("username", principal.getName());
	user.put("color", imService.getUserColor(principal.getName()));
	users.put(principal.getName(), user);
}

String message = request.getParameter("message");
String receiver = request.getParameter("receiver");
if (message != null && !message.trim().isEmpty()) {
	if(receiver != null && !receiver.isEmpty()) {
		message = receiver + "> " + message;
	}
	message = XHTMLHelper.autoLink(XHTMLHelper.escapeXHTML(message));
	imService.appendMessage(currentUser, message);
}

Long lastMessageId = StringHelper.safeParseLong(request.getParameter("lastMessageId"), null);
boolean queryUnreadNumber = new Long(-1).equals(lastMessageId);
if (queryUnreadNumber) {
	lastMessageId = imService.getLastReadMessageId(currentUser);
}
List<IMItem> messages = new ArrayList<IMItem>();
lastMessageId = imService.fillMessageList(currentUser, lastMessageId, messages);
if (!queryUnreadNumber) {
	imService.setLastReadMessageId(currentUser, lastMessageId);
}

request.setAttribute("currentUser", currentUser);
request.setAttribute("lastMessageId", lastMessageId);
request.setAttribute("messages", messages);
request.setAttribute("users", users);

InfoBean.updateInfoBean(ctx);
%>
<div class="messagelist">
	<h4>Instant messaging</h4>
	<ul class="im-messages" style="min-height: 50px; max-height: 200px; overflow: auto;">
		<c:forEach var="message" items="${messages}">
			<li>
				<span style="color: ${users[message.from].color};">${message.from}</span>
				<small>${message.message}</small>
			</li>
		</c:forEach>
	</ul>
	<form action="${info.editTemplateURL}/im.jsp" class="im-form">
		<input type="hidden" name="lastMessageId" value="${lastMessageId}" />
		<select name="receiver">
			<option value="">-user-</option>
			<c:forEach var="entry" items="${users}">
				<c:if test="${entry.key != currentUser}">
					<option style="color: ${entry.value.color};" value="${entry.value.username}">${entry.value.username}</option>
				</c:if>
			</c:forEach>
		</select>
		<input name="message" type="text" placeholder="Message" />
		<%--<input type="submit" value="Send" />--%>
	</form>
</div>
<script>
onIMLoad();
</script>