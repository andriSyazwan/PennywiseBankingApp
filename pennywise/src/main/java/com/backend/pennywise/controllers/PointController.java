package com.backend.pennywise.controllers;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.backend.pennywise.entities.Point;
import com.backend.pennywise.services.PointService;

@RestController
@RequestMapping("api/v1/points")
public class PointController {
	@Autowired
	private PointService pointService;
	
	@GetMapping
	public List<Point> getAllPoint(){
		return pointService.getAllPoints();
	}
	
	@GetMapping("/{id}")
	public Point getPoint(@PathVariable("id") long PointId) {
		return pointService.findPointById(PointId);
	}
	
	@PostMapping("/add")
	public ResponseEntity<Point> addNewPoint(@RequestBody Point Point){
		Point savedPoint = pointService.addPoint(Point);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				       .buildAndExpand(savedPoint.getClass()).toUri();

		return ResponseEntity.created(location).body(savedPoint);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<Point> updatePoint(@RequestBody Map<String, Object> updates,
																 @PathVariable(value = "id") long PointsId){
		Point updatedPoint = pointService.updatePointsDetails(updates, PointsId);
		
		return ResponseEntity.ok(updatedPoint);
	}

	@DeleteMapping("/{id}")
	public Map<String, Boolean> deletePoint(@PathVariable(value = "id") long PointId){
		return pointService.deletePointsById(PointId);
	}
	
	@GetMapping("/creditCard/{id}")
    public List<Point> getPointByCardId(@PathVariable("id") long cardId) {
        return pointService.findPointsByCardId(cardId);
    }
}
