package home.work;

import java.io.*;
import java.util.HashMap;

//конструирую файл report.json на основе tests.json, копируя фрагменты из tests.json и вставляя статусы из values
// кривовато, зато без использования сторонних библиотек типа Gson или Jackson

public class Main {

    public static void main(String[] args) {

        String testsJson = null;
        String valuesJson = null;

        try (
            BufferedReader testsJsonReader = new BufferedReader(new FileReader(args[0]));
            BufferedReader valuesJsonReader = new BufferedReader(new FileReader(args[1]))
        ) {
            testsJson = extracting(testsJsonReader);
            valuesJson = extracting(valuesJsonReader);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Ошибка. Не указаны пути к файлам");
        } catch (FileNotFoundException e) {
            System.out.println("Один или оба файла не найдены");
        } catch (IOException e) {
            System.out.println("Ошибка при извлечении данных из файла");
        }

        if (testsJson == null  || testsJson.isEmpty() ||
            valuesJson == null || valuesJson.isEmpty())
        {
            System.out.println("Обрабатывать нечего, данных не обнаружено");
        } else {
            final HashMap<Integer, String> mapOfValues = getMapOfValues(valuesJson);

            StringBuilder reportJsonBuilder = new StringBuilder();
            String[] testsFragments = testsJson.split("\"\"");

            for (String fragment : testsFragments) {

                if (fragment.contains("\"id\":")) {
                    final int beginningOfId = fragment.lastIndexOf("\"id\":") + 5; //changed
                    final int endingOfId = fragment.indexOf(",", beginningOfId);
                    final String idString = fragment.substring(beginningOfId, endingOfId);
                    int id = Integer.parseInt(idString);

                    final String value = mapOfValues.get(id);

                    if (value == null) {
                        reportJsonBuilder.append(fragment).append("\"").append("\"");
                    } else {
                        reportJsonBuilder.append(fragment).append("\"").append(value).append("\"");
                    }
                }
            }

            reportJsonBuilder.replace(2, 7, "report");
            reportJsonBuilder.append(testsFragments[testsFragments.length-1]);

            String reportJson = reportJsonBuilder.toString();
            System.out.println(reportJson);

            try (PrintWriter writer = new PrintWriter(new FileWriter("report.json"))) {
                writer.print(reportJson);
            } catch (IOException e) {
                System.out.println("Не удается создать файл report.json");
            }
        }
    }

    private static HashMap<Integer, String> getMapOfValues(String json) {
        HashMap<Integer, String> values = new HashMap<>();

        final String substring = json.substring(json.indexOf("[")+2, json.length()-3);

        String[] fragments = substring.split("},\\{");

        for (String f : fragments) {
            final String idString = f.substring(5, f.indexOf(","));
            Integer id = Integer.parseInt(idString);
            final String value = f.substring(f.indexOf("\"value\":\"")+9, f.length()-1);
            values.put(id, value);
        }

        return values;
    }

    private static String extracting(BufferedReader reader) throws IOException {
        StringBuilder collectLines = new StringBuilder();

        while (reader.ready()) {
            final String readLine = reader.readLine();
            collectLines.append(readLine);
        }

        final String json = collectLines.toString();

        return json.replaceAll(" ", "");
    }
}
