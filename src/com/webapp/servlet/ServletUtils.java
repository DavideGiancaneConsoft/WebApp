package com.webapp.servlet;

import java.io.IOException;
import java.lang.reflect.Type;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.webapp.bean.Region;

/**
 * Classe di funzioni utility per le servlet
 * @author davide.giancane
 *
 */
class ServletUtils {
	
	private static String jspErrorPath = "/error.jsp";
	
	/**
	 * Effettua il forwarding della richiesta verso una jsp che indica un errore
	 * @param request richiesta HTTP
	 * @param response risposta HTTP
	 * @param context contesto della servlet chiamante
	 * @param errorMessage messaggio di errore
	 * @throws ServletException
	 * @throws IOException
	 */
	static void forwardError(HttpServletRequest request, HttpServletResponse response, 
			ServletContext context, String errorMessage) throws ServletException, IOException {
		
		//Imposto l'errore come attributo della richiesta e nella response
		request.setAttribute("errorMessage", errorMessage);
		response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, errorMessage);
		
		//Forwarding verso la jsp di errore
		context.getRequestDispatcher(jspErrorPath).forward(request, response);
	}
	
	/**
	 * Modifica il path della error jsp di default
	 * @param jspPath path nuova jsp di errore
	 */
	public static void setDefaultJspErrorPath(String jspPath) {
		jspErrorPath = jspPath;
	}
	
	public static JsonSerializer<Region> getRegionSerializer(){
		return new JsonSerializer<Region>() {
			@Override
			public JsonElement serialize(Region src, Type typeOfSrc, JsonSerializationContext context) {
				JsonObject jsonRegion = new JsonObject();
				jsonRegion.addProperty("regionID", src.getRegionID());
				jsonRegion.addProperty("regionName", src.getRegionName());
				
				return jsonRegion;
			}
		};
	}
}
