<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>

<c:if test="${not empty param.closeFrame}">
<script type="text/javascript">
	top.location.href=top.location.href; // close iframe and refresh parent frame
</script>
</c:if>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Javlo : ${currentModule.title}</title>
<link rel="stylesheet" media="screen" href="/css/edit/components.css" />
<link rel="stylesheet" media="screen" href="${info.editTemplateURL}/css/style.css" />
<link rel="stylesheet" media="screen" href="${info.editTemplateURL}/css/javlo.css" />
<!--[if IE 9]>
    <link rel="stylesheet" media="screen" href="${info.editTemplateURL}/css/ie9.css"/>
<![endif]-->

<!--[if IE 8]>
    <link rel="stylesheet" media="screen" href="${info.editTemplateURL}/css/ie8.css"/>
<![endif]-->

<!--[if IE 7]>
    <link rel="stylesheet" media="screen" href="${info.editTemplateURL}/css/ie7.css"/>
<![endif]-->

<script type="text/javascript" src="${info.editTemplateURL}/js/plugins/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="${info.editTemplateURL}/js/plugins/jquery-ui-1.8.20.custom.min.js"></script>
<script type="text/javascript" src="${info.editTemplateURL}/js/plugins/jquery.alerts.js"></script>
<script type="text/javascript" src="/jsp/edit/global/dynamic_js.jsp"></script>
<script type="text/javascript" src="${info.editTemplateURL}/js/plugins/jquery.validate.min.js"></script>
<script type="text/javascript" src="${info.editTemplateURL}/js/plugins/jquery.colorbox-min.js"></script>
<script type="text/javascript" src="${info.editTemplateURL}/js/plugins/jquery.jgrowl.js"></script>
<script type="text/javascript" src="${info.editTemplateURL}/js/plugins/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="${info.editTemplateURL}/js/plugins/jquery.form.js"></script>
<script type="text/javascript" src="${info.editTemplateURL}/js/javlo/core.js"></script>
<script type="text/javascript" src="${info.editTemplateURL}/js/custom/gallery.js"></script>
<script type="text/javascript" src="/js/edit/ajax.js"></script>
<script type="text/javascript" src="/js/edit/core.js"></script>
<script type="text/javascript" src="${info.editTemplateURL}/js/javlo/core.js"></script>
<c:if test="${not info.editLanguage eq 'en'}"><script type="text/javascript" src="${info.editTemplateURL}/js/plugins/jquery.ui.datepicker-${info.editLanguage}.js"></script></c:if>
<script type="text/javascript" src="${info.editTemplateURL}/js/custom/general.js"></script>


<c:forEach var="css" items="${currentModule.CSS}">
<link rel="stylesheet" href="<c:url value='${css}' />"/>
</c:forEach>

<c:forEach var="js" items="${currentModule.JS}">
<script type="text/javascript" src="<c:url value='${js}' />"></script>
</c:forEach>

</head>

<body class="bodygrey ${not empty param.previewEdit?'previewEdit':''}">

