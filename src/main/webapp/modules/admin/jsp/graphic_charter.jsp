<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"
%><%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"
%><fieldset>
<legend>${i18n.edit['admin.title.graphic-charter']}</legend>

<input type="hidden" name="graphic-charter" value="true" />

<div class="row">
<div class="col-md-4">
<jsp:include page="template_data.jsp?name=background&value=${currentContext.templateData.background}&style=color" />
<jsp:include page="template_data.jsp?name=backgroundMenu&value=${currentContext.templateData.backgroundMenu}&style=color" />
<jsp:include page="template_data.jsp?name=backgroundActive&value=${currentContext.templateData.backgroundActive}&style=color" />
<jsp:include page="template_data.jsp?name=foreground&value=${currentContext.templateData.foreground}&style=color" />
<jsp:include page="template_data.jsp?name=border&value=${currentContext.templateData.border}&style=color" />
<%--<jsp:include page="template_data.jsp?name=textMenu&value=${currentContext.templateData.textMenu}&style=color" />--%>
<jsp:include page="template_data.jsp?name=text&value=${currentContext.templateData.text}&style=color" />
<jsp:include page="template_data.jsp?name=title&value=${currentContext.templateData.title}&style=color" />
<jsp:include page="template_data.jsp?name=link&value=${currentContext.templateData.link}&style=color" />
<jsp:include page="template_data.jsp?name=componentBackground&value=${currentContext.templateData.componentBackground}&style=color" />
<jsp:include page="template_data.jsp?name=special&value=${currentContext.templateData.special}&style=color" />
<div class="row">	
	<div class="col-xs-4">
	<label>color list</label>
	</div><div class="col-xs-8">	
		<div class="row">
		<c:forEach begin="0" end="5" varStatus="status">
		<div class="col-xs-2"><input class="form-control color" type="text" name="colorList${status.index}" value="${currentContext.templateData.colorList[status.index]}" /></div>
		</c:forEach>
		</div>
	</div>	
</div>
<h2>Message</h2>
<jsp:include page="template_data.jsp?name=messagePrimary&value=${currentContext.templateData.messagePrimary}&style=color" />
<jsp:include page="template_data.jsp?name=messageSecondary&value=${currentContext.templateData.messageSecondary}&style=color" />
<jsp:include page="template_data.jsp?name=messageSuccess&value=${currentContext.templateData.messageSuccess}&style=color" />
<jsp:include page="template_data.jsp?name=messageDanger&value=${currentContext.templateData.messageDanger}&style=color" />
<jsp:include page="template_data.jsp?name=messageWarning&value=${currentContext.templateData.messageWarning}&style=color" />
<jsp:include page="template_data.jsp?name=messageInfo&value=${currentContext.templateData.messageInfo}&style=color" />
</div><div class="col-md-8">
<div class="row"><div class="col-lg-4">
<div class="form-group">
	<label for="logo">logo : </label>
	<input type="file" name="logo" id="logo" />
</div>
<c:if test="${not empty logoPreview}">
	<c:url var="delURL" value="${info.currentURL}" context="/">
		<c:param name="webaction" value="admin.removelogo" />
	</c:url>
	<div class="delete-link"><a href="${delURL}" title="remove logo"><i class="fa fa-times" aria-hidden="true"></i></a></div>
	<img alt="logo" src="${logoPreview}" />
</c:if>

<h2>Layout</h2>
<div class="checkbox">	
	<label><input type="checkbox" name="fixMenu" ${currentContext.templateData.fixMenu?'checked="checked"':""}" />
	${i18n.edit['admin.template.fix-menu']}</label>
</div>
<div class="checkbox">	
	<label><input type="checkbox" name="largeMenu" ${currentContext.templateData.largeMenu?'checked="checked"':""}" />
	${i18n.edit['admin.template.large-menu']}</label>
</div>
<div class="checkbox">	
	<label><input type="checkbox" name="dropdownMenu" ${currentContext.templateData.dropdownMenu?'checked="checked"':""}" />
	${i18n.edit['admin.template.dropdown-menu']}</label>
</div>
<div class="checkbox">	
	<label><input type="checkbox" name="searchMenu" ${currentContext.templateData.searchMenu?'checked="checked"':""}" />
	${i18n.edit['admin.template.search-menu']}</label>
</div>
<div class="checkbox">	
	<label><input type="checkbox" name="jssearchMenu" ${currentContext.templateData.jssearchMenu?'checked="checked"':""}" />
	${i18n.edit['admin.template.jssearch-menu']}</label>
