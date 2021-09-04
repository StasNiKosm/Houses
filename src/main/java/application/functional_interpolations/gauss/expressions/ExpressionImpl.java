package application.functional_interpolations.gauss.expressions;

import java.util.List;


/**
 * Класс, который реализует интерфейс Expression<N,E>.
 *
 * Данный класс работает с числами типа Double.
 *
 * Реализует логику работы с уравнениями посредством коллекции чисел List<Double>
 *    в которой по очереди записаны от х1... до хn коэфициенты при неизвестный
 *    и в самом конце свободный член.
 */
public class ExpressionImpl implements Expression<Double, ExpressionImpl> {

    /**
     * Наш лист коэфициентов при неизвестных и + свободный член
     */
    private List<Double> coefficients;

    /**
     * Единственный конструктор класса.
     * @param coefficients - передаем список в котором уже записаны наши коэфициенты и свободный член.
     */
    public ExpressionImpl(List<Double> coefficients) {
        this.coefficients = coefficients;
    }

    /**
     * ГЕТор нашего поля со всеми элементами нашего уравнения.
     * @return
     */
    public List<Double> getCoefficients() {
        return coefficients;
    }

    /**
     * @param expression - уравнение которое мы прибавим к тому на котором был вызват этот метод.
     */
    @Override
    public void add(ExpressionImpl expression) {
        for (int i = 0; i < this.coefficients.size(); i++) {
            this.coefficients.set(i, this.coefficients.get(i) + expression.getCoefficients().get(i));
        }
    }

    /**
     * @param number - число на которое мы умножим все элементы нашей строки, т.е. все коэфициенты уравнения и свободный член.
     * @return
     */
    @Override
    public ExpressionImpl multiply(double number) {
        for (int i = 0; i < this.coefficients.size(); i++) {
            this.coefficients.set(i, this.coefficients.get(i) * number);
        }
        return this;
    }

    /**
     * Метод который вернет размер уравнения.
     * Под размером сдесь подрозумевается размер нашего листа со всеми элементами уравнения,
     * т.е. проще говоря это кол-во неизвестых + 1(свободный член).
     * @return - размер уравнения.
     */
    @Override
    public int size() {
        return this.coefficients.size();
    }

    /**
     * @param index - индекс нашего элемента, который мы хотим вернуть.
     * @return - вернет коэфицент при нейзветном под переданным индексом.
     */
    @Override
    public Double getCoefficient(int index) {
        return this.coefficients.get(index);
    }

    /**
     * @param index - индекс нашего элемента, который мы хотим изменить.
     * @param coeff - элемент НА который мы хотим поменять.
     */
    @Override
    public void setCoefficient(int index, Double coeff) {
        this.coefficients.set(index, coeff);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append('[');
        for (int i = 0; i < coefficients.size()-1; i++) {
            str.append("  " + coefficients.get(i));
        }
        str.append(" | " + coefficients.get(coefficients.size()-1) + " ]");
        return str.toString();
    }
}
