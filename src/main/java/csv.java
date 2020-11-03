import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import com.opencsv.*;


public class csv {
    private static final String CSV_FILE_PATH
            = "C:\\Users\\Eduardo\\Documents\\CSV\\pruebacsv.csv";

    private static final String CSV_FILE_CUSTOM_SEPERATOR
            = "C:\\Users\\Eduardo\\Documents\\CSV\\pruebacsv.csv";

    private static final String csvFile
            = "C:\\Users\\Eduardo\\Documents\\CSV\\pruebacsv.csv";

    public static void main(String[] args)
    {
            System.out.println("Read Data Line by Line With Header \n");
            readDataLineByLine(CSV_FILE_PATH);
            System.out.println("_______________________________________________");

            System.out.println("Read All Data at Once and Hide the Header also \n");
            readAllDataAtOnce(CSV_FILE_PATH);
            System.out.println("_______________________________________________");

            System.out.println("Custom Seperator here semi-colon\n");
            readDataFromCustomSeperator(CSV_FILE_CUSTOM_SEPERATOR);
            System.out.println("_______________________________________________");

    }

    public static void readDataLineByLine(String file)
    {

        try {

            // Create an object of filereader class
            // with CSV file as a parameter.
            FileReader filereader = new FileReader(file);

            // create csvReader object passing
            // filereader as parameter
            com.opencsv.CSVReader csvReader = new com.opencsv.CSVReader(filereader);
            String[] nextRecord;

            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                for (String cell : nextRecord) {
                    System.out.print(cell + "\t");
                }
                System.out.println("\n");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readAllDataAtOnce(String file)
    {
        try {

            // Create an object of filereader class
            // with CSV file as a parameter.
            FileReader filereader = new FileReader(file);

            // create csvReader object
            // and skip first Line
            com.opencsv.CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            List<String[]> allData = csvReader.readAll();

            // print Data
            for (String[] row : allData) {
                for (String cell : row) {
                    System.out.print(cell + "\t");
                }
                System.out.println();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readDataFromCustomSeperator(String file)
    {
        try {
            // Create object of filereader
            // class with csv file as parameter.
            FileReader filereader = new FileReader(file);

            // create csvParser object with
            // custom seperator semi-colon
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();

            // create csvReader object with
            // parameter filereader and parser
            com.opencsv.CSVReader csvReader = new CSVReaderBuilder(filereader).withCSVParser(parser).build();

            // Read all data at once
            List<String[]> allData = csvReader.readAll();

            // print Data
            for (String[] row : allData) {
                for (String cell : row) {
                    System.out.print(cell + "\t");
                }
            }
            System.out.println();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
