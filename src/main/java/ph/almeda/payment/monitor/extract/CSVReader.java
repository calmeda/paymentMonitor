package ph.almeda.payment.monitor.extract;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import ph.almeda.payment.monitor.model.Policy;
import ph.almeda.payment.monitor.view.ConsoleView;

public class CSVReader {

    public static void main(String[] args) {

        //String csvFile = "d://Users//mkyong//csv/country.csv";
        
        String csvFile = "D:\\Dev\\kitws\\paymentMonitor\\policyList.csv";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = ",";

        try {

            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] policyStr = line.split(cvsSplitBy);
                
                int policyStrLength = policyStr.length;
                System.out.println("policyStrLength "+ policyStrLength);
                
                for (int i=0; i < policyStr.length; i++)
                {
                	String result = policyStr[i].replaceAll("^\"+ \"+$", "");
                }
                
                Policy policyObj = new Policy();
                policyObj.setPolicyNumber(policyStr[0]);

                String lastName = policyStr[1].replaceAll("\"", "");
                policyObj.setLastName(lastName);
                
                String firstName = policyStr[2].replaceAll("\"", "");
                policyObj.setFirstName(firstName);

                policyObj.setPolicyName(policyStr[5]);
                
                ConsoleView cv = new ConsoleView();
                cv.consoleOutput(policyObj);
                
                
                
                
                //Policy policy = new Policy();

                //System.out.println("policyNumber [code= " + policy[0] + " , lastname=" + policy[1] + "]");

            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

}