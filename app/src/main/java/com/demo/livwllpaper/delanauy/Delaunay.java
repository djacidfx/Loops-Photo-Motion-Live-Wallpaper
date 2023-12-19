package com.demo.livwllpaper.delanauy;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import com.demo.livwllpaper.beans.Point;
import com.demo.livwllpaper.beans.TriangleBitmap;
import com.demo.livwllpaper.beans.Vertice;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class Delaunay {
    private Bitmap Delaunay_image;
    private List<Point> list_LPMLW_Points = new CopyOnWriteArrayList();
    private List<TriangleBitmap> list_Triangles;

    public Delaunay(Bitmap bitmap) {
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        this.list_Triangles = copyOnWriteArrayList;
        this.Delaunay_image = bitmap;
        copyOnWriteArrayList.addAll(TriangleBitmap.cutInitialBitmap(bitmap));
    }

    public void clear() {
        for (TriangleBitmap lPMLWTriangleBitmap : this.list_Triangles) {
            lPMLWTriangleBitmap.clear();
        }
        System.gc();
    }

    public Bitmap get_Static_Triangles(Bitmap.Config config) {
        Bitmap createBitmap = Bitmap.createBitmap(this.Delaunay_image.getWidth(), this.Delaunay_image.getHeight(), config);
        Canvas canvas = new Canvas(createBitmap);
        for (TriangleBitmap lPMLWTriangleBitmap : get_Triangles_List()) {
            if (lPMLWTriangleBitmap.is_static()) {
                lPMLWTriangleBitmap.desenhaDistorcao(canvas, config);
            }
        }
        return createBitmap;
    }

    public Bitmap getImagemDelaunay() {
        return this.Delaunay_image;
    }

    public List<TriangleBitmap> get_Triangles_List() {
        return this.list_Triangles;
    }

    public List<Point> get_Points_List() {
        return this.list_LPMLW_Points;
    }

    public void upgrade_Triangles() {
        CopyOnWriteArrayList<Point> copyOnWriteArrayList = new CopyOnWriteArrayList(this.list_LPMLW_Points);
        this.list_Triangles.clear();
        this.list_LPMLW_Points.clear();
        this.list_Triangles.addAll(TriangleBitmap.cutInitialBitmap(this.Delaunay_image));
        for (Point lPMLWPoint : copyOnWriteArrayList) {
            add_point(lPMLWPoint);
        }
    }

    public void setImagemDelaunay(Bitmap bitmap) {
        this.Delaunay_image = bitmap;
        for (TriangleBitmap lPMLWTriangleBitmap : this.list_Triangles) {
            lPMLWTriangleBitmap.setOriginalImage(bitmap);
        }
    }

    public void restart_Points() {
        for (Point lPMLWPoint : get_Points_List()) {
            if (!lPMLWPoint.is_static()) {
                lPMLWPoint.set_Current_Position_Animation(lPMLWPoint.getXInit(), lPMLWPoint.getYInit());
            }
        }
    }

    public void delete_Points(List<Point> list) {
        this.list_LPMLW_Points.removeAll(list);
        upgrade_Triangles();
    }

    public void add_point(Point lPMLWPoint) {
        TriangleBitmap lPMLWTriangleBitmap;
        this.list_LPMLW_Points.add(lPMLWPoint);
        Iterator<TriangleBitmap> it = this.list_Triangles.iterator();
        while (true) {
            if (!it.hasNext()) {
                lPMLWTriangleBitmap = null;
                break;
            }
            lPMLWTriangleBitmap = it.next();
            if (lPMLWTriangleBitmap.contains_PointDentro(lPMLWPoint)) {
                break;
            }
        }
        if (lPMLWTriangleBitmap != null) {
            TriangleBitmap lPMLWTriangleBitmap2 = new TriangleBitmap(this.Delaunay_image, lPMLWTriangleBitmap.getP1(), lPMLWTriangleBitmap.getP2(), lPMLWPoint);
            TriangleBitmap lPMLWTriangleBitmap3 = new TriangleBitmap(this.Delaunay_image, lPMLWTriangleBitmap.getP2(), lPMLWTriangleBitmap.getP3(), lPMLWPoint);
            TriangleBitmap lPMLWTriangleBitmap4 = new TriangleBitmap(this.Delaunay_image, lPMLWTriangleBitmap.getP3(), lPMLWTriangleBitmap.getP1(), lPMLWPoint);
            this.list_Triangles.add(lPMLWTriangleBitmap2);
            this.list_Triangles.add(lPMLWTriangleBitmap3);
            this.list_Triangles.add(lPMLWTriangleBitmap4);
            this.list_Triangles.remove(lPMLWTriangleBitmap);
            Legalize_Edge(lPMLWPoint, lPMLWTriangleBitmap2, new Vertice(lPMLWTriangleBitmap.getP1(), lPMLWTriangleBitmap.getP2()));
            Legalize_Edge(lPMLWPoint, lPMLWTriangleBitmap3, new Vertice(lPMLWTriangleBitmap.getP2(), lPMLWTriangleBitmap.getP3()));
            Legalize_Edge(lPMLWPoint, lPMLWTriangleBitmap4, new Vertice(lPMLWTriangleBitmap.getP3(), lPMLWTriangleBitmap.getP1()));
        }
    }

    private void Legalize_Edge(Point lPMLWPoint, TriangleBitmap lPMLWTriangleBitmap, Vertice lPMLWVertice) {
        TriangleBitmap lPMLWTriangleBitmap2 = get_Neighbor_Triangle(lPMLWTriangleBitmap, lPMLWVertice);
        if (lPMLWTriangleBitmap2 != null) {
            Point lPMLWPoint2 = lPMLWTriangleBitmap2.get_point_outside_vertex(lPMLWVertice);
            double rectangle_Opposite_edge = lPMLWTriangleBitmap.rectangle_Opposite_edge(lPMLWVertice) + lPMLWTriangleBitmap2.rectangle_Opposite_edge(lPMLWVertice);
            if (is_Illegal_Edge(lPMLWTriangleBitmap, lPMLWPoint2) || rectangle_Opposite_edge > 3.141592653589793d) {
                List<TriangleBitmap> flipTriangulos = flipTriangulos(lPMLWTriangleBitmap2, lPMLWTriangleBitmap);
                Vertice lPMLWVertice2 = new Vertice(lPMLWPoint, lPMLWPoint2);
                Point lPMLWPoint3 = flipTriangulos.get(0).get_point_outside_vertex(lPMLWVertice2);
                Point lPMLWPoint4 = flipTriangulos.get(1).get_point_outside_vertex(lPMLWVertice2);
                Legalize_Edge(lPMLWPoint, flipTriangulos.get(0), new Vertice(lPMLWPoint3, lPMLWPoint2));
                Legalize_Edge(lPMLWPoint, flipTriangulos.get(1), new Vertice(lPMLWPoint2, lPMLWPoint4));
            }
        }
    }

    private boolean is_Illegal_Edge(TriangleBitmap lPMLWTriangleBitmap, Point lPMLWPoint) {
        return Boolean.valueOf(lPMLWTriangleBitmap.point_no_circumscript(lPMLWPoint)).booleanValue();
    }

    private TriangleBitmap get_Neighbor_Triangle(TriangleBitmap lPMLWTriangleBitmap, Vertice lPMLWVertice) {
        for (TriangleBitmap lPMLWTriangleBitmap2 : this.list_Triangles) {
            if (!lPMLWTriangleBitmap2.equals(lPMLWTriangleBitmap) && lPMLWTriangleBitmap2.contains_vertice(lPMLWVertice.p1, lPMLWVertice.p2)) {
                return lPMLWTriangleBitmap2;
            }
        }
        return null;
    }

    public void addPontoEstudo(Point lPMLWPoint) {
        TriangleBitmap lPMLWTriangleBitmap;
        this.list_LPMLW_Points.add(lPMLWPoint);
        Iterator<TriangleBitmap> it = this.list_Triangles.iterator();
        while (true) {
            if (!it.hasNext()) {
                lPMLWTriangleBitmap = null;
                break;
            }
            lPMLWTriangleBitmap = it.next();
            if (lPMLWTriangleBitmap.contains_PointDentro(lPMLWPoint)) {
                break;
            }
        }
        if (lPMLWTriangleBitmap != null) {
            CopyOnWriteArrayList<TriangleBitmap> copyOnWriteArrayList = new CopyOnWriteArrayList();
            TriangleBitmap lPMLWTriangleBitmap2 = new TriangleBitmap(this.Delaunay_image, lPMLWTriangleBitmap.getP1(), lPMLWTriangleBitmap.getP2(), lPMLWPoint);
            TriangleBitmap lPMLWTriangleBitmap3 = new TriangleBitmap(this.Delaunay_image, lPMLWTriangleBitmap.getP2(), lPMLWTriangleBitmap.getP3(), lPMLWPoint);
            TriangleBitmap lPMLWTriangleBitmap4 = new TriangleBitmap(this.Delaunay_image, lPMLWTriangleBitmap.getP3(), lPMLWTriangleBitmap.getP1(), lPMLWPoint);
            copyOnWriteArrayList.add(lPMLWTriangleBitmap2);
            copyOnWriteArrayList.add(lPMLWTriangleBitmap3);
            copyOnWriteArrayList.add(lPMLWTriangleBitmap4);
            this.list_Triangles.add(lPMLWTriangleBitmap2);
            this.list_Triangles.add(lPMLWTriangleBitmap3);
            this.list_Triangles.add(lPMLWTriangleBitmap4);
            this.list_Triangles.remove(lPMLWTriangleBitmap);
            lPMLWTriangleBitmap.clear();
            for (TriangleBitmap lPMLWTriangleBitmap5 : this.list_Triangles) {
                for (TriangleBitmap lPMLWTriangleBitmap6 : copyOnWriteArrayList) {
                    if (lPMLWTriangleBitmap5 != lPMLWTriangleBitmap6 && lPMLWTriangleBitmap5.and_neighbor(lPMLWTriangleBitmap6)) {
                        Vertice lPMLWVertice = lPMLWTriangleBitmap5.get_common_edge(lPMLWTriangleBitmap6);
                        lPMLWTriangleBitmap5.rectangle_Opposite_edge(lPMLWVertice);
                        lPMLWTriangleBitmap6.rectangle_Opposite_edge(lPMLWVertice);
                        Point lPMLWPoint2 = lPMLWTriangleBitmap5.get_point_outside_vertex(lPMLWVertice);
                        if (lPMLWTriangleBitmap5.point_no_circumscript(lPMLWTriangleBitmap6.get_point_outside_vertex(lPMLWVertice)) || lPMLWTriangleBitmap6.point_no_circumscript(lPMLWPoint2)) {
                            List<TriangleBitmap> flipTriangulos = flipTriangulos(lPMLWTriangleBitmap5, lPMLWTriangleBitmap6);
                            copyOnWriteArrayList.remove(lPMLWTriangleBitmap6);
                            copyOnWriteArrayList.addAll(flipTriangulos);
                            break;
                        }
                    }
                }
            }
        }
    }

    private List<TriangleBitmap> flipTriangulos(TriangleBitmap lPMLWTriangleBitmap, TriangleBitmap lPMLWTriangleBitmap2) {
        CopyOnWriteArrayList copyOnWriteArrayList = new CopyOnWriteArrayList();
        this.list_Triangles.remove(lPMLWTriangleBitmap);
        this.list_Triangles.remove(lPMLWTriangleBitmap2);
        lPMLWTriangleBitmap.clear();
        lPMLWTriangleBitmap2.clear();
        Vertice lPMLWVertice = lPMLWTriangleBitmap.get_common_edge(lPMLWTriangleBitmap2);
        Point lPMLWPoint = lPMLWTriangleBitmap.get_point_outside_vertex(lPMLWVertice);
        Point lPMLWPoint2 = lPMLWTriangleBitmap2.get_point_outside_vertex(lPMLWVertice);
        TriangleBitmap lPMLWTriangleBitmap3 = new TriangleBitmap(this.Delaunay_image, lPMLWPoint, lPMLWVertice.p1, lPMLWPoint2);
        TriangleBitmap lPMLWTriangleBitmap4 = new TriangleBitmap(this.Delaunay_image, lPMLWPoint, lPMLWVertice.p2, lPMLWPoint2);
        this.list_Triangles.add(lPMLWTriangleBitmap3);
        this.list_Triangles.add(lPMLWTriangleBitmap4);
        copyOnWriteArrayList.add(lPMLWTriangleBitmap3);
        copyOnWriteArrayList.add(lPMLWTriangleBitmap4);
        return copyOnWriteArrayList;
    }

    public void addPontoOriginal(Point lPMLWPoint) {
        TriangleBitmap lPMLWTriangleBitmap;
        TriangleBitmap lPMLWTriangleBitmap2;
        TriangleBitmap lPMLWTriangleBitmap3;
        this.list_LPMLW_Points.add(lPMLWPoint);
        Iterator<TriangleBitmap> it = this.list_Triangles.iterator();
        while (true) {
            if (!it.hasNext()) {
                lPMLWTriangleBitmap = null;
                break;
            }
            lPMLWTriangleBitmap = it.next();
            if (lPMLWTriangleBitmap.contains_PointDentro(lPMLWPoint)) {
                break;
            }
        }
        if (lPMLWTriangleBitmap != null) {
            ArrayList<Vertice> arrayList = new ArrayList();
            arrayList.add(new Vertice(lPMLWTriangleBitmap.getP1(), lPMLWTriangleBitmap.getP2()));
            arrayList.add(new Vertice(lPMLWTriangleBitmap.getP2(), lPMLWTriangleBitmap.getP3()));
            arrayList.add(new Vertice(lPMLWTriangleBitmap.getP3(), lPMLWTriangleBitmap.getP1()));
            for (TriangleBitmap lPMLWTriangleBitmap4 : this.list_Triangles) {
                if (!lPMLWTriangleBitmap4.equals(lPMLWTriangleBitmap) && lPMLWTriangleBitmap.and_neighbor(lPMLWTriangleBitmap4)) {
                    Vertice lPMLWVertice = lPMLWTriangleBitmap4.get_common_edge(lPMLWTriangleBitmap);
                    double rectangle_Opposite_edge = lPMLWTriangleBitmap4.rectangle_Opposite_edge(lPMLWVertice) + lPMLWTriangleBitmap.rectangle_Opposite_edge(lPMLWVertice);
                    if (lPMLWTriangleBitmap4.point_no_circumscript(lPMLWPoint) || rectangle_Opposite_edge > 6.283185307179586d) {
                        this.list_Triangles.remove(lPMLWTriangleBitmap4);
                        if (lPMLWTriangleBitmap.contains_vertice(lPMLWTriangleBitmap4.getP1(), lPMLWTriangleBitmap4.getP2())) {
                            lPMLWTriangleBitmap3 = new TriangleBitmap(this.Delaunay_image, lPMLWTriangleBitmap4.getP1(), lPMLWTriangleBitmap4.getP3(), lPMLWPoint);
                            lPMLWTriangleBitmap2 = new TriangleBitmap(this.Delaunay_image, lPMLWTriangleBitmap4.getP3(), lPMLWTriangleBitmap4.getP2(), lPMLWPoint);
                        } else if (lPMLWTriangleBitmap.contains_vertice(lPMLWTriangleBitmap4.getP2(), lPMLWTriangleBitmap4.getP3())) {
                            lPMLWTriangleBitmap3 = new TriangleBitmap(this.Delaunay_image, lPMLWTriangleBitmap4.getP1(), lPMLWTriangleBitmap4.getP3(), lPMLWPoint);
                            lPMLWTriangleBitmap2 = new TriangleBitmap(this.Delaunay_image, lPMLWTriangleBitmap4.getP2(), lPMLWTriangleBitmap4.getP1(), lPMLWPoint);
                        } else {
                            lPMLWTriangleBitmap3 = new TriangleBitmap(this.Delaunay_image, lPMLWTriangleBitmap4.getP2(), lPMLWTriangleBitmap4.getP3(), lPMLWPoint);
                            lPMLWTriangleBitmap2 = new TriangleBitmap(this.Delaunay_image, lPMLWTriangleBitmap4.getP2(), lPMLWTriangleBitmap4.getP1(), lPMLWPoint);
                        }
                        this.list_Triangles.add(lPMLWTriangleBitmap3);
                        this.list_Triangles.add(lPMLWTriangleBitmap2);
                        if (lPMLWTriangleBitmap.contains_vertice(lPMLWTriangleBitmap4.getP1(), lPMLWTriangleBitmap4.getP2())) {
                            arrayList.remove(new Vertice(lPMLWTriangleBitmap4.getP1(), lPMLWTriangleBitmap4.getP2()));
                        }
                        if (lPMLWTriangleBitmap.contains_vertice(lPMLWTriangleBitmap4.getP2(), lPMLWTriangleBitmap4.getP3())) {
                            arrayList.remove(new Vertice(lPMLWTriangleBitmap4.getP2(), lPMLWTriangleBitmap4.getP3()));
                        }
                        if (lPMLWTriangleBitmap.contains_vertice(lPMLWTriangleBitmap4.getP3(), lPMLWTriangleBitmap4.getP1())) {
                            arrayList.remove(new Vertice(lPMLWTriangleBitmap4.getP3(), lPMLWTriangleBitmap4.getP1()));
                        }
                    }
                }
            }
            lPMLWTriangleBitmap.clear();
            this.list_Triangles.remove(lPMLWTriangleBitmap);
            if (!arrayList.isEmpty()) {
                for (Vertice lPMLWVertice2 : arrayList) {
                    this.list_Triangles.add(new TriangleBitmap(this.Delaunay_image, lPMLWVertice2.p1, lPMLWVertice2.p2, lPMLWPoint));
                }
            }
        }
    }
}
