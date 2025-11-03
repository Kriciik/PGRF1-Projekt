package clip;

import model.Edge;
import model.Point;

import java.util.ArrayList;
import java.util.List;


public class Clipper {
    public ArrayList<Point> clip(ArrayList<Point> clipperPoints, ArrayList<Point> pointsToClip) {
        ArrayList<Point> pointsToReturn = new ArrayList<Point>();


//        in - pointsToClip
//        clipPolygon - clipperPoints
//        out - pointsToReturn

//        for (Edge edge : clipPolygon){
//            out.clear();
//            Point v1 = in.last;
//            for (Point v2 : in){
//                if (v2 inside edge){
//                    if (v1 not inside edge)
//                    out.add(intersection(v1,v2,edge)); //var.4
//                    out.add(v2); //var.1,4
//                }else{
//                    if (v1 inside edge)
//                    out.add(intersection(v1,v2,edge)); //var.2
//                }
//                v1 = v2;
//            }
////aktualizuj ořezávaný polygon
//        }

//        • Leží bod 𝑥0, 𝑦0 vně nebo uvnitř vzhledem k přímce?
//• Tečný vektor přímky
//                𝑡 = 𝑝2. 𝑥 − 𝑝1. 𝑥, 𝑝2. 𝑦 − 𝑝1. 𝑦
//• Normálový vektor
//        𝑛 = 𝑡. 𝑦, −𝑡. 𝑥
//        𝑛 = −𝑡. 𝑦,𝑡. 𝑥
//• Vektor k bodu
// 𝑣 = 𝑥0 − 𝑝1. 𝑥, 𝑦0 − 𝑝1. �

        return pointsToReturn;
    }
}
