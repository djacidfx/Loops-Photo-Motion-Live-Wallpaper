package com.demo.livwllpaper.beans;


public class Vertice {
    public Point p1;
    public Point p2;

    
    public static class Vertice_Distance_Package implements Comparable<Vertice_Distance_Package> {
        public double distance;
        public Vertice edge;

        public Vertice_Distance_Package(Vertice lPMLWVertice, double d) {
            this.edge = lPMLWVertice;
            this.distance = d;
        }

        public int compareTo(Vertice_Distance_Package vertice_Distance_Package) {
            return Double.compare(this.distance, vertice_Distance_Package.distance);
        }
    }

    public Vertice(Point lPMLWPoint, Point lPMLWPoint2) {
        this.p1 = lPMLWPoint;
        this.p2 = lPMLWPoint2;
    }

    public float angular_coefficient() {
        return (this.p2.getYInit() - this.p1.getYInit()) / (this.p2.getXInit() - this.p1.getXInit());
    }

    public boolean equals(Object obj) {
        Vertice lPMLWVertice = (Vertice) obj;
        return (lPMLWVertice.p1.equals(this.p1) || lPMLWVertice.p1.equals(this.p2)) && (lPMLWVertice.p2.equals(this.p1) || lPMLWVertice.p2.equals(this.p2));
    }
}
