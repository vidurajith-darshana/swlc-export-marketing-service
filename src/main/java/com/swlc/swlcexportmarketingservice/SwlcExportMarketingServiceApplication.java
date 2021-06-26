package com.swlc.swlcexportmarketingservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class SwlcExportMarketingServiceApplication implements WebMvcConfigurer {

	@Value("${server.upload.url}")
	private String archivePath;

	@Value("${server.upload.folder}")
	private String folder;

	public static void main(String[] args) {
		SpringApplication.run(SwlcExportMarketingServiceApplication.class, args);
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {

		// Register resource handler for images
		registry.addResourceHandler("/swlc-data/**").addResourceLocations("file:///"+archivePath+folder+"/")
				.setCacheControl(CacheControl.maxAge(24, TimeUnit.HOURS).cachePublic());
	}
}
