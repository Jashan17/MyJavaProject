import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class Record {
    String firstname;
    String lastname;
    String date;
    int division;
    int points;
    String summary;

    public Record(String firstname, String lastname, String date, int division, int points, String summary) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.date = date;
        this.division = division;
        this.points = points;
        this.summary = summary;
    }
}

public class CSVProcessor {

    public static void main(String[] args) {
        String csvFile = "/Users/jashanchahal/Desktop/javaCSV.csv";
        String line;
        String csvSplitBy = ",";

        List<Record> records = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }

                // Split the line by commas
                String[] fields = line.split(csvSplitBy);

                // Check if the line has the expected number of fields (6)
                if (fields.length < 6) {
                    System.err.println("Skipping malformed line: " + line);
                    continue;

                }

                try {
                    // Parse fields
                    String firstname = fields[0].trim();
                    String lastname = fields[1].trim();
                    String date = fields[2].trim();
                    int division = Integer.parseInt(fields[3].trim());
                    int points = Integer.parseInt(fields[4].trim());
                    String summary = fields[5].trim();

                    // Add the record to the list
                    records.add(new Record(firstname, lastname, date, division, points, summary));
                } catch (NumberFormatException e) {
                    System.err.println("Skipping line with invalid number format: " + line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Sort records by division and points
        records.sort(Comparator.comparingInt((Record r) -> r.division).thenComparingInt(r -> -r.points));

        // Select the top three records
        List<Record> topRecords = records.subList(0, Math.min(3, records.size()));

        // Print the records in the specified YAML format
        System.out.println("records:");
        for (Record record : topRecords) {
            System.out.println("- name: " + record.firstname + " " + record.lastname);
            System.out.println("  details: In division " + record.division + " from " + record.summary);
        }
    }
}
