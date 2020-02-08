package com.cashtransfer;

import com.cashtransfer.config.DefaultAdminConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class Application {

	@Autowired
	private DefaultAdminConfig defaultAdminConfig;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@EventListener
	public void onApplicationEvent(final ApplicationReadyEvent event) {
		defaultAdminConfig.setup();
	}
}


