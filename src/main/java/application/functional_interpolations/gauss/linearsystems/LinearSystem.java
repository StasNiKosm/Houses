package application.functional_interpolations.gauss.linearsystems;

import application.functional_interpolations.gauss.expressions.Expression;

import java.util.List;

/**
 * Класс, который реализует интерфейс SystemOfExpressions<N,E>.
 *
 * Класс является абстрактной оберткой линейной системы уравнений,
 * которая состоих из чисел любого ЧИСЛОВОГО типа.
 *
 * Реализация непостредсвенной структуры линейной системы завязана на уравнениях,
 * логика и сруктура которых завязана на интерфейсе java.gauss.expressions.Expression.
 * Все уравнения организованны в виде списка.
 *
 *
 * @param <N> - аргумент типа, который позволяет работать с любым ЧИСЛОВЫМ типом.
 * @param <E> - аргумент типа, позволяющий собирать в систему уравнения строго одного типа,
 *           одного вида реализации (опять же - сложная логика generic-ов в java).
 */
public class LinearSystem<N extends Number, E extends Expression<N, E>> implements SystemOfExpressions<N,E> {

    /**
     * Список наших уравнений
     */
    private List<E> expressions;

    /**
     * Единственный конструктор класса.
     * @param list - передаем список уже заполнего всеми нашими уравнениями.
     */
    public LinearSystem(List<E> list) {
        this.expressions = list;
    }

    /**
     * @param index - индекс уравнения, который мы ходим вернуть.
     * @return - вернет уравнение системы, т.е. Expression
     */
    @Override
    public E getExpression(int index) {
        return this.expressions.get(index);
    }

    /**
     * Поменят местами два уравнения местами в системе заданых индексами.
     * @param indexFrom - индекс уравнения в с-ме которую мы меняем местами
     * @param indexTo - с этим урвнением.
     */
    @Override
    public void swapExpressions(int indexFrom, int indexTo) {
        E temp = this.expressions.get(indexFrom);
        this.expressions.set(indexFrom, this.expressions.get(indexTo));
        this.expressions.set(indexTo, temp);
    }

    /**
     * @return - кол-во уравнений системы.
     */
    @Override
    public int size() {
        return this.expressions.size();
    }

    /**
     * @param i - индекс уравнения.
     * @param j - индекс элемента, т.е. коэф. при неизвестной или свободного члена.
     * @return - вернет коэфициент или свободный член, заданного индексом, из заданного уравнения системы.
     */
    @Override
    public N getElement(int i, int j) {
        return this.expressions.get(i).getCoefficient(j);
    }

    /**
     * Метод изменит коэфициент или свободный член, заданного индексом, из заданного уравнения системы,
     * на переданое значение - element.
     * @param i - индекс уравнения.
     * @param j  - индекс элемента, т.е. коэф. при неизвестной или свободного члена.
     * @param element - значение на которое произойдет замена.
     */
    @Override
    public void setElement(int i, int j, N element) {
        this.expressions.get(i).setCoefficient(j, element);
    }

    @Override
    public String toString() {
        return "" + expressions;
    }
}
