package com.shahab.service;

import static org.asynchttpclient.Dsl.asyncHttpClient;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

import org.asynchttpclient.AsyncHttpClient;

public class WebPageFetcher {
	AsyncHttpClient client = null;

	public WebPageFetcher() {
		client = asyncHttpClient();
	}

	public CompletableFuture<String> getPageContent(ExecutorService exec, URI url) {
		System.out.println("< getPageContent :: thread name is : " + Thread.currentThread().getName());
		return CompletableFuture.supplyAsync(() -> {
			System.out.println("< getPageContent:supplyAsync :: thread name is : " + Thread.currentThread().getName());

			try {
				String body = client.prepareGet(url.toString()).execute().get().getResponseBody(StandardCharsets.UTF_8);
				return body;
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;

		}, exec);

	}

}
