package com.challenge.gateway.swagger;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

@Primary
@Component
public class SwaggerProvider implements SwaggerResourcesProvider {

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();

        SwaggerResource itinerariesResource = new SwaggerResource();
        itinerariesResource.setName("itineraries");
        itinerariesResource.setLocation("/api/itineraries/v2/api-docs");
        itinerariesResource.setSwaggerVersion("1.0");
        resources.add(itinerariesResource);

        SwaggerResource searchResource = new SwaggerResource();
        searchResource.setName("search");
        searchResource.setLocation("/api/search/v2/api-docs");
        searchResource.setSwaggerVersion("1.0");
        resources.add(searchResource);

        return resources;
    }
}
