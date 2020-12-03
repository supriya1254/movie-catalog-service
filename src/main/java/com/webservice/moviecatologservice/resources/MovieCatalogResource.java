package com.webservice.moviecatologservice.resources;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.webservice.moviecatologservice.models.Movie;
import com.webservice.moviecatologservice.models.Rating;
import com.webservice.moviecatologservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webservice.moviecatologservice.models.CatalogItem;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource 
{
	@Autowired
	RestTemplate restTemplate;

	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId") String userId)
	{
		//getForObject this method takes two arg
		//1.) url :  that you wanna call
		//2.) what it's get back like payroll type for example : String or any class object type
		//Movie movie = restTemplate.getForObject("http://localhost:8082/movies/ + ", Movie.class);

		//get all the rating
		UserRating ratings = restTemplate.getForObject("http://movie-data-service/ratingsdata/users/" + userId, UserRating.class);

		return ratings.getUserRating().stream().map(rating -> {
			//for each movie ID, call movie info service and get details
			Movie movie = restTemplate.getForObject("http://movie-info-service/movies/" + rating.getMovieId(), Movie.class);
			//put them all together
			return  new CatalogItem(movie.getName(), "Test" + movie.getMovieId(), rating.getRating());
		})
				.collect(Collectors.toList());

	}
}
