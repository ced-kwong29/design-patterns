package com.csen_359.design_patterns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Water Usage Monitor - entry point.
 *
 * <p>This class is the component-scan root: every package below
 * {@code com.csen_359.design_patterns} is picked up automatically.
 *
 * <ul>
 *   <li>{@code @EnableScheduling} - powers the {@code scheduler} package jobs.</li>
 *   <li>{@code @EnableAsync} - lets event listeners run off the request thread.</li>
 *   <li>{@code @EnableCaching} - powers {@code @Cacheable} on benchmark queries.</li>
 * </ul>
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableCaching
public class DesignPatternsApplication {

	public static void main(String[] args) {
		SpringApplication.run(DesignPatternsApplication.class, args);
	}

}
