package home.work;

public class Main {
    private static int arrayLength;
    private static int crawlLength;


    public static void main(String[] args) {
        if (checkInput(args)) return;

        calculate();
    }

    private static boolean checkInput(String[] args) {
        try {
            arrayLength = Integer.parseInt(args[0]);
            crawlLength = Integer.parseInt(args[1]);
        }
        catch (NumberFormatException e) {
            System.out.println("Введенные данные не соответствуют условию задания. Должны быть целыми положительными числами");
            return true;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка. Не введены аргументы");
            return true;
        }

        if (arrayLength < 1) {
            System.out.println("Длина кругового массива должна быть больше нуля");
            return true;
        }

        if (crawlLength < 1) {
            System.out.println("Длина обхода должна быть больше нуля");
            return true;
        }

        if (arrayLength == crawlLength) {
            System.out.println("Некорректные данные. Полученный путь будет состоять из бесконечного количества 1");
            return true;
        }
        return false;
    }

    private static void calculate() {
        StringBuilder answer = new StringBuilder();
        answer.append(1);

        int ratio = crawlLength / arrayLength;
        int remains = crawlLength % arrayLength;

        int lastDigit;

        if (remains == 0) {
            lastDigit = arrayLength;
        } else {
            lastDigit = remains;
        }

        while (lastDigit != 1) {
            answer.append(lastDigit);

            if (lastDigit + crawlLength - 1 <= arrayLength * (ratio+1)) {
                lastDigit += crawlLength - 1 - arrayLength * ratio;
            } else {
                lastDigit += crawlLength - 1 - arrayLength * (ratio + 1);
            }
        }

        System.out.println(answer.toString());
    }
}
