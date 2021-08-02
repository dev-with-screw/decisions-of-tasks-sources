package home.work;

import javax.sound.midi.Soundbank;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static Circle circle;
    private static List<Point> points;

    public static void main(String[] args) {
        try {
            loadCircleData(args[0]);
            loadPointsData(args[1]);
            calculating();
        } catch (IncorrectDataException e) {
            e.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка. Не обнаружено аргументов с путями к файлам");
        }
    }



    private static void loadCircleData(String path) throws IncorrectDataException {
        try (
            BufferedReader circleReader = new BufferedReader(new FileReader(path));
        ) {

            final String xyCircle;
            if (circleReader.ready()) {
                xyCircle = circleReader.readLine();
            } else {
                System.out.println("Нет строки с координатами центра окружности");
                throw new IncorrectDataException();
            }

            final String radiusCircle;
            if (circleReader.ready()) {
                radiusCircle = circleReader.readLine();
            } else {
                System.out.println("Нет строки с радиусом окружности");
                throw new IncorrectDataException();
            }

            final String[] coordinates = xyCircle.split(" ");

            final double radius = Double.parseDouble(radiusCircle);

            if (radius < 0) {
                System.out.println("Некорректные данные. Радиус должен быть больше или равен нулю");
                throw new IncorrectDataException();
            }

            circle = new Circle(
                    Double.parseDouble(coordinates[0]),
                    Double.parseDouble(coordinates[1]),
                    radius);
        } catch (FileNotFoundException e) {
            System.out.println("Файл с параметрами окружности не найден");
            throw new IncorrectDataException();
        } catch (IOException e) {
            System.out.println("Ошибка при извлечении данных из файла с параметрами окружности");
            throw new IncorrectDataException();
        } catch (NumberFormatException e) {
            System.out.println("Ошибка при извлечении данных из файла с параметрами окружности. Не выполнено условие, что параметры окружности подаются в диапазоне float");
            throw new IncorrectDataException();
        }
    }

    private static void loadPointsData(String path) throws IncorrectDataException {
        try (
                BufferedReader pointsReader = new BufferedReader(new FileReader(path));
        ) {
            points = new ArrayList<>();

            while (pointsReader.ready()) {
                final String[] xyPoint = pointsReader.readLine().split(" ");

                Point point = new Point(
                        Double.parseDouble(xyPoint[0]),
                        Double.parseDouble(xyPoint[1])
                );

                points.add(point);
            }
        } catch (FileNotFoundException e) {
            System.out.println("Файл с координатами точек не найден");
            throw new IncorrectDataException();
        } catch (IOException e) {
            System.out.println("Ошибка при извлечении данных из файла с координатами точек");
            throw new IncorrectDataException();
        } catch (NumberFormatException e) {
            System.out.println("Ошибка при извлечении данных из файла с координатами точек. Не выполнено условие, что координаты подаются в диапазоне float");
            throw new IncorrectDataException();
        }
    }

    private static void calculating() {
        if (circle != null && points != null && !points.isEmpty()) {
            BigDecimal xCircle = new BigDecimal(circle.getxCoordinate());
            BigDecimal yCircle = new BigDecimal(circle.getyCoordinate());
            BigDecimal radiusCircle = new BigDecimal(circle.getRadius());
            BigDecimal radiusSquare = radiusCircle.multiply(radiusCircle);

            for (Point point : points) {
                BigDecimal xPoint = new BigDecimal(point.getxCoordinate());
                BigDecimal yPoint = new BigDecimal(point.getyCoordinate());

                BigDecimal xDifferenceSquare = xCircle.subtract(xPoint)
                        .multiply(xCircle.subtract(xPoint));

                BigDecimal yDifferenceSquare = yCircle.subtract(yPoint)
                        .multiply(yCircle.subtract(yPoint));

                BigDecimal sumDifferenceSquare = xDifferenceSquare.add(yDifferenceSquare);

                int result = sumDifferenceSquare.compareTo(radiusSquare);

                switch (result) {
                    case 1 : {
                        System.out.println(2);
                        break;
                    }
                    case 0 : {
                        System.out.println(0);
                        break;
                    }
                    case -1 : {
                        System.out.println(1);
                        break;
                    }
                }
            }
        }
    }

}
