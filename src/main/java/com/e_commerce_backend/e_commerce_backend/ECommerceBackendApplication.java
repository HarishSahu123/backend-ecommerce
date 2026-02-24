package com.e_commerce_backend.e_commerce_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@EnableWebSecurity
@EnableJpaAuditing  // ✅ Add this
public class ECommerceBackendApplication {





	public static void main(String[] args) {
		SpringApplication.run(ECommerceBackendApplication.class, args);

//		List<List<Integer>> list = Arrays.asList(
//				Arrays.asList(1),
//				Arrays.asList(1, 2),
//				Arrays.asList(3, 4),
//				Arrays.asList(5, 6)
//		);
//
//		List<Object> data = List.of(
//				1,
//				List.of(2, 3),
//				4,
//				List.of(3, 4, 5),
//				3
//		);
//		List<Object> unique = data.stream()
//				.distinct()
//				.collect(Collectors.toList());
//		System.out.println("unique" + unique);
//
//		System.out.println(list);
//
//		List<Integer> flatList =
//				list.stream()
//						.flatMap(List::stream)
//						.collect(Collectors.toList());
//		System.out.println(flatList);

	}

}
