package clip;

import model.Edge;
import model.Point;

import java.util.ArrayList;
import java.util.List;

// Sutherland–Hodgman
public class Clipper {
    public ArrayList<Point> clip(ArrayList<Point> clipperPoints, ArrayList<Point> pointsToClip) {

        ArrayList<Point> pointsToReturn = new ArrayList<Point>(pointsToClip);

        for (int i = 0; i < clipperPoints.size(); i++) {
            Point A = clipperPoints.get(i);
            Point B = clipperPoints.get((i + 1) % clipperPoints.size());

            ArrayList<Point> inputList = pointsToReturn;
            pointsToReturn = new ArrayList<>();

            if (inputList.isEmpty()) break;

            // bereme předchozí vrchol jako v1
            Point v1 = inputList.getLast();

            for (Point v2 : inputList) {

                boolean v1Inside = isInside(A, B, v1);
                boolean v2Inside = isInside(A, B, v2);

                if (v2Inside) {
                    if (!v1Inside) {
                        // var. 4 – vstup dovnitř → přidat průsečík
                        pointsToReturn.add(intersection(v1, v2, A, B));
                    }
                    // var. 1/4 – přidat v2
                    pointsToReturn.add(v2);
                } else if (v1Inside) {
                    // var. 2 – vystup ven → přidáme jen průsečík
                    pointsToReturn.add(intersection(v1, v2, A, B));
                }

                v1 = v2;
            }
        }

        return pointsToReturn;
    }

    //  bod leží "uvnitř" vůči orientované hraně clipperu
    private boolean isInside(Point A, Point B, Point P) {
        // využíváme orientaci — cross product
        int cross = (B.getX() - A.getX()) * (P.getY() - A.getY())
                - (B.getY() - A.getY()) * (P.getX() - A.getX());
        return cross >= 0; // uvnitř = po levé straně hrany
    }



    // výpočet průsečíku dvou úseček (v1–v2) a (A–B)
    private Point intersection(Point v1, Point v2, Point A, Point B) {
        double x1 = v1.getX(), y1 = v1.getY();
        double x2 = v2.getX(), y2 = v2.getY();
        double x3 = A.getX(), y3 = A.getY();
        double x4 = B.getX(), y4 = B.getY();

        double denom = (x1 - x2)*(y3 - y4) - (y1 - y2)*(x3 - x4);
        if (denom == 0) return new Point((int)x1, (int)y1); // paralelní (nestává se u konvexu)

        double px = ((x1*y2 - y1*x2)*(x3 - x4) - (x1 - x2)*(x3*y4 - y3*x4)) / denom;
        double py = ((x1*y2 - y1*x2)*(y3 - y4) - (y1 - y2)*(x3*y4 - y3*x4)) / denom;

        return new Point((int)Math.round(px), (int)Math.round(py));
    }


}
