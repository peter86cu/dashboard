package com.shopping.dashboard.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import com.ayalait.modelo.Cliente;
import com.ayalait.modelo.Producto;
import com.ayalait.modelo.Session;
import com.ayalait.utils.ResponsePrefactura;
import com.ayalait.web.VisitantesLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.gson.Gson;
import com.multishop.modelo.Prefactura;
import com.multishop.modelo.PrefacturaDetalle;
import com.shopping.dashboard.repositorio.VisitantesJPA;
import com.shopping.dashboard.vo.ErrorState;

@Service
public class CotizacionServiceImpl implements CotizacionService {

	public String stock;

	@Autowired
	VisitantesJPA visitantesJPA;
	
	

	ObjectWriter ow = (new ObjectMapper()).writer().withDefaultPrettyPrinter();
	private boolean desarrollo = true;

	ErrorState error = new ErrorState();

	@Autowired
	RestTemplate restTemplate;

	void cargarServer() throws IOException {
		Properties p = new Properties();

		try {
			URL url = this.getClass().getClassLoader().getResource("application.properties");
			if (url == null) {
				throw new IllegalArgumentException("application.properties" + " is not found 1");
			} else {
				InputStream propertiesStream = url.openStream();
				p.load(propertiesStream);
				propertiesStream.close();
				this.stock = p.getProperty("server.stock");

			}
		} catch (FileNotFoundException var3) {
			System.err.println(var3.getMessage());
		}

	}

	public CotizacionServiceImpl() {
		try {
			if (desarrollo) {
				stock = "http://localhost:8082";

			} else {
				cargarServer();
			}
		} catch (IOException var2) {
			System.err.println(var2.getMessage());
		}

	}


	

	@Override
	public ResponseEntity<String> obtenerVisitantesMensuales() {
		
		try {
		Iterator<Object> lst = visitantesJPA.visitantesMensuales().iterator();
		List<VisitantesLog> lstVisitantes= new ArrayList<VisitantesLog>();
		if(lst != null && lst.hasNext()) {
			while( lst.hasNext()) {
				Object[] objArray = (Object[]) lst.next();
				VisitantesLog response= new VisitantesLog();
				   response.setCantidad(Integer.parseInt(objArray[0].toString()) );
				   					  
				   response.setMes(objArray[1].toString());
				   response.setAnio(Integer.parseInt(objArray[2].toString()));
				  
				   lstVisitantes.add(response);
			}
		
	        
	     // Ordenar la lista por fecha en orden descendente usando una expresión lambda
			//Collections.sort(lstVisitantes, (r1, r2) -> r1.getFecha().compareTo(r2.getFecha()));


	        
			return new ResponseEntity<String>(new Gson().toJson(lstVisitantes),HttpStatus.OK);
		}else {
			error.setCode(90002);
			error.setMenssage("No hay registros de prefacturas.");
			return new ResponseEntity<String>(new Gson().toJson(error),HttpStatus.BAD_GATEWAY);
		}
	} catch (Exception e) {
		error.setCode(90020);
		error.setMenssage(e.getCause().getMessage());
		return new ResponseEntity<String>(new Gson().toJson(error),HttpStatus.NOT_ACCEPTABLE);
	}
	}

}
