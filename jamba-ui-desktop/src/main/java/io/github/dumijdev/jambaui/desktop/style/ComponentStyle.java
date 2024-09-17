package io.github.dumijdev.jambaui.desktop.style;

import io.github.dumijdev.jambaui.core.style.Style;
import javafx.scene.Node;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ComponentStyle implements Style {
    private final Node component;
    private final Map<String, Object> styleMap = new HashMap<>();

    public ComponentStyle(Node component) {
        this.component = component;
        updateStyle();
    }

    @Override
    public Style setBackgroundColor(String color) {
        return set("-fx-background-color", color);
    }

    @Override
    public String getBackgroundColor() {
        return (String) get("-fx-background-color");
    }

    @Override
    public Style setFontColor(String color) {
        return set("-fx-text-fill", color);
    }

    @Override
    public String getFontColor() {
        return (String) get("-fx-text-fill");
    }

    @Override
    public Style setFontSize(int size) {
        return set("-fx-font-size", size + "px");
    }

    @Override
    public int getFontSize() {
        Object value = get("-fx-font-size");
        if (value != null) {
            String size = value.toString().replace("px", "");
            try {
                return Integer.parseInt(size);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setFontFamily(String family) {
        return set("-fx-font-family", family);
    }

    @Override
    public String getFontFamily() {
        return (String) get("-fx-font-family");
    }

    @Override
    public Style setFontStyle(String style) {
        return set("-fx-font-style", style);
    }

    @Override
    public String getFontStyle() {
        return (String) get("-fx-font-style");
    }

    @Override
    public Style setFontWeight(float weight) {
        return set("-fx-font-weight", weight + "px");
    }

    @Override
    public float getFontWeight() {
        Object value = get("-fx-font-weight");
        if (value != null) {
            String weight = value.toString().replace("px", "");
            try {
                return Float.parseFloat(weight);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setHeight(double height) {
        return set("-fx-height", height + "px");
    }

    @Override
    public double getHeight() {
        Object value = get("-fx-height");
        if (value != null) {
            String height = value.toString().replace("px", "");
            try {
                return Double.parseDouble(height);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setWidth(double width) {
        return set("-fx-width", width + "px");
    }

    @Override
    public double getWidth() {
        Object value = get("-fx-width");
        if (value != null) {
            String width = value.toString().replace("px", "");
            try {
                return Double.parseDouble(width);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setLineSpacing(int spacing) {
        return set("-fx-line-spacing", spacing + "px");
    }

    @Override
    public int getLineSpacing() {
        Object value = get("-fx-line-spacing");
        if (value != null) {
            String spacing = value.toString().replace("px", "");
            try {
                return Integer.parseInt(spacing);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setTitle(String title) {

        styleMap.put("title", title);

        return this;
    }

    @Override
    public String getTitle() {
        return (String) styleMap.get("title");
    }

    @Override
    public Style set(String key, Object value) {
        styleMap.put(key, value);
        updateStyle();
        return this;
    }

    @Override
    public Object get(String key) {
        return styleMap.get(key);
    }

    @Override
    public Style set(Style style) {
        for (Map.Entry<String, Object> entry : style) {
            set(entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public Style remove(String key) {
        styleMap.remove(key);
        updateStyle();
        return this;
    }

    @Override
    public Style setMargin(String margin) {
        return set("-fx-margin", margin);
    }

    @Override
    public String getMargin() {
        return (String) get("-fx-margin");
    }

    @Override
    public Style setMarginTop(int marginTop) {
        return set("-fx-margin-top", marginTop + "px");
    }

    @Override
    public int getMarginTop() {
        Object value = get("-fx-margin-top");
        if (value != null) {
            String marginTop = value.toString().replace("px", "");
            try {
                return Integer.parseInt(marginTop);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setMarginBottom(int marginBottom) {
        return set("-fx-margin-bottom", marginBottom + "px");
    }

    @Override
    public int getMarginBottom() {
        Object value = get("-fx-margin-bottom");
        if (value != null) {
            String marginBottom = value.toString().replace("px", "");
            try {
                return Integer.parseInt(marginBottom);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setMarginLeft(int marginLeft) {
        return set("-fx-margin-left", marginLeft + "px");
    }

    @Override
    public int getMarginLeft() {
        Object value = get("-fx-margin-left");
        if (value != null) {
            String marginLeft = value.toString().replace("px", "");
            try {
                return Integer.parseInt(marginLeft);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setMarginRight(int marginRight) {
        return set("-fx-margin-right", marginRight + "px");
    }

    @Override
    public int getMarginRight() {
        Object value = get("-fx-margin-right");
        if (value != null) {
            String marginRight = value.toString().replace("px", "");
            try {
                return Integer.parseInt(marginRight);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setPadding(int padding) {
        return set("-fx-padding", padding + "px");
    }

    @Override
    public int getPadding() {
        Object value = get("-fx-padding");
        if (value != null) {
            String padding = value.toString().replace("px", "");
            try {
                return Integer.parseInt(padding);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setPaddingTop(int paddingTop) {
        return set("-fx-padding-top", paddingTop + "px");
    }

    @Override
    public int getPaddingTop() {
        Object value = get("-fx-padding-top");
        if (value != null) {
            String paddingTop = value.toString().replace("px", "");
            try {
                return Integer.parseInt(paddingTop);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setPaddingBottom(int paddingBottom) {
        return set("-fx-padding-bottom", paddingBottom + "px");
    }

    @Override
    public int getPaddingBottom() {
        Object value = get("-fx-padding-bottom");
        if (value != null) {
            String paddingBottom = value.toString().replace("px", "");
            try {
                return Integer.parseInt(paddingBottom);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setPaddingLeft(int paddingLeft) {
        return set("-fx-padding-left", paddingLeft + "px");
    }

    @Override
    public int getPaddingLeft() {
        Object value = get("-fx-padding-left");
        if (value != null) {
            String paddingLeft = value.toString().replace("px", "");
            try {
                return Integer.parseInt(paddingLeft);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setPaddingRight(int paddingRight) {
        return set("-fx-padding-right", paddingRight + "px");
    }

    @Override
    public int getPaddingRight() {
        Object value = get("-fx-padding-right");
        if (value != null) {
            String paddingRight = value.toString().replace("px", "");
            try {
                return Integer.parseInt(paddingRight);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setBorder(int border) {
        return set("-fx-border-width", border + "px");
    }

    @Override
    public int getBorder() {
        Object value = get("-fx-border-width");
        if (value != null) {
            String border = value.toString().replace("px", "");
            try {
                return Integer.parseInt(border);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setBorderTop(int borderTop) {
        return set("-fx-border-top-width", borderTop + "px");
    }

    @Override
    public int getBorderTop() {
        Object value = get("-fx-border-top-width");
        if (value != null) {
            String borderTop = value.toString().replace("px", "");
            try {
                return Integer.parseInt(borderTop);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setBorderBottom(int borderBottom) {
        return set("-fx-border-bottom-width", borderBottom + "px");
    }

    @Override
    public int getBorderBottom() {
        Object value = get("-fx-border-bottom-width");
        if (value != null) {
            String borderBottom = value.toString().replace("px", "");
            try {
                return Integer.parseInt(borderBottom);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setBorderLeft(int borderLeft) {
        return set("-fx-border-left-width", borderLeft + "px");
    }

    @Override
    public int getBorderLeft() {
        Object value = get("-fx-border-left-width");
        if (value != null) {
            String borderLeft = value.toString().replace("px", "");
            try {
                return Integer.parseInt(borderLeft);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setBorderRight(int borderRight) {
        return set("-fx-border-right-width", borderRight + "px");
    }

    @Override
    public int getBorderRight() {
        Object value = get("-fx-border-right-width");
        if (value != null) {
            String borderRight = value.toString().replace("px", "");
            try {
                return Integer.parseInt(borderRight);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setBorderRadius(int borderRadius) {
        return set("-fx-border-radius", borderRadius + "px");
    }

    @Override
    public int getBorderRadius() {
        Object value = get("-fx-border-radius");
        if (value != null) {
            String borderRadius = value.toString().replace("px", "");
            try {
                return Integer.parseInt(borderRadius);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setBorderColor(String color) {
        return set("-fx-border-color", color);
    }

    @Override
    public String getBorderColor() {
        return (String) get("-fx-border-color");
    }

    @Override
    public Style setBorderWidth(int borderWidth) {
        return set("-fx-border-width", borderWidth + "px");
    }

    @Override
    public int getBorderWidth() {
        Object value = get("-fx-border-width");
        if (value != null) {
            String borderWidth = value.toString().replace("px", "");
            try {
                return Integer.parseInt(borderWidth);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    @Override
    public Style setBorderStyle(String style) {
        return set("-fx-border-style", style);
    }

    @Override
    public String getBorderStyle() {
        return (String) get("-fx-border-style");
    }

    @Override
    public Style setDisplay(String display) {
        return set("-fx-display", display);
    }

    @Override
    public String getDisplay() {
        return (String) get("-fx-display");
    }

    @Override
    public Style setFont(String font) {
        return set("-fx-font", font);
    }

    @Override
    public String getFont() {
        return (String) get("-fx-font");
    }

    @Override
    public Style setFlexDirection(String flexDirection) {
        return set("-fx-flex-direction", flexDirection);
    }

    @Override
    public String getFlexDirection() {
        return (String) get("-fx-flex-direction");
    }

    @Override
    public Style setFlexWrap(String flexWrap) {
        return set("-fx-flex-wrap", flexWrap);
    }

    @Override
    public String getFlexWrap() {
        return (String) get("-fx-flex-wrap");
    }

    @Override
    public Style setAlign(String align) {
        return set("-fx-align", align);
    }

    @Override
    public String getAlign() {
        return (String) get("-fx-align");
    }

    @Override
    public Style setAlignItems(String alignItems) {
        return set("-fx-align-items", alignItems);
    }

    @Override
    public String getAlignItems() {
        return (String) get("-fx-align-items");
    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return styleMap.entrySet().iterator();
    }

    private void updateStyle() {
        StringBuilder styleBuilder = new StringBuilder();
        for (Map.Entry<String, Object> entry : styleMap.entrySet()) {
            styleBuilder.append(entry.getKey()).append(": ").append(entry.getValue()).append("; ");
        }
        component.setStyle(styleBuilder.toString());
    }
}
