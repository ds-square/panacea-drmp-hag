package org.panacea.drmp.hag.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
	@Bean

	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.components(new Components())
				.info(new Info().title("HAG REST API")
						.description("REST API for Human Layer Attack Graph Generator (HAG)")
						.version("1.0.0")
						.license(new License().name("License: LGPL v3.0").url("https://www.gnu.org/licenses/lgpl-3.0.html")));
	}

}