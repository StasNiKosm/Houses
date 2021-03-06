package application.functional_interpolations.gauss.linearsystems;

import application.functional_interpolations.gauss.expressions.Expression;

/**
 * Инттерфейс всех линейных систем, по соглашению с которой линейная с-ма обладает след. св-вами и возможностями:
 *      возврат уравнения
 *      поменять местами два уравнения
 *      знать количество уравнений
 *      вернуть/ихменить любой элемент системы, т.е. коэф. при неизвестой или свободный член.
 *
 * @param <N> - аргумент типа, который позволяет работать с любым ЧИСЛОВЫМ типом.
 * @param <E> - аргумент типа,, позволяющий собирать в систему уравнения строго одного типа,
 *           одного вида реализации (опять же - сложная логика generic-ов в java).
 */
public interface SystemOfExpressions<N extends Number, E extends Expression<N, E>> {

    /**
     * Вернет наше уравнение из системы под заданым индексом.
     * @param index - индекс уравнения, который мы ходим вернуть.
     * @return - уравнение, под заданым индексом.
     */
    E getExpression(int index);

    /**
     * Метод позволяющий поменять местами строки в системе, которые задают своими индексами.
     * @param indexFrom - индекс уравнения в с-ме которую мы меняем местами
     * @param indexTo - с этим урвнением.
     */
    void swapExpressions(int indexFrom, int indexTo);

    /**
     * вернет количесво урравнений в системе.
     * @return - кол-во уранений.
     */
    int size();

    /**
     * Метод вернет любой коэф. или свободный член, заданого аргументом j, из любого уравнеия
     * системы, заданого переданным аргументом i.
     * @param i - индекс уравнения.
     * @param j - индекс элемента, т.е. коэф. при неизвестной или свободного члена.
     * @return - возвращает элемент из системы уравнения.
     */
    N getElement(int i, int j);

    /**
     * Метод изменит любой коэф. или свободный член, заданого аргументом j, из любого уравнеия
     * системы, заданого переданным аргументом i, на переданное значение.
     * @param i - индекс уравнения.
     * @param j  - индекс элемента, т.е. коэф. при неизвестной или свободного члена.
     * @param element - значение на которое произойдет изменение.
     */
    void setElement(int i, int j, N element);
}
