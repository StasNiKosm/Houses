package application;

import application.functional_interpolations.gauss.expressions.ExpressionImpl;
import application.functional_interpolations.gauss.linearsystems.LinearSystem;
import application.functional_interpolations.gauss.methods.GaussMethod;

import java.util.*;

public class SplineInterpolator {

    private List<Double> listX;
    private List<Double> listY;

    private Map<Double, Double> mapXY;

    private List<Double> listH = new ArrayList<>();

    private List<Double> listCoefficients_C = new ArrayList<>();
    private List<Double> listCoefficients_A = new ArrayList<>();
    private List<Double> listCoefficients_B = new ArrayList<>();
    private List<Double> listCoefficients_D = new ArrayList<>();

    public SplineInterpolator(Map<Double, Double> mapXY) {
        this.mapXY = mapXY;
        this.listX = new ArrayList<>(mapXY.keySet());
        this.listY = new ArrayList<>(mapXY.values());
        for (int i = 0; i < mapXY.size() - 1; i++) {
            this.listH.add(this.listX.get(i + 1) - this.listX.get(i));
        }
        defineCoefficients_C();
        defineCoefficients_D();
        defineCoefficients_B();
        defineCoefficients_A();
    }

    public SplineInterpolator(List<Double> listX, List<Double> listY) {
        this.listX = listX;
        this.listY = listY;
        for (int i = 0; i < listX.size() - 1; i++) this.listH.add(listX.get(i+1) - listX.get(i));
        defineCoefficients_C();
        defineCoefficients_D();
        defineCoefficients_B();
        defineCoefficients_A();
    }

    private void defineCoefficients_C(){
        listCoefficients_C.add(0.0);

        List<ExpressionImpl> expressions = new ArrayList<>();

        for (int i = 0; i < listX.size() - 2; i++) {
            List<Double> coefficients = new ArrayList<>();
            if(i == 0) {
                coefficients.add(2 * (this.listH.get(0) + this.listH.get(1)));
                coefficients.add(this.listH.get(1));
                for (int j = 0; j < listX.size() - 4; j++) coefficients.add(0.0);
                coefficients.add(3 * ((listY.get(2) - listY.get(1)) / this.listH.get(1) - (listY.get(1) - listY.get(0)) / this.listH.get(0)));
            } else if(i == listX.size() - 3) {
                for (int j = 0; j < listX.size() - 4; j++) coefficients.add(0.0);
                coefficients.add(this.listH.get(this.listH.size()-2));
                coefficients.add(2 * (this.listH.get(this.listH.size()-2) + this.listH.get(this.listH.size()-1)));
                coefficients.add(3 * ((listY.get(listY.size()-1) - listY.get(listY.size()-2)) / this.listH.get(this.listH.size()-1) - (listY.get(listY.size()-2) - listY.get(listY.size()-3)) / this.listH.get(this.listH.size()-2)));
            } else {
                for (int j = 0; j < i-1; j++) coefficients.add(0.0);
                coefficients.add(this.listH.get(i));
                coefficients.add(2 * (this.listH.get(i) + this.listH.get(i+1)));
                coefficients.add(this.listH.get(i+1));
                while (coefficients.size() != listX.size()-2) coefficients.add(0.0);
                coefficients.add(3 * ((this.listY.get(i+2) - this.listY.get(i+1)) / this.listH.get(i+1) - (this.listY.get(i+1) - this.listY.get(i)) / this.listH.get(i)));
            }
            expressions.add(new ExpressionImpl(coefficients));
        }
        listCoefficients_C.addAll(new GaussMethod(new LinearSystem<>(expressions)).solve());
        listCoefficients_C.add(0.0);
    }

    private void defineCoefficients_D(){
        for (int i = 0; i < listX.size()-2; i++) {
            listCoefficients_D.add((this.listCoefficients_C.get(i+1) - this.listCoefficients_C.get(i)) / 3 / this.listH.get(i));
        }
        listCoefficients_D.add(-listCoefficients_C.get(listCoefficients_C.size()-2) / 3 / listH.get(listH.size()-1));
    }

    private void defineCoefficients_B(){
        for (int i = 0; i < listX.size()-1; i++){
            listCoefficients_B.add((listY.get(i+1) - listY.get(i)) / listH.get(i)
                    - listH.get(i) * (2*listCoefficients_C.get(i) + listCoefficients_C.get(i+1)) / 3);
        }
    }

    private void defineCoefficients_A(){
        for (int i = 0; i < listY.size()-1; i++) {
            listCoefficients_A.add(listY.get(i));
        }
    }

    public List<Double> getListCoefficients_C() {
        return listCoefficients_C;
    }

    public List<Double> getListCoefficients_A() {
        return listCoefficients_A;
    }

    public List<Double> getListCoefficients_B() {
        return listCoefficients_B;
    }

    public List<Double> getListCoefficients_D() {
        return listCoefficients_D;
    }

    public List<Double> getListX() {
        return listX;
    }

    public List<Double> getListY() {
        return listY;
    }

    public Map<Double, Double> getMapXY() {
        return mapXY;
    }

    public void print(double step){
        double x = listX.get(0);
        for (int i = 0; i < listX.size() - 1; i++) {
            while (x >= listX.get(i) && x < listX.get(i+1)){
                System.out.print((x + " ").replace(".", ","));
                System.out.println(new String(String.valueOf((Double) (listCoefficients_A.get(i)
                        + listCoefficients_B.get(i)*(x - listX.get(i))
                        + listCoefficients_C.get(i)*(x - listX.get(i))*(x - listX.get(i))
                        + listCoefficients_D.get(i)*(x - listX.get(i))*(x - listX.get(i))*(x - listX.get(i))))).replace(".", ","));
                x+=step;
            }
        }
    }

}