</div>
<div class="checkbox">	
	<label><input type="checkbox" name="large" id="large-content" ${currentContext.templateData.large?'checked="checked"':""} onchange="jQuery('#small-content').removeProp('checked');" />
	${i18n.edit['admin.template.large']}</label>
</div>
<div class="checkbox">	
	<label><input type="checkbox" name="small" id="small-content" ${currentContext.templateData.small?'checked="checked"':""} onchange="jQuery('#large-content').removeProp('checked');" />
	${i18n.edit['admin.template.small']}</label>
</div>
</div><div class="col-lg-8">

<script>
	var fontRef = new Array();
	<c:forEach var="font" items="${fonts}">fontRef['${font}'] = '${fontsMap[font]}';
	</c:forEach>
</script>

<c:if test="${info.admin || not empty contentContext.currentTemplate.templateData['fontHeading']}">
<div class="fonts">
	<h2>${i18n.edit['admin.title.font.heading']}</h2>
	<script>
		function updateHeadingFont(fontName) {
			jQuery('body').append(fontRef[fontName]);
			jQuery('#heading-exemple').attr('style', 'font-family:'+fontName)
		}
		document.addEventListener("DOMContentLoaded", function(event) { 
			updateHeadingFont('${currentContext.templateData.fontHeading}');
			updateTextFont('${currentContext.templateData.fontText}');
		});
	</script>

	<a class="nav" href="#" onclick="jQuery('#heading-font>option:selected').prop('selected',false).prev().prop('selected',true);updateHeadingFont(jQuery('#heading-font option:selected').text());return false;" title="${i18n.edit['global.previous']}"><i class="fa fa-arrow-circle-left" aria-hidden="true"></i></a>
	<select id="heading-font" name="fontHeading" onchange="updateHeadingFont(jQuery('#heading-font option:selected').text());">
		<option></option>
		<c:forEach var="font" items="${fonts}"><option ${currentContext.templateData.fontHeading == font?'selected="selected"':''}>${font}</option></c:forEach>
	</select>
	<a class="nav" href="#" onclick="jQuery('#heading-font>option:selected').prop('selected',false).next().prop('selected',true);updateHeadingFont(jQuery('#heading-font option:selected').text());return false;" title="${i18n.edit['global.next']}"><i class="fa fa-arrow-circle-right" aria-hidden="true"></i></a>
	
	<div class="exemple" id="heading-exemple">
		<p>0123456789</p>
		<p>abcdefghijklmnopqrstuvwxyz</p>
		<p>ABCDEFGHIJKLMNOPQRSTUVWXYZ</p>
		<p>���������</p>
	</div>
</div></c:if>
<c:if test="${info.admin || not empty contentContext.currentTemplate.templateData['fontText']}">
<div class="fonts">
	<h2>${i18n.edit['admin.title.font.text']}</h2>
	<script>
		function updateTextFont(fontName) {
			jQuery('body').append(fontRef[fontName]);
			jQuery('#text-exemple').attr('style', 'font-family:'+fontName)
		}
	</script>
	
	<a class="nav" href="#" onclick="jQuery('#text-font>option:selected').prop('selected',false).prev().prop('selected',true);updateTextFont(jQuery('#text-font option:selected').text());return false;" title="${i18n.edit['global.previous']}"><i class="fa fa-arrow-circle-left" aria-hidden="true"></i></a>
	<select id="text-font" name="fontText" onchange="updateTextFont(jQuery('#text-font option:selected').text());">
		<option></option>
		<c:forEach var="font" items="${fonts}"><option ${currentContext.templateData.fontText == font?'selected="selected"':''}>${font}</option></c:forEach>
	</select>
	<a class="nav" href="#" onclick="jQuery('#text-font>option:selected').prop('selected',false).next().prop('selected',true);updateTextFont(jQuery('#text-font option:selected').text());return false;" title="${i18n.edit['global.next']}"><i class="fa fa-arrow-circle-right" aria-hidden="true"></i></a>

	
	<div class="exemple" id="text-exemple">
		<p>0123456789</p>
		<p>abcdefghijklmnopqrstuvwxyz</p>
		<p>ABCDEFGHIJKLMNOPQRSTUVWXYZ</p>
		<p>���������</p>
	</div>
</div></c:if>
</div>
</div>
</div>
</div>
</fieldset>