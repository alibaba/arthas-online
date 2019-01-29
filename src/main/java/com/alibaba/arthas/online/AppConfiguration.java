package com.alibaba.arthas.online;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerCertificateException;

@Configuration
@EnableConfigurationProperties(AppProperties.class)
public class AppConfiguration {

	@Bean
	@ConditionalOnProperty(name = "app.initDockerClient", matchIfMissing = true)
	public DockerClient dockerClient() throws DockerCertificateException {
		return DefaultDockerClient.fromEnv().build();
	}
}
