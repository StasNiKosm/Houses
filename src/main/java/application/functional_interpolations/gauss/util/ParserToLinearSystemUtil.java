package application.functional_interpolations.gauss.util;

import application.functional_interpolations.gauss.expressions.ExpressionImpl;
import application.functional_interpolations.gauss.linearsystems.LinearSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Класс утилита - вспомогательный класс.
 *
 * Нужен для парсинга из списка строк, который можно получить из файла, в систему линейных уравнений.
 *
 * Структура списка и самих строк в этом списке:
 * в первой строке записано количество уравнений, а значит и количесво неизвестных,
 * в последующих строках записанны коэфициенты при неизвестных каждого уравнения системы
 * и свободный член через пробел (одна строка - одноно уравнение)
 *
 */
public final class ParserToLinearSystemUtil {

    /**
     * Статический метод, который делает из списка строк нашу систему линейных уравнений.
     * @param strFromFile - список строк (например из файла).
     * @return - вернет систему линейных уравнений.
     *         - null, если если переданный аргумент null или пустой список;
     */
    public static LinearSystem<Double, ExpressionImpl> pars(List<String> strFromFile){
        if(strFromFile == null || strFromFile.size() == 0) return null;
        int n = Integer.parseInt(strFromFile.get(0).trim());
        List<ExpressionImpl> expressions = new ArrayList<>(n);
        for (int i = 1; i < n + 1; i++) {
            List<String> elements = Arrays.asList(strFromFile.get(i).split(" ", n + 1));
            List<Double> coeff = new ArrayList<>(n+1);
            for (String elem: elements) {
                double e = Double.parseDouble(elem);
                coeff.add(e);
            }
            expressions.add(new ExpressionImpl(coeff));
        }
        return new LinearSystem<Double, ExpressionImpl>(expressions);
    }

}
