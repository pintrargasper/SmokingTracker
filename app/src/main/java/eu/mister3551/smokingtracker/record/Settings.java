package eu.mister3551.smokingtracker.record;

import android.graphics.Paint;

import java.util.List;

public class Settings {

    private String language;
    private final Graph graph;

    public Settings(String language, int color, List<Integer> pointColor, List<Integer> lineColor, Paint.Style paintStyle) {
        this.language = language;
        this.graph = new Graph(color, pointColor, lineColor, paintStyle);
    }

    public static class Graph {
        private int color;
        private List<Integer> pointColors;
        private List<Integer> lineColors;
        private Paint.Style paintStyle;

        public Graph(int color, List<Integer> pointColors, List<Integer> lineColors, Paint.Style paintStyle) {
            this.color = color;
            this.pointColors = pointColors;
            this.lineColors = lineColors;
            this.paintStyle = paintStyle;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public List<Integer> getPointColors() {
            return pointColors;
        }

        public void setPointColors(List<Integer> pointColors) {
            this.pointColors = pointColors;
        }

        public List<Integer> getLineColors() {
            return lineColors;
        }

        public void setLineColors(List<Integer> lineColors) {
            this.lineColors = lineColors;
        }

        public Paint.Style getPaintStyle() {
            return paintStyle;
        }

        public void setPaintStyle(Paint.Style paintStyle) {
            this.paintStyle = paintStyle;
        }
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Graph getGraph() {
        return graph;
    }
}