package com.shahab.servlet;

import java.io.IOException;

import javax.servlet.AsyncEvent;
import javax.servlet.AsyncListener;

public class WebPageWordsAverageListener implements AsyncListener {

	@Override
	public void onComplete(AsyncEvent asyncEvent) throws IOException {
		asyncEvent.getAsyncContext().getResponse().getWriter().println("Task Completed");
	}

	@Override
	public void onError(AsyncEvent asyncEvent) throws IOException {
		asyncEvent.getAsyncContext().getResponse().getWriter().println("Task Unable to proceed");
		asyncEvent.getAsyncContext().complete();
		
	}

	@Override
	public void onStartAsync(AsyncEvent asyncEvent) throws IOException {}

	@Override
	public void onTimeout(AsyncEvent asyncEvent) throws IOException {
		asyncEvent.getAsyncContext().getResponse().getWriter().println("Task Timeout");
		asyncEvent.getAsyncContext().complete();
	}

	
	
}
