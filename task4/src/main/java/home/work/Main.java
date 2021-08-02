package home.work;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        try {
            final List<Integer> nums = loadData(args[0]);

            Collections.sort(nums);

            if (nums.size() % 2 == 0) {
                calculateForEvenSize(nums);
            } else {
                calculateForOddSize(nums);
            }
        } catch (IncorrectDataException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка. Не обнаружено аргументов с путями к файлам");
            e.printStackTrace();
        }
    }

    private static List<Integer> loadData(String path) throws IncorrectDataException {
        try (
            BufferedReader reader = new BufferedReader(new FileReader(path));
        ) {
            List<Integer> array = new ArrayList<>();

            while (reader.ready()) {
                final int number = Integer.parseInt(reader.readLine());
                array.add(number);
            }

            if (array.isEmpty()) {
                System.out.println("Ошибка. Пустой файл. Нечего обсчитывать");
                throw new IncorrectDataException();
            }

            return array;

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден");
            throw new IncorrectDataException();
        } catch (IOException e) {
            System.out.println("Ошибка при извлечении данных из файла");
            throw new IncorrectDataException();
        } catch (NumberFormatException e) {
            System.out.println("Ошибка при извлечении данных из файла. Данные должны быть целыми числами");
            throw new IncorrectDataException();
        }
    }

    private static void calculateForEvenSize(List<Integer> nums) {

        int size = nums.size();
        int halfSize = size/2;

        int middleLeft = halfSize - 1;
        int middleLeftValue = nums.get(middleLeft);
        int middleRightValue = nums.get(halfSize);

        int count = 0;

        for (int i = 0; i < middleLeft; i++) {
            count += middleLeftValue - nums.get(i);
        }

        for (int i = halfSize + 1; i < size; i++) {
            count += nums.get(i) - middleRightValue;
        }

        count += (middleRightValue - middleLeftValue) * halfSize;

        System.out.println(count);
    }

    private static void calculateForOddSize(List<Integer> nums) {
        //"срединный" элемент никогда не изменится. Поэтому считаем относительно него

        int middle = nums.size()/2;
        int middleElementValue = nums.get(middle);

        int count = 0;

        for (int i = 0; i < middle; i++) {
            count += middleElementValue - nums.get(i);
        }

        for (int i = middle+1; i < nums.size(); i++) {
            count += nums.get(i) - middleElementValue;
        }

        System.out.println(count);
    }
}
