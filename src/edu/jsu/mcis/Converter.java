package edu.jsu.mcis;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import org.json.simple.*;
import org.json.simple.parser.*;

public class Converter {
    
     JSONArray records = new JSONArray(); // Container for all records
     LinkedHashMap<String, String> jsonObject = new LinkedHashMap<>();
     String[] record;
     String jsonString = "";
     
    @SuppressWarnings("unchecked")
    public static String csvToJson(String csvString) {
        
        String results;
        
        try {
            
            CSVReader reader = new CSVReader(new StringReader(csvString));
            List<String[]> full = reader.readAll();
            Iterator<String[]> iterator = full.iterator();
            
            JSONObject jsonObject = new JSONObject(); // Map
            
            JSONArray rowHeaders = new JSONArray();   // List
            JSONArray mainData = new JSONArray();     // List
            JSONArray colHeaders = new JSONArray();   // List
            
            for (String string : iterator.next()) {
                
                colHeaders.add(string);
                
            }
            
            while (iterator.hasNext()) {
                
                JSONArray rowData = new JSONArray(); 
                
                String[] rows = iterator.next();
                rowHeaders.add(rows[0]);
                
                
                for (int j = 1; j < rows.length; ++j) {
                    
                   rowData.add(Integer.parseInt(rows[j]));
                    
                }
                
                mainData.add(rowData);
                
            }
           
             jsonObject.put("colHeaders", colHeaders);
             jsonObject.put("rowHeaders", rowHeaders);
            jsonObject.put("data", mainData);
            
            results = JSONValue.toJSONString(jsonObject);
            
        }        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }
    
    public static String jsonToCsv(String jsonString) {
        
        String results;
        
        try {

            StringWriter writer = new StringWriter();
            CSVWriter csvWriter = new CSVWriter(writer, ',', '"', '\n');
            
           JSONParser parser = new JSONParser();
           JSONObject jsonObject = (JSONObject)parser.parse(jsonString);
           
            JSONArray colHeaders = (JSONArray)jsonObject.get("colHeaders");
            JSONArray rowHeaders = (JSONArray)jsonObject.get("rowHeaders");
            JSONArray data = (JSONArray)jsonObject.get("data");
            
            String[] colStringArray = new String[colHeaders.size()];
            String[] rowStringArray = new String[rowHeaders.size()];
            String[] dataStringArray = new String[data.size()];
            
             for (int i = 0; i < colHeaders.size(); i++){
                colStringArray[i] = colHeaders.get(i).toString();
            }
              
            csvWriter.writeNext(colStringArray);
            
             for (int i = 0; i < rowHeaders.size(); i++){
   
                rowStringArray[i] = rowHeaders.get(i).toString();
                dataStringArray[i] = data.get(i).toString();
                
            }
             
             for (int i = 0; i < dataStringArray.length; i++) {
            
                JSONArray dataValues = (JSONArray)parser.parse(dataStringArray[i]);
                String[] row = new String[dataValues.size() + 1];
                row[0] = rowStringArray[i];
                
                for(int j = 0; j < dataValues.size(); j++) {
                    row[j+1] = dataValues.get(j).toString();
                }
                
                csvWriter.writeNext(row);
                
            }
             results = writer.toString();
            
        }
        
        catch(Exception e) { return e.toString(); }
        
        return results.trim();
        
    }

}