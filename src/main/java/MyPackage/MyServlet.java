package MyPackage;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;


public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
   
    public MyServlet() {
        super();
    }

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String inputData =   request.getParameter("userInput");
		System.out.println(inputData);
		
		String apiKey = "6fcf3602e11ade4fa46fa4a1092874d8";
		
        String city = request.getParameter("city"); 

      
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + apiKey;
        try {
            // Api Intaeration
            URL url = new URL(apiUrl);
            HttpURLConnection connection =(HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            // jo input mil rha h usse read krayenge
            InputStream inputStream =connection.getInputStream();
            InputStreamReader reader =new InputStreamReader(inputStream);
            
            // store data in String
            
            StringBuilder responseContent = new StringBuilder();
            Scanner scanner =new Scanner(reader);
            
            while(scanner.hasNext()) {
            	responseContent.append(scanner.nextLine());
            }
            scanner.close();
            System.out.println(responseContent);
            
            //typeCasting ..........................................................................
            
            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(responseContent.toString(),JsonObject.class);
            
            // date time
            Long dateTimestamp = jsonObject.get("dt").getAsLong() * 1000;
            String date =new Date(dateTimestamp).toString();
            
            //Temperature
            double temperatureKelvin = jsonObject.getAsJsonObject("main").get("temp").getAsDouble();
            int temperatureCelsius =(int) (temperatureKelvin - 273.15);
            
            //Humidity
        int humidity = jsonObject.getAsJsonObject("main").get("humidity").getAsInt();
        
        //WindSpeed
        double windSpeed =jsonObject.getAsJsonObject("wind").get("speed").getAsDouble();
        
        //WeatherCondition
        String weatherCondition = jsonObject.getAsJsonArray("weather").get(0).getAsJsonObject().get("main").getAsString();
            
            request.setAttribute("date", date);
            request.setAttribute("city", city);
            request.setAttribute("temperature",  temperatureCelsius);
            request.setAttribute("weatherCondition", weatherCondition);
            request.setAttribute("humidity", humidity);
            request.setAttribute("windSpeed", windSpeed);
            request.setAttribute("weatherDate", responseContent.toString());
            
            connection.disconnect();
           
        }catch (IOException e) {
            e.printStackTrace();
        }
     
        request.getRequestDispatcher("index.jsp").forward(request, response);
        
        

	}

}
