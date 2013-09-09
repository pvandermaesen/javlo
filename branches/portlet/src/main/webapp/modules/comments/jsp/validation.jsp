<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<div class="content">
	<div class="tabs">
	<ul>
		<li><a href="#new">${i18n.edit['title.new']}</a></li>
		<li><a href="#comment">${i18n.edit['title.comments']}</a></li>
		<li><a href="#deleted">${i18n.edit['title.deleted']}</a></li>
	</ul>
	<div id="new">
		<ul>
		<c:forEach var="commentGroup" items="${comments}">
		<c:if test="${commentGroup.countUnvalid > 0}">
		<div class="page${commentGroup.needValidation?' unvalid':' valid'}">
			<a target="_blanck" href="${commentGroup.page.viewURL}">${commentGroup.page.info.title}</a> - ${commentGroup.countUnvalid} / ${fn:length(commentGroup.comments)}
			<ul class="page">
			<c:forEach var="comment" items="${commentGroup.comments}">
			<c:if test="${!comment.valid}">
				<li class="${!comment.valid?'unvalid':'valid'}">
						<span class="actions">					 	
					 		<a class="delete" title="${i18n.view['comments.action.refuse']}" href="${info.currentURL}?webaction=refuse&comp_id=${commentGroup.id}&comment_id=${comment.id}">${i18n.view['comments.action.refuse']}</a>
							<a class="valid" title="${i18n.view['comments.action.accept']}" href="${info.currentURL}?webaction=accept&comp_id=${commentGroup.id}&comment_id=${comment.id}">${i18n.view['comments.action.accept']}</a>					
						
						<c:if test="${comment.valid}">
							<a class="delete needconfirm" href="${info.currentURL}?webaction=refuse&comp_id=${commentGroup.id}&comment_id=${comment.id}">refuse</a>
						</c:if>
					</span>
					<span class="date">${comment.displayableDate}</span> - <span class="authors"><a href="mailto:${comment.email}">${comment.authors}</a></span> - <span class="title">${comment.title}</span> - <span class="text">${comment.text}</span>
				</li>
			</c:if>
			</c:forEach>
			</ul>
		</div>
		</c:if>
		</c:forEach>
		</ul>
	</div>
	<div id="comment">
		<ul>
		<c:forEach var="commentGroup" items="${comments}">
		<c:if test="${fn:length(commentGroup.comments) > 0}">
		<div class="page${commentGroup.needValidation?' unvalid':' valid'}">
			<a target="_blanck" href="${commentGroup.page.viewURL}">${commentGroup.page.info.title}</a> - ${commentGroup.countUnvalid} / ${fn:length(commentGroup.comments)}
			<ul class="page">
			<c:forEach var="comment" items="${commentGroup.comments}">
				<li class="${!comment.valid?'unvalid':'valid'}">
					<span class="actions">
					 	<c:if test="${!comment.valid}">
					 		<a class="delete" title="${i18n.view['comments.action.refuse']}" href="${info.currentURL}?webaction=refuse&comp_id=${commentGroup.id}&comment_id=${comment.id}">${i18n.view['comments.action.refuse']}</a>
							<a class="valid" title="${i18n.view['comments.action.accept']}" href="${info.currentURL}?webaction=accept&comp_id=${commentGroup.id}&comment_id=${comment.id}">${i18n.view['comments.action.accept']}</a>						
						</c:if>
						<c:if test="${comment.valid}">
							<a class="delete needconfirm" href="${info.currentURL}?webaction=refuse&comp_id=${commentGroup.id}&comment_id=${comment.id}">refuse</a>
						</c:if>
					</span>
					<span class="date">${comment.displayableDate}</span> - <span class="authors"><a href="mailto:${comment.email}">${comment.authors}</a></span> - <span class="title">${comment.title}</span> - <span class="text">${comment.text}</span>
				</li>
			</c:forEach>
			</ul>
		</div>
		</c:if>
		</c:forEach>
		</ul>
	</div>
	<div id="deleted">
		<ul>
		<c:forEach var="commentGroup" items="${comments}">
		<li class="page${commentGroup.needValidation?' unvalid':' valid'}">
			<a target="_blanck" href="${commentGroup.page.viewURL}">${commentGroup.page.info.title}</a> - ${commentGroup.countUnvalid} / ${fn:length(commentGroup.comments)}
			<ul class="page">
			<c:forEach var="comment" items="${commentGroup.deletedComments}">
				<li class="${!comment.valid?'unvalid':'valid'}">
					<span class="date">${comment.displayableDate}</span> - <span class="authors"><a href="mailto:${comment.email}">${comment.authors}</a></span> - <span class="title">${comment.title}</span> - <span class="text">${comment.text}</span>
				</li>
			</c:forEach>
			</ul>
		</li>
		</c:forEach>
		</ul>
	</div>
	</div>
</div>