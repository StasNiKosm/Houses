package application.functional_interpolations.gauss.methods;

import application.functional_interpolations.gauss.expressions.ExpressionImpl;
import application.functional_interpolations.gauss.linearsystems.LinearSystem;
import application.functional_interpolations.gauss.linearsystems.SystemOfExpressions;
import application.functional_interpolations.gauss.util.CopyLinearSystemUtil;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс, который отвечает за работу метода Гаусса для
 * системы линейных уравнений. Производит прямой и обратный ход.
 * Сложность всего метода Гаусса  <рямой ход>O(n^3) + <обратный ход>O(n^2) = O(n^3);
 *
 * С-ма линейных ур-ний фактически представлена в виде расширенной матрицы,
 * а именно в виде двумерного списка всех коэфициентов при неизвестных + столбец
 * свободный членов:
 * [a11 ... a1n | b1 ]
 * [... ... ... | ...]
 * [an1 ... ann | bn ]
 * Вообще говоря, метод оборачивает класс SystemOfExpressions<N,E>.
 *
 * Ддя того чтобы метод сработал матриуа коэфициентов должна быть строго квадратной,
 * т.е. кол-во уранений = кол-ву неизвестных, иначе UNDEFINED
 *
 * ВАЖНО. Метод работает с копией системы => на ереданной системе это никак не отразится.
 */
public class GaussMethod {

    /**
     * sleCopy - копия системы, над которой мы и работаем.
     */
    private SystemOfExpressions<Double, ExpressionImpl> sleCopy;

    /**
     * Единственный конструктор метода Гаусса
     * @param sle - передаем сформированную систему урвнением.
     */
    public GaussMethod(SystemOfExpressions<Double, ExpressionImpl> sle) {
        this.sleCopy = CopyLinearSystemUtil.copyLS(sle);
    }

    /**
     * Метод, который работает непосредственно над решение линейного уравнения.
     * для этого она сначала приводит нашу систему (представнение которой в виде матрици)
     * к верхне-треугольному виду, т.е. нули под главной диагональю.
     * А далее обратным ходом соберет значения для всех неизвестных.
     *
     * Сложность обратного хода = O(n^2).
     * Сложность прямого хода = O(n^3).
     *
     *
     * @return - List<> - результат если он есть,
     *           null, если система уравнений составлена не правильнно,
     *           бесконечно много решений или вообще нет, или с-ма изначальон = null.
     */
    public List<Double> solve(){
        if(this.sleCopy == null) return null;
        if( !this.zerosUnderDiag()) return null;
        int n = this.sleCopy.size();
        List<Double> res = new ArrayList<>();
        for (int i = 0; i < n; i++)
            res.add(0.0);
        for (int i = 0; i < n; i++){
            double sum = 0;
            for (int j = 0; j < i; j++)
                sum += res.get(n-j-1) * sleCopy.getElement(n-i-1, n-j-1);
            res.set(n-i-1, (sleCopy.getElement(n-i-1, n) - sum)/sleCopy.getElement(n-i-1, n-i-1));
        }
        return res;
    }

    /**
     * Вспомогательный метод, помечен как private.
     *
     * Приводит исходную систему, представленную в виде расширенной матрицы
     * к верхне-треугольному виду, т.е. нули под главной диагональю.
     *
     * Использует метод @ double maxUp(int indexOfColumn) @ для проверки с его возвращающим
     * аргументом для равенства на 0. Если всруг он равен 0, то наша система линейных
     * уравнений несовместна.
     *
     * Является прмым ходом метода Гаусса.
     * Сложность прямого хода = O(n^3).
     *
     * @return - true - если максимальное значение по модулю не равно = 0
     *           и мы можем без последсвий моднять строку с этим элементом.
     *           false - если вдруг получается, что нах максимальный элемен = 0,
     *           то по алгоритму мы делим на него => UNDEFINED (е сущ. ед. решения).
     */
    //если не проверить деление на 0 (maxUp(i) != 0) то будет NaN
    private boolean zerosUnderDiag(){
        int n = this.sleCopy.size();
        for (int i = 0; i < n; i++) {
            double max = maxUp(i);
            if(Math.abs(max) > 0.00000000001) {
                for (int j = 0; j < n - i - 1; j++) {
                    if(Math.abs(sleCopy.getElement(n-j-1, i)) < 0.00000000001) continue;
                    sleCopy.getExpression(n-j-1).add(sleCopy.getExpression(i).multiply(-1 * sleCopy.getElement(n-j-1, i) / sleCopy.getElement(i,i)));
                }
            } else return false;
        }
        return true;
    }

    /**
     * Вспомогательный метод - помечен как private;
     *
     * Метод совершает поиск максимального элемента из участка столбца: элемент гл.диагонали и всё, что под нем.
     * После поиска он переставляет строку с этим элементом на место строки с тем же индексом
     * столбца в котором совершился поиск (т.к., если, например, наш indexOfColumn != 0,
     * то есть строки выше и столбцы левее, над которыми алгоритм уже поработал => если
     * мы их еще раз тронем, то нарушим порядок и структуру всего метода)
     *
     * Происходит возпрат нашего максимального элемента для дальнейшей с ним работы.
     *
     * @param indexOfColumn - индекс столбца в котором происходит поиск максимального элемента
     * @return - возвращает наш максимальный элемент из участка столбца: элемент гл.диагонали и всё, что под нем.
     */
    private double maxUp(int indexOfColumn){
        double maxElem = Math.abs( this.sleCopy.getElement(indexOfColumn,indexOfColumn));
        int indexOfMaxElem = indexOfColumn;
        for (int i = indexOfColumn + 1; i < this.sleCopy.size(); i++) {
            if (maxElem < Math.abs(this.sleCopy.getElement(i, indexOfColumn))) {
                maxElem = Math.abs(this.sleCopy.getElement(i, indexOfColumn));
                indexOfMaxElem = i;
            }
        }
        sleCopy.swapExpressions(indexOfColumn, indexOfMaxElem);
        return maxElem;
    }

    /**
     * Метод для расчета вектора невязки. ВООБЩЕ ЕГО МОЖНО УБРАТЬ.
     * @param sle - наша система
     * @param solve - решение переданной ранее системы линейных ур-ний.
     * @return - возвращает вектор невязки.
     */
    public List<Double> residual(LinearSystem<Double, ExpressionImpl> sle, List<Double> solve){
        int n = solve.size();
        List<Double> vec = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            double sum = 0.0;
            for (int j = 0; j < n; j++) {
                 sum += sle.getElement(i, j) * solve.get(j);
            }
            vec.add(sle.getElement(i, n) - sum);
        }
        return vec;
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
        for (int i = 0; i < sleCopy.size(); i++) {
            if(sleCopy.size() == sleCopy.getExpression(i).size()-1) count++;
        }
        return sleCopy.size() == count;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < sleCopy.size(); i++) {
            str.append(sleCopy.getExpression(i).toString() + '\n');
        }
        return str.toString();
    }
}
