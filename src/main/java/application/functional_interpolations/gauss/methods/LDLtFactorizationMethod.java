package application.functional_interpolations.gauss.methods;

import application.functional_interpolations.gauss.expressions.ExpressionImpl;
import application.functional_interpolations.gauss.linearsystems.SystemOfExpressions;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс, который отвечает за работу метода LDLt факторизации для
 * системы линейных уравнений.
 * Сложность всего метода LDLt факторизации = O(n^3), но он примерно
 * раза в два быстрее обычного метода Гаусса;
 *
 * С-ма линейных ур-ний фактически представлена в виде расширенной симметричной матрицы,
 * а именно в виде двумерного списка всех коэфициентов при неизвестных + столбец
 * свободный членов:
 * [a11 ... a1n | b1 ]
 * [... ... ... | ...]
 * [an1 ... ann | bn ], aij = aji при i!=j
 * Вообще говоря, метод оборачивает класс SystemOfExpressions<N,E>.
 *
 * Ддя того чтобы метод сработал матриуа коэфициентов должна быть строго квадратной и симметричной,
 * т.е. кол-во уранений = кол-ву неизвестных, иначе UNDEFINED
 *
 * ВАЖНО. При создании обьекта этого класса, в конструктор передается обьект
 * типа SystemOfExpressions<N,E>, т.к. в java всё передается "по значению", то
 * то любая модификация с этим переданным обьктом отразится на нем полностью.
 * Это значит, что повторно на системе линейных ур-ний использование методов для ее решения
 * НЕ приведет ни к чему хорошему и не желательно.
 * (линейная система для этого класса - вещь одноразовая, для определения решения)
 */
public class LDLtFactorizationMethod {
    /**
     * sle - непосредвенно наша система уравнений.
     */
    private SystemOfExpressions<Double, ExpressionImpl> sle;

    /**
     * Единственный конструктор метода LDLt факторизации.
     * @param sle - передаем сформированную систему урвнением.
     *        ВАЖНО - матрица этой системы должна быть симметричной.
     */
    public LDLtFactorizationMethod(SystemOfExpressions<Double, ExpressionImpl> sle) {
        this.sle = sle;
    }

    /**
     * Метод, который занимается непосредсвеннно решением системы линейных уравнений.
     *
     * Сначала представляем матрицу в виде LDLt,
     * затем используем ее как:
     * [1 ... 0 ... 0] [a11 0  0 ... 0] [1  an-1... an] [ x1] [ b1]
     * [a2   1  ... 0]*[0  a22 0 ... 0]*[... 1 ... ...]*[ x2]=[ b2]
     * [... ... 1 ...] [.. ... ...  ..] [0 ...   1  a2] [...] [...]
     * [an ...an-1  1] [0  ... 0  ann ] [0 ... 0 ... 1] [ xn] [ bn]
     *       L                D                Lt         B
     *  A = LDLt
     *  AX = B - наша система уравнений, Х-столбец неизвестных.
     *  решаем LY = V, DV = Z и наконец LtZ = B, где Z и есть решение с-мы.
     *
     * @return - возвращает вектор решения с-мы.
     *         - null, если изначальная система = null.
     */
    public List<Double> solve() {
        if(this.sle == null) return null;
        this.toLDLtForm();
        int n = this.sle.size();
        List<Double> res = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
            res.add(0.0);
        for (int i = 0; i < n; i++) {
            double sum = 0.0;
            for (int j = n-i; j < n; j++) {
                sum += this.sle.getElement(i, n-j-1) * res.get(n-j-1);
            }
            res.set(i, this.sle.getElement(i, n) - sum);
        }
        for(int i = 0; i < n; i++){
            res.set(i, res.get(i)/this.sle.getElement(i,i));
        }
        for (int i = 0; i < n; i++) {
            double sum = 0;
            for (int j = 0; j < i; j++)
                sum += res.get(n-j-1) * this.sle.getElement(n-j-1, n-i-1);
            res.set(n-i-1, res.get(n-i-1) - sum);
        }
        return res;
    }

    /**
     * Вспомогательный метод - помечен как private.
     *
     * Представляет исходную матрицу A в виде: A = LDLt.
     * а именно в самом алгоритме в расширенную матрицу вида:
     * [b11 á12 á13 ... á1n | r1 ]   áij = аij - (k=1,j-1)Σ(áik * ljk) , i = j+1,n ;
     * [l21 b22 á23 ... á2n | r2 ]   djj = ajj - (k=1,j-1)Σ(áik * ljk) ;
     * [... ... ... ... ... |... ]   lij = áij / dij , i = j+1,n ;
     * [ln-1,1  ... an-1,n-1|rn-1]
     * [ln1 .... ln,n-1 bnn | rn ]
     *
     * Дадее в методе solve() она будет использоваться уже как LDLt, будет "разбита" на три матрицы.
     */
    private void toLDLtForm(){
        int n = this.sle.size();
        for (int i = 0; i < n; i++) {
            if (i != 0)
                for (int j = i; j < n; j++) {
                    double sum = 0.0;
                    for (int k = 0; k < i; k++) {
                        sum += this.sle.getElement(i, i - k - 1) * this.sle.getElement(i - k - 1, j);
                    }
                    this.sle.setElement(i, j, this.sle.getElement(i, j) - sum);
                }

            for (int j = i+1; j < n; j++) {
                this.sle.setElement(j, i, this.sle.getElement(i, j) / this.sle.getElement(i, i));
            }
        }
    }

    /**
     * Вспомогательный метод - помечен как private.
     *
     * Проверяет количественно, что у кажного уравнения есть своюодный член
     * и что кол-во неизвестных в кажном уравннии совподает с количесвом самих ур-ний.
     *
     * Данный метод в решении системы линнейныйх уравнений я не использую,
     * пусть эта проверка ложится на обязанность пользователя :)
     *
     * @return - true - если с-ма записанна верно.
     *           false - если с-ма не верна.
     */
    private boolean isValidSystem(){
        int count = 0;
        for (int i = 0; i < sle.size(); i++) {
            if(sle.size() == sle.getExpression(i).size()-1) count++;
        }
        return sle.size() == count;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < sle.size(); i++) {
            str.append(sle.getExpression(i).toString() + '\n');
        }
        return str.toString();
    }
}
