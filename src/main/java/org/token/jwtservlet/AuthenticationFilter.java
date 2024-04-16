package org.token.jwtservlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.HashMap;

import com.google.gson.Gson;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

   String []EXCLUDED_PATH;

   @Override
   public void init(FilterConfig filterConfig) throws ServletException {
      System.out.println("Hereee i ammm bitch");
      try {
         EXCLUDED_PATH = initExcludedEndpoint().split(",");

        // System.out.println(EXCLUDED_PATH);
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   @Override
   public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
         throws IOException, ServletException {
      HttpServletRequest req = (HttpServletRequest) request;
      HttpServletResponse res = (HttpServletResponse) response;

      String path = req.getRequestURI().substring(req.getContextPath().length());
      
      String method = req.getMethod();

      System.out.println("HTTP Method: " + method);

      System.out.println(path);
      // Exclude the /login endpoint from authentication


      if (isExluded(path,method)) {
         filterChain.doFilter(request, response);
         return;
      }

      // Check for JWT token in Authorization header
      String token = extractToken(req.getHeader("Authorization"));

      if (!isTokenValid(token)) {
         Gson gson = new Gson();
         HashMap<String, Object> map = new HashMap<>();

         map.put("error", "No valid token found");
         map.put("code", 400);
         res.setStatus(400);
         res.setContentType("application/json");

         String jsonString = gson.toJson(map);

         // Write JSON response to PrintWriter
         PrintWriter out = res.getWriter();
         out.println(jsonString);

      }

   }

   private boolean isTokenValid(String token) {
      return false;
   }

   private String extractToken(String token) {

      return null;
   }

   private String initExcludedEndpoint() throws FileNotFoundException, IOException {
      StringBuilder contentBuilder = new StringBuilder();
      try {
         File file = new File(getClass().getClassLoader().getResource("exclude-endpoint.txt").toURI());
         try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))) {
            String line;
            while ((line = reader.readLine()) != null) {
               contentBuilder.append(line).append("\n");
            }
         }

         return contentBuilder.toString();
      } catch (URISyntaxException e) {
         e.printStackTrace();
         return null;
      }

   }


   private boolean isExluded(String endpoint,String method){
      for (String string : EXCLUDED_PATH) {
         String d = method+":"+endpoint;
         if(string.equals(d)){
            return true;
         }
      }

      return false;
   }

}
