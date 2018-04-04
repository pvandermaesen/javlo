package org.javlo.image;

import java.io.File;

public class ProjectionConfig {
	
	private Polygon4 polygon;
	private float alpha;
	private File background;
	private File foreground;
	
	public ProjectionConfig(Polygon4 polygon, float alpha, File background, File foreground) {
		super();
		this.polygon = polygon;
		this.alpha = alpha;
		this.background = background;
		this.foreground = foreground;
	}
	public Polygon4 getPolygon() {
		return polygon;
	}
	public void setPolygon(Polygon4 polygon) {
		this.polygon = polygon;
	}
	public float getAlpha() {
		return alpha;
	}
	public void setAlpha(float alpha) {
		this.alpha = alpha;
	}
	public File getBackground() {
		return background;
	}
	public void setBackground(File background) {
		this.background = background;
	}
	public File getForeground() {
		return foreground;
	}
	public void setForeground(File foreground) {
		this.foreground = foreground;
	}
}