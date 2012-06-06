<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:if test="${not empty param.path}">
	<c:set var="ELPath" value="${param.path}" scope="session" />
</c:if>
<c:if test="${not info.editLanguage eq 'en'}"><script type="text/javascript" src="${currentModule.path}/js/i18n/elfinder.${info.editLanguage}.js"></script></c:if>
<div class="content nopadding">
<div id="fileManager" class="elfinder"></div>
</div>
<script type="text/javascript">
jQuery(document).ready(function() {
	var language = "${info.editLanguage}";
	jQuery('#fileManager').elfinder({
		url : '${info.staticRootURL eq "/"?"":info.staticRootURL}${currentModule.path}/jsp/connector.jsp${not empty changeRoot?"?changeRoot=true":""}',
		lang : '${info.editLanguage}',
		height: jQuery("#footer").offset().top - jQuery("#fileManager").offset().top - jQuery(".mainBoxe .widgetbox h3").height(),
		handlers : {			
			open: function(event) { ajaxRequest("${info.currentURL}?webaction=updateBreadcrumb"); }
		},
		quicklook : {
			autoplay : true,
			jplayer  : 'extensions/jplayer'
		},
		uiOptions : {
		toolbar : [
		   		['back', 'forward'],
 		   		['mkdir', 'mkfile', 'upload'],
		   		['open', 'download', 'getfile'],
		   		['info'],
		   		/*['quicklook'],*/
		   		['copy', 'cut', 'paste'],
		   		['rm'],
		   		['duplicate', 'rename', 'edit', 'resize'],		   		
		   		['search'],
		   		['view'],
		   		['help']
		   	] },
		commandsOptions : {
		    edit : {
		      editors : [
		        {
		          mimes : ['text/html','text/properties','text/plain'],  // add here other mimes if required
		          load : function(textarea) {
		            openEditor(textarea);
		          },
		          close : function(textarea, instance) {
		            closeEditor(textarea);
		          },
		          save : function(textarea, editor) {		            
		            saveEditor(textarea);
		          }
		        }		        
		      ]
		    }
		  }
	}).elfinder('instance');
	changeFooter();
});
</script>
