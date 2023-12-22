<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<div class="content">
<form id="actions" method="post" action="${info.currentURL}">
<div>
	<input type="hidden" name="webaction" value="ecom.storeBasket" />
	<input type="submit" value="export baskets" />
</div>
</form>
<form class="standard-form" id="actions" method="post" action="${info.currentURL}" >
<fieldset>
<legend>Ecom Data.</legend>
	<input type="hidden" name="webaction" value="ecom.update" />
	<div class="line">
		<label for="delivery">default delivery</label>
		<input type="text" name="delivery" value="${delivery}" />
	</div>
	<div class="action">
	<input type="submit" value="${i18n.edit['global.ok']}" />
	</div>
</fieldset>
</form>
<form class="standard-form" id="actions" method="post" action="${info.currentURL}" enctype="multipart/form-data">
<fieldset>
<legend>Import payement.</legend>
	<input type="hidden" name="webaction" value="ecom.importPayement" />
	<div class="line">
	<input type="file" name="file" />
	</div>
	<div class="action">
	<input type="submit" value="${i18n.edit['global.ok']}" />
	</div>
</fielset>
</form>
</div>