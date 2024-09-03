package io.github.dumijdev.jambaui.core;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class StyleObject implements Style{
    private final Map<String, Object> attributes = new HashMap<>();

    @Override
    public Style setBackgroundColor(String color) {
        attributes.put("background-color", color);
        return this;
    }

    @Override
    public String getBackgroundColor() {
        return attributes.get("background-color").toString();
    }

    @Override
    public Style setFontColor(String color) {
        attributes.put("font-color", color);
        return this;
    }

    @Override
    public String getFontColor() {
        return attributes.get("font-color").toString();
    }

    @Override
    public Style setFontSize(int size) {
        attributes.put("font-size", size);
        return this;
    }

    @Override
    public int getFontSize() {
        return (int) attributes.get("font-size");
    }

    @Override
    public Style setFontFamily(String family) {
        attributes.put("font-family", family);
        return this;
    }

    @Override
    public String getFontFamily() {
        return attributes.get("font-family").toString();
    }

    @Override
    public Style setFontStyle(String style) {
        attributes.put("font-style", style);
        return this;
    }

    @Override
    public String getFontStyle() {
        return attributes.get("font-style").toString();
    }

    @Override
    public Style setFontWeight(float weight) {
        attributes.put("font-weight", weight);
        return this;
    }

    @Override
    public float getFontWeight() {
        return (int) attributes.get("font-weight");
    }

    @Override
    public Style setHeight(double height) {
        attributes.put("height", height);
        return this;
    }

    @Override
    public double getHeight() {
        return (double) attributes.get("height");
    }

    @Override
    public Style setWidth(double width) {
        attributes.put("width", width);
        return this;
    }

    @Override
    public double getWidth() {
        return (double) attributes.get("width");
    }

    @Override
    public Style setLineSpacing(int spacing) {
        attributes.put("line-spacing", spacing);
        return this;
    }

    @Override
    public int getLineSpacing() {
        return (int) attributes.get("line-spacing");
    }

    @Override
    public Style setTitle(String title) {
        attributes.put("title", title);
        return this;
    }

    @Override
    public String getTitle() {
        return attributes.get("title").toString();
    }

    @Override
    public Style set(String key, Object value) {
        attributes.put(key, value);
        return this;
    }

    @Override
    public Object get(String key) {
        return attributes.get(key);
    }

    @Override
    public Style set(Style style) {
        for (var item : style){
            attributes.put(item.getKey(), item.getValue());
        }

        return this;
    }

    @Override
    public Style remove(String key) {
        attributes.remove(key);
        return this;
    }

    @Override
    public Style setMargin(String margin) {
        attributes.put("margin", margin);
        return this;
    }

    @Override
    public String getMargin() {
        return attributes.get("margin").toString();
    }

    @Override
    public Style setMarginTop(int marginTop) {
        attributes.put("margin-top", marginTop);
        return this;
    }

    @Override
    public int getMarginTop() {
        return (int) attributes.get("margin-top");
    }

    @Override
    public Style setMarginBottom(int marginBottom) {
        attributes.put("margin-bottom", marginBottom);
        return this;
    }

    @Override
    public int getMarginBottom() {
        return (int) attributes.get("margin-bottom");
    }

    @Override
    public Style setMarginLeft(int marginLeft) {
        attributes.put("margin-left", marginLeft);
        return this;
    }

    @Override
    public int getMarginLeft() {
        return (int) attributes.get("margin-left");
    }

    @Override
    public Style setMarginRight(int marginRight) {
        attributes.put("margin-right", marginRight);
        return this;
    }

    @Override
    public int getMarginRight() {
        return (int) attributes.get("margin-right");
    }

    @Override
    public Style setPadding(int padding) {
        attributes.put("padding", padding);
        return this;
    }

    @Override
    public int getPadding() {
        return (int) attributes.get("padding");
    }

    @Override
    public Style setPaddingTop(int paddingTop) {
        attributes.put("padding-top", paddingTop);
        return this;
    }

    @Override
    public int getPaddingTop() {
        return (int) attributes.get("padding-top");
    }

    @Override
    public Style setPaddingBottom(int paddingBottom) {
        attributes.put("padding-bottom", paddingBottom);
        return this;
    }

    @Override
    public int getPaddingBottom() {
        return (int) attributes.get("padding-bottom");
    }

    @Override
    public Style setPaddingLeft(int paddingLeft) {
        attributes.put("padding-left", paddingLeft);
        return this;
    }

    @Override
    public int getPaddingLeft() {
        return (int) attributes.get("padding-left");
    }

    @Override
    public Style setPaddingRight(int paddingRight) {
        attributes.put("padding-right", paddingRight);
        return this;
    }

    @Override
    public int getPaddingRight() {
        return (int) attributes.get("padding-right");
    }

    @Override
    public Style setBorder(int border) {
        attributes.put("border", border);
        return this;
    }

    @Override
    public int getBorder() {
        return (int) attributes.get("border");
    }

    @Override
    public Style setBorderTop(int borderTop) {
        attributes.put("border-top", borderTop);
        return this;
    }

    @Override
    public int getBorderTop() {
        return (int) attributes.get("border-top");
    }

    @Override
    public Style setBorderBottom(int borderBottom) {
        attributes.put("border-bottom", borderBottom);
        return this;
    }

    @Override
    public int getBorderBottom() {
        return (int) attributes.get("border-bottom");
    }

    @Override
    public Style setBorderLeft(int borderLeft) {
        attributes.put("border-left", borderLeft);
        return this;
    }

    @Override
    public int getBorderLeft() {
        return (int) attributes.get("border-left");
    }

    @Override
    public Style setBorderRight(int borderRight) {
        attributes.put("border-right", borderRight);
        return this;
    }

    @Override
    public int getBorderRight() {
        return (int) attributes.get("border-right");
    }

    @Override
    public Style setBorderRadius(int borderRadius) {
        attributes.put("border-radius", borderRadius);
        return this;
    }

    @Override
    public int getBorderRadius() {
        return (int) attributes.get("border-radius");
    }

    @Override
    public Style setBorderColor(String color) {
        attributes.put("border-color", color);
        return this;
    }

    @Override
    public String getBorderColor() {
        return attributes.get("border-color").toString();
    }

    @Override
    public Style setBorderWidth(int borderWidth) {
        return this;
    }

    @Override
    public int getBorderWidth() {
        return (int) attributes.get("border-width");
    }

    @Override
    public Style setBorderStyle(String style) {
        attributes.put("border-style", style);
        return this;
    }

    @Override
    public String getBorderStyle() {
        return attributes.get("border-style").toString();
    }

    @Override
    public Style setDisplay(String display) {
        attributes.put("display", display);
        return this;
    }

    @Override
    public String getDisplay() {
        return attributes.get("display").toString();
    }

    @Override
    public Style setFont(String font) {
        attributes.put("font", font);
        return this;
    }

    @Override
    public String getFont() {
        return attributes.get("font").toString();
    }

    @Override
    public Style setFlexDirection(String flexDirection) {
        attributes.put("flex-direction", flexDirection);
        return this;
    }

    @Override
    public String getFlexDirection() {
        return attributes.get("flex-direction").toString();
    }

    @Override
    public Style setFlexWrap(String flexWrap) {
        attributes.put("flex-wrap", flexWrap);
        return this;
    }

    @Override
    public String getFlexWrap() {
        return attributes.get("flex-wrap").toString();
    }

    @Override
    public Style setAlign(String align) {
        attributes.put("align", align);
        return this;
    }

    @Override
    public String getAlign() {
        return attributes.get("align").toString();
    }

    @Override
    public Style setAlignItems(String alignItems) {
        attributes.put("align-items", alignItems);
        return this;
    }

    @Override
    public String getAlignItems() {
        return attributes.get("align-items").toString();
    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return attributes.entrySet().iterator();
    }
}
