import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.hamcrest.Matchers;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MatrixTest {

    //Тест на совпадение размеров исходной матрицы и повёрнутой
    @ParameterizedTest
    @ValueSource(ints = {90, 180, 270})
    public void testSize(int angle) {
        int sizeBefore = 8;
        int[][] testMatrix = Matrix.randomMatrix(sizeBefore);
        int[][] rotateMatrix = Matrix.rotateMatrix(testMatrix, angle);
        int sizeAfter = rotateMatrix.length;
        Assertions.assertEquals(sizeBefore, sizeAfter);
    }

    //Тест на возникновение ошибки при вводе неправильного угла
    @Test
    public void testAngle() {
        int testAngle = 123;
        int[][] testMatrix = Matrix.randomMatrix(8);
        Assertions.assertThrows(RotateAngleError.class, () -> Matrix.rotateMatrix(testMatrix, testAngle));
    }

    //Тест на правильность поворота, в файлах содержатся классы RotateTest с правильными данными (исходная матрица, повёрнутая матрица, угол)
    @ParameterizedTest
    @MethodSource("testRotateMatrixSource")
    public void testRotateMatrix(RotateTest rotateTest) {
        int[][] testMatrix = rotateTest.initMatrix;
        int[][] rotateMatrix = Matrix.rotateMatrix(testMatrix, rotateTest.angle);
        Assertions.assertArrayEquals(rotateTest.rotateMatrix, rotateMatrix);
    }

    //Перебираем файлы в папке tests и возвращаем в виде стрима RotateTest
    private Stream<Arguments> testRotateMatrixSource() {
        List<Arguments> arguments = new ArrayList<>();
        File[] listFiles = new File("tests").listFiles();
        for (File f : listFiles) {
            RotateTest rotateTest = new RotateTest(f.getAbsolutePath());
            arguments.add(Arguments.of(rotateTest));
        }
        return arguments.stream();
    }

    //Hamcrest тест на содержание всех элементов исходной марицы в повёрнутой
    @Test
    public void testContents() {
        int[][] testMatrix = Matrix.randomMatrix(8);
        int[][] rotateMatrix = Matrix.rotateMatrix(testMatrix, 270);
        ArrayList<Integer> testMatrixList = Matrix.toFlatList(testMatrix);
        ArrayList<Integer> rotateMatrixList = Matrix.toFlatList(rotateMatrix);
        MatcherAssert.assertThat(testMatrixList, Matchers.containsInAnyOrder(rotateMatrixList.toArray()));
        MatcherAssert.assertThat(rotateMatrixList, Matchers.containsInAnyOrder(testMatrixList.toArray()));
    }

    //Hamcrest тест на не пустоту повёрнутой матрицы
    @Test
    public void testEmpty() {
        int[][] testMatrix = Matrix.randomMatrix(16);
        int[][] rotateMatrix = Matrix.rotateMatrix(testMatrix, 180);
        ArrayList<Integer> rotateMatrixList = Matrix.toFlatList(rotateMatrix);
        MatcherAssert.assertThat(rotateMatrixList, Matchers.is(Matchers.not(Matchers.empty())));
    }
}
