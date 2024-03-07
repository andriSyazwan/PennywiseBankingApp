package com.backend.pennywise.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.backend.pennywise.entities.CreditCard;
import com.backend.pennywise.entities.Point;
import com.backend.pennywise.entities.Transaction;
import com.backend.pennywise.exceptions.CreditCardNotFoundException;
import com.backend.pennywise.exceptions.PointNotFoundException;
import com.backend.pennywise.repositories.CreditCardRepository;
import com.backend.pennywise.repositories.PointRepository;

@Service
public class PointService {
	@Autowired
	private PointRepository pointRepo;
	
	@Autowired
	private CreditCardRepository ccRepo;
	
	Logger logger = LogManager.getLogger(PointService.class);
	
	public List<Point> getAllPoints() {
		return pointRepo.findAll();
	}

	public Point findPointById(long pointId) {
		Point Point = pointRepo.findById(pointId)
								 .orElseThrow(()-> {
									 logger.error("Point id " + pointId + " not found");
									 return new PointNotFoundException("Cashback Point not found");
								 } );
		
		return Point;
	}

	public Point addPoint(Point Point) {
		return pointRepo.save(Point);
	}

	public Point updatePointsDetails(Map<String, Object> updates, long pointId) {
		Point existingPoint = pointRepo.findById(pointId)
							 .orElseThrow(()-> {
								 logger.error("Cashback point id " + pointId + " not found");
								 return new PointNotFoundException("Cashback Point not found");
							 } );
		
		if (updates.containsKey("creditCard")) {
			existingPoint.setCreditCard((CreditCard)updates.get("creditCard"));
			logger.info("Updating credit card for point Id " + pointId);
		}
		
		if (updates.containsKey("transaction")) {
			existingPoint.setTransaction((Transaction)updates.get("transaction"));
			logger.info("Updating transaction for point Id " + pointId);
		}
		
		return pointRepo.save(existingPoint);
	}

	public Map<String, Boolean> deletePointsById(long pointId) {
		Point existingPoint = pointRepo.findById(pointId)
				 .orElseThrow(()-> {
					 logger.error("Cashback point id " + pointId + " not found");
					 return new PointNotFoundException("Cashback Point not found");
				 } );
		
		pointRepo.delete(existingPoint);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		
		return response;
		
	}
	
	 public List<Point> findPointsByCardId(long cardId){
		 CreditCard existingCC = ccRepo.findById(cardId).orElseThrow(() -> {
			 						logger.error("Credit card id " + cardId + " not found");
			 						return new CreditCardNotFoundException("Credit Card not found");
		 						});

		 List<Point> points = new ArrayList<>();
	     points.addAll(pointRepo.findByCreditCard(existingCC));
	     
	     return points;
	}

}
