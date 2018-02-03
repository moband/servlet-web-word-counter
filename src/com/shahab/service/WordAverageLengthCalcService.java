package com.shahab.service;


import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import javax.servlet.AsyncContext;

public class WordAverageLengthCalcService {

	public CompletableFuture<Double> getStringAverageWordsLengthValue( ExecutorService exec, String content) {
		System.out.println("< getStringAverageWordsLengthValue :: thread name is : " + Thread.currentThread().getName());
		return CompletableFuture.supplyAsync(() -> {
			System.out.println("< getStringAverageWordsLengthValue:supplyAsync :: thread name is : " + Thread.currentThread().getName());
			String words = content.replaceAll("[^a-zA-Z0-9]", " ");
			//Java 8 Map Reduce feature using stream to calculate average of the words of the given webpage url 
			Double result= Arrays.stream(words.replaceAll("[^a-zA-Z0-9]", " ").trim().split("\\s+")).mapToInt(p -> p.length()).average().getAsDouble();
			System.out.println("> getStringAverageWordsLengthValue:supplyAsync :: thread name is : " + Thread.currentThread().getName() +" result :: "+result);
			return result;
		},exec);
	}
}
