import java.io.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.Scanner;

public class LogParser {

    public static void main(String args[]) {

    	//System.out.println("Hello " + args[0]);
    	
        try {

            PrintWriter responses = new PrintWriter(args[1]);
            responses.println("JourneyId;DateTimeStamp;Time;EventType;Request URL;BAPI");
            Scanner s = new Scanner(new File(args[0]));
            while (s.hasNextLine()) {
                String line = s.nextLine();
                if(line.contains("CLIENT REQUEST") || line.contains("SERVER REQUEST") || line.contains("SERVER RESPONSE")|| line.contains("CLIENT RESPONSE")){
                	if(line.contains("CLIENT REQUEST")){
                		String journeyId = line.substring(line.indexOf("JourneyId:")+10, line.indexOf("|| Resource:"));
                		String timestamp = line.substring(1, line.indexOf("]"));
                		String type = line.substring(line.indexOf("Resource:")+9, line.indexOf("CLIENT REQUEST")+14);
                		String requestURL = line.substring(line.indexOf("HTTP Request URL:")+17, line.indexOf("|| Payload"));
                		String finalString = journeyId + ";" + timestamp + ";" + timeValue(timestamp) + ";" +  type + ";" + requestURL + ";";
                		System.out.println(finalString + " Time Value--> ");
                		responses.println(finalString);
                	}

                	if(line.contains("SERVER REQUEST")){
                		String journeyId = line.substring(line.indexOf("JourneyId:")+10, line.indexOf("|| Resource:"));
                		String timestamp = line.substring(1, line.indexOf("]"));
                		String type = line.substring(line.indexOf("Resource:")+9, line.indexOf("SERVER REQUEST")+14);
                		String finalString = journeyId + ";" + timestamp + ";" + timeValue(timestamp) + ";" + type + ";" + ";";
                		line = s.nextLine();
                		finalString = finalString.concat(line.substring(1,line.length()-1));
                		//String requestURL = line.substring(line.indexOf("HTTP Request URL:")+17, line.indexOf("|| Payload"));
                		System.out.println(finalString);
                		responses.println(finalString);
                	}

                	if(line.contains("SERVER RESPONSE")){
                		String journeyId = line.substring(line.indexOf("JourneyId:")+10, line.indexOf("|| Resource:"));
                		String timestamp = line.substring(1, line.indexOf("]"));
                		String type = line.substring(line.indexOf("Resource:")+9, line.indexOf("SERVER RESPONSE")+15);
                		String bapi = line.substring(line.indexOf("Payload XML:<?xml version=\"1.0\" encoding=\"UTF-8\"?><")+51, line.length()-1);
                		//String requestURL = line.substring(line.indexOf("HTTP Request URL:")+17, line.indexOf("|| Payload"));
                		String finalString = journeyId + ";" + timestamp + ";" + timeValue(timestamp) + ";"+ type + ";"  + ";" + bapi;
                		System.out.println(finalString );
                		responses.println(finalString);
                	}
                	
                	if(line.contains("CLIENT RESPONSE")){
                		String journeyId = line.substring(line.indexOf("JourneyId:")+10, line.indexOf("|| Resource:"));
                		String timestamp = line.substring(1, line.indexOf("]"));
                		String type = line.substring(line.indexOf("Resource:")+9, line.indexOf("CLIENT RESPONSE")+15);
                		String requestURL = line.substring(line.indexOf("HTTP Request URL:")+17, line.indexOf("|| Payload"));
                		String finalString = journeyId + ";" + timestamp + ";" + timeValue(timestamp) + ";" + type + ";" + requestURL + ";";
                		System.out.println(finalString);
                		responses.println(finalString);
                	}
                	
                }
/*                if (line.startsWith("Request from"))
                    currentLog = requests;
                else if (line.startsWith("Response from"))
                    currentLog = responses;
                else if (currentLog != null)
                    currentLog.println(line);
*/           
                }

            responses.close();
            s.close();
        } catch (IOException ioex) {
            // handle exception...
        }
    }
    public static String timeValue(String timeStamp){
        Calendar calendar = Calendar.getInstance();    
        calendar.set(Calendar.MILLISECOND, 0); // Clear the millis part. Silly API.
        DateFormat formatter;
        formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
         // you can change format of date
        Date date = null;
		try {
			date = formatter.parse(timeStamp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        java.sql.Timestamp timeStampDate = new Timestamp(date.getTime());
        return Long.toString(timeStampDate.getTime());
    	
    }
}