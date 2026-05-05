package org.graphicsEditor.global;

public class GConstants {
    public enum EShapeType {
        eSelect("선택"),
        eRectangle("네모"),
        eOval("동그라미"),
        eLine("라인"),
        ePolygon("폴리곤");

        private String name;
        private EShapeType(String name) {
            this.name = name;
        }
        public String getName() {
            return this.name;
        }
    };
}
