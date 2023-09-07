package org.javlo.module.content;

import org.javlo.component.core.IContentVisualComponent;
import org.javlo.component.dynamic.DynamicComponent;
import org.javlo.context.ContentContext;

public class ComponentWrapper {
    private ContentContext ctx;
    private IContentVisualComponent comp;
    private boolean selected;
    private String hexColor;

    public ComponentWrapper(ContentContext ctx, IContentVisualComponent comp) {
        this.ctx = ctx;
        this.comp = comp;
        hexColor = comp.getHexColor();
    }

    public String getType() {
        return comp.getType();
    }

    public String getLabel() {
        return comp.getComponentLabel(ctx, ctx.getGlobalContext().getEditLanguage(ctx.getRequest().getSession()));
    }

    public String getValue() {
        return comp.getValue(ctx);
    }

    public boolean isMetaTitle() {
        return comp.isMetaTitle();
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getComplexityLevel() {
        return comp.getComplexityLevel(ctx);
    }

    public boolean isDynamicComponent() {
        return comp instanceof DynamicComponent;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    public IContentVisualComponent getComponent() {
        return comp;
    }

    public String getFontAwesome() {
        return comp.getFontAwesome();
    }

    public String getIcon() {
        return comp.getIcon();
    }

    public String getGroup() {
        return comp.getGroup();
    }

    public String getXHTMLCode() throws Exception {
        return comp.getXHTMLCode(ctx);
    }
}