<c:if test="${empty param.previewEdit}">
<div class="headerspace"></div>
<div class="header">
	
    <c:if test="${currentModule.search}">
    <form id="search" action="${info.currentURL}" method="post">
    	<input type="hidden" name="webaction" value="search" />
    	<input type="text" name="query" /> <button class="searchbutton"></button>
    </form>
    </c:if>    
    
    <div class="topheader">
        <ul class="notebutton">
            <%--<li class="note">
                <a href="${info.editTemplateURL}/pages/message.html" class="messagenotify">
                    <span class="wrap">
                        <span class="thicon msgicon"></span>
                        <span class="count">1</span>
                    </span>
                </a>
            </li>  --%>
            <li class="note">
            	<a href="${info.editTemplateURL}/notifications.jsp" class="alertnotify">
                	<span class="wrap">
                    	<span class="thicon infoicon"></span>
                        <span id="notification-count" class="count">${notificationSize}</span>
                    </span>
                </a>
            </li>
        </ul>
    </div><!-- topheader -->
    
    <!-- logo -->
	<img src="${info.editTemplateURL}/images/logo2.png" alt="Logo" />
    
    <div class="tabmenu">
    	<ul>
    	    <c:forEach var="module" items="${modules}">
        	<li class="module ${module.name} ${module.name == currentModule.name?'current':''} ${module.name == fromModule.name?'from':''}">        		
        		<a href="${info.currentURL}?module=${module.name}"><span>${module.title}</span></a>
        		${module.name == currentModule.name?'<div id="ajax-loader"></div>':''}
        	</li>
        	</c:forEach>            	
        </ul>
    </div><!-- tabmenu -->
    
    <div class="accountinfo">
    	<img src="${info.editTemplateURL}/images/avatar.png" alt="Avatar" />
        <div class="info">
        	<h3>${currentUser.name}</h3>
            <small>${currentUser.userInfo.email}&nbsp;</small>
            <p>
            	<a href="${info.currentURL}?module=users&webaction=user.ChangeMode&mode=myself">${i18n.edit["global.account-setting"]}</a> <a href="${info.currentURL}?edit-logout=logout">${i18n.edit["global.logout"]}</a>
            </p>
        </div><!-- info -->
    </div><!-- accountinfo -->
</div><!-- header -->
</c:if>


<c:if test="${empty param.previewEdit}">
<div class="sidebar">
	<div id="accordion">
		<c:forEach var="box" items="${currentModule.navigation}">
			<c:if test="${box.title != null}">		
			<h3 class="open">${box.title}</h3>
			</c:if>
			<div class="content leftmenu" style="display: block;" >			
				<jsp:include page="${box.renderer}" />			
			</div>		
		</c:forEach>		        
        <c:if test="${currentModule.helpTitle != null}">
        <h3 class="open">${currentModule.helpTitle}</h3>
        <div class="content" style="display: block;">${currentModule.helpText}</div>
        </c:if>
	</div>
	
</div><!-- leftmenu -->
</c:if>

<div class="maincontent ${currentModule.name}">

<c:if test="${currentModule.breadcrumb}">
	<div id="breadcrumbs" class="breadcrumbs">
		<jsp:include page="breadcrumbs.jsp" />
	</div>
</c:if>

<c:if test="${currentModule.sidebar}">
<div class="two_third maincontent_inner">
</c:if>
	<div class="left">		 
		
		<div id="message-container">
		<jsp:include page="message.jsp" />
		</div>
		
		<c:if test="${currentModule.toolsRenderer != null}">
			<div id="tools">
				<h3>${currentModule.toolsTitle}</h3>
				<div class="content"><jsp:include page="${currentModule.toolsRenderer}" /></div>
			</div>
		</c:if>
	
		<c:if test="${currentModule.renderer != null}">
			<div id="main-renderer">			
			<jsp:include page="${currentModule.renderer}" />
			</div>
		</c:if>
	
		<c:forEach var="currentBox" items="${currentModule.mainBoxes}">
			<c:set var="box" value="${currentBox}" scope="request" />
			<div class="mainBox" id="${box.id}">		
			<jsp:include page="box.jsp" />
			</div>
		</c:forEach>
	</div>
<c:if test="${currentModule.sidebar}">
</div> <!-- side bar -->
</c:if>
<c:if test="${empty param.previewEdit}">
<c:if test="${currentModule.sidebar}">
<div class="one_third last">
<div class="right">
	<c:forEach var="currentBox" items="${currentModule.sideBoxes}">
		<c:set var="box" value="${currentBox}" scope="request" />
		<div class="sidebox" id="${box.id}">	
		<jsp:include page="box.jsp" />
		</div>
	</c:forEach>
</div>
</div>
</c:if>
</c:if>
</div><!--maincontent-->

<br />
<c:if test="${empty param.previewEdit}">
<div id="footer" class="footer footer_float">
	<div class="footerinner">
    	<a href="http://javlo.org">javlo.org</a> 2012 - v ${info.version}
    </div><!-- footerinner -->
</div><!-- footer -->
</c:if>

</body>
</html>
